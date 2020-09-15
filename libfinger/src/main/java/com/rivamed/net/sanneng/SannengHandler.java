package com.rivamed.net.sanneng;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.TransferUtils;

import java.nio.ByteBuffer;
import java.util.Date;

import com.rivamed.net.callback.FingerHandler;
import com.rivamed.net.callback.FingerMessageListener;

import io.netty.channel.ChannelHandlerContext;

/**
 * 三能指纹仪的处理类
 * 它是一个事件对象，注册监听器，负责触发监听器事件
 * create by 孙朝阳 on 2019-3-7
 */
public class SannengHandler extends BaseSanNengHandler implements FingerHandler {


    private short currentCMDParameter = 0x00;//CMD参数
    private FingerMessageListener fingerMessageListener;
    private byte[] readResultDataBytes = new byte[498];//读取到的指纹模板
    private byte[] sendTemplateData = new byte[498];//待发送的指纹模板498个
    private byte[] readResultBytes2 = new byte[64 * 1024];//根据条件清空结果
    private byte[] readResultDataAllBytes = new byte[510];//指纹模板全
    int currentReadDataLen = 0;//当前已经接收的数据长度
    int nextReadDataLen = 0;//下一次还需接收的数据长度

    /***
     * 指纹是否处于注册模式
     */
    boolean fingerRegisterModel = false;
    int fingerRegisterCount = 0;

    //存储指纹数据
    ByteBuffer fingerData = ByteBuffer.allocate(1024);
    /***
     * 等待注册
     */
    boolean waitFingerReg = false;

    //记录最后一次指纹返回的数据，超过30秒，则表示需要重新发送指纹采集
    //超过5分钟，则应该发送指纹错误消息
    Date lastFingerData = new Date();

    //指纹注册监控线程；防止指纹注册出现超时等
    int fingerRegisterTimer = 0;
    Thread fingerRegStateThread = new Thread(() -> {
        fingerRegisterTimer = 0;
        LogUtils.e("指纹注册监控线程启动");
        while (fingerRegisterTimer < 40) {  //指纹注册最长等待时间为30秒
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
            fingerRegisterTimer++;
        }
        if (fingerRegisterModel)
            fingerRegisterModel = false;
    });


    @Override
    public void processData(byte[] buf) {
        String readStr = TransferUtils.Byte2String(buf);
        String readStrHead = readStr.substring(0, 4).toUpperCase();
        switch (readStrHead) {
            //对应  tar / src  位，协议中第4位 （头和len（2位）之后第一位
            case "AA55":
                //响应包
                processAck(buf);
                break;
            case "A55A":
                //Target响应数据包
                processDataAck(buf);
                break;
            case "5AA5":
                //响应数据包
                processDataAck(buf);
                break;
            default:
                break;
        }

        if (nextReadDataLen != 0 && !readStrHead.equals("AA55")) {

            if (nextReadDataLen - buf.length >= 0) {

                System.arraycopy(buf, 0, readResultDataAllBytes, currentReadDataLen, buf.length);//指纹数据
                LogUtils.e("接收指纹模板数据cmd_up_char：readResultDataAllBytes.length=" + readResultDataAllBytes.length + ":" + TransferUtils.Byte2String(readResultDataAllBytes));
                LogUtils.e("" + currentReadDataLen);
                currentReadDataLen = currentReadDataLen + buf.length;
                LogUtils.e("" + currentReadDataLen);
                if (nextReadDataLen - buf.length == 0) {
                    LogUtils.e("指纹模板全的长度为：" + readResultDataAllBytes.length + ":" + TransferUtils.Byte2String(readResultDataAllBytes));
                    System.arraycopy(readResultDataAllBytes, 10, readResultDataBytes, 0, 498);//指纹数据最后一次
                    LogUtils.e("指纹模板的长度为：" + readResultDataBytes.length + ":" + TransferUtils.Byte2String(readResultDataBytes));
                    if (!DataProtocol.checkReceive(readResultDataAllBytes, readResultDataAllBytes.length, (short) DataProtocol.RCM_DATA_PREFIX_CODE, currentCMD)) {//RCM_DATA_PREFIX_CODE
                        LogUtils.w("对客户端" + getIdentification() + "的消息校验不通过，消息体=" + readStr + "\r 将强制断开设备");
                        this.close();
                        return;
                    }
                    // TODO: 2019/10/14  修改
//                    if (this.fingerMessageListener != null) {
//                        this.fingerMessageListener.onRegisterResult(getIdentification(), true, TransferUtils.Byte2String(readResultDataBytes));
//                    }
                    currentReadDataLen = 0;
                    fingerRegisterModel = false;//停止指纹注册
                    fingerRegisterTimer = 50;
                    sendFingerGetImage();
                }
                nextReadDataLen = nextReadDataLen - buf.length;
            }
        }

        //计算时间差
        long intenval = ((new Date()).getTime() - lastFingerData.getTime()) / 1000;
        if (intenval > 5 * 60) {
            // TODO: 2019/10/14  修改
//            if (fingerMessageListener != null) {
//                fingerMessageListener.onFingerDeviceError(getIdentification(), true);
//            }
            fingerData.clear();
            if (waitFingerReg) {
                try {
                    sendFingerRegister();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    sendFingerGetImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    //处理响应包
    public void processAck(byte[] readBytes) {
        switch (currentCMD) {
            case 0x0001:
                //测试连接
                processTestConn(readBytes);
                break;
            case 0x0003:
                //获取设备参数
                processGetParam(readBytes);
                break;
            case 0x0020:
                //采集指纹图像
                processGetImage(readBytes);
                break;
            case 0x0060:
                //ImageBuffer生成Template 并保存到Ram Buffer中
                processGetGenerate(readBytes);
                break;
            case 0x0061:
                //合成3个指纹模板到rambuffer3中
                processMerge(readBytes);
                break;
            case 0x0062:
                //两个指纹仪模板进行比对
                processMatch(readBytes);
                break;
            case 0x0042:
                //上传到host（主机）
                processUpChar(readBytes);
                break;
            case 0x0043:
                //从主机下载到指纹仪内 CMD_DOWN_CHAR
                processDownCharCMD(readBytes);
                break;
            default:
                break;
        }

    }

    //处理响应数据包
    public void processDataAck(byte[] readBytes) {

        LogUtils.e("processDataAck: 看比对的方法是否进来");

        byte c4 = (byte) (currentCMD & 0xFF);
        byte c5 = (byte) ((currentCMD >> 8) & 0xFF);
        if (readBytes[4] == c4 && readBytes[5] == c5) {
            switch (currentCMD) {
                case 0x0042:
                    processDataUpChar(readBytes);
                    break;
                case 0x0043:
                    processDataDownChar(readBytes);
                    break;
                default:
                    break;
            }
        }
    }

    //处理测试连接响应
    public void processTestConn(byte[] readBytes) {
        short retCode = DataProtocol.GetRetCode(readBytes);
        if (retCode == DataProtocol.ERR_SUCCESS) {
            LogUtils.e("测试指纹仪连接成功");
        }
    }

    //处理获取参数响应
    public void processGetParam(byte[] readBytes) {
        try {
            short retCode = DataProtocol.GetRetCode(readBytes);
            if (retCode == DataProtocol.ERR_SUCCESS) {
                if (currentCMDParameter == 0x00) {
                    //super.setIdentification(String.valueOf(readBytes[10]));
                    byte[] bytes = new byte[7];
                    System.arraycopy(readBytes, 0, bytes, 0, 7);
                    setIdentification(TransferUtils.Byte2String(bytes));
                    if (fingerMessageListener != null) {
                        fingerMessageListener.onConnectState(SannengHandler.this, getIdentification(), true);
                    }
                    sendFingerGetImage();
                } else {
                    sendGetId();
                }
            } else {
                LogUtils.e("获取设备id失败");
                sendGetId();
            }
        } catch (Exception ex) {
            LogUtils.e("获取设备id异常");
        }
    }

    //处理采集指纹图像响应
    public void processGetImage(byte[] readBytes) {
        short retCode = DataProtocol.GetRetCode(readBytes);
        if (retCode == DataProtocol.ERR_SUCCESS) {
            LogUtils.e("采集指纹图像成功,正确码为：" + DataProtocol.GetRetName(retCode));
            new Thread(() -> {
                try {
                    if (fingerRegisterModel) {
                        if (fingerRegisterCount < 3) {
                            sendGenerate(fingerRegisterCount);
                        } else {
                            fingerRegisterModel = false;
                        }
                    } else if (!fingerRegisterModel) {
                        sendGenerate(0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                }
            }).start();

        } else {
            try {
                fingerData.clear();
                if (waitFingerReg) {
                    sendFingerRegister();
                } else {
                    sendFingerGetImage();
                }
            } catch (Exception ex) {
            }
            //Log.d(LOG_TAG, "采集指纹图像失败，错误码为：" + DataProtocol.GetRetName(retCode));
        }
    }

    //处理生成模板响应
    public void processGetGenerate(byte[] readBytes) {
        short retCode = DataProtocol.GetRetCode(readBytes);
        if (retCode == DataProtocol.ERR_SUCCESS) {

            LogUtils.e("产生模板到ram buffer成功,正确码为：" + DataProtocol.GetRetName(retCode));

            try {
                if (fingerRegisterModel) {
                    fingerRegisterCount++;
                    if (fingerRegisterCount == 3) {
                        sendMerge();
                    } else {
                        try {
                            fingerData.clear();
                            if (waitFingerReg) {
                                sendFingerRegister();
                            } else {
                                sendFingerGetImage();
                            }
                        } catch (Exception ex) {
                        }
                    }
                } else {
                    //测试用 SendDownCharCMD(readResultDataBytes);//不是指纹注册，就下载模板到片内，然后进行比对。
                    LogUtils.e("设备" + getIdentification() + " 指纹采集成功，请发送");
                    // TODO: 2019/10/14  修改
//                    if (this.fingerMessageListener != null) {
//                        this.fingerMessageListener.onFingerCanMatch(getIdentification());
//                        //通知前端指纹采集成功，可以进行比对了
//                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception ex) {

            }
        } else {
            try {
                fingerData.clear();
                if (waitFingerReg) {
                    sendFingerRegister();
                } else {
                    sendFingerGetImage();
                }
            } catch (Exception ex) {
            }
            LogUtils.e("产生模板到ram buffer失败，错误码为：" + DataProtocol.GetRetName(retCode));
        }
    }
    //endregion

    //处理合并相应
    public void processMerge(byte[] readBytes) {
        short retCode = DataProtocol.GetRetCode(readBytes);
        if (retCode == DataProtocol.ERR_SUCCESS) {
            LogUtils.e("合成模板到ram buffer0中成功,正确码为：" + DataProtocol.GetRetName(retCode));

            if (fingerRegisterModel) {
                if (fingerRegisterCount == 3) {
                    sendUpChar();
                }
            }

            //Run_TestConnection();//测试连接
        } else {

            LogUtils.e("产生模板到ram buffer失败，错误码为：" + DataProtocol.GetRetName(retCode));
        }
    }

    //处理模板比对相应
    public void processMatch(byte[] readBytes) {
        short retCode = DataProtocol.GetRetCode(readBytes);
        String retName = DataProtocol.GetRetName(retCode);
        if (retCode == DataProtocol.ERR_SUCCESS) {
            LogUtils.e("processMatch:RamBuffer0与RamBuffer1的比对结果成功：" + retName);
            // TODO: 2019/10/14  修改
//            if (this.fingerMessageListener != null) {
//                this.fingerMessageListener.onFingerMatchResult(getIdentification(), true, "指纹比对结果成功," + retName);
//            }
        } else {
            LogUtils.e("processMatch:RamBuffer0与RamBuffer1的比对结果失败,错误为：" + retName);
            // TODO: 2019/10/14  修改
//            if (this.fingerMessageListener != null) {
//                this.fingerMessageListener.onFingerMatchResult(getIdentification(), false, "指纹比对结果失败," + retName);
//            }
            //告知前端比对结果
        }
        sendFingerGetImage();
    }

    //处理upchar命令响应
    public void processUpChar(byte[] readBytes) {
        short retCode = DataProtocol.GetRetCode(readBytes);
        if (retCode == DataProtocol.ERR_SUCCESS) {

            LogUtils.e("将暂存在RamBuffer中的指纹模板上传到主机（CMD_UP_CHAR）成功");
            byte[] a = new byte[26];
            System.arraycopy(readBytes, 0, this.readResultBytes2, 0, 26);
            System.arraycopy(readBytes, 0, a, 0, 26);
            if (readBytes.length > 26) {
                System.arraycopy(readBytes, 26, readResultDataAllBytes, 0, readBytes.length - 26);//指纹数据第一次
                currentReadDataLen = readBytes.length - 26;

                LogUtils.e("" + DataProtocol.MAKEWORD(readBytes[10], readBytes[11]));
                nextReadDataLen = 536 - readBytes.length;
                LogUtils.e("已经接收长度：" + currentReadDataLen + "需要接收数据包的长度为：" + nextReadDataLen);
            }

        }
    }

    //处理下载指纹模板数据到模块指定的RamBuffer（CMD_DOWN_CHAR）命令
    public void processDownCharCMD(byte[] readBytes) {
        //Target响：AA 55 01 00 4300 02000000 00 00 00 00 00 00 00 00 00 00 00 00 00 00 45 01
        short retCode = DataProtocol.GetRetCode(readBytes);
        if (retCode == DataProtocol.ERR_SUCCESS) {
            LogUtils.e("processDownCharCMD:上传指纹模板:" + DataProtocol.GetRetName(retCode) + ",模块准备接受数据。");
            sendDownCharData();
        } else {
            LogUtils.e("processDownCharCMD:上传指纹模板:" + DataProtocol.GetRetName(retCode) + ",不可以发送数据。");
            // TODO: 2019/10/14  修改
//            if (this.fingerMessageListener != null) {
//                this.fingerMessageListener.onFingerMatchResult(getIdentification(), false, "发送指纹模板命令返回结果失败：" + DataProtocol.GetRetName(retCode));
//            }
            sendFingerGetImage();
        }
    }

    //处理upchar下载到指纹仪提交的数据响应
    public void processDataUpChar(byte[] readBytes) {
        short retCode = DataProtocol.GetRetCode(readBytes);
        if (retCode == DataProtocol.ERR_SUCCESS) {
            readResultDataBytes = readBytes;
            LogUtils.e("将暂存在RamBuffer中的指纹模板上传到主机（CMD_UP_CHAR）数据接收成功");

        }
    }

    //处理下载指纹模板数据到模块指定的RamBuffer（CMD_DOWN_CHAR）数据
    public void processDataDownChar(byte[] readBytes) {

        LogUtils.e("processDataDownChar: 采集到的指纹是否下载到指定的RamBuffer中");

        //Target响应数据包：A5 5A 01 00 43 00 02 00 00 00 45 01 ；数据应答包的长度因为没有数据是12个字节
        short retCode = DataProtocol.GetRetCode(readBytes);
        if (retCode == DataProtocol.ERR_SUCCESS) {
            LogUtils.e("processDataDownChar:上传指纹模板数据成功,可以发送比对了");
            sendMatch();
        } else {
            LogUtils.e("processDataDownChar:发送指纹模板数据返回结果失败:" + DataProtocol.GetRetName(retCode) + ",不进行比对。");
            // TODO: 2019/10/14  修改
//            if (this.fingerMessageListener != null) {
//                this.fingerMessageListener.onFingerMatchResult(getIdentification(), false, "发送指纹模板数据返回结果失败：" + DataProtocol.GetRetName(retCode));
//            }
            sendFingerGetImage();
        }
    }


    /**
     * @return boolean
     * @Description 发送采集指纹图像命令，该命令从采集器采集指纹图像并保存于 ImageBuffer 中。
     * 从采集器采集指纹图像。若采集图像正确，则返回ERR_SUCCESS 。否则返回错误码。
     * 释义：ERR_SUCCESS	0x00	指令处理成功。
     * @Param []
     **/
    public void sendFingerGetImage() {

        byte[] w_abyData = new byte[1];
        w_abyData[0] = (byte) 0x00;//0代表获取设备ID
        byte[] buf = DataProtocol.InitCmdPacket(DataProtocol.CMD_GET_IMAGE, w_abyData, (short) 0);
        //Log.d(LOG_TAG,"向设备" + getIdentification() + "发送指纹采集消息：" + Transfer.Byte2String(buf));
        sendBuf(DataProtocol.CMD_GET_IMAGE, buf);

    }

    /**
     * @return int
     * @Description 从暂存在ImageBuffer中的指纹图像产生模板（CMD_GENERATE）
     * @Param [p_nRamBufferID] Ram Buffer编号
     **/
    public void sendGenerate(int p_nRamBufferID) throws Exception {

        boolean w_bRet;
        byte[] w_abyData = new byte[2];

        w_abyData[0] = DataProtocol.LOBYTE((short) p_nRamBufferID);
        w_abyData[1] = DataProtocol.HIBYTE((short) p_nRamBufferID);

        byte[] buf = DataProtocol.InitCmdPacket(DataProtocol.CMD_GENERATE, w_abyData, 2);

        //. Send command packet to target
        if (sendBuf(DataProtocol.CMD_GENERATE, buf)) {
            LogUtils.e("向设备" + getIdentification() + "发送产生模板命令并存储到ram buffer" + p_nRamBufferID + "中：" + TransferUtils.Byte2String(buf));
        }

    }

    /**
     * @return int
     * @Description 将暂存在RamBuffer0中的指纹模板上传到主机（CMD_UP_CHAR）
     * @Param []
     **/
    public int sendUpChar() {
        boolean w_bRet = false;
        byte[] w_abyData = new byte[2];

        w_abyData[0] = DataProtocol.LOBYTE((short) 0);
        w_abyData[1] = DataProtocol.HIBYTE((short) 0);
        byte[] buf = DataProtocol.InitCmdPacket(DataProtocol.CMD_UP_CHAR, w_abyData, 2);
        w_bRet = sendBuf(DataProtocol.CMD_UP_CHAR, buf);

        if (!w_bRet) {
            return DataProtocol.ERR_CONNECTION;
        }

        return DataProtocol.ERR_SUCCESS;
    }

    /**
     * @return void
     * @Description 指定2个RamBuffer之间的模板做比对(RamBuffer0与RamBuffer1比对) （CMD_MATCH）
     * ○1若指定 Ram Buffer编号无效，则返回错误码ERR_INVALID_BUFFER_ID 。
     * ○2指定的 Ram Buffer中的两个 Template 之间进行比对并返回其结果。
     * 若比对成功，则 RET 返回 ERR_SUCCESS 且 DATA 返回智能更新结果。
     * 否则，RET 返回 ERR_VERIFY 。
     * @Param []
     **/
    public void sendMatch() {

        LogUtils.e("SendMatch: 是否发送比对命令");
//       4.22 例子：将RamBuffer0与RamBuffer1中的指纹模板进行1:1比对
//       Host命令包：	55 AA 00 00 62 0004 0000 0001 00 00 00 00 00 00 00 00 00 00 00 00 00 66 01
//       Target包：	    AA 55 01 00 62 0002 0000 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 64 01

        boolean w_bRet;
        byte[] w_abyData = new byte[4];

        w_abyData[0] = DataProtocol.LOBYTE((short) 0);
        w_abyData[1] = DataProtocol.HIBYTE((short) 0);
        w_abyData[2] = DataProtocol.LOBYTE((short) 1);
        w_abyData[3] = DataProtocol.HIBYTE((short) 1);

        byte[] buf = DataProtocol.InitCmdPacket(DataProtocol.CMD_MATCH, w_abyData, 4);

        w_bRet = sendBuf(DataProtocol.CMD_MATCH, buf);
        if (!w_bRet) {
            LogUtils.e("SendMatch:发送比对命令失败," + TransferUtils.Byte2String(buf));
            // TODO: 2019/10/14  修改
//            if (this.fingerMessageListener != null) {
//                this.fingerMessageListener.onFingerMatchResult(getIdentification(), false, "SendMatch:发送指纹比对命令失败");
//            }
            sendFingerGetImage();
        } else {
            LogUtils.e("SendMatch:发送比对命令成功,将设备" + getIdentification() + "中的RamBuffer0与RamBuffer1数据进行比对," + TransferUtils.Byte2String(buf));
        }
    }

    /**
     * @return void
     * @Description 向指纹模块发送指纹模板数据。
     * 从 Host 接收指纹模板数据（Template Data）并保存于指定的 Ram Buffer1 中。
     * @Param []
     **/
    public void sendDownCharData() {
        // Assemble data packet
        //Ram Buffer编号为1
        int p_nRamBufferID = 1;
        byte[] w_abyData = new byte[DataProtocol.MAX_DATA_LEN + 2];
        w_abyData[0] = DataProtocol.LOBYTE((short) p_nRamBufferID);
        w_abyData[1] = DataProtocol.HIBYTE((short) p_nRamBufferID);
        //memcpy(&w_abyData[2], p_pbyTemplate, GD_RECORD_SIZE);
        LogUtils.e("sendTemplateData:" + TransferUtils.Byte2String(sendTemplateData));
        System.arraycopy(sendTemplateData, 0, w_abyData, 2, DataProtocol.MAX_DATA_LEN);

        byte[] bufData = DataProtocol.InitCmdDataPacket(DataProtocol.CMD_DOWN_CHAR, w_abyData, DataProtocol.MAX_DATA_LEN + 2);
        LogUtils.e("sendTemplateData:" + TransferUtils.Byte2String(bufData));
        //. Send data packet to target
        try {
            boolean ret = sendBuf(DataProtocol.CMD_DOWN_CHAR, bufData);
            if (!ret) {
                LogUtils.e("SendDownCharData:上传指纹模板数据命令发送失败");
                // TODO: 2019/10/14  修改
//                if (this.fingerMessageListener != null) {
//                    this.fingerMessageListener.onFingerMatchResult(getIdentification(), false, "上传指纹模板数据命令发送失败");
//                }
            } else {
                LogUtils.e("SendDownCharData:上传指纹模板数据命令发送成功");
            }
        } catch (Exception ex) {
            LogUtils.e("SendDownCharData:上传指纹模板数据命令发送异常," + ex.getMessage());
            // TODO: 2019/10/14  修改
//            if (this.fingerMessageListener != null) {
//                this.fingerMessageListener.onFingerMatchResult(getIdentification(), false, "上传指纹模板数据命令发送异常：" + ex.getMessage());
//            }
        }
    }

    /**
     * @return boolean
     * @Description 客户端发送指纹注册开启
     * @Param []
     **/
    public boolean sendFingerRegister() throws Exception {

        fingerRegisterModel = true;
        waitFingerReg = false;
        // TODO: 2019/10/14  修改
//        if (this.fingerMessageListener != null) {
//            this.fingerMessageListener.onFingerRegisterCmdExcuted(getIdentification());
//        }
        if (fingerRegStateThread.isAlive()) {
            fingerRegisterTimer = 0;
        } else {
            fingerRegStateThread.start();
        }
        sendFingerGetImage();
        LogUtils.e("开启设备的" + getIdentification() + "的指纹注册");
        return true;
    }


    /**
     * 发送合成指纹模板指令
     */

    public void sendMerge() throws Exception {
        byte[] w_abyData = new byte[3];
        int p_nRamBufferID = 0;
        w_abyData[0] = DataProtocol.LOBYTE((short) p_nRamBufferID);
        w_abyData[1] = DataProtocol.HIBYTE((short) p_nRamBufferID);
        w_abyData[2] = (byte) 3;
        byte[] buf = DataProtocol.InitCmdPacket(DataProtocol.CMD_MERGE, w_abyData, 3);

        if (sendBuf(DataProtocol.CMD_MERGE, buf)) {
            LogUtils.e("将" + getIdentification() + "中的3个模板数据合成一个模板并存储到ram buffer" + p_nRamBufferID + "中：" + TransferUtils.Byte2String(buf));
        }
    }

    /**
     * 开始注册指纹
     * @return 返回码
     */
    public int register() {
        if (fingerRegisterModel || waitFingerReg) {
            return FunctionCode.DEVICE_BUSY;
        }
        try {
            fingerRegisterCount = 0;
            waitFingerReg = true;
            return FunctionCode.SUCCESS;
        } catch (Exception ex) {
            LogUtils.e("发送指纹注册信息错误,message=" + ex.toString());
            return FunctionCode.OPERATION_FAIL;
        }
    }


    @Override
    public int closeChannel() {
        //调用父类的关闭方法
        return close() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int startReadFinger() {
        return 0;
    }

    @Override
    public int stopReadFinger() {
        return 0;
    }

    @Override
    public int startRegisterFinger(int timeOut, String filePath) {
        return 0;
    }

    @Override
    public int stopRegisterFinger() {
        return 0;
    }

    @Override
    public String getRemoteIP() {
        return getIP();
    }

    @Override
    public String getProducer() {
        return "IDWD";
    }

    @Override
    public String getVersion() {
        return "V2.0";
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LogUtils.e("连接断开了");
        if (fingerMessageListener != null) {
            fingerMessageListener.onConnectState(this, getIdentification(), false);
        }
        super.channelInactive(ctx);
    }

    /**
     * 注册监听器
     *
     * @param fingerMessageListener
     */
    public void registerFingerMessageListener(FingerMessageListener fingerMessageListener) {
        this.fingerMessageListener = fingerMessageListener;
    }
}

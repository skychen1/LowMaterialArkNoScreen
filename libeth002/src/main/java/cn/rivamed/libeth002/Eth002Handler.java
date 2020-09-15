package cn.rivamed.libeth002;

import android.os.Handler;
import android.os.HandlerThread;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.TransferUtils;

import java.nio.ByteBuffer;

import cn.rivamed.callback.Eth002HandlerInterface;
import cn.rivamed.callback.Eth002MessageListener;
import io.netty.channel.ChannelHandlerContext;

public class Eth002Handler extends BaseEth002Handler implements Eth002HandlerInterface {


    /***
     * 指纹是否处于注册模式
     * fingerData   //存储指纹数据
     * waitFingerReg 标识，是否是在注册指纹
     * eth002MessageListener 回调监听
     * mHandler 延时控制器
     */
    private boolean fingerRegisterModel = false;
    private ByteBuffer fingerData = ByteBuffer.allocate(1024);
    private volatile boolean waitFingerReg = false;
    private Eth002MessageListener eth002MessageListener;
    private Handler mHandler;

    /**
     * 构造函数，初始化handler 用来执行定时任务
     */
    public Eth002Handler() {
        HandlerThread mHandlerThread = new HandlerThread("delay_thread::::" + this.hashCode());
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    @Override
    public void processData(byte[] buf) {
        switch (buf[3]) {
            //对应  tar / src  位，协议中第4位 （头和len（2位）之后第一位
            case DataProtocol.CHAN_DEVICESELF:
                //来自模块本身(心跳数据等)
                processModelMessage(buf);
                break;
            case DataProtocol.CHAN_IO:
                //IO（锁的数据）
                processIOData(buf);
                break;
            case DataProtocol.CHAN_SER1_HFREADER:
                //串口1（id卡的数据）
                processIdCard(buf);
                break;
            case DataProtocol.CHAN_SER2_FINGER:
                //串口2（指纹的数据）
                processFingerData(buf);
                break;
            default:
                break;
        }
    }

    /**
     * 解析模块儿本身的数据，如心跳，设备id等
     *
     * @param buffer 数据
     */
    private void processModelMessage(byte[] buffer) {
        switch (buffer[4] & 0xff) {
            case 0x67:
                //心跳
                responseHearBear(buffer);
                break;
            case 0x68:
                //读取模块id
                processDeviceId(buffer);
                break;
            case 0x69:
                //修改模块ID返回接口
                LogUtils.e("判断消息为修改ID确认消息");
                break;
            default:
                break;
        }
    }

    /**
     * 回复心跳数据
     *
     * @param buffer 心跳数据
     */
    private void responseHearBear(byte[] buffer) {
        if (buffer.length > 0 && buffer[4] == 0x67 && buffer[5] == 0x05) {
            LogUtils.w("心跳计算错误");
            return;
        }
        byte[] buf = new byte[]{0x00, 0x67, 0x01};
        LogUtils.e("收到心跳数据回复了");
        sendBuf(buf);
    }

    /**
     * 获取设备id
     *
     * @param buf 数据
     */
    private void processDeviceId(byte[] buf) {
        String id = "";
        if (buf.length == 14 ) {
            byte[] bufId = new byte[8];
            System.arraycopy(buf, 5, bufId, 0, 8);
            id = TransferUtils.Byte2String(bufId);
        }
        if (id.length() == 16) {
            setIdentification(id);
            if (this.eth002MessageListener != null) {
                this.eth002MessageListener.onConnectState(Eth002Handler.this, getIdentification(), true);
            }
            //发送获取指纹信息的时候就要发送延时处理
            sendFingerGetImage();
            cancelSendFingerGetImageDelay();
            sendFingerGetImageDelay();
        } else {
            sendGetId();
        }
    }

    private void processIOData(byte[] buf) {
        switch (buf[4] & 0xff) {
            case 0x70:
                //开锁
                LogUtils.d("判断消息为开锁结果返回");
                processOpenDoor(buf);
                break;
            case 0x71:
                //关锁
                LogUtils.d("判断消息为关锁");
                processDoorClosed(buf);
                break;
            case 0x72:
                //查看锁状态
                LogUtils.d("判断消息为检查锁状态 opened=" + (buf[5] == 0x01));
                processDoorState(buf);
                break;
            default:
                break;
        }
    }

    private void processOpenDoor(byte[] buf) {
        if (eth002MessageListener == null) {
            return;
        }
        eth002MessageListener.doorOpenRet(buf[5] == 0x01);
    }

    private void processDoorClosed(byte[] buf) {
        if (buf.length == 7 && buf[4] == 0x71) {
            if (eth002MessageListener != null) {
                eth002MessageListener.doorClosed(buf[5] == 0x01);
            }
        }
//        sendCheckLockState();
    }

    private void processDoorState(byte[] buf) {
        if (buf.length == 7 && buf[4] == 0x72) {
            if (eth002MessageListener != null) {
                eth002MessageListener.doorState(buf[5] == 0x01);
            }
        }
    }

    /**
     * 串口1 连接HF读卡器
     */
    private void processIdCard(byte[] buf) {
        if (buf.length == 14) {
            byte[] bufId = new byte[8];
            System.arraycopy(buf, 5, bufId, 0, 8);
            String id = TransferUtils.Byte2StringAscII(bufId);
            if (this.eth002MessageListener != null) {
                this.eth002MessageListener.reciveIDCard(id);
            }
        }

    }

    /**
     * 串口2 解析指纹仪数据
     */
    private void processFingerData(byte[] buf) {
        boolean completed = false;
        boolean error = false;
        //计算校验码
        int retCheck = DataProtocol.checkSum(buf, 0, buf.length - 1);
        if (retCheck != (buf[buf.length - 1] & 0xff)) {
            LogUtils.e("客户端" + this.getIdentification() + "接收指纹消息错误，校验码未通过；消息=" + TransferUtils.Byte2String(buf));
            error = true;
        }
        //判断指令
        if (buf[4] != 0x6b) {
            LogUtils.e("客户端" + this.getIdentification() + "接收指纹消息指令错误，需要指令为 0x6b,实际指令为" + buf[4] + " 消息=" + TransferUtils.Byte2String(buf));
            error = true;
        }
        //收到指纹数据不管对错，都要取消延时操作；
        cancelSendFingerGetImageDelay();
        if (!error) {
            /**
             * evt002 协议；按照协议进行拆包
             *
             * head  |  len   | dst/src |cmd    |  data  | check
             * 1byte |  2byte | 1byte   | 1byte | N byte | 1byte
             *
             * len = dst ~ check  包含 data,check
             * */
            int len = buf[1] * 256 + buf[2];
            byte[] fingerOrg = new byte[len - 3];
            System.arraycopy(buf, 5, fingerOrg, 0, len - 3);
            LogUtils.d("对指纹数据进行etv002拆包得到原始指纹数据: len=" + fingerOrg.length + " data=" + TransferUtils.Byte2String(fingerOrg));

            /**
             * 为保证数据能正常通过终端传输，所有上传、下发数据都拆解为2字节可见字符。
             * 如 0x23 拆分为0x32 0x33,及高4位+0x30 低4位+0x30.
             * 拆包时需要反其道而行之
             *
             * */
            byte[] finger = new byte[fingerOrg.length / 2];
            for (int i = 0, index = 0; i < fingerOrg.length && index < finger.length; i += 2, index++) {
                //解析原始数据
                Integer b = ((fingerOrg[i] % 0x10) * 0x10 + (fingerOrg[i + 1] % 0x10));
                finger[index] = (byte) (b & 0xff);
            }
            LogUtils.d("去除指纹数据中 0x3 后的指纹数据为 len=" + finger.length + "data=" + TransferUtils.Byte2String(finger));

            /**
             *
             *  指纹协议 （返回数据）
             *  头  0xEF01 | 	芯片地址  |	包标识 03/ 07/08  |	包长度（从确认码第1位开始计算）  |	确认码  |	返回参数 |	校验和
             *  2  bytes   |    4bytes	  |   1  byte	      | 2  byte                        |   1  bytes |	N  bytes |	2  bytes
             *
             *  说明：返回包每包数据大小最大为128字节，
             *  超过128字节的数据拆分为多包传输，
             *  包标识：07表示返回结果，包标识 03代表还有后续包传输，包标识 08表示数据传输结束。
             *  确认码：	00h：表示指令执行完毕或 OK 	01h：表示数据包接收错误
             *  校验位：  命令 从包标识到参数最后1个字节计算   返回 从包标识到返回参数最后1个字节计算
             *  当包标识为 02 或 08 时，没有确认码
             * */

            //头部必须为 0xef 0x01
            if (((0xff & finger[0]) != 0xef) || (0xff & finger[1]) != 0x01) {
                LogUtils.e("指纹原始数据格式错误，数据为" + TransferUtils.Byte2String(finger));
                error = true;
            }
            //计算校验码
            int postion = 0;
            //每个数据包的开始
            while (!error && postion < finger.length) {
                len = (0xff & finger[postion + 7]) * 256 + (0xff & finger[postion + 8]);
                //从len后开始计算，
                byte[] check = DataProtocol.fingerCheckSum(finger, postion + 6, len - 2 + 3);
                if (finger[postion + 9 + len - 2] != check[0] || finger[postion + 9 + len - 1] != check[1]) {
                    //确认码前有9个字节未计入len//校验未通过
                    LogUtils.e("指纹校验码计算失败，Postion=" + postion + ",data=" + TransferUtils.Byte2String(finger));
                    completed = true;
                    error = true;
                }
                LogUtils.e("指纹数据通过数据检测了" + error);
                //判断包标识  执行结果   0x07 表示执行结果，目前的指令中，07之后都还有数据
                if (finger[postion + 6] == 0x07) {
                    if ((0xff & finger[postion + 9]) != 0x00 && len == 3) {
                        LogUtils.e("指纹数据错误   0x07");
                        error = true;
                    } else if (len > 3) {
                        //有实际数据
                        LogUtils.e("有指纹数据了   0x07");
                        fingerData.put(finger, postion + 10, len - 3);
                    }
                } else if (finger[postion + 6] == 0x08) {
                    //结束
                    LogUtils.e("有指纹数据了   0x08");
                    fingerData.put(finger, postion + 9, len - 2);
                    completed = true;
                } else {
                    LogUtils.e("有中间指纹数据了");
                    fingerData.put(finger, postion + 9, len - 2);
                }
                postion = postion + 9 + len;
            }
            if (!error && completed) {
                //无错误，并读取完成
                fingerData.flip();
                byte[] fingerSData = new byte[fingerData.limit()];
                fingerData.get(fingerSData);
                String fingStr = TransferUtils.byte2Base64StringFun(fingerSData);
                if (fingerRegisterModel) {
                    fingerRegisterModel = false;
                    LogUtils.d("设备" + getIdentification() + "指纹注册成功 ,指纹数据为" + fingStr);
                    if (this.eth002MessageListener != null) {
                        //todo  fingerRegister userid
                        this.eth002MessageListener.fingerRegisterRet(true, fingStr, null);
                    }
                } else {
                    LogUtils.d("设备" + getIdentification() + " 指纹采集成功{}" + fingStr);
                    //指纹采集成功，并且不是在注册模式才回调指纹采集数据，因为点击了注册的时候，第一次是采集模式
                    if (this.eth002MessageListener != null && !waitFingerReg) {
                        this.eth002MessageListener.fingerGetImage(fingStr);
                    }
                }

            }
        }
        if (error || completed) {
            if (fingerRegisterModel) {
                //如果是注册模式，注册失败，则发送MQ消息。
                fingerRegisterModel = false;
                if (error) {
                    if (this.eth002MessageListener != null) {
                        this.eth002MessageListener.fingerRegisterRet(false, null, null);
                    }
                }
            }
            fingerData.clear();
            if (waitFingerReg) {
                sendFingerRegister();
            } else {
                sendFingerGetImage();
            }
            sendFingerGetImageDelay();
        }
    }

    private void sendFingerRegister() {
        byte[] buf = new byte[]{0x03, 0x6d, 0x3e, 0x3f, 0x30, 0x31, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x30, 0x31, 0x30, 0x30, 0x30, 0x33, 0x32, 0x31, 0x30, 0x30, 0x32, 0x35};
        fingerRegisterModel = true;
        waitFingerReg = false;
        if (this.eth002MessageListener != null) {
            this.eth002MessageListener.fingerRegisgerCmdExcuted();
        }
        sendBuf(buf);
    }

    private void sendFingerGetImage() {
        byte[] buf = new byte[]{0x03, 0x6d, 0x3e, 0x3f, 0x30, 0x31, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x3f, 0x30, 0x31, 0x30, 0x30, 0x30, 0x33, 0x32, 0x32, 0x30, 0x30, 0x32, 0x36};
        sendBuf(buf);
    }

    /**
     * 发送延时获取指纹照片信息数据包
     * 延时12秒，因为指纹返回最迟为10秒返回数据
     */
    private void sendFingerGetImageDelay() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //延时发送
                if (waitFingerReg) {
                    sendFingerRegister();
                } else {
                    sendFingerGetImage();
                }
            }
        }, 12 * 1000);
    }

    /**
     * 取消延时发送
     */
    private void cancelSendFingerGetImageDelay() {
        mHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public int openDoor() {
        return sendUnLock() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    private boolean sendUnLock() {
        byte[] buf = new byte[]{0x01, 0x70};
        return sendBuf(buf);
    }

    @Override
    public int checkLockState() {
        return sendCheckLockState() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    /***
     * 发送检测门锁状态  IO 1
     */
    private boolean sendCheckLockState() {
        byte[] buf = new byte[]{0x01, 0x72};
        return sendBuf(buf);
    }

    @Override
    public int fingerReg() {
        if (fingerRegisterModel || waitFingerReg) {
            return FunctionCode.DEVICE_BUSY;
        }
        //修改标识
        waitFingerReg = true;
        return FunctionCode.SUCCESS;
    }

    @Override
    public int openLight() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int closeLight() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public String getRemoteIP() {
        return getIP();
    }

    @Override
    public String getProducer() {
        return "Eth";
    }

    @Override
    public String getVersion() {
        return "V2.0";
    }

    @Override
    public int closeChannel() {
        return close() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LogUtils.e("连接断开了");
        mHandler.removeCallbacksAndMessages(null);
        mHandler =null;
        if (eth002MessageListener != null) {
            eth002MessageListener.onConnectState(this, getIdentification(), false);
        }
        super.channelInactive(ctx);
    }

    @Override
    public void registerMessageListener(Eth002MessageListener messageListener) {
        this.eth002MessageListener = messageListener;
        this.eth002MessageListener.setEth002HandlerInterface(this);
    }
}

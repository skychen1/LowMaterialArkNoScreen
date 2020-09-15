package com.rivamed.net.zhiang;

import android.text.TextUtils;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.local.zhiang.CharUtils;
import com.rivamed.net.callback.FingerHandler;

/**
 * describe ：指昂网络类型通道输出处理类
 *
 * @author : Yich
 * date: 2019/10/11
 */
public class ZhiAngHandler extends BaseZhiAngHandler implements FingerHandler {

    /**
     * isReading 是否在读取
     * isRegister 是否在注册
     * isFirstGetImg 是否是第一次获取到指纹图片
     * isFirstRegister 是否是注册第一获取指纹特征
     * isSameOne 中途是否有抬起手指（避免注册按的是同一次手指）
     * mFeatures 指纹特征；
     */
    private volatile boolean isReading = false;
    private volatile boolean isRegister = false;
    private volatile boolean isFirstGetImg = true;
    private volatile boolean isFirstRegister = true;
    private boolean isSameOne = false;
    private byte[] mFeatures;
    private int registerTimeOut = 10;
    private volatile long startTime;


    @Override
    void onLostConnect() {
        //设备断开
        if (mListener != null && !TextUtils.isEmpty(mId)) {
            mListener.onConnectState(this, mId, false);
        }
    }

    @Override
    void processData(byte[] buf) {
        switch (mLastSendCmd) {
            case ZhiAngProtocol.CMD_GET_IMG:
                //获取手指图片结果（检测是否有手指）
                processGetImg(buf);
                break;
            case ZhiAngProtocol.CMD_GEN_CHAR:
                //将获取到的图片提取特征值
                processGenChar(buf);
                break;
            case ZhiAngProtocol.CMD_REG_MODEL:
                //合成指纹模板
                processRegModel(buf);
                break;
            case ZhiAngProtocol.CMD_UP_CHAR:
                //上传指纹特征
                processUpChar(buf);
                break;
            case ZhiAngProtocol.CMD_GET_PARAM:
                //获取设备参数
                processParam(buf);
                break;
            default:
                break;
        }
    }


    /**
     * 解析参数
     *
     * @param buf 数据
     */
    private void processParam(byte[] buf) {
        //返回正确的验证码标识通信正常就回调连接成功
        if (buf[9] == 0x00 && mListener != null) {
            mId = getIP();
            mListener.onConnectState(this, mId, true);
        }
    }

    /**
     * 解析获取手指指令
     *
     * @param buf 数据
     */
    private void processGetImg(byte[] buf) {
        //停止读取或者注册，直接返回
        if (!isReading && !isRegister) {
            return;
        }
        //如果是注册，就先检测是否超时
        if (isRegister && checkTime()) {
            return;
        }
        //有手指了
        if (buf[9] == 0x00) {
            if (isSameOne) {
                //如果没有抬起过手指就延迟200ms再获取
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
                //如果是注册记录抬起手指的时间
                if (isRegister) {
                    startTime = System.currentTimeMillis();
                }
                sendGetImg();
                return;
            }
            //如果是第一次获取，就在获取一次，保证指纹按的完整
            if (isFirstGetImg) {
                isFirstGetImg = false;
                sendGetImg();
                return;
            }
            //修改是第一次读到手指的标识
            isFirstGetImg = true;
            //如果是读取指纹
            if (isReading) {
                //发送提取指纹特征的
                sendGenChar((byte) 0x01);
            }
            //如果是注册指纹，
            if (isRegister) {
                //如果是注册获取到第一个指纹，就提取到buffer1；
                if (isFirstRegister) {
                    //发送提取指纹特征的
                    sendGenChar((byte) 0x01);
                } else {
                    //如果不是就提取到buffer2中
                    sendGenChar((byte) 0x02);
                }
            }
        } else {
            //没有手指就延迟200ms继续发送获取手指图片指令
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                LogUtils.e(e.toString());
            }
            isSameOne = false;
            //回调剩余时间
            if (isRegister && mListener != null) {
                mListener.onRegisterTimeLeft(mId, registerTimeOut - (System.currentTimeMillis() - startTime));
            }
            sendGetImg();
        }
    }

    /**
     * 发送将获取的指纹图片提取指纹命令的指令
     *
     * @param which 提取的特征放到的缓存区
     */
    private void sendGenChar(byte which) {
        byte[] bytes = ZhiAngProtocol.pieceCommand(ZhiAngProtocol.BAG_TAG_CMD, new byte[]{ZhiAngProtocol.CMD_GEN_CHAR, which});
        sendData(bytes);
        mLastSendCmd = ZhiAngProtocol.CMD_GEN_CHAR;
    }

    /**
     * 收到指纹提取特征
     *
     * @param buf 指纹提取特征
     */
    private void processGenChar(byte[] buf) {
        if (!isReading && !isRegister) {
            return;
        }
        //查看返回结果是否提取成功
        if (buf[9] == 0x00) {
            //提取特征成功了就需要抬起手指
            isSameOne = true;
            //入股是识别提取成功就直接上传
            if (isReading) {
                sendUpChar();
            }
            //如果注册
            if (isRegister) {
                //如果是第一次生成特征码成功，就继续获取图片
                if (isFirstRegister) {
                    //回调用户抬起手指
                    if (mListener != null) {
                        mListener.onFingerUp(mId);
                    }
                    //重新记录按手指时间
                    startTime = System.currentTimeMillis();
                    //修改为不是第一次生成特征
                    isFirstRegister = false;
                    sendGetImg();
                    return;
                }
                //修改为是第一次生成特征
                isFirstRegister = true;
                //发送合成命令
                sendRegModel();
            }
        } else {
            //提取特征失败就继续发送获取图片指令；
            sendGetImg();
        }
    }

    /**
     * 发送合成模板命令
     */
    private void sendRegModel() {
        byte[] bytes = ZhiAngProtocol.pieceCommand(ZhiAngProtocol.BAG_TAG_CMD, new byte[]{ZhiAngProtocol.CMD_REG_MODEL});
        sendData(bytes);
        mLastSendCmd = ZhiAngProtocol.CMD_REG_MODEL;
    }

    /**
     * 发送上传指纹特指令
     */
    private void sendUpChar() {
        byte[] bytes = ZhiAngProtocol.pieceCommand(ZhiAngProtocol.BAG_TAG_CMD, new byte[]{ZhiAngProtocol.CMD_UP_CHAR, 0x01});
        sendData(bytes);
        mLastSendCmd = ZhiAngProtocol.CMD_UP_CHAR;
    }

    /**
     * 合成模板结果
     *
     * @param buf 结果数据
     */
    private void processRegModel(byte[] buf) {
        if (!isRegister) {
            return;
        }
        //合成成功
        if (buf[9] == 0x00) {
            sendUpChar();
            return;
        }
        isRegister = false;
        //合成失败，
        if (mListener != null) {
            mListener.onRegisterResult(mId, 2, "", null, "合成失败，请用同一根手指");
        }
    }


    /**
     * 收到上传指纹特征码协议
     *
     * @param buf 数据
     */
    private void processUpChar(byte[] buf) {
        if (!isReading && !isRegister) {
            return;
        }
        //上传指纹特征应答包
        if (buf[6] == ZhiAngProtocol.BAG_TAG_ACK) {
            //如果上传指纹特征失败就继续获取图片，如果成功了后续就会有指纹特征包，不再做其他操作
            if (buf[9] != 0x00) {
                sendGetImg();
            }
            return;
        }
        //指纹特征数据包，并且有后续包
        if (buf[6] == ZhiAngProtocol.BAG_TAG_DATA) {
            //计算出数据长度
            getChar(buf, false);
            return;
        }
        //指纹特征数据结束包
        if (buf[6] == ZhiAngProtocol.BAG_TAG_END) {
            getChar(buf, true);
        }
    }

    private void getChar(byte[] buf, boolean isEnd) {
        //取出特征数据
        int length1 = (buf[7] & 0xFF) * 256;
        int length2 = buf[8] & 0xFF;
        int nLen = length1 + length2;
        byte[] mChar = new byte[nLen - 2];
        System.arraycopy(buf, 9, mChar, 0, mChar.length);
        //如果前面已经收到特征数据了，继续累加在最后
        byte[] mBuf;
        if (mFeatures != null) {
            mBuf = new byte[mFeatures.length + mChar.length];
            System.arraycopy(mFeatures, 0, mBuf, 0, mFeatures.length);
            System.arraycopy(mChar, 0, mBuf, mFeatures.length, mChar.length);
        } else {
            mBuf = mChar;
        }
        mFeatures = mBuf;
        if (isEnd) {
            //如果是获取特征
            if (isReading) {
                //解析特征
                String fingerStr = CharUtils.byteToHexString(mFeatures, 256);
                //有回调。回调数据
                if (mListener != null) {
                    mListener.onFingerFeatures(mId, fingerStr);
                }
                //然后继续发送获取特征
                sendGetImg();
            }
            //如果是注册
            if (isRegister) {
                //获取特征
                String fingerStr = CharUtils.byteToHexString(mFeatures, 512);
                //回调数据
                if (mListener != null) {
                    mListener.onRegisterResult(mId, 0, fingerStr, null, "成功");
                }
                isRegister = false;
            }
            //结束后把数据清空
            mFeatures = null;
        }
    }

    @Override
    public int closeChannel() {
        //调用父类的关闭方法
        return close() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int startReadFinger() {
        //如果在注册或者在读取指纹就提醒正在忙
        if (isReading || isRegister) {
            return FunctionCode.DEVICE_BUSY;
        }
        //修改在读标识为true
        isReading = true;
        //置其他标志位回到原始状态
        isRegister = false;
        isFirstGetImg = true;
        isFirstRegister = true;
        isSameOne = false;
        //发送获取手指照片指令
        return sendGetImg();
    }


    @Override
    public int stopReadFinger() {
        isReading = false;
        return FunctionCode.SUCCESS;
    }

    @Override
    public int startRegisterFinger(int timeOut, String filePath) {
        //如果在注册或者在读取指纹就提醒正在忙
        if (isReading || isRegister) {
            return FunctionCode.DEVICE_BUSY;
        }
        isRegister = true;
        //置其他标志位回到原始状态
        isReading = false;
        isFirstGetImg = true;
        isFirstRegister = true;
        isSameOne = false;
        //记录超时间
        registerTimeOut = timeOut;
        startTime = System.currentTimeMillis();
        //发送获取手指照片指令
        return sendGetImg();
    }

    @Override
    public int stopRegisterFinger() {
        isRegister = false;
        return FunctionCode.SUCCESS;
    }

    /**
     * 发送获取指纹命令
     *
     * @return 发送是否成功
     */
    private int sendGetImg() {
        //拼装协议
        byte[] bytes = ZhiAngProtocol.pieceCommand(ZhiAngProtocol.BAG_TAG_CMD, new byte[]{ZhiAngProtocol.CMD_GET_IMG});
        //判断是否发送成功
        if (sendData(bytes)) {
            //成功，记录法从的指令
            mLastSendCmd = ZhiAngProtocol.CMD_GET_IMG;
            return FunctionCode.SUCCESS;
        }
        return FunctionCode.OPERATION_FAIL;
    }

    /**
     * 检测是否超时
     *
     * @return 是否超时
     */
    private boolean checkTime() {
        //如果超过了最长时间
        if (System.currentTimeMillis() - startTime > registerTimeOut) {
            //设置标识不再读取
            isRegister = false;
            //回调错误数据
            if (mListener != null) {
                mListener.onRegisterResult(mId, 1, "", null, "采集超时");
            }
            return true;
        }
        return false;
    }

    @Override
    public String getRemoteIP() {
        return getIP();
    }

    @Override
    public String getProducer() {
        return "zhi_ang";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
}

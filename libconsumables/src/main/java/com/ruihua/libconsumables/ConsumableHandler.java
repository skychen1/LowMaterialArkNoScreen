package com.ruihua.libconsumables;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.NonNull;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.TransferUtils;
import com.ruihua.libconsumables.callback.ConsumableOperate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import io.netty.channel.ChannelHandlerContext;

/**
 * describe ： 高值耗材控制板通道数据处理
 *
 * @author : Yich
 * date: 2019/9/10
 */
public class ConsumableHandler extends BaseConsumableHandler implements ConsumableOperate {
    private ConsumableManager manager;
    private String mId;
    private volatile boolean isSending;
    private byte[] lastSendBuf;
    private Handler mHandler;
    private volatile int sendTimes = 0;
    private byte[] upDateData;
    private volatile int sendPosition;
    private HandlerThread mHandlerThread;
    private volatile boolean isReadAll = false;

    public ConsumableHandler(@NonNull ConsumableManager manager) {
        this.manager = manager;
        mHandlerThread = new HandlerThread("delay_handler::::" + this.hashCode());
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    @Override
    void onLostConnect() {
        //清空消息、
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (mHandlerThread != null) {
            //断开连接就要退出循环，销毁
            mHandlerThread.quit();
            mHandlerThread = null;
        }
        LogUtils.e("3.0控制板断开了");
        if (manager.getCallback() != null) {
            manager.getCallback().onConnectState(mId, false);
        }
        manager.delDisConnectHandler(mId);
    }

    @Override
    void processData(byte[] buf) {
        //收到正确数据就取消超时重发
        cancelReSendDataDelay();
        //修改标识位
        sendTimes = 0;
        isSending = false;
        //收到消息，根据协议，低三个字段为数据类型字段
        switch (buf[2]) {
            case ControlBoardProtocol.GET_VERSION:
                processVersion(buf);
                break;
            case ControlBoardProtocol.GET_WORK_MODE:
                processMode(buf);
                break;
            case ControlBoardProtocol.CLEAN_DATA:
                processClean();
                break;
            case ControlBoardProtocol.SEND_UPDATE:
                processUpdate();
                break;
            case ControlBoardProtocol.UPDATE_CHECK:
                processUpdateCheck(buf);
                break;
            case ControlBoardProtocol.UPDATE_LOAD:
                processUpdateLoad();
                break;
            case ControlBoardProtocol.CONTROL:
                processControl(buf);
                break;
            default:
                break;
        }

    }


    /**
     * 解析获取版本号数据
     *
     * @param buf 数据
     */
    private void processVersion(byte[] buf) {
        if (buf.length != 18) {
            getFirmwareVersion();
            return;
        }
        byte[] bytes = new byte[4];
        System.arraycopy(buf, 11, bytes, 0, bytes.length);
        String version = TransferUtils.Byte2String(bytes);
        if (manager.getCallback() != null) {
            manager.getCallback().onFirmwareVersion(mId, version);
        }
    }

    /**
     * 解析获取设备工作模式数据
     *
     * @param buf 数据
     */
    private void processMode(byte[] buf) {
        //收到工作模式的回包，就通知设备连接成功
        if (manager.getCallback() != null) {
            manager.getCallback().onConnectState(mId, true);
        }
        //加入集合维护
        manager.addConnectHandler(mId, this);
        int result = buf[10] & 0xff;
        //结果为0表示是升级模式  如果是app模式就不做任何操作
        if (result == 0) {
            if (manager.getCallback() != null) {
                manager.getCallback().onNeedUpdateFile(mId);
            }
        }
    }

    /**
     * 解析清除数据
     */
    private void processClean() {
        //收到清除数据就可以开始发送数据了
        sendPosition = 0;
        sendUpdateData();
    }

    /**
     * 解析发送升级数据
     */
    private void processUpdate() {
        //先查看数据是否发送完毕，如果发送完毕
        if (upDateData.length <= sendPosition * ConsumableConfig.UPDATE_DATA_LENGTH) {
            //发送数据完毕，发送校验命令
            //先转换次数为16进制数据
            byte[] updateTimes = getUpdateTimes(sendPosition);
            lastSendBuf = ControlBoardProtocol.pieceCommand(ControlBoardProtocol.UPDATE_CHECK, updateTimes);
            sendBuf(lastSendBuf);
        } else {
            //回调已发送的数据进度
            if (manager.getCallback() != null) {
                //计算升级的进度并回调
                int percent = sendPosition * ConsumableConfig.UPDATE_DATA_LENGTH / upDateData.length;
                manager.getCallback().onUpdateProgress(mId, percent);
            }
            //继续发送数据
            sendUpdateData();
        }
    }


    private byte[] getUpdateTimes(int number) {
        int[] data = new int[10];
        int i = 0;
        byte[] result;
        if (number == 0) {
            result = new byte[]{0};
        } else {
            while (number != 0) {
                int t = number % 256;
                data[i] = t;
                i++;
                number = number / 256;
            }
            result = new byte[i];
            for (int j = 0; j < i; j++) {
                result[j] = (byte) data[i - 1 - j];
            }
        }
        LogUtils.e("解析的数据是：：：" + TransferUtils.Byte2String(result));
        return result;
    }


    /**
     * 发送升级文件
     */
    private void sendUpdateData() {
        //查看需要发送的剩余的数据
        int length;
        //计算需要发送的数据的长度
        if (upDateData.length <= (sendPosition + 1) * ConsumableConfig.UPDATE_DATA_LENGTH) {
            length = upDateData.length - sendPosition * ConsumableConfig.UPDATE_DATA_LENGTH;
        } else {
            length = ConsumableConfig.UPDATE_DATA_LENGTH;
        }
        byte[] buf = new byte[length];
        System.arraycopy(upDateData, sendPosition * ConsumableConfig.UPDATE_DATA_LENGTH, buf, 0, buf.length);
        lastSendBuf = ControlBoardProtocol.pieceCommand(ControlBoardProtocol.SEND_UPDATE, buf);
        sendBuf(lastSendBuf);
        //标识为下移；
        sendPosition++;
    }

    /**
     * 解析升级校验
     *
     * @param buf 升级校验
     */
    private void processUpdateCheck(byte[] buf) {
        int result = buf[10] & 0xff;
        //成功就发送加载命令
        if (result == 1) {
            lastSendBuf = ControlBoardProtocol.pieceCommand(ControlBoardProtocol.UPDATE_LOAD, null);
            sendBuf(lastSendBuf);
        } else {
            //回调升级失败结果
            if (manager.getCallback() != null) {
                manager.getCallback().onUpdateResult(mId, false);
            }
        }
    }

    /**
     * 解析升级加载
     */
    private void processUpdateLoad() {
        //收到之后，就开始执行升级
        if (manager.getCallback() != null) {
            manager.getCallback().onUpdateResult(mId, true);
        }
    }

    /**
     * 解析控制指令
     *
     * @param buf 数据
     */
    private void processControl(byte[] buf) {
        //那个设备
        int which = buf[10] & 0xff;
        //结果
        int result = buf[11] & 0Xff;
        if (manager.getCallback() == null) {
            return;
        }
        Log.i("3342","result   "+result+"   which   "+which);

        switch (result) {

            case 0:
                //开失败
                if (which == 0 || which == 1) {
                    if (isReadAll && which == 0) {
                        isReadAll = false;
                        sendOpenDoor(1);
                    }
                    manager.getCallback().onOpenDoor(mId, which, false);
                }

                if (which == 2 || which == 3) {
                    if (isReadAll && which == 2) {
                        isReadAll = false;
                        sendOpenLight(3);
                    }
                    manager.getCallback().onOpenLight(mId, which, false);
                }
                if (which==10){
                    manager.getCallback().onOpenDoor(mId, which, false);
                }
                if (which==11){
                    manager.getCallback().onOpenLight(mId, which, false);
                }
                break;
            case 1:
                //开成功
                if (which == 0 || which == 1) {
                    if (isReadAll && which == 0) {
                        isReadAll = false;
                        sendOpenDoor(1);
                    }
                    manager.getCallback().onOpenDoor(mId, which, true);
                }
                if (which == 2 || which == 3) {
                    if (isReadAll && which == 2) {
                        isReadAll = false;
                        sendOpenLight(3);
                    }
                    manager.getCallback().onOpenLight(mId, which, true);
                }
                if (which==10){
                    manager.getCallback().onOpenDoor(mId, which, true);
                }
                if (which==11){
                    manager.getCallback().onOpenLight(mId, which, true);
                }
                break;
            case 2:
                //关失败
                if (which == 0 || which == 1) {
                    if (isReadAll && which == 0) {
                        isReadAll = false;
                        sendCloseDoor(1);
                    }
                    manager.getCallback().onCloseDoor(mId, which, false);
                }
                if (which == 2 || which == 3) {
                    if (isReadAll && which == 2) {
                        isReadAll = false;
                        sendCloseLight(3);
                    }
                    manager.getCallback().onCloseLight(mId, which, false);
                }
                if (which==10){
                    manager.getCallback().onCloseDoor(mId, which, false);
                }
                if (which==11){
                    manager.getCallback().onCloseLight(mId, which, false);
                }
                break;
            case 3:
                //关成功
                if (which == 0 || which == 1) {
                    if (isReadAll && which == 0) {
                        isReadAll = false;
                        sendCloseDoor(1);
                    }
                    manager.getCallback().onCloseDoor(mId, which, true);
                }
                if (which == 2 || which == 3) {
                    if (isReadAll && which == 2) {
                        isReadAll = false;
                        sendCloseLight(3);
                    }
                    manager.getCallback().onCloseLight(mId, which, true);
                }
                if (which==10){
                    manager.getCallback().onCloseDoor(mId, which, true);
                }
                if (which==11){
                    manager.getCallback().onCloseLight(mId, which, true);
                }
                break;
            case 4:
                //状态开
                if (which == 0 || which == 1) {
                    if (isReadAll && which == 0) {
                        isReadAll = false;
                        sendCheckDoor(1);
                    }
                    manager.getCallback().onDoorState(mId, which, true);
                }
                if (which == 2 || which == 3) {
                    if (isReadAll && which == 2) {
                        isReadAll = false;
                        sendCheckLight(3);
                    }
                    manager.getCallback().onLightState(mId, which, true);
                }
                if (which==10){
                    manager.getCallback().onDoorState(mId, which, true);
                }
                if (which==11){
                    manager.getCallback().onLightState(mId, which, true);
                }
                break;
            case 5:
                //状态关
                if (which == 0 || which == 1) {
                    if (isReadAll && which == 0) {
                        isReadAll = false;
                        sendCheckDoor(1);
                    }
                    manager.getCallback().onDoorState(mId, which, false);
                }
                if (which == 2 || which == 3) {
                    if (isReadAll && which == 2) {
                        isReadAll = false;
                        sendCheckLight(3);
                    }
                    manager.getCallback().onLightState(mId, which, false);
                }
                if (which==10){
                    manager.getCallback().onDoorState(mId, which, false);
                }
                if (which==11){
                    manager.getCallback().onLightState(mId, which, false);
                }
                break;
            case 8:
                if (which==10){
                    //开门反馈
                    manager.getCallback().onOpenDoor(mId,which,true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public int closeChannel() {
        //调用父类的关闭方法
        return close() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int openDoor(int which) {
        isReadAll = false;
        return sendOpenDoor(which);
    }

    public int sendOpenDoor(int which) {
        if (isSending) {
            return FunctionCode.DEVICE_BUSY;
        }
        //如果传入的锁不是0或者1就返回
        if (which != 0 && which != 1) {
            return FunctionCode.PARAM_ERROR;
        }
        lastSendBuf = ControlBoardProtocol.pieceCommand(ControlBoardProtocol.CONTROL, new byte[]{(byte) which, 1});
        return sendBuf(lastSendBuf);
    }


    @Override
    public int openDoor() {
        isReadAll = true;
        return sendOpenDoor(0);
    }

    @Override
    public int closeDoor(int which) {
        isReadAll = false;
        return sendCloseDoor(which);
    }

    private int sendCloseDoor(int which) {
        if (isSending) {
            return FunctionCode.DEVICE_BUSY;
        }
        //如果传入的锁不是0或者1就返回
        if (which != 0 && which != 1) {
            return FunctionCode.PARAM_ERROR;
        }
        lastSendBuf = ControlBoardProtocol.pieceCommand(ControlBoardProtocol.CONTROL, new byte[]{(byte) which, 0});
        return sendBuf(lastSendBuf);
    }

    @Override
    public int closeDoor() {
        isReadAll = true;
        return sendCloseDoor(0);
    }

    @Override
    public int checkDoorState(int which) {
        isReadAll = false;
        return sendCheckDoor(which);
    }

    private int sendCheckDoor(int which) {
        if (isSending) {
            return FunctionCode.DEVICE_BUSY;
        }
        //如果传入的锁不是0或者1就返回
        if (which != 0 && which != 1&&which!=10) {
            return FunctionCode.PARAM_ERROR;
        }
        lastSendBuf = ControlBoardProtocol.pieceCommand(ControlBoardProtocol.CONTROL, new byte[]{(byte) which, 3});
        return sendBuf(lastSendBuf);
    }

    @Override
    public int checkDoorState() {
        isReadAll = true;
        return sendCheckDoor(0);
    }

    @Override
    public int openLight(int which) {
        isReadAll = false;
        return sendOpenLight(which);
    }

    private int sendOpenLight(int which) {
        if (isSending) {
            return FunctionCode.DEVICE_BUSY;
        }
        //如果传入的灯不是2或者3就返回
        if (which != 2 && which != 3 &&which != 11) {
            return FunctionCode.PARAM_ERROR;
        }
        lastSendBuf = ControlBoardProtocol.pieceCommand(ControlBoardProtocol.CONTROL, new byte[]{(byte) which, 1});
        return sendBuf(lastSendBuf);
    }

    @Override
    public int openLight() {
        isReadAll = true;
        return sendOpenLight(2);
    }

    @Override
    public int closeLight(int which) {
        isReadAll = false;
        return sendCloseLight(which);
    }

    private int sendCloseLight(int which) {
        if (isSending) {
            return FunctionCode.DEVICE_BUSY;
        }
        //如果传入的灯不是2或者3就返回
        if (which != 2 && which != 3&&which != 11) {
            return FunctionCode.PARAM_ERROR;
        }
        lastSendBuf = ControlBoardProtocol.pieceCommand(ControlBoardProtocol.CONTROL, new byte[]{(byte) which, 0});
        return sendBuf(lastSendBuf);
    }

    @Override
    public int closeLight() {
        isReadAll = true;
        return sendCloseLight(2);
    }

    @Override
    public int checkLightState(int which) {
        isReadAll = false;
        return sendCheckLight(which);
    }

    private int sendCheckLight(int which) {
        if (isSending) {
            return FunctionCode.DEVICE_BUSY;
        }
        //如果传入的灯不是2或者3就返回
        if (which != 2 && which != 3&&which != 11) {
            return FunctionCode.PARAM_ERROR;
        }
        lastSendBuf = ControlBoardProtocol.pieceCommand(ControlBoardProtocol.CONTROL, new byte[]{(byte) which, 3});
        return sendBuf(lastSendBuf);
    }

    @Override
    public int checkLightState() {
        isReadAll = true;
        return sendCheckLight(2);
    }

    @Override
    public int getFirmwareVersion() {
        if (isSending) {
            return FunctionCode.DEVICE_BUSY;
        }
        lastSendBuf = ControlBoardProtocol.pieceCommand(ControlBoardProtocol.GET_VERSION, null);
        return sendBuf(lastSendBuf);
    }

    /**
     * 发送命令
     *
     * @param lastSendBuf 发送的命令
     * @return 返回码
     */
    private int sendBuf(byte[] lastSendBuf) {
        isSending = true;
        if (sendData(lastSendBuf)) {
            sendTimes++;
            reSendDataDelay();
            return FunctionCode.SUCCESS;
        }
        //发送数据失败就修改标识为没有发送，并且返回操作失败；
        isSending = false;
        return FunctionCode.OPERATION_FAIL;
    }

    /**
     * 延时重发指令，防止丢包，造成流程终端
     */
    private void reSendDataDelay() {
        //超时重发次数如果大于3次，就认为设备已经断开或者出问题，就掐断设备
        if (sendTimes > ConsumableConfig.MAX_SEND_TIMES) {
            closeChannel();
            return;
        }
        //延时指定时间重发指令
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.e("超时重发数据了");
                sendBuf(lastSendBuf);
            }
        }, ConsumableConfig.RESEND_DATA_DELAY_TIME);
    }

    /**
     * 取消超时重发消息
     */
    private void cancelReSendDataDelay() {
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public int restart() {
        if (isSending) {
            return FunctionCode.DEVICE_BUSY;
        }
        mHandler.removeCallbacksAndMessages(null);
        //重启设备，不进入升级模式
        return sendRestart((byte) 0x00);
    }

    @Override
    public int update() {
        if (isSending) {
            return FunctionCode.DEVICE_BUSY;
        }
        return sendRestart((byte) 0x01);
    }

    @Override
    public int sendUpDateFile(String filePath) {
        if (isSending) {
            return FunctionCode.DEVICE_BUSY;
        }
        isSending = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //查看升级文件是否是文件
                File file = new File(filePath);
                if (!file.isFile()) {
                    if (manager.getCallback() != null) {
                        manager.getCallback().onUpdateResult(mId, false);
                    }
                }
                //拿到文件总长度
                int length = (int) file.length();
                upDateData = new byte[length];
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int i = 0;
                    int len = fileInputStream.read(buffer);
                    //读取文件内容
                    while (len > 0) {
                        //将读出来的数据拷贝到指定位置
                        System.arraycopy(buffer, 0, upDateData, i * 1024, len);
                        i++;
                        //继续将数据放到buffer中
                        len = fileInputStream.read(buffer);
                    }
                    //关闭输入流
                    fileInputStream.close();

                    LogUtils.e("文件长度：：：" + upDateData.length);
                    //文件读取完毕就发送擦除数据命令
                    lastSendBuf = ControlBoardProtocol.pieceCommand(ControlBoardProtocol.CLEAN_DATA, null);
                    sendBuf(lastSendBuf);
                } catch (Exception e) {
                    //读取文件失败，清楚已读取的数据，修改标识位
                    upDateData = null;
                    isSending = false;
                    //回调升级失败
                    LogUtils.e(e.toString());
                    if (manager.getCallback() != null) {
                        manager.getCallback().onUpdateResult(mId, false);
                    }
                } finally {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            LogUtils.e(e.toString());
                        }
                    }
                }
            }
        });
        return FunctionCode.SUCCESS;
    }


    /**
     * 发送重启指令（发出重启指令以后就断开设备，回调断开）
     *
     * @param data 重启是否进入升级模式的指令
     * @return 返回码
     */
    private int sendRestart(byte data) {
        isSending = true;
        byte[] bytes = ControlBoardProtocol.pieceCommand(ControlBoardProtocol.RESTART, new byte[]{data});
//        if (sendData(bytes)) {
//            closeChannel();
//            manager.delDisConnectHandler(mId);
//            if (manager.getCallback() != null) {
//                manager.getCallback().onConnectState(mId, false);
//            }
//            return FunctionCode.SUCCESS;
//        }
//        //发送数据失败就修改标识为没有发送，并且返回操作失败；
//        isSending = false;
//        return FunctionCode.OPERATION_FAIL;
        sendData(bytes);
        mHandler.removeCallbacksAndMessages(null);
        return FunctionCode.SUCCESS;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        mId = getIP();
        //收到设备连接的时候就或去设备模式
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastSendBuf = ControlBoardProtocol.pieceCommand(ControlBoardProtocol.GET_WORK_MODE, null);
                sendBuf(lastSendBuf);
            }
        }, 500);

    }

    @Override
    public String getRemoteIP() {
        return getIP();
    }

    @Override
    public String getProducer() {
        return "rivamed";
    }

    @Override
    public String getVersion() {
        return null;
    }
}

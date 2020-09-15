package com.rivamed.libidcard.local.ande;

import android.content.Context;

import com.rfid.api.ADReaderInterface;
import com.rfid.api.ISO14443AInterface;
import com.rfid.api.ISO14443ATag;
import com.rfid.def.ApiErrDefinition;
import com.rfid.def.RfidDef;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.TransferUtils;
import com.rivamed.libidcard.local.LocalIdCardManager;
import com.rivamed.libidcard.local.callback.IdCardOperate;

public class AndeManager implements IdCardOperate {

    private LocalIdCardManager mLocalIdCardManager;
    private ADReaderInterface reader = new ADReaderInterface();
    private Thread mThread = null;
    private boolean isRunning = false;
    private String devName = "RL8000";

    public AndeManager(LocalIdCardManager localIdCardManager) {
        this.mLocalIdCardManager = localIdCardManager;
    }

    /**
     * 连接设备，并回调结果：成功或者失败
     *
     * @param context 上下文
     */
    @Override
    public int connect(Context context) {
        String conStr;
        //调用jar包的找到设备方法
        int mUsbCnt = ADReaderInterface.EnumerateZTEK(context, 0x0403, 0x6001);
        //如果返回值小于0，就表示没有找到设备，回调给用户连接失败
        if (mUsbCnt <= 0) {
            if (mLocalIdCardManager.getCallback() != null) {
                mLocalIdCardManager.getCallback().onConnectState(devName, false);
            }
            return FunctionCode.CONNECT_FAILED;
        }
        //如果找到设备就执行打开设备命令
        conStr = String.format("RDType=%s;CommType=Z-TEK;port=1;Baund=%s;Frame=%s;Addr=255", devName, "38400", "8E1");
        int i = reader.RDR_Open(conStr);
        LogUtils.e("ApiErrDefinition.NO_ERROR: " + ApiErrDefinition.NO_ERROR);
        if (i == ApiErrDefinition.NO_ERROR) {
            //打开设备成功，添加到管理类中维护并且回调给用户
            mLocalIdCardManager.addConnectIdCard(devName, AndeManager.this);
            if (mLocalIdCardManager.getCallback() != null) {
                mLocalIdCardManager.getCallback().onConnectState(devName, true);
            }
            return FunctionCode.SUCCESS;
        }
        //打开失败，回调给用户
        if (mLocalIdCardManager.getCallback() != null) {
            mLocalIdCardManager.getCallback().onConnectState(devName, false);
        }
        return FunctionCode.CONNECT_FAILED;

    }

    /**
     * 连接设备断开
     *
     * @return 返回码
     */
    @Override
    public int disConnect() {
        if (mThread != null && mThread.isAlive()) {
            return FunctionCode.DEVICE_BUSY;
        }
        reader.RDR_Close();
        mLocalIdCardManager.delDisConnectIdCard(devName);
        if (mLocalIdCardManager.getCallback() != null) {
            mLocalIdCardManager.getCallback().onConnectState(devName, false);
        }
        //这个地方的返回，再想一下
        return FunctionCode.SUCCESS;
    }

    /**
     * 开始盘存（扫描）
     *
     * @return 返回码
     */
    @Override
    public int startRead() {
        mThread = new Thread(new InventoryThrd());
        mThread.start();
        return FunctionCode.SUCCESS;
    }

    /**
     * 停止盘存（扫描）
     *
     * @return 返回码
     */
    @Override
    public int stopRead() {
        reader.RDR_SetCommuImmeTimeout();
        isRunning = false;
        return FunctionCode.SUCCESS;
    }

    /**
     * 获取设备的厂家
     *
     * @return 返回厂家
     */
    @Override
    public String getProducer() {
        return "ande";
    }

    /**
     * 获取设备的版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return "v_1.0.0";
    }

    /**
     * 开启盘存（扫描）的线程
     * 监听扫描到的卡号，并回调
     */
    private class InventoryThrd implements Runnable {
        @Override
        public void run() {
            Object hInvenParamSpecList;
            byte newAI = RfidDef.AI_TYPE_CONTINUE;
            byte[] useAnt = null;
            hInvenParamSpecList = ADReaderInterface.RDR_CreateInvenParamSpecList();
            ISO14443AInterface.ISO14443A_CreateInvenParam(hInvenParamSpecList, (byte) 0);
            isRunning = true;
            while (isRunning) {
                int iret = reader.RDR_TagInventory(newAI, useAnt, 0, hInvenParamSpecList);
                if (iret == ApiErrDefinition.NO_ERROR || iret == -ApiErrDefinition.ERR_STOPTRRIGOCUR) {
                    Object tagReport = reader.RDR_GetTagDataReport(RfidDef.RFID_SEEK_FIRST);
                    while (tagReport != null) {
                        ISO14443ATag tag = new ISO14443ATag();
                        iret = ISO14443AInterface.ISO14443A_ParseTagDataReport(tagReport, tag);
                        if (iret == ApiErrDefinition.NO_ERROR) {
                            tagReport = reader.RDR_GetTagDataReport(RfidDef.RFID_SEEK_NEXT);
                            String icNum = TransferUtils.Byte2String(tag.uid);
                            LogUtils.e("扫到的卡号：" + icNum);
                            if (mLocalIdCardManager.getCallback() != null) {
                                mLocalIdCardManager.getCallback().onReceiveCardNum(icNum);
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
            }
        }
    }
}

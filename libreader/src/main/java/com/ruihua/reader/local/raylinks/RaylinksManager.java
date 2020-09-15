package com.ruihua.reader.local.raylinks;

import android.text.TextUtils;

import com.raylinks.ModuleControl;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.ruihua.reader.local.LocalReaderManager;
import com.ruihua.reader.local.callback.LocalReaderOperate;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * describe ： 睿芯联科reader管理类 单例模式
 *
 * @author : Yich
 * date: 2019/3/22
 */
public class RaylinksManager implements LocalReaderOperate {

    /**
     * DEVICE 设备标识
     * fd 设备值
     * mCallback 回调接口
     * loopFlag 循环读取标签数据
     */
    private int fd = -1;
    private ScheduledThreadPoolExecutor scheduled = null;
    private volatile boolean loopFlag = false;
    private volatile Map<String, Integer> epcMap = new HashMap<>();
    private LocalReaderManager localReaderManager;
    private String deviceId;
    private String filter = "";

    public RaylinksManager(LocalReaderManager manager) {
        localReaderManager = manager;
    }

    /**
     * 连接设备，
     *
     * @return 是否成功
     */
    @Override
    public int connect(String port, int baud) {
        //如果fd不为-1就标识设备已经正常连接
        if (fd != -1) {
            return FunctionCode.SUCCESS;
        }
        fd = ModuleControl.UhfReaderConnect(port, baud, RaylinksConfig.FLAG_CRC);
        LogUtils.e("连接设备：：" + fd);
        //如果等于-1就说明设备没有连接上
        if (fd == -1) {
            return FunctionCode.CONNECT_FAILED;
        }
        byte[] status = new byte[1];
        boolean b = ModuleControl.UhfGetPaStatus(fd, status, RaylinksConfig.FLAG_CRC);
        if (b) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                LogUtils.e(e.toString());
            }
            byte[] bUid = new byte[12];
            if (ModuleControl.UhfGetReaderUID(fd, bUid, RaylinksConfig.FLAG_CRC)) {
                deviceId = CommonTool.bytesToHexString(bUid, 12);
                localReaderManager.addConnectReader(deviceId, RaylinksManager.this);
                if (localReaderManager.getCallback() != null) {
                    localReaderManager.getCallback().onConnectState(deviceId, true);
                }
                return FunctionCode.SUCCESS;
            } else {
                LogUtils.e("获取id失败：：");
                fd = -1;
                return FunctionCode.CONNECT_FAILED;
            }
        } else {
            fd = -1;
            return FunctionCode.CONNECT_FAILED;
        }
    }

    /**
     * 设置功率
     *
     * @param powerInt 功率值
     * @return 设置返回码
     */
    @Override
    public int setPower(int powerInt) {
        if (fd == -1) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        byte power = (byte) powerInt;
        boolean b = ModuleControl.UhfSetPower(fd, (byte) 0x01, power, RaylinksConfig.FLAG_CRC);
        //回调数据
        if (localReaderManager.getCallback() != null) {
            localReaderManager.getCallback().onSetPower(deviceId, b);
        }
        return b ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }


    /**
     * 获取设备功率
     *
     * @return 功率，字符串类型，如果失败就是空
     */
    @Override
    public int getPower() {
        if (fd == -1) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        byte[] bPower = new byte[1];
        boolean b = ModuleControl.UhfGetPower(fd, bPower, RaylinksConfig.FLAG_CRC);
        if (b) {
            byte power = (byte) (bPower[0] & 0x7F);
            if (localReaderManager.getCallback() != null) {
                localReaderManager.getCallback().onGetPower(deviceId, (int) power);
            }
            return FunctionCode.SUCCESS;
        } else {
            return FunctionCode.OPERATION_FAIL;
        }
    }

    /**
     * 检测天线
     *
     * @return 返回码
     */
    @Override
    public int checkAnt() {
        //睿芯联科设备没有检测天线功能
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    /**
     * 获取设备频率
     *
     * @return 返回码
     */
    @Override
    public int getFrequency() {
        if (fd == -1) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        byte[] bFreMode = new byte[1];
        byte[] bFreBase = new byte[1];
        byte[] bBaseFre = new byte[2];
        byte[] bChannNum = new byte[1];
        byte[] bChannSpc = new byte[1];
        byte[] bFreHop = new byte[1];
        boolean b = ModuleControl.UhfGetFrequency(fd, bFreMode, bFreBase, bBaseFre, bChannNum, bChannSpc, bFreHop, RaylinksConfig.FLAG_CRC);
        if (!b) {
            return FunctionCode.OPERATION_FAIL;
        }
        int iFreBase0 = bBaseFre[0] & 0xFF;
        int iFreBase1 = bBaseFre[1] & 0xFF;
        int freI = (iFreBase0 << 3) + (iFreBase1 >> 5);
        int freD;
        int eFreD;
        int eFreI;
        if (bFreBase[0] == 0) {
            freD = (iFreBase1 & 0x1F) * 50;
            eFreD = (freD + bChannSpc[0] * 50 * (bChannNum[0] - 1)) % 1000;
            eFreI = freI + (freD + bChannSpc[0] * 50 * (bChannNum[0] - 1)) / 1000;
        } else {
            freD = (iFreBase1 & 0x1F) * 125;
            eFreD = (freD + bChannSpc[0] * 125 * (bChannNum[0] - 1)) % 1000;
            eFreI = freI + (freD + bChannSpc[0] * 125 * (bChannNum[0] - 1)) / 1000;
        }
        //回调数据
        if (localReaderManager.getCallback() != null) {
            localReaderManager.getCallback().onGetFrequency(deviceId, String.valueOf(freI) + "." + String.valueOf(freD) + "-" + String.valueOf(eFreI) + "." + String.valueOf(eFreD) + "MHz");
        }
        return FunctionCode.SUCCESS;
    }

    /**
     * 开始扫描,返回操作成功或者失败，操作失败需要自己弥补逻辑
     * 操作成功扫描到数据就会有回调；
     *
     * @return 操作返回码
     */
    @Override
    public int startScan(int timeOut) {
        if (fd == -1) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        //如果正在扫描就返回正在扫描中；
        if (loopFlag) {
            return FunctionCode.DEVICE_BUSY;
        }
        //清空上一次的数据
        epcMap.clear();
        boolean b = ModuleControl.UhfStartInventory(fd, (byte) 1, RaylinksConfig.INIT_Q, RaylinksConfig.FLAG_CRC);
        if (!b) {
            //调用关闭指令 ,返回调用失败码
            ModuleControl.UhfStopOperation(fd, RaylinksConfig.FLAG_CRC);
            return FunctionCode.OPERATION_FAIL;
        }
        filter = localReaderManager.getFilter();
        loopFlag = true;
        //开启读取数据的线程
        startRead();
        return FunctionCode.SUCCESS;
    }

    /**
     * 停止扫描
     *
     * @return 返回值
     */
    @Override
    public int stopScan() {
        if (fd == -1) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        //修改循环的标识
        loopFlag = false;
        //关闭线程，
        if (scheduled != null) {
            scheduled.shutdownNow();
            scheduled = null;
        }
        //操作设备
        boolean b = ModuleControl.UhfStopOperation(fd, RaylinksConfig.FLAG_CRC);
        if (b) {
            fd = -1;
            return FunctionCode.SUCCESS;
        }
        return FunctionCode.OPERATION_FAIL;
    }

    /**
     * 断开设备连接方法
     *
     * @return 返回码
     */
    @Override
    public int disConnect() {
        if (fd == -1) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        //断开连接前先停止一次扫描
        ModuleControl.UhfStopOperation(fd, RaylinksConfig.FLAG_CRC);
        //断开设备连接
        boolean b = ModuleControl.UhfReaderDisconnect(fd, RaylinksConfig.FLAG_CRC);
        if (b) {
            //将fd置为初始值
            fd = -1;
            //从集合中删除
            localReaderManager.delDisConnectReader(deviceId);
            if (localReaderManager.getCallback() != null) {
                localReaderManager.getCallback().onConnectState(deviceId, false);
            }
            return FunctionCode.SUCCESS;
        }
        return FunctionCode.OPERATION_FAIL;
    }

    /**
     * 在线程中开始读取数据
     */
    private void startRead() {
        if (scheduled == null) {
            //开启线程读取标签
            ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("Raylinks-schedule-pool-%d").daemon(true).build();
            scheduled = new ScheduledThreadPoolExecutor(1, namedThreadFactory);
        }
        scheduled.execute(new Runnable() {
            @Override
            public void run() {
                readTag();
            }
        });
    }

    /**
     * 读取数据
     */
    private void readTag() {
        byte[] bLenUii = new byte[1];
        byte[] bUii = new byte[255];
        while (loopFlag) {
            if (ModuleControl.UhfReadInventory(fd, bLenUii, bUii)) {
                String tagUii = CommonTool.bytesToHexString(bUii, bLenUii[0]);
                if (TextUtils.isEmpty(tagUii)) {
                    continue;
                }
                //丢弃前4位
                tagUii = tagUii.substring(4);
                //如果需要过滤（过滤规则不为空），并且标签不是以规则开始的，就去掉标签
                if (!TextUtils.isEmpty(filter) && !tagUii.startsWith(filter)) {
                    return;
                }
                if (epcMap.containsKey(tagUii)) {
                    //如果是已有的标签，就++
                    Integer integer = epcMap.get(tagUii);
                    integer++;
                    epcMap.put(tagUii, integer);
                } else {
                    //新标签就加入集合，并且回调数据
                    epcMap.put(tagUii, 1);
                    if (localReaderManager.getCallback() != null) {
                        localReaderManager.getCallback().onScanNewEpc(deviceId, tagUii, 0);
                    }
                }
            }
        }
    }

    /**
     * 重新扫描标签
     * 正在扫描的时候调用，可以重新返回以前扫描过的标签
     */
    @Override
    public synchronized int reScan() {
        epcMap.clear();
        return FunctionCode.SUCCESS;
    }

    /**
     * 重置设备
     *
     * @return 没有这个操作
     */
    @Override
    public int reset() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    /**
     * 删除扫描到的标签中的一个标签，从而让这个标签能够再次扫描到
     *
     * @param epc 标签
     * @return 返回码
     */
    @Override
    public synchronized int delOneEpc(String epc) {
        if (epcMap.containsKey(epc)) {
            epcMap.remove(epc);
        }
        return FunctionCode.SUCCESS;
    }


    @Override
    public String getProducer() {
        return "raylinks";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }


}

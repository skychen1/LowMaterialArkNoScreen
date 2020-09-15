package com.ruihua.reader.local;

import androidx.annotation.IntRange;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libdevicesbase.base.DeviceType;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.ruihua.reader.ReaderCallback;
import com.ruihua.reader.ReaderProducerType;
import com.ruihua.reader.local.callback.LocalReaderOperate;
import com.ruihua.reader.local.corelinks.CoreLinksConfig;
import com.ruihua.reader.local.corelinks.CoreLinksManager1;
import com.ruihua.reader.local.corelinks.CoreLinksManager2;
import com.ruihua.reader.local.raylinks.RaylinksConfig;
import com.ruihua.reader.local.raylinks.RaylinksManager;
import com.ruihua.reader.local.rodinbell.RodinbellConfig;
import com.ruihua.reader.local.rodinbell.RodinbellManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * describe ： 本地连接（usb或者串口）reader管理类(单例模式)
 *
 * @author : Yich
 * date: 2019/4/2
 */
public class LocalReaderManager {
    private static LocalReaderManager manager;

    private LocalReaderManager() {
    }

    public static synchronized LocalReaderManager getManager() {
        if (manager == null) {
            manager = new LocalReaderManager();
        }
        return manager;
    }

    /**
     * 保存各个已连接的Reader的集合
     */
    private Map<String, LocalReaderOperate> connectReader = new HashMap<>();
    private ReaderCallback mCallback;
    private String mFilter = "";

    /**
     * 添加连接好的设备到集合中
     *
     * @param readerId 设备id，唯一标识
     * @param operate  设备通用接口
     */
    public void addConnectReader(String readerId, LocalReaderOperate operate) {
        connectReader.put(readerId, operate);
    }


    /**
     * 删除断开连接的设备
     *
     * @param readerId 设备标识
     */
    public void delDisConnectReader(String readerId) {
        connectReader.remove(readerId);
    }

    /**
     * 获取已连接的设备
     *
     * @return 已连接的设备的集合
     */
    public List<DeviceInfo> getConnectedDevice() {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        for (Map.Entry<String, LocalReaderOperate> d : connectReader.entrySet()) {
            if (d.getValue() != null) {
                deviceInfos.add(new DeviceInfo(d.getKey(), "", d.getValue().getProducer(), d.getValue().getVersion(), DeviceType.DEVICE_TYPE_RFID_READER));
            }
        }
        return deviceInfos;
    }

    /**
     * 连接本地设备， 传入设备类型
     *
     * @param type 设备厂家的类型，LocalReaderType类中列出
     * @return 返回码
     */
    public int connect(int type) {
        //根据传入的类型，连接对应的设备
        int code;
        switch (type) {
            case ReaderProducerType.TYPE_LOCAL_RAYLINKS:
                RaylinksManager raylinksManager = new RaylinksManager(this);
                code = raylinksManager.connect(RaylinksConfig.COM_STR, RaylinksConfig.BAND_RATE);
                break;
            case ReaderProducerType.TYPE_LOCAL_CORELINKS:
                CoreLinksManager1 coreLinksManager1 = new CoreLinksManager1(this);
                code = coreLinksManager1.connect(CoreLinksConfig.READER_COM_STR, CoreLinksConfig.READER_BOUND);
                break;
            case ReaderProducerType.TYPE_LOCAL_CORELINKS_TWO:
                CoreLinksManager2 coreLinksManager2 = new CoreLinksManager2(this);
                code = coreLinksManager2.connect(CoreLinksConfig.READER_COM_STR, CoreLinksConfig.READER_BOUND);
                break;
            case ReaderProducerType.TYPE_LOCAL_RODINBELL:
                RodinbellManager rodinbellManager = new RodinbellManager(this);
                code = rodinbellManager.connect(RodinbellConfig.COM_STR, RodinbellConfig.BAND_RATE);
                break;
            default:
                code = FunctionCode.NOT_SUPPORT_PRODUCER_TYPE;
                break;
        }
        return code;
    }

    /**
     * 断开指定设备连接
     *
     * @param readerId ，设备的id值
     */
    public int disConnect(String readerId) {
        LocalReaderOperate operate = connectReader.get(readerId);
        if (operate == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return operate.disConnect();
    }

    /**
     * 关闭所有已连接的设备
     *
     * @return 返回码
     */
    public int stopAllReader() {
        //遍历集合中所有的reader，并且关闭
        for (LocalReaderOperate reader : connectReader.values()) {
            reader.disConnect();
        }
        //清空集合
        connectReader.clear();
        return FunctionCode.SUCCESS;
    }

    /**
     * 开始扫描
     *
     * @param readerId 设备id
     * @param timeOut  超时时间
     * @return 操作返回码
     */
    public int startScan(String readerId, int timeOut) {
        //如果没有指定的设备，返回没有该设备的码
        LocalReaderOperate operate = connectReader.get(readerId);
        if (operate == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return operate.startScan(timeOut);
    }

    /**
     * 重新扫描，实际是清空下部去重集合数据，能够重新扫描返回数据
     *
     * @param readerId 设备唯一标识
     * @return 返回码
     */
    public int reScan(String readerId) {
        //如果没有指定的设备，返回没有该设备的码
        LocalReaderOperate operate = connectReader.get(readerId);
        if (operate == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return operate.reScan();
    }

    /**
     * 停止扫描
     *
     * @param readerId 设备唯一标识
     * @return 返回码
     */
    public int stopScan(String readerId) {
        LocalReaderOperate operate = connectReader.get(readerId);
        if (operate == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return operate.stopScan();
    }

    /**
     * 设置设备的功率
     *
     * @param readerId 设备唯一标识
     * @param power    功率（限制输入范围是1-30）
     * @return 返回码
     */
    public int setPower(String readerId, @IntRange(from = 1, to = 30) int power) {
        LocalReaderOperate operate = connectReader.get(readerId);
        if (operate == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return operate.setPower(power);
    }

    /**
     * 查询设备现在的输出功率
     *
     * @param readerId 设备唯一标识
     * @return 返回码
     */
    public int getPower(String readerId) {
        LocalReaderOperate operate = connectReader.get(readerId);
        if (operate == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return operate.getPower();
    }

    /**
     * 检测设备天线
     *
     * @param readerId 设备id
     * @return 返回码
     */
    public int checkAnt(String readerId) {
        LocalReaderOperate operate = connectReader.get(readerId);
        if (operate == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return operate.checkAnt();
    }

    /**
     * 重置设备
     *
     * @param readerId 设备唯一标识
     * @return 返回码
     */
    public int restDevice(String readerId) {
        LocalReaderOperate operate = connectReader.get(readerId);
        if (operate == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return operate.reset();
    }

    /**
     * 获取设备频率
     *
     * @param readerId 设备id
     * @return 返回码
     */
    public int getFrequency(String readerId) {
        LocalReaderOperate operate = connectReader.get(readerId);
        if (operate == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return operate.getFrequency();
    }

    /**
     * 删除下部去重结合中的某个epc，使其能够再次返回这个标签
     *
     * @param readerId 设备id
     * @param epc      epc
     * @return 返回码
     */
    public int delOneEpc(String readerId, String epc) {
        LocalReaderOperate operate = connectReader.get(readerId);
        if (operate == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return operate.delOneEpc(epc);
    }


    /**
     * 设置过滤规则
     *
     * @param filterStr 过滤的字符串
     */
    public void setFilter(String filterStr) {
        this.mFilter = filterStr;
    }

    /**
     * 获取过滤规则
     *
     * @return 规则字符串
     */
    public String getFilter() {
        return this.mFilter;
    }

    /**
     * 获取回调
     *
     * @return 回调接口实例
     */
    public ReaderCallback getCallback() {
        return mCallback;
    }

    /**
     * 注册监听回调
     *
     * @param callback 回调
     */
    public void registerCallback(ReaderCallback callback) {
        this.mCallback = null;
        this.mCallback = callback;
    }

    /**
     * 反注册回调
     */
    public void unRegisterCallback() {
        this.mCallback = null;
    }

}

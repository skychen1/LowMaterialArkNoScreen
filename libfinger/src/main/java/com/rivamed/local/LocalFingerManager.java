package com.rivamed.local;

import android.app.Activity;

import com.rivamed.FingerCallback;
import com.rivamed.FingerType;
import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libdevicesbase.base.DeviceType;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.local.zhiang.ZhiAngManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * describe ：指纹本地连接管理类(单例模式)
 *
 * @author : Yich
 * date: 2019/5/14
 */
public class LocalFingerManager {
    private static LocalFingerManager manager;

    private LocalFingerManager() {
    }

    public static synchronized LocalFingerManager getManager() {
        if (manager == null) {
            manager = new LocalFingerManager();
        }
        return manager;
    }

    /**
     * 保存各个已连接的设备的集合
     */
    private Map<String, LocalFingerOperate> connectFinger = new HashMap<>();
    private FingerCallback mCallback;

    /**
     * 添加连接好的设备到集合中
     *
     * @param readerId 设备id，唯一标识
     * @param operate  设备通用接口
     */
    public void addConnectFinger(String readerId, LocalFingerOperate operate) {
        //如果集合中有该设备，删除该设备
        connectFinger.put(readerId, operate);
    }

    /**
     * 删除断开连接的设备
     *
     * @param readerId 设备标识
     */
    public void delDisConnectFinger(String readerId) {
        connectFinger.remove(readerId);
    }

    /**
     * 获取已连接的设备
     *
     * @return 已连接的设备的集合
     */
    public List<DeviceInfo> getConnectedDevice() {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        for (Map.Entry<String, LocalFingerOperate> d : connectFinger.entrySet()) {
            if (d.getValue() != null) {
                deviceInfos.add(new DeviceInfo(d.getKey(), "", d.getValue().getProducer(), d.getValue().getVersion(), DeviceType.DEVICE_TYPE_FINGER));
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
    public int connect(Activity context, int type) {
        //根据传入的类型，连接对应的设备
        int code;
        switch (type) {
            case FingerType.TYPE_LOCAL_ZHI_ANG:
                ZhiAngManager mManager = new ZhiAngManager(this);
                code = mManager.connect(context);
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
     * @param fingerId ，设备的id值
     */
    public int disConnect(String fingerId) {
        LocalFingerOperate fingerOperate = connectFinger.get(fingerId);
        if (fingerOperate == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return fingerOperate.disConnect();
    }

    /**
     * 关闭所有已连接的设备
     *
     * @return 返回码
     */
    public int stopAllReader() {
        //遍历集合中所有的reader，并且关闭
        for (LocalFingerOperate reader : connectFinger.values()) {
            reader.disConnect();
        }
        //清空集合
        connectFinger.clear();
        return FunctionCode.SUCCESS;
    }

    /**
     * 开始读取指纹
     *
     * @param fingerId 指纹id
     * @return 返回码
     */
    public int startReadFinger(String fingerId) {
        LocalFingerOperate fingerOperate = connectFinger.get(fingerId);
        if (fingerOperate == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return fingerOperate.startReadFinger();
    }

    /**
     * 停止读取指纹
     *
     * @param fingerId 指纹id
     * @return 返回码
     */
    public int stopReadFinger(String fingerId) {
        LocalFingerOperate fingerOperate = connectFinger.get(fingerId);
        if (fingerOperate == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return fingerOperate.stopReadFinger();
    }

    /**
     * 开始注册指纹
     *
     * @param fingerId 指纹id
     * @param timeOut  超时时长（s）
     * @param filePath 图片文件保存位置（文件夹）
     * @return 返回码
     */
    public int startRegisterFinger(String fingerId, int timeOut, String filePath) {
        LocalFingerOperate fingerOperate = connectFinger.get(fingerId);
        if (fingerOperate == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return fingerOperate.startRegisterFinger(timeOut, filePath);
    }

    /**
     * 停止注册指纹
     *
     * @param fingerId 指纹id
     * @return 返回码
     */
    public int stopRegisterFinger(String fingerId) {
        LocalFingerOperate fingerOperate = connectFinger.get(fingerId);
        if (fingerOperate == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return fingerOperate.stopRegisterFinger();
    }


    /**
     * 获取回调
     *
     * @return 回调接口实例
     */
    public FingerCallback getCallback() {
        return mCallback;
    }

    /**
     * 注册监听回调
     *
     * @param callback 回调
     */
    public void registerCallback(FingerCallback callback) {
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

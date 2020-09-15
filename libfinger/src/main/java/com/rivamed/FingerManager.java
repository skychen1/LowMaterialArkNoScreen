package com.rivamed;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libdevicesbase.base.DeviceType;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.local.LocalFingerManager;
import com.rivamed.net.NetFingerManager;
import com.rivamed.test.TestFingerHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * describe ：指纹总管理类 提供给用户各种操作方法
 *
 * @author : Yich
 * date: 2019/5/14
 */
public class FingerManager {
    private static FingerManager manager;

    private FingerManager() {
    }

    /**
     * 单例模式，一个种设备只需要一个地方操作
     *
     * @return 实例
     */
    public static synchronized FingerManager getManager() {
        if (manager == null) {
            manager = new FingerManager();
        }
        return manager;
    }

    /**
     * mConnectType 连接模式
     */
    private int mConnectType = FingerConnectType.TYPE_NET;
    private FingerCallback callBack;
    private boolean isSimulation = false;
    private TestFingerHandler testHandler;

    /**
     * 获取已连接的设备
     *
     * @return 已连接的设备的集合
     */
    public List<DeviceInfo> getConnectedFinger() {
        List<DeviceInfo> connectedDevice = new ArrayList<>();
        //根据类型返回对应的设备集合
        if (isSimulation) {
            connectedDevice.add(new DeviceInfo(TestFingerHandler.M_ID, "", testHandler.getProducer(), testHandler.getVersion(), DeviceType.DEVICE_TYPE_FINGER));
            return connectedDevice;
        }
        switch (mConnectType) {
            case FingerConnectType.TYPE_NET:
                connectedDevice = NetFingerManager.getManager().getConnectedDevice();
                break;
            case FingerConnectType.TYPE_LOCAL:
                connectedDevice = LocalFingerManager.getManager().getConnectedDevice();
                break;
            default:
                //如果连接的类型不对，返回null（理论上不会存在）
                connectedDevice = null;
                break;
        }
        return connectedDevice;
    }

    /**
     * 设置是否是模拟形式
     *
     * @param isSimulation 是否是模拟形式，true 就是模拟形式，false就是连接设备
     */
    public void setSimulation(boolean isSimulation) {
        this.isSimulation = isSimulation;
        if (isSimulation) {
            //准备测试通道
            testHandler = new TestFingerHandler(this);
        }
    }

    /**
     * 连接设备,根据设备厂家类型，启用对应的连接
     *
     * @param context      页面
     * @param producerType 厂家的类型
     * @return 返回码
     */
    public int connectFinger(Activity context, int producerType) {
        //根据选择的厂家的类型设置连接的模式（100-200之间是网络连接，200—300之间是本地连接）
        if (producerType >= 100 && producerType < 200) {
            mConnectType = FingerConnectType.TYPE_NET;
        } else if (producerType >= 200 && producerType < 300) {
            mConnectType = FingerConnectType.TYPE_LOCAL;
        }
        int code;
        switch (producerType) {
            //网络三能
            case FingerType.TYPE_NET_SANNENG:
                code = NetFingerManager.getManager().startService(40003, FingerType.TYPE_NET_SANNENG);
                break;
            //网络指昂
            case FingerType.TYPE_NET_ZHI_ANG:
                code = NetFingerManager.getManager().startService(8018, FingerType.TYPE_NET_ZHI_ANG);
                break;
            case FingerType.TYPE_LOCAL_ZHI_ANG:
                code = LocalFingerManager.getManager().connect(context, producerType);
                break;
            default:
                code = FunctionCode.NOT_SUPPORT_PRODUCER_TYPE;
                break;
        }
        return code;
    }

    /**
     * 断开设备连接，主要用于本地，因为不断开，下次可能会连不上
     *
     * @param fingerId 设备的id
     * @return 是否成功
     */
    public int disConnectFinger(String fingerId) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case FingerConnectType.TYPE_NET:
                code = NetFingerManager.getManager().closeChannel(fingerId);
                break;
            case FingerConnectType.TYPE_LOCAL:
                code = LocalFingerManager.getManager().disConnect(fingerId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 停止上一种类型的reader，切换reader类型
     *
     * @return 返回码
     */
    public int destroyFinger() {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case FingerConnectType.TYPE_NET:
                code = NetFingerManager.getManager().stopService();
                break;
            case FingerConnectType.TYPE_LOCAL:
                code = LocalFingerManager.getManager().stopAllReader();
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 开始读取指纹
     *
     * @param fingerId 设备id
     * @return 返回码
     */
    public int startReadFinger(String fingerId) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case FingerConnectType.TYPE_NET:
                code = NetFingerManager.getManager().startReadFinger(fingerId);
                break;
            case FingerConnectType.TYPE_LOCAL:
                code = LocalFingerManager.getManager().startReadFinger(fingerId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 停止读取指纹
     *
     * @param fingerId 设备id
     * @return 返回码
     */
    public int stopReadFinger(String fingerId) {
        if (isSimulation) {
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case FingerConnectType.TYPE_NET:
                code = NetFingerManager.getManager().stopReadFinger(fingerId);
                break;
            case FingerConnectType.TYPE_LOCAL:
                code = LocalFingerManager.getManager().stopReadFinger(fingerId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 开始注册指纹
     *
     * @param fingerId 设备id
     * @param timeOut  超时时间（ms）
     * @param filePath 图片文件保存位置，（文件夹全路径）
     * @return 返回码
     */
    public int startRegisterFinger(String fingerId, int timeOut, @NonNull String filePath) {
        if (isSimulation) {
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case FingerConnectType.TYPE_NET:
                code = NetFingerManager.getManager().startRegisterFinger(fingerId, timeOut, filePath);
                break;
            case FingerConnectType.TYPE_LOCAL:
                code = LocalFingerManager.getManager().startRegisterFinger(fingerId, timeOut, filePath);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 停止注册指纹
     *
     * @param fingerId 设备id
     * @return 返回码
     */
    public int stopRegisterFinger(String fingerId) {
        if (isSimulation) {
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case FingerConnectType.TYPE_NET:
                code = NetFingerManager.getManager().stopRegisterFinger(fingerId);
                break;
            case FingerConnectType.TYPE_LOCAL:
                code = LocalFingerManager.getManager().stopRegisterFinger(fingerId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }


    /**
     * 拿到回调
     *
     * @return 用户层回调
     */
    public FingerCallback getCallBack() {
        return callBack;
    }

    /**
     * 注册监听回调
     *
     * @param callBack 回调
     */
    public void registerCallback(FingerCallback callBack) {
        this.callBack = null;
        this.callBack = callBack;
        //测试模式就不需要了
        if (isSimulation) {
            return;
        }
        switch (mConnectType) {
            case FingerConnectType.TYPE_NET:
                NetFingerManager.getManager().registerCallBack(callBack);
                break;
            case FingerConnectType.TYPE_LOCAL:
                LocalFingerManager.getManager().registerCallback(callBack);
                break;
            default:
                break;
        }
    }

    /**
     * 反注册注册监听回调、
     * 在页面销毁的之前必须反注册，防止内存泄露
     */
    public void unRegisterCallback() {
        this.callBack = null;
        switch (mConnectType) {
            case FingerConnectType.TYPE_NET:
                NetFingerManager.getManager().unRegisterCallback();
                break;
            case FingerConnectType.TYPE_LOCAL:
                LocalFingerManager.getManager().unRegisterCallback();
                break;
            default:
                break;
        }
    }
}

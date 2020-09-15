package com.ruihua.reader.net;

import androidx.annotation.IntRange;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libdevicesbase.base.DeviceType;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.ruihua.reader.ReaderCallback;
import com.ruihua.reader.net.callback.ReaderHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * describe ：  扫描设备 管理类(包括罗丹贝尔，和鸿陆的)
 * 提供给用户各种设备操作方法，回调各种数据（详情见文档）
 *
 * @author : Yich
 * date: 2019/2/22
 */

public class NetReaderManager {
    private static NetReaderManager manager;

    private NetReaderManager() {
    }

    /**
     * 单例模式，一个种设备只需要一个地方操作
     *
     * @return 实例
     */
    public static synchronized NetReaderManager getManager() {
        if (manager == null) {
            manager = new NetReaderManager();
        }
        return manager;
    }

    /**
     * callback 回调。给外部使用的地方调用
     * connectHandler 所有连接设备通道的的集合，用于对设备的操作
     * mService 服务
     * isSimulation 是否是模拟形式；模拟形式下不需要连接设备，所有数据都是假数据返回  默认是正常模式
     * testHandler 测试数据的类，返回假数据
     */
    private ReaderCallback callback;
    private Map<String, ReaderHandler> connectHandler = new ConcurrentHashMap<>();
    private ReaderService mService;
    private ReaderService mService2;
    private String mFilter = "";


    /**
     * 添加一个设备的通道进行维护
     *
     * @param identification 通道唯一标识
     * @param handler        通道
     */
    public void addConnectHandler(String identification, ReaderHandler handler) {
        ReaderHandler mHandler = connectHandler.get(identification);
        if (mHandler != null) {
            mHandler.closeChannel();
            mHandler = null;
        }
        //加入到集合中维护
        connectHandler.put(identification, handler);
    }

    /**
     * 当一个设备断开的时候就删除 掉集合中维护的对应的通道
     *
     * @param identification 标识
     */
    public void delDisConnectHandler(String identification) {
        ReaderHandler handler = connectHandler.get(identification);
        if (handler != null) {
            handler.closeChannel();
            handler = null;
        }
        connectHandler.remove(identification);
    }

    public List<DeviceInfo> getConnectedDevice() {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        for (Map.Entry<String, ReaderHandler> d : connectHandler.entrySet()) {
            if (d.getValue() != null) {
                deviceInfos.add(new DeviceInfo(d.getKey(), d.getValue().getRemoteIP(), d.getValue().getProducer(), d.getValue().getVersion(), DeviceType.DEVICE_TYPE_RFID_READER));
            }
        }
        return deviceInfos;
    }


    /**
     * 开启服务
     *
     * @param port 端口号。限制输入范围是0-65536
     * @param type 设备的类型（罗丹贝尔或者鸿陆）
     */
    public int startService(@IntRange(from = 1, to = 65536) int port, int type) {
        //如果服务部存在创建该服务，只能有一个服务
        if (mService == null) {
            mService = new ReaderService(this);
            mService.startService(port, type);
            return FunctionCode.SUCCESS;
        }
        if (mService2 == null) {
            mService2 = new ReaderService(this);
            mService2.startService(port, type);
            return FunctionCode.SUCCESS;
        }
        return FunctionCode.ALREADY_START_SERVICE;
    }

    /**
     * 关闭服务，不再使用扫描设备的时候可以关闭服务
     */
    public int stopService() {
        if (mService != null) {
            mService.stopService();
            mService = null;
        }
        if (mService2 != null) {
            mService2.stopService();
            mService2 = null;
        }
        return FunctionCode.SUCCESS;
    }

    /**
     * 断开某个设备的连接
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int closeChannel(String deviceId) {
        //如果没有指定的设备，返回没有该设备的码
        ReaderHandler handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.closeChannel();
    }

    /**
     * 开始扫描的方法
     *
     * @param deviceId 设备的唯一标识id
     * @param timeOut  持续扫描的时候（多久没有新标签就借宿扫描）限制输入范围是1-10
     * @return 返回码
     */
    public int startScan(String deviceId, int timeOut) {
        if (timeOut <= 0) {
            return FunctionCode.PARAM_ERROR;
        }
        //如果没有指定的设备，返回没有该设备的码
        ReaderHandler handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        //根据id，调用对应通道id进行扫描
        return handler.startScan(timeOut);
    }

    /**
     * 开始扫描的方法
     *
     * @param deviceId 设备的唯一标识id
     * @param timeOut  持续扫描的时候（多久没有新标签就借宿扫描）限制输入范围是1-10
     * @param ants     扫描使用的天线集合
     * @return 返回码
     */
    public int startScan(String deviceId, int timeOut, byte[] ants) {
        if (timeOut <= 0) {
            return FunctionCode.PARAM_ERROR;
        }
        //如果没有指定的设备，返回没有该设备的码
        ReaderHandler handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        //根据id，调用对应通道id进行扫描
        return handler.startScan(timeOut, ants);
    }

    /**
     * 停止扫描 （该方法只能在此通道服务端停止扫描操作，不能直接停止底层扫描返回）
     * 所以调用改方法以后需要延时2秒以后才能进行下一次扫描
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int stopScan(String deviceId) {

        ReaderHandler handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.stopScan();
    }

    /**
     * 设置设备的功率
     *
     * @param deviceId 设备唯一标识
     * @param power    功率（限制输入范围是1-30）
     * @return 返回码
     */
    public int setPower(String deviceId, @IntRange(from = 1, to = 30) int power) {

        ReaderHandler handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.setPower((byte) power);
    }

    /**
     * 设置设备功率（每根天线的具体参数）
     *
     * @param deviceId 设备唯一标识
     * @param powers   功率（限制输入范围是1-30）
     * @return 返回码
     */
    public int setPower(String deviceId, @IntRange(from = 1, to = 30) int[] powers) {
        ReaderHandler handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        byte[] data = new byte[powers.length];
        for (int i = 0; i < powers.length; i++) {
            data[i] = (byte) powers[i];
        }
        return handler.setPower(data);
    }

    /**
     * 查询设备现在的输出功率
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int getPower(String deviceId) {

        ReaderHandler handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.getPower();
    }

    /**
     * 查询所有天线的状态  （暂未实现，后期处理）
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int checkAnt(String deviceId) {
        ReaderHandler handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.checkAnts();
    }

    /**
     * 重置设备
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int restDevice(String deviceId) {

        ReaderHandler handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.reset();
    }

    /**
     * 开锁
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int openLock(String deviceId) {

        ReaderHandler handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.openLock();
    }

    /**
     * 关锁
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int closeLock(String deviceId) {
        ReaderHandler handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.closeLock();
    }

    /**
     * 检测锁的状态
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int checkLockState(String deviceId) {

        ReaderHandler handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.checkLockState();
    }

    /**
     * 开灯
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int openLight(String deviceId) {

        ReaderHandler handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.openLight();
    }

    /**
     * 关灯
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int checkLightState(String deviceId) {
        ReaderHandler handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.checkLightState();
    }

    /**
     * 检测等的状态
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int closeLight(String deviceId) {
        ReaderHandler handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.closeLight();
    }

    /**
     * 拿到回调
     *
     * @return 用户层回调
     */
    public ReaderCallback getCallback() {
        return callback;
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
     * 注册监听回调
     *
     * @param callback 回调
     */
    public void registerCallback(ReaderCallback callback) {
        this.callback = null;
        this.callback = callback;
    }

    /**
     * 反注册注册监听回调、
     * 在页面销毁的之前必须反注册，防止内存泄露
     */
    public void unRegisterCallback() {
        this.callback = null;
    }

}

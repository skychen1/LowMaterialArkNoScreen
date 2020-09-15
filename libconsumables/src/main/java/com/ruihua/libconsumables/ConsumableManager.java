package com.ruihua.libconsumables;

import android.text.TextUtils;

import androidx.annotation.IntRange;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libdevicesbase.base.DeviceType;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.ruihua.libconsumables.callback.ConsumableCallBack;
import com.ruihua.libconsumables.callback.ConsumableOperate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * describe ： 高值耗材控制板管理类
 *
 * @author : Yich
 * date: 2019/9/10
 */
public class ConsumableManager {
    private static volatile ConsumableManager manager;

    private ConsumableManager() {
    }

    public static ConsumableManager getManager() {
        if (manager == null) {
            synchronized (ConsumableManager.class) {
                if (manager == null) {
                    manager = new ConsumableManager();
                }
            }
        }
        return manager;
    }

    private ConsumableCallBack mCallBack;
    private Map<String, ConsumableOperate> connectHandler = new ConcurrentHashMap<>();
    private ConsumableService mService;


    /**
     * 添加一个设备的通道进行维护
     *
     * @param id      通道唯一标识
     * @param handler 通道
     */
    public void addConnectHandler(String id, ConsumableOperate handler) {
        ConsumableOperate consumableOperate = connectHandler.get(id);
        if (consumableOperate != null) {
            consumableOperate.closeChannel();
        }
        //加入到集合中维护
        connectHandler.put(id, handler);
    }

    /**
     * 当一个设备断开的时候就删除 掉集合中维护的对应的通道
     *
     * @param id 标识
     */
    public void delDisConnectHandler(String id) {
        ConsumableOperate consumableOperate = connectHandler.get(id);
        if (consumableOperate != null) {
            consumableOperate.closeChannel();
        }
        connectHandler.remove(id);
    }

    /**
     * 获取已连接的设备
     *
     * @return 已连接的设备
     */
    public List<DeviceInfo> getConnectedDevice() {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        for (Map.Entry<String, ConsumableOperate> d : connectHandler.entrySet()) {
            if (d.getValue() != null) {
                deviceInfos.add(new DeviceInfo(d.getKey(), d.getValue().getRemoteIP(), d.getValue().getProducer(), d.getValue().getVersion(), DeviceType.DEVICE_TYPE_CONSUMABLE));
            }
        }
        return deviceInfos;
    }

    /**
     * 连接设备
     *
     * @return 开启服务是否成功
     */
    public int connect() {
        return startService(8016);
    }


    /**
     * 开启服务
     *
     * @param port 端口号。限制输入范围是0-65536
     */
    private int startService(@IntRange(from = 1, to = 65536) int port) {
        //如果服务部存在创建该服务，只能有一个服务
        if (mService == null) {
            mService = new ConsumableService(this);
            mService.startService(port);
            return FunctionCode.SUCCESS;
        }
        return FunctionCode.ALREADY_START_SERVICE;
    }

    /**
     * 停止使用控制板
     *
     * @return 是否断开
     */
    public int destroy() {
        return stopService();
    }

    /**
     * 关闭服务，不再使用扫描设备的时候可以关闭服务
     */
    private int stopService() {
        if (mService != null) {
            mService.stopService();
            mService = null;
        }
        return FunctionCode.SUCCESS;
    }

    /**
     * 断开某个设备的连接
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int disconnect(String deviceId) {
        //如果没有指定的设备，返回没有该设备的码
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.closeChannel();
    }

    /**
     * 开门
     *
     * @param deviceId 设备id
     * @param which    门的编号（目前只能是0或者1）
     * @return 返回码
     */
    public int openDoor(String deviceId, int which) {
        //如果没有指定的设备，返回没有该设备的码
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.openDoor(which);
    }

    /**
     * 开所有门
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int openDoor(String deviceId) {
        //如果没有指定的设备，返回没有该设备的码
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.openDoor();
    }

    /**
     * 关门
     *
     * @param deviceId 设备id
     * @param which    门的编号（目前只能是0或者1）
     * @return 返回码
     */
    public int closeDoor(String deviceId, int which) {
        //如果没有指定的设备，返回没有该设备的码
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.closeDoor(which);
    }

    /**
     * 关所有
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int closeDoor(String deviceId) {
        //如果没有指定的设备，返回没有该设备的码
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.closeDoor();
    }

    /**
     * 检测门状态
     *
     * @param deviceId 设备id
     * @param which    门的编号（目前只能是0或者1）
     * @return 返回码
     */
    public int checkDoorState(String deviceId, int which) {
        //如果没有指定的设备，返回没有该设备的码
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.checkDoorState(which);
    }

    /**
     * 检测所有门状态
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int checkDoorState(String deviceId) {
        //如果没有指定的设备，返回没有该设备的码
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.checkDoorState();
    }

    /**
     * 开灯
     *
     * @param deviceId 设备id
     * @param which    灯的编号（目前只能是2或者3）
     * @return 返回码
     */
    public int openLight(String deviceId, int which) {
        //如果没有指定的设备，返回没有该设备的码
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.openLight(which);
    }

    /**
     * 开所有灯
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int openLight(String deviceId) {
        //如果没有指定的设备，返回没有该设备的码
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.openLight();
    }

    /**
     * 关灯
     *
     * @param deviceId 设备id
     * @param which    灯的编号（目前只能是2或者3）
     * @return 返回码
     */
    public int closeLight(String deviceId, int which) {
        //如果没有指定的设备，返回没有该设备的码
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.closeLight(which);
    }

    /**
     * 关所有灯
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int closeLight(String deviceId) {
        //如果没有指定的设备，返回没有该设备的码
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.closeLight();
    }

    /**
     * 检测灯状态
     *
     * @param deviceId 设备id
     * @param which    灯的编号（目前只能是2或者3）
     * @return 返回码
     */
    public int checkLightState(String deviceId, int which) {
        //如果没有指定的设备，返回没有该设备的码
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.checkLightState(which);
    }

    /**
     * 检测所有灯状态
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int checkLightState(String deviceId) {
        //如果没有指定的设备，返回没有该设备的码
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.checkLightState();
    }

    /**
     * 获取设备固件版本号
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int getFirmwareVersion(String deviceId) {
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.getFirmwareVersion();
    }

    /**
     * 升级硬件设备
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int update(String deviceId) {
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        //通知设备升级，实际是让设备重启进入升级模式；
        return handler.update();
    }

    /**
     * 发送升级文件
     *
     * @param deviceId 设备id
     * @param filePath 文件全路径
     * @return 返回码
     */
    public int sendUpdateFile(String deviceId, String filePath) {
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        //文件路劲为空
        if (TextUtils.isEmpty(filePath)) {
            return FunctionCode.PARAM_ERROR;
        }
        // 文件后缀不对
        int i = filePath.lastIndexOf(".");
        if (i <= 0 || i >= filePath.length() - 1 || !"bin".equals(filePath.substring(i + 1))) {
            return FunctionCode.UPDATE_FILE_ERROR;
        }
        //文件不存在
        File file = new File(filePath);
        if (!file.exists()) {
            return FunctionCode.UPDATE_FILE_ERROR;
        }

        return handler.sendUpDateFile(filePath);
    }


    /**
     * 重启设备
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int restart(String deviceId) {
        ConsumableOperate handler = connectHandler.get(deviceId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.restart();
    }


    /**
     * 拿到回调
     *
     * @return 用户层回调
     */
    public ConsumableCallBack getCallback() {
        return mCallBack;
    }

    /**
     * 注册监听回调
     *
     * @param callback 回调
     */
    public void registerCallback(ConsumableCallBack callback) {
        this.mCallBack = null;
        this.mCallBack = callback;
    }

    /**
     * 反注册注册监听回调、
     * 在页面销毁的之前必须反注册，防止内存泄露
     */
    public void unRegisterCallback() {
        this.mCallBack = null;
    }

}

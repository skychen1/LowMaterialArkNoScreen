package com.ruihua.reader;

import androidx.annotation.IntRange;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libdevicesbase.base.DeviceType;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.ruihua.reader.local.LocalReaderManager;
import com.ruihua.reader.net.NetReaderManager;
import com.ruihua.reader.test.TestHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * describe ：  扫描设备 管理类(包括罗丹贝尔，和鸿陆的)
 * 提供给用户各种设备操作方法，回调各种数据（详情见文档）
 *
 * @author : Yich
 * date: 2019/2/22
 */

public class ReaderManager {
    private static ReaderManager manager;

    private ReaderManager() {
    }

    /**
     * 单例模式，一个种设备只需要一个地方操作
     *
     * @return 实例
     */
    public static synchronized ReaderManager getManager() {
        if (manager == null) {
            manager = new ReaderManager();
        }
        return manager;
    }

    /**
     * mConnectType  设备类型，网络的或者本地的(默认是网路的)
     * callBack 回调。给外部使用的地方调用
     * isSimulation 是否是模拟形式；模拟形式下不需要连接设备，所有数据都是假数据返回  默认是正常模式
     * testHandler 测试数据的类，返回假数据
     * filterStart 标签过滤头（过滤出以这个字符串开始的标签，公司目前是8227）
     */
    private int mConnectType = ReaderConnectType.TYPE_NET;
    private ReaderCallback callBack;
    private boolean isSimulation = false;
    private TestHandler testHandler;
    private String filterStart;


    /**
     * 获取已连接的设备
     *
     * @return 已连接的设备的集合
     */
    public List<DeviceInfo> getConnectedReader() {
        List<DeviceInfo> connectedDevice = new ArrayList<>();
        //如果是测试模式
        if (isSimulation) {
            connectedDevice.add(new DeviceInfo(TestHandler.MID, "", testHandler.getProducer(), testHandler.getVersion(), DeviceType.DEVICE_TYPE_RFID_READER));
            return connectedDevice;
        }
        //根据类型返回对应的设备集合
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                connectedDevice = NetReaderManager.getManager().getConnectedDevice();
                break;
            case ReaderConnectType.TYPE_LOCAL:
                connectedDevice = LocalReaderManager.getManager().getConnectedDevice();
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
            testHandler = new TestHandler(this);
        }
    }

    /**
     * 设置过滤字符串
     *
     * @param startStr 头字符串
     */
    public void setFilter(String startStr) {
        //记录规则
        filterStart = startStr;
        //设置规则
        filter();
    }

    /**
     * 设置过滤规则
     */
    private void filter() {
        //测试模式就不需要了
        if (isSimulation) {
            return;
        }
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                NetReaderManager.getManager().setFilter(filterStart);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                LocalReaderManager.getManager().setFilter(filterStart);
                break;
            default:
                break;
        }
    }

    /**
     * 连接设备,根据设备厂家类型，启用对应的连接
     *
     * @param producerType 厂家的类型
     * @return 返回码
     */
    public int connectReader(int producerType) {
        //根据选择的厂家的类型设置连接的模式（100-200之间是网络连接，200—300之间是本地连接）
        if (producerType >= 100 && producerType < 200) {
            mConnectType = ReaderConnectType.TYPE_NET;
        } else if (producerType >= 200 && producerType < 300) {
            mConnectType = ReaderConnectType.TYPE_LOCAL;
        }
        //如果有回调，就将回调注册到实际管理类
        if (callBack != null) {
            register(callBack);
        }
        //设置过滤规则
        filter();
        int code;
        switch (producerType) {
            //网络连接
            case ReaderProducerType.TYPE_NET_RODINBELL:
                 NetReaderManager.getManager().startService(8014, ReaderProducerType.TYPE_NET_RODINBELL);
                code = NetReaderManager.getManager().startService(8015, ReaderProducerType.TYPE_NET_RODINBELL);
                break;
            case ReaderProducerType.TYPE_NET_COLU:
                code = NetReaderManager.getManager().startService(8010, ReaderProducerType.TYPE_NET_COLU);
                break;
            //本地连接
            case ReaderProducerType.TYPE_LOCAL_RAYLINKS:
            case ReaderProducerType.TYPE_LOCAL_CORELINKS:
            case ReaderProducerType.TYPE_LOCAL_CORELINKS_TWO:
            case ReaderProducerType.TYPE_LOCAL_RODINBELL:
                code = LocalReaderManager.getManager().connect(producerType);
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
     * @param readerId 设备的id
     * @return 是否成功
     */
    public int disConnectReader(String readerId) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().closeChannel(readerId);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().disConnect(readerId);
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
    public int destroyReader() {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().stopService();
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().stopAllReader();
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }


    /**
     * 开始扫描的方法
     *
     * @param deviceId 设备的唯一标识id
     * @param timeOut  持续扫描的时候（多久没有新标签就结束扫描）建议范围在1-5秒
     *                 如果输入的时间为0；表示一直扫描，只能手动调用停止才停止（支持的设备）
     * @return 返回码
     */
    public int startScan(String deviceId, int timeOut) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            testHandler.startScan(timeOut);
            return FunctionCode.SUCCESS;
        }
        //如果时间小于0了就直接返回
        if (timeOut < 0) {
            return FunctionCode.PARAM_ERROR;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().startScan(deviceId, timeOut);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().startScan(deviceId, timeOut);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 开始扫描的方法
     *
     * @param deviceId 设备的唯一标识id
     * @param timeOut  持续扫描的时候（多久没有新标签就结束扫描）建议范围在1-5秒
     *                 如果输入的时间为0；表示一直扫描，只能手动调用停止才停止（支持的设备）
     * @param ants     需要扫描的天线的数组
     * @return 返回码
     */
    public int startScan(String deviceId, int timeOut, byte[] ants) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            testHandler.startScan(timeOut);
            return FunctionCode.SUCCESS;
        }
        //如果时间小于0了就直接返回
        if (timeOut < 0) {
            return FunctionCode.PARAM_ERROR;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().startScan(deviceId, timeOut, ants);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                //本地的不支持分天线扫描
                code = FunctionCode.DEVICE_NOT_SUPPORT;
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 停止扫描 （该方法只能在此通道服务端停止扫描操作，不能直接停止底层扫描返回）
     * 所以调用改方法以后需要延时2秒以后才能进行下一次扫描
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int stopScan(String deviceId) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            return testHandler.stopScan();
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().stopScan(deviceId);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().stopScan(deviceId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 设置设备的功率
     *
     * @param deviceId 设备唯一标识
     * @param power    功率（限制输入范围是1-30）
     *                 注意！！！！！有部分设备的功率范围不一样：：：芯联本地连接功率范围是5-30，输入其他值会提示设置失败
     * @return 返回码
     */
    public int setPower(String deviceId, @IntRange(from = 1, to = 30) int power) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            testHandler.setPower((byte) power);
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().setPower(deviceId, power);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().setPower(deviceId, power);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 设置设备的功率 (具体的每根天线值)
     *
     * @param deviceId 设备唯一标识
     * @param powers   功率（限制输入范围是1-30）
     *                 注意！！！！！有部分设备的功率范围不一样：：：芯联本地连接功率范围是5-30，输入其他值会提示设置失败
     * @return 返回码
     */
    public int setPower(String deviceId, @IntRange(from = 1, to = 30) int[] powers) {
        //如果是模拟模式，直接操作模拟类，返回正确值
//        //8根天线，设置天线的就是长度为8的数组
//        if (powers.length != 8) {
//            return FunctionCode.PARAM_ERROR;
//        }
        if (isSimulation) {
//            testHandler.setPower(powers);
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().setPower(deviceId, powers);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = FunctionCode.DEVICE_NOT_SUPPORT;
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 查询设备现在的输出功率
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int getPower(String deviceId) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            testHandler.getPower();
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().getPower(deviceId);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().getPower(deviceId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 查询所有天线的状态
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int checkAnt(String deviceId) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            testHandler.checkAnts();
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().checkAnt(deviceId);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().checkAnt(deviceId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 获取设备频率(目前就本地芯联的支持)
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int getFrequency(String deviceId) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = FunctionCode.DEVICE_NOT_SUPPORT;
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().getFrequency(deviceId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 删除单个epc（目前本地芯联支持）
     *
     * @param deviceId 设备id
     * @param epc      单个epc
     * @return 返回码
     */
    public int delOneEpc(String deviceId, String epc) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = FunctionCode.DEVICE_NOT_SUPPORT;
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().delOneEpc(deviceId, epc);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 重新扫描，实际是清空下部去重集合数据，能够重新扫描返回数据
     * 目前就本地的（芯联）reader支持
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int reScan(String deviceId) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = FunctionCode.DEVICE_NOT_SUPPORT;
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().reScan(deviceId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 重置设备
     *
     * @param deviceId 设备唯一标识
     * @return 返回码
     */
    public int restDevice(String deviceId) {
        //如果是模拟模式，直接操作模拟类，返回正确值
        if (isSimulation) {
            testHandler.reset();
            return FunctionCode.SUCCESS;
        }
        int code;
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                code = NetReaderManager.getManager().restDevice(deviceId);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                code = LocalReaderManager.getManager().restDevice(deviceId);
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
    public ReaderCallback getCallBack() {
        return callBack;
    }

    /**
     * 注册监听回调
     *
     * @param callBack 回调
     */
    public void registerCallback(ReaderCallback callBack) {
        this.callBack = null;
        this.callBack = callBack;
        register(callBack);
    }

    /**
     * 注册回调到具体连接方式上
     *
     * @param callBack 回调
     */
    private void register(ReaderCallback callBack) {
        //测试模式就不需要了
        if (isSimulation) {
            return;
        }
        switch (mConnectType) {
            case ReaderConnectType.TYPE_NET:
                NetReaderManager.getManager().registerCallback(callBack);
                break;
            case ReaderConnectType.TYPE_LOCAL:
                LocalReaderManager.getManager().registerCallback(callBack);
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
            case ReaderConnectType.TYPE_NET:
                NetReaderManager.getManager().unRegisterCallback();
                break;
            case ReaderConnectType.TYPE_LOCAL:
                LocalReaderManager.getManager().unRegisterCallback();
                break;
            default:
                break;
        }
    }

}

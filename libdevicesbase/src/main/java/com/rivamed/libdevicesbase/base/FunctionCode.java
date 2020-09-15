package com.rivamed.libdevicesbase.base;

/**
 * describe ：操作设备返回的标准码
 *
 * @author : Yich
 * data: 2019/2/27
 */
public class FunctionCode {
    private FunctionCode() {
    }

    /**
     * 操作成功
     */
    public static final int SUCCESS = 0;
    /**
     * 设备不存在（没有连接）
     */
    public static final int DEVICE_NOT_EXIST = 1;
    /**
     * 设备正忙，正在执行其他耗时的指令等
     */
    public static final int DEVICE_BUSY = 2;
    /**
     * 设备不支持该指令
     */
    public static final int DEVICE_NOT_SUPPORT = 3;

    /**
     * 参数错误
     */
    public static final int PARAM_ERROR = 4;

    /**
     * 连接设备失败
     */
    public static final int CONNECT_FAILED = 5;
    /**
     * 连接类型错误
     */
    public static final int CONNECT_TYPE_ERROR = 6;
    /**
     * 设备已存在，或者已经连接打开
     */
    public static final int DEVICE_ALREADY_CONNECTED = 7;
    /**
     * 一种设备（如reader）一次只能启动一个服务，所以不能重复启动
     * 已经启动了服务，需要先关闭服务
     */
    public static final int ALREADY_START_SERVICE = 8;
    /**
     * 没有支持的厂家类型
     */
    public static final int NOT_SUPPORT_PRODUCER_TYPE = 9;
    /**
     * USB权限不够，root权限
     */
    public static final int NO_USB_PERMISSION = 10;
    /**
     * 设备故障
     */
    public static final int DEVICE_ERROR = 11;
    /**
     * 蓝牙功能未打开
     */
    public static final int BLUE_TOOTH_NOT_OPEN = 12;

    /**
     * 升级文件错误
     */
    public static final int UPDATE_FILE_ERROR = 13;

    /**
     * 连接设备的时候没有找到对应的设备（查看设备是否已经插好）
     */
    public static final int  DEVICE_NOT_FOUND = 14;

    /**
     * 因其他原因导致操作失败
     */
    public static final int OPERATION_FAIL = 100;


}

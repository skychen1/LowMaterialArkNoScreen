package com.rivamed.local.zhiang;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.text.TextUtils;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.FilesUtils;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.local.LocalFingerManager;
import com.rivamed.local.LocalFingerOperate;
import com.za.finger.FingerPower;
import com.za.finger.ZAandroid;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * describe ： 指昂usb连接管理类
 *
 * @author : Yich
 * date: 2019/5/14
 */
public class ZhiAngManager implements LocalFingerOperate {

    /**
     * DEV_ADDRESS 设备地址，固定值
     * IMG_SIZE 图片大小，固定值
     * ID 设备id，因为厂家没有提供id，所以直接写死
     * pPassword 启动参数
     * isConnect是否连接的标识，决定后续操作
     * scheduled线程池
     * isSameOne 是否是同一次按手指，是同一次按手指，就只处理一次数据
     * isRegisterSame 注册是同一次按手指
     * registerTimeOut  超时时间
     * filePath 图片保存位置
     * isRegister 是否是注册
     * fingerStr 储存指纹注册的数据
     */
    private static final int DEV_ADDRESS = 0xffffffff;
    private static final int IMG_SIZE = 0;
    private static final String ID = "zhi_ang_001";
    private byte[] pPassword = new byte[4];
    private LocalFingerManager localReaderManager;
    private volatile boolean isConnect = false;
    private volatile boolean isSameOne = false;
    private boolean isRegisterSame = false;
    private volatile boolean isReading = false;
    private volatile int registerTimeOut = 10;
    private String filePath;
    private volatile boolean isRegister = false;
    private long startTime;
    private String fingerStr = null;
    private List<String> pictureList = new ArrayList<>();
    private ScheduledThreadPoolExecutor scheduled;
    private Activity mActivity;


    public ZhiAngManager(LocalFingerManager manager) {
        this.localReaderManager = manager;
        ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("zhi_ang-schedule-pool-%d").daemon(true).build();
        scheduled = new ScheduledThreadPoolExecutor(2, namedThreadFactory);
    }

    /**
     * 循环读取指纹信息，根据设置的模式不同走不同的功能
     */
    private Runnable mFingerRunnable = new Runnable() {
        @Override
        public void run() {
            while (isConnect) {
                if (isReading) {
                    readChar();
                }
                if (isRegister) {
                    register();
                }
                //100毫秒读一次
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
            }
        }
    };

    /**
     * 去取指纹，用于后台识别
     */
    private void readChar() {
        int nRet;
        //获取图片，如果获取图成功在进行下一步
        nRet = ZAandroid.ZAZGetImage(DEV_ADDRESS);
        if (nRet == 0) {
            nRet = ZAandroid.ZAZGetImage(DEV_ADDRESS);
        }
        if (nRet == 0) {
            //读取指纹成功了，期间没有抬起手指，就认为是同一次,就直接退回
            if (isSameOne) {
                return;
            }
            //提取指纹特征
            nRet = ZAandroid.ZAZGenChar(DEV_ADDRESS, ZAandroid.CHAR_BUFFER_A);
            //成功，就获取特征
            if (nRet == ZAandroid.PS_OK) {
                int[] length = {0, 0};
                byte[] data = new byte[512];
                //设置获取数据的大小
                ZAandroid.ZAZSetCharLen(512);
                //上传特征值到data中
                nRet = ZAandroid.ZAZUpChar(DEV_ADDRESS, ZAandroid.CHAR_BUFFER_A, data, length);
                if (nRet == ZAandroid.PS_OK) {
                    isSameOne = true;
                    //上传成功就转成16进制的字符串回调,只去前256字节的特征值
                    String result = CharUtils.byteToHexString(data, 256);
                    if (localReaderManager.getCallback() != null) {
                        localReaderManager.getCallback().onFingerFeatures(ID, result);
                    }
                }
            }
        } else {
            //有一次检查图片失败就认为抬起手指了
            isSameOne = false;
        }
    }

    /**
     * 服务器注册，需要两次录取指纹，验证是否是同一个
     */
    private void register() {
        if (checkTime()) {
            return;
        }
        int nRet;
        nRet = ZAandroid.ZAZGetImage(DEV_ADDRESS);
        if (nRet == 0) {
            nRet = ZAandroid.ZAZGetImage(DEV_ADDRESS);
        }
        if (nRet == 0) {
            if (isRegisterSame) {
                //记录抬起手指时间
                startTime = System.currentTimeMillis();
                return;
            }
            //保存图片
            savePicture();
            int[] length = {0, 0};
            byte[] data = new byte[2304];
            if (TextUtils.isEmpty(fingerStr)) {
                //第一次的指纹存到a中
                nRet = ZAandroid.ZAZGenChar(DEV_ADDRESS, ZAandroid.CHAR_BUFFER_A);
                if (nRet == ZAandroid.PS_OK) {
                    nRet = ZAandroid.ZAZUpChar(DEV_ADDRESS, ZAandroid.CHAR_BUFFER_A, data, length);
                    if (nRet == ZAandroid.PS_OK) {
                        //第一次读取指纹成功，将指纹数据保存下来
                        isRegisterSame = true;
                        fingerStr = CharUtils.byteToHexString(data, 256);
                        startTime = System.currentTimeMillis();
                        if (localReaderManager.getCallback() != null) {
                            localReaderManager.getCallback().onFingerUp(ID);
                        }
                    }
                }
            } else {
                //第二次的指纹存到b中
                nRet = ZAandroid.ZAZGenChar(DEV_ADDRESS, ZAandroid.CHAR_BUFFER_B);
                if (nRet == ZAandroid.PS_OK) {
                    //将指纹读取到bufferB中
                    nRet = ZAandroid.ZAZUpChar(DEV_ADDRESS, ZAandroid.CHAR_BUFFER_B, data, length);
                    if (nRet == ZAandroid.PS_OK) {
                        //第二次读取指纹成功，就合成指纹（合成a和b中的指纹）
                        nRet = ZAandroid.ZAZRegModule(DEV_ADDRESS);
                        if (nRet == ZAandroid.PS_OK) {
                            //合成成功，将两次的指纹特征加在一起回调
                            fingerStr = fingerStr + CharUtils.byteToHexString(data, 256);
                            //回调
                            if (localReaderManager.getCallback() != null) {
                                //成功的回调,回调后清空保存的数据
                                localReaderManager.getCallback().onRegisterResult(ID, 0, fingerStr, pictureList, "成功");
                            }
                        } else {
                            LogUtils.e("指纹合成失败：：：" + nRet);
                            //删除文件
                            for (String path : pictureList) {
                                FilesUtils.deleteFile(path);
                            }
                            //清空集合
                            pictureList.clear();
                            if (localReaderManager.getCallback() != null) {
                                //回调失败,
                                localReaderManager.getCallback().onRegisterResult(ID, 2, "", pictureList, "合成失败，请用同一根手指");
                            }
                        }
                        //回调完成以后
                        isRegister = false;
                    }
                }
            }
        } else {
            //抬起手指了就进行下一次
            isRegisterSame = false;
            //回调剩余时间
            if (localReaderManager.getCallback() != null) {
                localReaderManager.getCallback().onRegisterTimeLeft(ID, registerTimeOut - (System.currentTimeMillis() - startTime));
            }
        }
    }

    /**
     * 保存指纹图片
     */
    private void savePicture() {
        int[] len = {0, 0};
        byte[] image = new byte[256 * 360];
        ZAandroid.ZAZUpImage(DEV_ADDRESS, image, len);
        String path;
        if (TextUtils.isEmpty(fingerStr)) {
            path = filePath + "/finger1.bmp";
        } else {
            path = filePath + "/finger2.bmp";
        }
        pictureList.add(path);
        ZAandroid.ZAZImgData2BMP(image, path);
    }

    /**
     * 检测是否超时
     *
     * @return 是否超时
     */
    private boolean checkTime() {
        //如果超过了最长时间
        if (System.currentTimeMillis() - startTime > registerTimeOut) {
            //删除文件
            for (String path : pictureList) {
                FilesUtils.deleteFile(path);
            }
            pictureList.clear();
            //回调错误数据
            if (localReaderManager.getCallback() != null) {
                localReaderManager.getCallback().onRegisterResult(ID, 1, "", pictureList, "采集超时");
            }
            //设置标识不再读取
            isRegister = false;
            return true;
        }
        return false;
    }

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    /**
     * 连接设备
     *
     * @return 返回码
     */
    @Override
    public int connect(Activity context) {
        mActivity = context;
        //输入usb权限，如果失败就返回连接设备失败
        if (!checkUSB()) {
            //没有root权限，申请usb权限
            UsbManager mDevManager = ((UsbManager) context.getSystemService(Context.USB_SERVICE));
            if (mDevManager == null) {
                return FunctionCode.CONNECT_FAILED;
            }
            HashMap<String, UsbDevice> deviceList = mDevManager.getDeviceList();
            for (UsbDevice usbDevice : deviceList.values()) {
                if (usbDevice.getVendorId() == 0x2109 && (usbDevice.getProductId() == 0x7638)) {
                    if (mDevManager.hasPermission(usbDevice)) {
                        //如果设备有权限就直接打开设备
                        UsbDeviceConnection connection = mDevManager.openDevice(usbDevice);
                        //查看usb接口是否正常，不正常返回失败
                        if (!connection.claimInterface(usbDevice.getInterface(0), true)) {
                            return FunctionCode.CONNECT_FAILED;
                        }
                        if (usbDevice.getInterfaceCount() < 1) {
                            return FunctionCode.CONNECT_FAILED;
                        }
                        UsbInterface usbInterface = usbDevice.getInterface(0);
                        if (usbInterface.getEndpointCount() == 0) {
                            return FunctionCode.CONNECT_FAILED;
                        }
                        //确认设备正常以后，打开设备
                        open(connection.getFileDescriptor());
                    } else {
                        //没有权限就准备广播，申请权限申请权限
                        PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                        context.registerReceiver(mUsbReceiver, filter);
                        mDevManager.requestPermission(usbDevice, permissionIntent);
                    }
                    return FunctionCode.SUCCESS;
                }
            }
            return FunctionCode.DEVICE_NOT_FOUND;
        }
        //有root权限直接打开设备
        return open(-1);
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mActivity.unregisterReceiver(mUsbReceiver);
            UsbManager mDevManager = ((UsbManager) context.getSystemService(Context.USB_SERVICE));
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                //申请权限的结果
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                boolean booleanExtra = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false);
                //如果是自己的设备并且同意了权限才去打开设备
                if (device.getVendorId() == 0x2109 && (device.getProductId() == 0x7638) && booleanExtra) {
                    if (mDevManager == null) {
                        return;
                    }
                    UsbDeviceConnection connection = mDevManager.openDevice(device);
                    open(connection.getFileDescriptor());

                }
            }
        }
    };

    private int open(int fd) {
        LogUtils.e("打开指纹设备了：：："+fd);
        //调用so方法打开设备
        int i = ZAandroid.ZAZOpenDevice(fd, 2, 2, 6, 0, 0);
        if (i != 0) {
            return FunctionCode.CONNECT_FAILED;
        }
        //调用so方法验证连接
        int re = ZAandroid.ZAZVfyPwd(DEV_ADDRESS, pPassword);
        ZAandroid.ZAZSetImageSize(IMG_SIZE);
        if (re != 0) {
            return FunctionCode.CONNECT_FAILED;
        }
        //添加到已经连接的集合中
        localReaderManager.addConnectFinger(ID, this);
        //设置连接的标识
        isConnect = true;
        //回调
        if (localReaderManager.getCallback() != null) {
            localReaderManager.getCallback().onConnectState(ID, true);
        }
        //连接成功了就开始循环获取数据
        scheduled.execute(mFingerRunnable);
        return FunctionCode.SUCCESS;
    }

    @Override
    public int disConnect() {
        //如果没有连接，直接返回设备不存在
        if (!isConnect) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        isConnect = false;
        //调用so方法关闭设备
        ZAandroid.ZAZCloseDeviceEx();
        //关闭电源
        FingerPower.close();
        //删除集合中的设备
        localReaderManager.delDisConnectFinger(ID);
        //回调断开
        if (localReaderManager.getCallback() != null) {
            localReaderManager.getCallback().onConnectState(ID, false);
        }
        return FunctionCode.SUCCESS;
    }

    @Override
    public int startReadFinger() {
        //如果没有连接，直接返回设备不存在
        if (!isConnect) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        //如果正在注册就回设备正忙
        if (isRegister) {
            return FunctionCode.DEVICE_BUSY;
        }
        //设置需要读取标识即可
        isReading = true;
        return FunctionCode.SUCCESS;
    }

    @Override
    public int stopReadFinger() {
        //如果没有连接，直接返回设备不存在
        if (!isConnect) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        //设置需要读取标识即可
        isReading = false;
        return FunctionCode.SUCCESS;
    }

    @Override
    public int startRegisterFinger(int timeOut, String filePath) {
        //如果没有连接，直接返回设备不存在
        if (!isConnect) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        //如果是在读指纹或者是注册中返回设备正忙；
        if (isReading || isRegister) {
            return FunctionCode.DEVICE_BUSY;
        }
        //记录时间
        registerTimeOut = timeOut;
        this.filePath = filePath;
        //注册的时候就不能获取特征
        //清空原有数据
        fingerStr = null;
        pictureList.clear();
        isRegister = true;
        startTime = System.currentTimeMillis();
        return FunctionCode.SUCCESS;
    }

    @Override
    public int stopRegisterFinger() {
        //如果没有连接，直接返回设备不存在
        if (!isConnect) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        //记录时间
        isRegister = false;
        return FunctionCode.SUCCESS;
    }


    private boolean checkUSB() {
        Process process;
        DataOutputStream os;
        String path = "/dev/bus/usb/00*/*";
        String command = "chmod 777 " + path;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            return true;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return false;
    }

    @Override
    public String getProducer() {
        return "zhi_ang_ke_ji";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}

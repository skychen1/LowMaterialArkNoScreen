package android_serialport_api;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class SerialPort {

    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    public boolean openPort(File device, int baud, int flags) {
        //如果句柄不为空就表示已经连接设备了
        if (mFd != null) {
            return false;
        }
        //查看设备是否是否有读写权限，没有读写权限，动态申请
        if (!device.canRead() || !device.canWrite()) {
            try {
                Process su;
                su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    //申请权限失败，就返回连接设备失败
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        //打开串口
        mFd = open(device.getAbsolutePath(), baud, flags);
        if (mFd == null) {
            return false;
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
        return true;
    }

    /**
     * 获取输入流
     *
     * @return 输入流
     */
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    /**
     * 获取输出流
     *
     * @return 输出流
     */
    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    /**
     * jni方法，打开本地串口通道
     *
     * @param path     串口路径
     * @param baudrate 波特率
     * @param flags    状态
     * @return 返回句柄
     */
    private static native FileDescriptor open(String path, int baudrate, int flags);

    public static native void close();

    static {
        System.loadLibrary("ic_serial");
    }
}



package com.uhf.uhf.serialport;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 串口打开公路类
 *
 * @author Administrator
 */
public class SerialPort {

    private static final String TAG = "SerialPort";

    /**
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    /**
     * Build SerialPort Object
     *
     * @param device device file
     * @param baud   baud
     * @param flags  read and write mode of device files
     * @throws SecurityException Unauthorized read file
     * @throws IOException       Device file read and write exceptions
     */
    public boolean openPort(File device, int baud, int flags) {
        //检测设备权限
        if (!device.canRead() || !device.canWrite()) {
            try {
                //如果没有权限就用root修改权限
                Process su;
                su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        //打开设备
        mFd = open(device.getAbsolutePath(), baud, flags);
        if (mFd == null) {
            return false;
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
        return true;
    }

    /**
     * Get the files Inputstream
     *
     * @return Returns the Inputstream object
     */
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    /**
     * Get the files Outputstream
     *
     * @return Returns the Outputstream object
     */
    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    /**
     * Open serialport device file
     *
     * @param path     Device file path
     * @param baudrate Set the serialport baud rate
     * @param flags    Read and write mode of device files
     * @return Device file description object
     */
    private static native FileDescriptor open(String path, int baudrate, int flags);

    /**
     * Close device file
     */
    public native void close();

    static {
        System.loadLibrary("serial_port");
    }
}

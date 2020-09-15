package org.wh.engineer.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;

import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;


/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/6/4
 * 描述：
 */
public class RxPermissionUtils {
    public interface PermissionCallBack {
        void permissionCallback(boolean hasPermission);
    }


    /**
     * 检测是否有相机权限
     *
     * @param callBack
     */
    @SuppressLint("CheckResult")
    public static void checkCameraPermission(FragmentActivity activity, final PermissionCallBack callBack) {
        new RxPermissions(activity)
                .request(Manifest.permission.CAMERA)
                .subscribe(aBoolean -> {
                    if (aBoolean ) {
                        callBack.permissionCallback(true);
                    } else {
                        callBack.permissionCallback(false);
                        ToastUtils.showShort("获取相机权限失败");
                    }
                });
    }

    /**
     * 检测读写文件权限
     */
    @SuppressLint("CheckResult")
    public static void checkReadWritePermission(FragmentActivity activity, final PermissionCallBack callBack) {
        new RxPermissions(activity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        callBack.permissionCallback(true);
                    } else {
                        callBack.permissionCallback(false);
                        ToastUtils.showShort("获取存储权限失败");
                    }
                });
    }

    /**
     * 检测读写文件权限以及相机权限
     *
     * @param callBack
     */
    @SuppressLint("CheckResult")
    public static void checkCameraReadWritePermission(FragmentActivity activity, final PermissionCallBack callBack) {
        new RxPermissions(activity)
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean ) {
                        callBack.permissionCallback(true);
                    } else {
                        callBack.permissionCallback(false);
                        ToastUtils.showShort("获取相机或存储权限失败");
                    }
                });
    }


    /**
     * 6.0之前的系统 判断app是否有相机权限
     *
     * @return
     */
    private static boolean isCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open(0);
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        return canUse;
    }

    /**
     * 6.0之前的系统 判断app是否有录音权限
     *
     * @return
     */
    private static boolean isVoicePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        AudioRecord record = null;
        try {
            record = new AudioRecord(MediaRecorder.AudioSource.MIC, 22050,
						 AudioFormat.CHANNEL_CONFIGURATION_MONO,
						 AudioFormat.ENCODING_PCM_16BIT,
						 AudioRecord.getMinBufferSize(22050,
                            AudioFormat.CHANNEL_CONFIGURATION_MONO,
                            AudioFormat.ENCODING_PCM_16BIT));
            record.startRecording();
            int recordingState = record.getRecordingState();
            if (recordingState == AudioRecord.RECORDSTATE_STOPPED) {
                return false;
            }
            //第一次  为true时，先释放资源，在进行一次判定
            //************
            record.release();
            record = new AudioRecord(MediaRecorder.AudioSource.MIC, 22050,
						 AudioFormat.CHANNEL_CONFIGURATION_MONO,
						 AudioFormat.ENCODING_PCM_16BIT,
						 AudioRecord.getMinBufferSize(22050,
											AudioFormat.CHANNEL_CONFIGURATION_MONO,
											AudioFormat.ENCODING_PCM_16BIT));
            record.startRecording();
            int recordingState1 = record.getRecordingState();
            if (recordingState1 == AudioRecord.RECORDSTATE_STOPPED) {
            }
//            //**************
//            //如果两次都是true， 就返回true  原因未知
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (record != null) {
                record.release();
            }
        }
    }
}

package com.ruihua.libfacerecognitionv3.main.ui;

/**
 * describe ：
 *
 * @author : boyu
 * date: 2019/12/25
 */
public interface IFaceRegister {

    /**
     * 跳转人脸注册界面返回数据
     * @param code  -1 跳转失败
     * @param msg
     */
    void registerResult(int code, String msg);
}

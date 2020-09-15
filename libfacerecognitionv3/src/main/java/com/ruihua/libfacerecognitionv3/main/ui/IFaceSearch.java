package com.ruihua.libfacerecognitionv3.main.ui;

import com.ruihua.libfacerecognitionv3.main.model.User;

/**
 * describe ：
 *
 * @author : boyu
 * date: 2019/12/26
 */
public interface IFaceSearch {
    /**
     * 人脸识别返回数据， code = 200 时返回user
     * @param code
     * @param msg
     * @param user
     */
    void onTip(int code, String msg, User user);
}

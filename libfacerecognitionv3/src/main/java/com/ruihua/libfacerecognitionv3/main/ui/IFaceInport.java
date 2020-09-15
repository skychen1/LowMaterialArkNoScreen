package com.ruihua.libfacerecognitionv3.main.ui;

/**
 * describe ：
 *
 * @author : boyu
 * date: 2019/12/25
 */
public interface IFaceInport {

    /**
     * 导入人脸数据返回结果
     * @param code -1 导入失败
     * @param msg
     */
    void importFaceImageResult(int code, String msg);
}

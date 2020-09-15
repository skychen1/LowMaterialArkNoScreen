package com.ruihua.libfacerecognitionv3.main.ui;

/**
 * describe ：
 *      sdk授权界面均可以使用
 * @author : boyu
 * date: 2019/12/25
 */
public interface IFaceLicense {
    /**
     * 授权成功，可以使用人脸识别功能模块
     */
    void initLicenseSuccess();

    /**
     * 授权失败, 如果授权失败，可以在其中跳转授权页面
     */
    void initLicenseFail(int errorCode, String msg);

    /**
     * 离线激活,未查找到License.zip文件
     */
    void notFindLicense();
}

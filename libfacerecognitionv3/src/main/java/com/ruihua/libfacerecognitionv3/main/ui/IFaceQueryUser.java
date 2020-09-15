package com.ruihua.libfacerecognitionv3.main.ui;

import java.util.List;

/**
 * describe ：用户和用户组查询列表
 *
 * @author : boyu
 * date: 2019/12/26
 */
public interface IFaceQueryUser<T> {

     void uquerySuccess(List<T> listInfo);

     void uqueryFailure(String message);
}

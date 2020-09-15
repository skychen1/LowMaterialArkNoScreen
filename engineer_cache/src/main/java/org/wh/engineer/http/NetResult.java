package org.wh.engineer.http;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/4 16:51
 * 描述:
 * 包名:        high.rivamed.myapplication.http
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public interface NetResult {
	
	/**
	 * 成功
	 *
	 * @param result 服务器返回全部数据的result
	 */
	void onSucceed(String result);
	
	
	/**
	 * 成功,但数据为空
	 */
	void onSucceedButNoData();
	
	/**
	 * 网络失败
	 *
	 * @param result
	 */
	void onNetFailing(String result);
	
	/**
	 * 数据错误
	 *
	 * @param errorCode
	 * @param msg       中文
	 */
	void onDataError(int errorCode, String msg);

   void onError(String result);
}

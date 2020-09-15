package org.wh.engineer.utils;

import java.security.MessageDigest;

/**
 * 
 * <p>
 * 描述: 基础编码组件
 * </p>
 * <p>
 * 公司: rivamed
 * </p>
 * <p>
 * 版权: liuyg2013
 * </p>
 * 
 * @author liuyg 250000576@qq.com
 * @date 2015年2月27日 下午5:02:33
 * @version V1.0
 */
public class Coder {

	private static final String KEY_SHA = "SHA";
	private static final String CHARSET = "UTF-8";

	private static String hexString = "0123456789abcdef";


	public static void main(String[] args) {
		try {
			
			String shaPassword= Coder.shaDigest(Coder.shaDigest("000000"), "F3uJ1rZsLljRRyDn");
			//  shaPassword=3576311414e73b9b208438a28200b6bcb7b090ea
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** 
	* 方法名:          loginCheck
	* 方法功能描述:    
	* @param:   userInputPassword  用户输入密码      
	* @param:   salt  离线接口返回盐值     
	* @param:   dataPassowrd  离线接口返回密码   
	* @return:        
	* @Author:        魏小波
	* @Create Date:   2018年11月19日 下午4:23:19
	*/
	public static boolean loginCheck(String userInputPassword,String salt,String dataPassowrd)  {

	   String shaPassword= null;
	   try {
		shaPassword = Coder.shaDigest(Coder.shaDigest(userInputPassword), salt);
	   } catch (Exception e) {
		e.printStackTrace();
	   }
	   LogUtils.i("Login", "shaPassword    " + shaPassword);
	   if (shaPassword.equals(dataPassowrd)) {
		LogUtils.i("Login", "shaPasswordtrue    " + true);
		return true;
	   }else {
		return false;
	   }
	}
	/**
	 * 
	 * 将字节编码成16进制数字
	 * 
	 * @param bytes
	 *            字节数组
	 * @return 16进制数字字符串
	 */
	private static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}


	/**
	 * 
	 * 使用Sha算法生成摘要信息
	 * 
	 * @param data
	 *            需要生成摘要信息的字符串
	 * @return 生成的摘要信息
	 */
	public static String shaDigest(String data)  throws Exception{
		String result = null;
		try {
			MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
			sha.update(data.getBytes(CHARSET));
			result = bytesToHexString(sha.digest());
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 
	 * 使用sha算法生成摘要信息，默认使用sha1
	 * 
	 * @param data
	 *            需要生成摘要信息的字符串
	 * @param salt
	 * @return 生成的摘要信息
	 * @throws Exception 
	 */
	public static String shaDigest(String data, String salt) throws Exception {
		String result = null;
		try {
			MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
			sha.update(salt.getBytes(CHARSET));
			result = bytesToHexString(sha.digest(data.getBytes(CHARSET)));
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
		return result;
	}


}

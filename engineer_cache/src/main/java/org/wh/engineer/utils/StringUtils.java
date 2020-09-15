package org.wh.engineer.utils;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import org.wh.engineer.dbmodel.AccountVosBean;
import org.wh.engineer.dbmodel.OperationRoomsBean;
import org.wh.engineer.dto.vo.InventoryVo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

   public final static String UTF_8 = "utf-8";

   /**
    * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
    */
   public static boolean isEmpty(String value) {
	if (value != null && !"".equalsIgnoreCase(value.trim()) &&
	    !"null".equalsIgnoreCase(value.trim())) {
	   return false;
	} else {
	   return true;
	}
   }

   /**
    * 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true
    */
   public static boolean isEquals(String... agrs) {
	String last = null;
	for (int i = 0; i < agrs.length; i++) {
	   String str = agrs[i];
	   if (isEmpty(str)) {
		return false;
	   }
	   if (last != null && !str.equalsIgnoreCase(last)) {
		return false;
	   }
	   last = str;
	}
	return true;
   }

   /**
    * 返回一个高亮spannable
    *
    * @param content 文本内容
    * @param color   高亮颜色
    * @param start   起始位置
    * @param end     结束位置
    * @return 高亮spannable
    */
   public static CharSequence getHighLightText(
	   String content, int color, int start, int end) {
	if (TextUtils.isEmpty(content)) {
	   return "";
	}
	start = start >= 0 ? start : 0;
	end = end <= content.length() ? end : content.length();
	SpannableString spannable = new SpannableString(content);
	CharacterStyle span = new ForegroundColorSpan(color);
	spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	return spannable;
   }

   /**
    * 获取链接样式的字符串，即字符串下面有下划线
    *
    * @param resId 文字资源
    * @return 返回链接样式的字符串
    */
   public static Spanned getHtmlStyleString(int resId) {
	StringBuilder sb = new StringBuilder();
	sb.append("<a href=\"\"><u><b>").append(UIUtils.getString(resId)).append(" </b></u></a>");
	return Html.fromHtml(sb.toString());
   }

   /**
    * 格式化文件大小，不保留末尾的0
    */
   public static String formatFileSize(long len) {
	return formatFileSize(len, false);
   }

   /**
    * 格式化文件大小，保留末尾的0，达到长度一致
    */
   public static String formatFileSize(long len, boolean keepZero) {
	String size;
	DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
	DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
	if (len < 1024) {
	   size = String.valueOf(len + "B");
	} else if (len < 10 * 1024) {
	   // [0, 10KB)，保留两位小数
	   size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
	} else if (len < 100 * 1024) {
	   // [10KB, 100KB)，保留一位小数
	   size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
	} else if (len < 1024 * 1024) {
	   // [100KB, 1MB)，个位四舍五入
	   size = String.valueOf(len / 1024) + "KB";
	} else if (len < 10 * 1024 * 1024) {
	   // [1MB, 10MB)，保留两位小数
	   if (keepZero) {
		size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / 1024 / (float) 100)) +
			 "MB";
	   } else {
		size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100) + "MB";
	   }
	} else if (len < 100 * 1024 * 1024) {
	   // [10MB, 100MB)，保留一位小数
	   if (keepZero) {
		size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float) 10)) +
			 "MB";
	   } else {
		size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
	   }
	} else if (len < 1024 * 1024 * 1024) {
	   // [100MB, 1GB)，个位四舍五入
	   size = String.valueOf(len / 1024 / 1024) + "MB";
	} else {
	   // [1GB, ...)，保留两位小数
	   size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
	}
	return size;
   }

   /*判断手机好的正则表达式 */
   public static boolean isPhoneNumberValid(String phoneNumber) {
	boolean isValid = false;
	String expression = "^[1][3,4,5,7,8][0-9]{9}$";
	CharSequence inputStr = phoneNumber;
	Pattern pattern = Pattern.compile(expression);
	Matcher matcher = pattern.matcher(inputStr);
	if (matcher.matches()) {
	   isValid = true;
	}
	return isValid;
   }

   /*判断密码的正则表达式 */
   public static boolean isPsWNumberValid(String pswNumber) {
	boolean isValid = false;

	//	String expression = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$";
	String expression = "^[a-zA-Z0-9]{6,12}$";
	CharSequence inputStr = pswNumber;
	Pattern pattern = Pattern.compile(expression);
	Matcher matcher = pattern.matcher(inputStr);
	if (matcher.matches()) {
	   isValid = true;
	}
	return isValid;
   }

   /**
    * 判断是否纯数字
    */
   public static boolean isPhone(String phone) {

	String str = "[0-9]+";

	return Pattern.matches(str, phone);

   }

   /**
    * 校验邮箱
    *
    * @param email
    * @return 校验通过返回true，否则返回false
    */
   public static boolean isEmail(String email) {
	return Pattern.matches(
		"^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",
		email);
   }

   /**
    * 获取指定位数的随机字符串(包含小写字母、大写字母、数字,0<length)
    *
    * @param length
    * @return
    */
   public static String getRandomString(int length) {
	//随机字符串的随机字符库
	String KeyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	StringBuffer sb = new StringBuffer();
	int len = KeyString.length();
	for (int i = 0; i < length; i++) {
	   sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
	}
	return sb.toString();
   }

   /**
    * 版本号比较
    *
    * @param version1
    * @param version2
    * @return
    */
   public static int compareVersion(String version1, String version2) {
	if (version1.equals(version2)) {
	   return 0;
	}
	String[] version1Array = version1.split("\\.");
	String[] version2Array = version2.split("\\.");

	int index = 0;
	// 获取最小长度值
	int minLen = Math.min(version1Array.length, version2Array.length);
	int diff = 0;
	// 循环判断每位的大小
	while (index < minLen && (diff =
		Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0) {
	   index++;
	}
	if (diff == 0) {
	   // 如果位数不一致，比较多余位数
	   for (int i = index; i < version1Array.length; i++) {
		if (Integer.parseInt(version1Array[i]) > 0) {
		   return 1;
		}
	   }

	   for (int i = index; i < version2Array.length; i++) {
		if (Integer.parseInt(version2Array[i]) > 0) {
		   return -1;
		}
	   }
	   return 0;
	} else {
	   return diff > 0 ? 1 : -1;
	}
   }
   /**
    * 弹窗过期
    * @param voList
    * @return
    */
   public static boolean isExceedTime( List<InventoryVo> voList) {
	for (InventoryVo s : voList) {
	   if (s.getIsErrorOperation() == 1&&s.getDeleteCount()==0) {
		return true;
	   }
	}
	return false;
   }
   /**
    * 将列表中重复的用户移除，重复指的是EPC相同
    * @param userList
    * @return
    */
   public static ArrayList<String> removeDuplicteUsers(ArrayList<String> userList) {
	   try {
		   Set<String> s = new TreeSet<String>(new Comparator<String>() {
              @Override
              public int compare(String o1, String o2) {
               return o1.compareTo(o2);
              }
           });
		   s.addAll(userList);
		   return new ArrayList<String>(s);
	   } catch (Exception e) {
		   e.printStackTrace();
		   return new ArrayList<String>();
	   }
   }

   /**
    *
    * @param list
    * @return
    */
   public static String listToString(List<String> list) {
	String string ="扫描到以下未知耗材：\n";
	for (int i = 0; i < list.size(); i++) {
	   String keyString = list.get(i);
	   if (i==list.size()-1){
		string += keyString;
	   }else {
		string += keyString + "\n";
	   }
	}
	return string;
   }
   /**
    * 无网络情况下的多的epc
    * @param list
    * @return
    */
   public static String listToStrings(List<String> list) {
	String string ="扫描到以下无网状态入柜EPC：\n";
	for (int i = 0; i < list.size(); i++) {
	   String keyString = list.get(i);
	   if (i==list.size()-1){
		string += keyString;
	   }else {
		string += keyString + "\n";
	   }
	}
	return string;
   }
   //模糊查询
   public List likeString(String likename,List<InventoryVo> list){
	for(int i=0;i<list.size();i++){
	   if(((list.get(i))).getCstName().indexOf(likename)<=-1)
		list.remove(i);
	}
	return list;

   }
   public static List<InventoryVo>  search(String name,String cstId,List<InventoryVo> list) {
	List<InventoryVo> results = new ArrayList();
	String pattenName = Pattern.quote("" + name);
	String pattenCstId = Pattern.quote("" + cstId);
	Pattern pattern = Pattern.compile(pattenName);
	Pattern pattern3 = Pattern.compile(pattenName,Pattern.CASE_INSENSITIVE);
	Pattern pattern2 = Pattern.compile(pattenCstId,Pattern.CASE_INSENSITIVE);
	for (InventoryVo vo : list) {
	   Matcher matcher = pattern.matcher(vo.getCstName());
	   Matcher matcher2 = pattern2.matcher(vo.getCstSpec());
	   Matcher matcher4 = pattern3.matcher(Pinyin4jUtil.getFirstSpell(vo.getCstName()));
	   if(matcher4.lookingAt()||matcher.find()||matcher2.find()){
		results.add(vo);
	   }
	}
//	for(int i=0; i < list.size(); i++){
//	   Matcher matcher = pattern.matcher(list.get(i).getCstName());
//	   Matcher matcher2 = pattern2.matcher(list.get(i).getCstSpec());
//	   Log.i("FADADF"," FAEE           "+Pinyin4jUtil.getFirstSpell(list.get(i).getCstName()));
//	   Matcher matcher4 = pattern3.matcher(Pinyin4jUtil.getFirstSpell(list.get(i).getCstName()));
//
//	   if(matcher4.lookingAt()||matcher.find()||matcher2.find()){
//		results.add(list.get(i));
//	   }
//	}
	return results;
   }
   /**
    * 是否包含锁
    *
    * @return
    */
   public static boolean getStringType(ArrayList<String> vos, String DeviceId) {
	if (vos != null) {
	   Iterator<String> iterator = vos.iterator();
	   while (iterator.hasNext()){
		String next = iterator.next();
		if (DeviceId.equals(next)) {
		   return true;
		}
	   }
	}
	return false;
   }

   /**
    * 判断list是否有此用户
    * @return
    */
   public static boolean getUserId(List<AccountVosBean> all, String accountId) {
	if (all != null) {
	   Iterator<AccountVosBean> iterator = all.iterator();
	   while (iterator.hasNext()){
		AccountVosBean next = iterator.next();
		String mAccountId = next.getAccountId();
		if (mAccountId.equals(accountId)) {
		   return true;
		}
	   }
	}
	return false;
   }
   /**
    * 判断list是否有此手术间
    * @return
    */
   public static boolean getRooms(List<OperationRoomsBean> all, String optRoomId) {
	if (all != null) {
	   Iterator<OperationRoomsBean> iterator = all.iterator();
	   while (iterator.hasNext()){
		OperationRoomsBean next = iterator.next();
		String mOptRoomId = next.getOptRoomId();
		if (mOptRoomId.equals(optRoomId)) {
		   return true;
		}
	   }
	}
	return false;
   }
}

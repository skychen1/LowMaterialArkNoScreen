package org.wh.engineer.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.util.IOUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.wh.engineer.base.mvp.Kits.File.FILE_EXTENSION_SEPARATOR;

/**
 * 项目名称:    Process_platform
 * 创建者:      LiangDanMing
 * 创建时间:    2017/2/8 9:22
 * 描述:        文件管理工具类
 * 包名:        oa.p5w.system.utils
 * <p/>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class FileUtils {
   public static final String ROOT_DIR = "Android/data/"
						     + UIUtils.getPackageName();
   public static final String DOWNLOAD_DIR = "download";
   public static final String CACHE_DIR = "cache";
   public static final String ICON_DIR = "icon";
   private static File sF;
   private static Bitmap sBitmap;

   /** 判断SD卡是否挂载 */
   public static boolean isSDCardAvailable() {
	if (Environment.MEDIA_MOUNTED.equals(Environment
								 .getExternalStorageState())) {
	   return true;
	} else {
	   return false;
	}
   }

   /** 获取下载目录 */
   public static String getDownloadDir() {
	return getDir(DOWNLOAD_DIR);
   }

   /** 获取缓存目录 */
   public static String getCacheDir() {
	return getDir(CACHE_DIR);
   }

   /** 获取icon目录 */
   public static String getIconDir() {
	return getDir(ICON_DIR);
   }

   /** 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录 */
   public static String getDir(String name) {
	StringBuilder sb = new StringBuilder();
	if (isSDCardAvailable()) {
	   sb.append(getExternalStoragePath());
	} else {
	   sb.append(getCachePath());
	}
	sb.append(name);
	sb.append(File.separator);
	String path = sb.toString();
	if (createDirs(path)) {
	   return path;
	} else {
	   return null;
	}
   }

   /** 获取SD下的应用目录 */
   public static String getExternalStoragePath() {
	StringBuilder sb = new StringBuilder();
	sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
	sb.append(File.separator);
	sb.append(ROOT_DIR);
	sb.append(File.separator);
	return sb.toString();
   }

   /** 获取应用的cache目录 */
   public static String getCachePath() {
	File f = UIUtils.getContext().getCacheDir();
	if (null == f) {
	   return null;
	} else {
	   return f.getAbsolutePath() + "/";
	}
   }

   /** 创建文件夹 */
   public static boolean createDirs(String dirPath) {
	File file = new File(dirPath);
	if (!file.exists() || !file.isDirectory()) {
	   return file.mkdirs();
	}
	return true;
   }




   /** 判断文件是否可写 */
   public static boolean isWriteable(String path) {
	try {
	   if (StringUtils.isEmpty(path)) {
		return false;
	   }
	   File f = new File(path);
	   return f.exists() && f.canWrite();
	} catch (Exception e) {
	   LogUtils.e("ss",e);
	   return false;
	}
   }

   /** 修改文件的权限,例如"777"等 */
   public static void chmod(String path, String mode) {
	try {
	   String command = "chmod " + mode + " " + path;
	   Runtime runtime = Runtime.getRuntime();
	   runtime.exec(command);
	} catch (Exception e) {
		LogUtils.e("ss",e);
	}
   }

   /**
    * 把数据写入文件
    *
    * @param is
    *            数据流
    * @param path
    *            文件路径
    * @param recreate
    *            如果文件存在，是否需要删除重建
    * @return 是否写入成功
    */
   public static boolean writeFile(InputStream is, String path,
					     boolean recreate) {
	boolean res = false;
	File f = new File(path);
	FileOutputStream fos = null;
	try {
	   if (recreate && f.exists()) {
		f.delete();
	   }
	   if (!f.exists() && null != is) {
		File parentFile = new File(f.getParent());
		parentFile.mkdirs();
		int count = -1;
		byte[] buffer = new byte[1024];
		fos = new FileOutputStream(f);
		while ((count = is.read(buffer)) != -1) {
		   fos.write(buffer, 0, count);
		}
		res = true;
	   }
	} catch (Exception e) {
		LogUtils.e("ss",e);
	} finally {
	   IOUtils.close(fos);
	   IOUtils.close(is);
	}
	return res;
   }

   /**
    * 把字符串数据写入文件
    *
    * @param content
    *            需要写入的字符串
    * @param path
    *            文件路径名称
    * @param append
    *            是否以添加的模式写入
    * @return 是否写入成功
    */
   public static boolean writeFile(byte[] content, String path, boolean append) {
	boolean res = false;
	File f = new File(path);
	RandomAccessFile raf = null;
	try {
	   if (f.exists()) {
		if (!append) {
		   f.delete();
		   f.createNewFile();
		}
	   } else {
		f.createNewFile();
	   }
	   if (f.canWrite()) {
		raf = new RandomAccessFile(f, "rw");
		raf.seek(raf.length());
		raf.write(content);
		res = true;
	   }
	} catch (Exception e) {
		LogUtils.e("ss",e);
	} finally {
	   IOUtils.close(raf);
	}
	return res;
   }

   /**
    * 把字符串数据写入文件
    *
    * @param content
    *            需要写入的字符串
    * @param path
    *            文件路径名称
    * @param append
    *            是否以添加的模式写入
    * @return 是否写入成功
    */
   public static boolean writeFile(String content, String path, boolean append) {
	return writeFile(content.getBytes(), path, append);
   }

   /**
    * 把键值对写入文件
    *
    * @param filePath
    *            文件路径
    * @param key
    *            键
    * @param value
    *            值
    * @param comment
    *            该键值对的注释
    */
   public static void writeProperties(String filePath, String key,
						  String value, String comment) {
	if (StringUtils.isEmpty(key) || StringUtils.isEmpty(filePath)) {
	   return;
	}
	FileInputStream fis = null;
	FileOutputStream fos = null;
	File f = new File(filePath);
	try {
	   if (!f.exists() || !f.isFile()) {
		f.createNewFile();
	   }
	   fis = new FileInputStream(f);
	   Properties p = new Properties();
	   p.load(fis);// 先读取文件，再把键值对追加到后面
	   p.setProperty(key, value);
	   fos = new FileOutputStream(f);
	   p.store(fos, comment);
	} catch (Exception e) {
		LogUtils.e("ss",e);
	} finally {
	   IOUtils.close(fis);
	   IOUtils.close(fos);
	}
   }

   /** 根据值读取 */
   public static String readProperties(String filePath, String key,
						   String defaultValue) {
	if (StringUtils.isEmpty(key) || StringUtils.isEmpty(filePath)) {
	   return null;
	}
	String value = null;
	FileInputStream fis = null;
	File f = new File(filePath);
	try {
	   if (!f.exists() || !f.isFile()) {
		f.createNewFile();
	   }
	   fis = new FileInputStream(f);
	   Properties p = new Properties();
	   p.load(fis);
	   value = p.getProperty(key, defaultValue);
	} catch (IOException e) {
		LogUtils.e("ss",e);
	} finally {
	   IOUtils.close(fis);
	}
	return value;
   }

   /** 把字符串键值对的map写入文件 */
   public static void writeMap(String filePath, Map<String, String> map,
					 boolean append, String comment) {
	if (map == null || map.size() == 0 || StringUtils.isEmpty(filePath)) {
	   return;
	}
	FileInputStream fis = null;
	FileOutputStream fos = null;
	File f = new File(filePath);
	try {
	   if (!f.exists() || !f.isFile()) {
		f.createNewFile();
	   }
	   Properties p = new Properties();
	   if (append) {
		fis = new FileInputStream(f);
		p.load(fis);// 先读取文件，再把键值对追加到后面
	   }
	   p.putAll(map);
	   fos = new FileOutputStream(f);
	   p.store(fos, comment);
	} catch (Exception e) {
		LogUtils.e("ss",e);
	} finally {
	   IOUtils.close(fis);
	   IOUtils.close(fos);
	}
   }

   /** 把字符串键值对的文件读入map */
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public static Map<String, String> readMap(String filePath,
							   String defaultValue) {
	if (StringUtils.isEmpty(filePath)) {
	   return null;
	}
	Map<String, String> map = null;
	FileInputStream fis = null;
	File f = new File(filePath);
	try {
	   if (!f.exists() || !f.isFile()) {
		f.createNewFile();
	   }
	   fis = new FileInputStream(f);
	   Properties p = new Properties();
	   p.load(fis);
	   map = new HashMap<String, String>((Map) p);// 因为properties继承了map，所以直接通过p来构造一个map
	} catch (Exception e) {
		LogUtils.e("ss",e);
	} finally {
	   IOUtils.close(fis);
	}
	return map;
   }


   public static File getCacheFile(File parent, String child) {
	// 创建File对象，用于存储拍照后的图片
	File file = new File(parent, child);

	if (file.exists()) {
	   file.delete();
	}
	try {
	   file.createNewFile();
	} catch (IOException e) {
	   e.printStackTrace();
	}
	return file;
   }
   public static String getDiskCacheDir(Context context) {
	String cachePath = null;
	if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
	    || !Environment.isExternalStorageRemovable()) {
	   cachePath = context.getExternalCacheDir().getPath();
	} else {
	   cachePath = context.getCacheDir().getPath();
	}
	return cachePath;
   }


   public static String getImagePath(Uri uri, String selection) {
	String path = null;
	// 通过Uri和selection来获取真实的图片路径
	Cursor cursor = UIUtils.getContext().getContentResolver().query(uri, null, selection, null, null);
	if (cursor != null) {
	   if (cursor.moveToFirst()) {
		path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
	   }
	   cursor.close();
	}
	return path;
   }


//   public static String SDPATH = Environment.getExternalStorageDirectory()
//					   + "/formats/";

   public static Bitmap returnBitmap(String url) {
	OkGo.<Bitmap>get(url).execute(new BitmapCallback() {
	   @Override
	   public void onSuccess(Response<Bitmap> response) {
		sBitmap = response.body();
		LogUtils.i("kkssss","sBitmap   "+(sBitmap==null));
	   }
	});
	return sBitmap;
   }


   public static boolean queryFile(String string){
	///storage/emulated/0
	//	storage/emulated/0/download/zbq.apk
	try {
	   File file = new File(string);
	   if(!file.exists()){
		return false;
	   }
	}catch (Exception e){
	   return false;
	}

	return true;
   }
   /**
    * 删除指定目录中特定的文件
    *
    * @param dir
    * @param filter
    */
   public static void delete(String dir, FilenameFilter filter) {
	if (TextUtils.isEmpty(dir))
	   return;
	File file = new File(dir);
	if (!file.exists())
	   return;
	if (file.isFile())
	   file.delete();
	if (!file.isDirectory())
	   return;

	File[] lists = null;
	if (filter != null)
	   lists = file.listFiles(filter);
	else
	   lists = file.listFiles();

	if (lists == null)
	   return;
	for (File f : lists) {
	   if (f.isFile()) {
		f.delete();
	   }
	}
   }
   /**
    * 获得不带扩展名的文件名称
    *
    * @param filePath 文件路径
    * @return
    */
   public static String getFileNameWithoutExtension(String filePath) {
	if (TextUtils.isEmpty(filePath)) {
	   return filePath;
	}
	int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
	int filePosi = filePath.lastIndexOf(File.separator);
	if (filePosi == -1) {
	   return (extenPosi == -1 ? filePath : filePath.substring(0,
										     extenPosi));
	}
	if (extenPosi == -1) {
	   return filePath.substring(filePosi + 1);
	}
	return (filePosi < extenPosi ? filePath.substring(filePosi + 1,
									  extenPosi) : filePath.substring(filePosi + 1));
   }

   /**
    * base64转图片
    * @return
    */
   public static boolean savePicture(String base64DataStr,String name){

//	// 1.去掉base64中的前缀
//	String base64Str = base64DataStr.substring(base64DataStr.indexOf(",")+1, base64DataStr.length());
	// 首先保存图片
	File appDir = new File(Environment.getExternalStorageDirectory(), name);// ":图片保存的文件夹的名称
	Log.i("eees", "图片+     " + Environment.getExternalStorageDirectory());
	if (!appDir.exists()) {
	   appDir.mkdir();
	}
	// 2.拼接图片的后缀，根据自己公司的实际情况拼接，也可从base64中截取图片的格式。
	String imgName = name+ ".png";
	File fileTest = new File(appDir, imgName);
	// 3. 解析保存图片
	byte[] data = Base64.decode(base64DataStr,Base64.DEFAULT);

	for (int i = 0; i < data.length; i++) {
	   if(data[i] < 0){
		data[i] += 256;//调整异常数据
	   }
	}
	OutputStream os = null;
	try {
	   os = new FileOutputStream(fileTest);
	   os.write(data);
	   os.flush();
	   os.close();
	   return true;
	} catch (FileNotFoundException e) {
	   e.printStackTrace();
	   return false;
	}catch (IOException e){

	   e.printStackTrace();
	   return false;
	}
   }


}

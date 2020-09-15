package org.wh.engineer.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;
import org.wh.engineer.base.App;
import org.wh.engineer.bean.ExceptionResultBean;
import org.wh.engineer.dto.InventoryDto;
import org.wh.engineer.dto.vo.InventoryVo;
import org.wh.engineer.dto.vo.InventoryVoError;
import org.wh.engineer.http.BaseResult;
import org.wh.engineer.http.NetRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.wh.engineer.cont.Constants.KEY_ACCOUNT_ID;
import static org.wh.engineer.cont.Constants.KEY_USER_NAME;
import static org.wh.engineer.cont.Constants.THING_CODE;
import static org.wh.engineer.timeutil.PowerDateUtils.getDates;
import static org.wh.engineer.utils.LyDateUtils.getVosType;

/**
 * 项目名称:    Android_PV_2.6.5New
 * 创建者:      DanMing
 * 创建时间:    2019/1/24 14:30
 * 描述:        无网获取新的再柜耗材和数据库改变后的提交
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class UnNetCstUtils {

//   private static List<InventoryVo> mAllCstVos;
   static         int               type       = 0;//控制第一次请求，然后需要等到重新获取新的耗材后才重置
   private static Gson sGson = new Gson();;

   /**
    * 所有耗材数据的获取（用于本地）
    */
   public static void getAllCstDate( Object activity) {

	NetRequest.getInstance().getUnEntCstDate(activity, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		type = 0;
		InventoryDto dto = sGson.fromJson(result, InventoryDto.class);
		List<InventoryVo> mAllCstVos = getLocalAllCstVos();
		InventoryDto localDto = new InventoryDto();
		List<InventoryVo> vos = dto.getInventoryVos();
		List<InventoryVo> tVos = new ArrayList<>();
		localDto.setThingId(dto.getThingId());
		localDto.setAccount(dto.getAccount());
		localDto.setTotalCount(dto.getTotalCount());
		if (vos.size() == 0) {
		   LitePal.deleteAll(InventoryVo.class);
		   mAllCstVos.clear();
		}
		if (mAllCstVos.size() == 0) {
		   localDto.setInventoryVos(vos);
		   localDto.save();
		   tVos.addAll(vos);
		   for (InventoryVo tVo : tVos) {
			tVo.setDateNetType(true);
		   }
		   LitePal.saveAll(tVos);
		} else {
		   List<InventoryVo> list = LitePal.where("operationstatus > ? ", "0")
			   .find(InventoryVo.class);
		   if (list.size() > 0) {
			LitePal.deleteAll(InventoryVo.class, "operationstatus > ? ", "0");
		   }
		   for (int i = 0; i < vos.size(); i++) {
			vos.get(i).setDateNetType(true);
			if (!getVosType(mAllCstVos, vos.get(i).getEpc())) {
			   boolean save = vos.get(i).save();
			   Log.i("UnNetc", "结束save     " + save);
			}
		   }
		}
	   }
	});
   }

   /**
    * 获取本地所有的耗材数据信息
    *
    * @return
    */
   public static List<InventoryVo> getLocalAllCstVos() {
//	mAllCstVos.clear();
	List<InventoryVo>  mAllCstVos = LitePal.findAll(InventoryVo.class);
	return mAllCstVos;
   }

   /**
    * 判断耗材数据库是否有变动
    * @return
    */
   public static boolean getSqlChangeType() {
	List<InventoryVoError> voErrors = LitePal.where("operationstatus > ? ", "0")
		.order("renewtime desc")
		.find(InventoryVoError.class);
	if (voErrors != null && voErrors.size() > 0) {
	   return true;
	}
	return false;
   }

   /**
    * 查询需要提交的离线后修改的耗材数据
    * @return
    */
   public static String putSqlDate() {
	List<InventoryVoError> voErrors = LitePal.where("operationstatus > ? ", "0")
		.order("renewtime desc")
		.find(InventoryVoError.class);
	String toJson = sGson.toJson(voErrors);
	List<InventoryVo> sVos = sGson.fromJson(toJson, new TypeToken<List<InventoryVo>>() {}.getType());
	InventoryDto localDto = new InventoryDto();
	localDto.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	localDto.setInventoryVos(sVos);
	return sGson.toJson(localDto);
   }

   /**
    * 删除数据库已有的已经操作过的耗材
    *
    * @param result
    */
   public static void deleteVo(String result) {
	List<InventoryVo> voList = LitePal.findAll(InventoryVo.class);
	InventoryDto inventoryDto = sGson.fromJson(result, InventoryDto.class);
	List<InventoryVo> vos = inventoryDto.getInventoryVos();
	for (InventoryVo vo : vos) {
	   for (int i = voList.size() - 1; i >= 0; i--) {
		if (voList.get(i).getEpc().equals(vo.getEpc())&&vo.getDeleteCount()==0) {
		   voList.get(i).delete();
		}
	   }
	}
//	getAllCstDate(mGson, activity);
   }
   /**
    * 删除数据库已有的已经操作过的耗材（异常处理中的删除）
    *
    * @param result
    */
   public static void deleteVoException(String result) {
	List<InventoryVo> voList = LitePal.findAll(InventoryVo.class);
	ExceptionResultBean resultBean = sGson.fromJson(result, ExceptionResultBean.class);
	List<String> handleVos = resultBean
		.getEpcs();

	for (String vo : handleVos) {
	   for (int i = voList.size() - 1; i >= 0; i--) {
		if (voList.get(i).getEpc().equals(vo)) {
		   voList.get(i).delete();
		}
	   }
	}
	//	getAllCstDate(mGson, activity);
   }
   /**
    * 强开删除数据库已有的已经操作过的耗材
    */
   public static void deleteVo(List<InventoryVo> voList,String epc) {
	Iterator<InventoryVo> iterator = voList.iterator();
	while (iterator.hasNext()){
	   InventoryVo next = iterator.next();
	   if (next.getEpc().equals(epc)) {
		next.delete();
	   }
	}
   }

   /**
    *
    * 强开放入数据库存入
    * @param errorType false存库存，true存error流水
    * @param out false放入，true拿出
    * @param userName false不存用户信息，true存用户信息
    * @return
    */
   public static boolean saveErrorVo(String epc,String deviceId,boolean errorType,boolean out,boolean userName) {
      if (errorType){
	   InventoryVoError inventory = new InventoryVoError();
	   inventory.setEpc(epc);
	   inventory.setDeviceId(deviceId);
	   inventory.setRenewTime(getDates());
	   if (out){
		inventory.setStatus("3");//出柜
		inventory.setOperationStatus(98);
	   }else {
		inventory.setStatus("2");//入柜
		inventory.setOperationStatus(99);
	   }
	   if (userName){
		inventory.setAccountId(SPUtils.getString(App.getAppContext(), KEY_ACCOUNT_ID));
		inventory.setUserName(SPUtils.getString(App.getAppContext(), KEY_USER_NAME));
	   }
	   boolean save = inventory.save();
	   return save;
	}else {
	   InventoryVo inventory = new InventoryVo();
	   inventory.setEpc(epc);
	   inventory.setDeviceId(deviceId);
	   inventory.setRenewTime(getDates());
	   inventory.setStatus("2");//入柜
	   inventory.setOperationStatus(99);
	   boolean save = inventory.save();
	   return save;
	}
   }
}

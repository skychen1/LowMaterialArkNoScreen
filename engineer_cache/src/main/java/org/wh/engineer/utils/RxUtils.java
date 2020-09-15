package org.wh.engineer.utils;

import org.litepal.LitePal;
import org.wh.engineer.dbmodel.BoxIdBean;
import org.wh.engineer.dto.entity.Inventory;
import org.wh.engineer.dto.vo.DeviceInventoryVo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static org.wh.engineer.utils.LyDateUtils.getVosBoxId;


/**
 * @ProjectName: InterOfficeBilling
 * @Package: com.rivamed.interofficebilling.utils
 * @ClassName: RxUtils
 * @Description: Rxjava工具类
 * @Author: Amos_Bo
 * @CreateDate: 2019/3/20 13:43
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/3/20 13:43
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class RxUtils {

   private List<Disposable> mDisposableList;

   private RxUtils() {
	if (mDisposableList == null) {
	   mDisposableList = new ArrayList<>();
	}
   }

   private static class SingletonClassInstance {

	private static final RxUtils instance = new RxUtils();
   }

   public static RxUtils getInstance() {
	return SingletonClassInstance.instance;
   }

   //    /**
   //     * 输入框监听
   //     *
   //     * @param editText
   //     * @param listener
   //     */
   //    public void setSeachResultListener(EditText editText, SearchDebounceResultListener listener) {
   //        Observable.create((ObservableOnSubscribe<String>) e -> editText.addTextChangedListener(new SingleTextWatchListener() {
   //            @Override
   //            public void afterTextChanged(Editable s) {
   //                e.onNext(s.toString().trim());
   //            }
   //        })).debounce(500, TimeUnit.MILLISECONDS)
   //                .filter((String s) -> s.length() > 0)
   //                .observeOn(AndroidSchedulers.mainThread())
   //                .subscribe(new Observer<String>() {
   //                    @Override
   //                    public void onSubscribe(Disposable d) {
   //                        mDisposableList.add(d);
   //                    }
   //
   //                    @Override
   //                    public void onNext(String s) {
   //                        listener.goSerach(s);
   //                    }
   //
   //                    @Override
   //                    public void onError(Throwable e) {
   //
   //                    }
   //
   //                    @Override
   //                    public void onComplete() {
   //
   //                    }
   //                });
   //    }

   /**
    * EPC扫描监听
    *
    * @param observableOnSubscribe
    * @param listener
    */
   public void setEpcResultListener(
	   BaseEpcObservable observableOnSubscribe, EpcDebounceResultListener listener) {
	Observable.create(observableOnSubscribe)
		.debounce(500, TimeUnit.MILLISECONDS)
		.filter((List<DeviceInventoryVo> s) -> s.size() > 0)
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe(new Observer<List<DeviceInventoryVo>>() {
		   @Override
		   public void onSubscribe(Disposable d) {
			mDisposableList.add(d);
		   }

		   @Override
		   public void onNext(List<DeviceInventoryVo> s) {
			listener.goEpcSearch(s);
		   }

		   @Override
		   public void onError(Throwable e) {

		   }

		   @Override
		   public void onComplete() {

		   }
		});
   }

   public interface SearchDebounceResultListener {

	/**
	 * 回调方法
	 *
	 * @param searchKey
	 */
	void goSerach(String searchKey);
   }

   public interface EpcDebounceResultListener {

	/**
	 * 回调方法
	 *
	 * @param vos
	 */
	void goEpcSearch(List<DeviceInventoryVo> vos);
   }

   /**
    * 注销监听
    */
   public void unRegister() {
	for (int i = 0, len = mDisposableList.size(); i < len; i++) {
	   mDisposableList.get(i).dispose();
	}
	mDisposableList.clear();
   }

   abstract public static class BaseEpcObservable
	   implements ObservableOnSubscribe<List<DeviceInventoryVo>> {

	private List<DeviceInventoryVo> inventoryVos = new ArrayList<>();//设备ID对应epc信息；
	private DeviceInventoryVo       inventoryVo  = new DeviceInventoryVo();//每个设备ID对应epc信息；
	private List<Inventory>         epcList      = new ArrayList<>();//epc的list
	private Inventory               inventory    = new Inventory();  //epc
	private String                  sBox_id      = "";
	private ObservableEmitter<List<DeviceInventoryVo>> observableEmitter;
	private String mDev = null;

	public BaseEpcObservable() {

	}

	private void addEpc(
		List<DeviceInventoryVo> Vos, ObservableEmitter<List<DeviceInventoryVo>> e) {

	   e.onNext(Vos);
	}

	public void removeVos() {

	   inventoryVos.clear();
	   mDev = null;
	   sBox_id = "";
	}

	/**
	 * 扫描的EPC
	 * @return
	 */
	public void getScanEpc(String device, String epc) {//这个方法需要把new的对象放外面后续改;todo
	   List<Inventory> epcList = new ArrayList<>();
	   DeviceInventoryVo inventoryVo = new DeviceInventoryVo();
	   Inventory inventory = new Inventory();
	   List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ?", device).find(BoxIdBean.class);
	   for (BoxIdBean boxIdBean : boxIdBeans) {
		sBox_id = boxIdBean.getBox_id();
	   }
	   inventory.setEpc(epc);
	   epcList.add(inventory);
	   if (inventoryVos.size() > 0) {
		if (getVosBoxId(inventoryVos, sBox_id)) {
		   for (int i = inventoryVos.size() - 1; i >= 0; i--) {
			if (inventoryVos.get(i).getDeviceId().equals(sBox_id)) {
			   inventoryVos.get(i).getInventories().addAll(epcList);
			}
		   }
		} else {
		   inventoryVo.setDeviceId(sBox_id);
		   inventoryVo.setInventories(epcList);
		   inventoryVos.add(inventoryVo);
		}
	   } else {
		inventoryVo.setDeviceId(sBox_id);
		inventoryVo.setInventories(epcList);
		inventoryVos.add(inventoryVo);
	   }
	   addEpc(inventoryVos, observableEmitter);
	}

	@Override
	public void subscribe(ObservableEmitter<List<DeviceInventoryVo>> e) throws Exception {
	   observableEmitter = e;
	}
   }
}

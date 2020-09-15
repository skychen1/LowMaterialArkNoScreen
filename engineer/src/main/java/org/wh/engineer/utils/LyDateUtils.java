package org.wh.engineer.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.ruihua.libconsumables.ConsumableManager;

import org.litepal.LitePal;
import org.wh.engineer.base.App;
import org.wh.engineer.bean.BillStockResultBean;
import org.wh.engineer.dbmodel.BoxIdBean;
import org.wh.engineer.dto.InventoryDto;
import org.wh.engineer.dto.entity.Inventory;
import org.wh.engineer.dto.vo.DeviceInventoryVo;
import org.wh.engineer.dto.vo.InventoryVo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.wh.engineer.cont.Constants.CONSUMABLE_TYPE;
import static org.wh.engineer.cont.Constants.KEY_ACCOUNT_ID;
import static org.wh.engineer.cont.Constants.KEY_USER_NAME;
import static org.wh.engineer.cont.Constants.SAVE_SEVER_IP;
import static org.wh.engineer.devices.AllDeviceCallBack.mBomDoorDeviceIdList;
import static org.wh.engineer.devices.AllDeviceCallBack.mEthDeviceIdBack;
import static org.wh.engineer.timeutil.PowerDateUtils.getDates;
import static org.wh.engineer.utils.UnNetCstUtils.getLocalAllCstVos;

/**
 * 项目名称:    Android_PV_2.6.6_514C
 * 创建者:      DanMing
 * 创建时间:    2019/6/12 14:02
 * 描述:       选择操作和绑定患者领用的部分逻辑抽取
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LyDateUtils {

    /**
     * 是否包含epc
     *
     * @return
     */
    public static boolean getVosType(List<InventoryVo> vos, String epc) {
        if (vos != null) {
            //	   int size = vos.size();
            Iterator<InventoryVo> iterator = vos.iterator();
            while (iterator.hasNext()) {
                InventoryVo next = iterator.next();
                String epc1 = next.getEpc();
                if (epc == null || epc.equals("0") || epc1.equals(epc)) {
                    return true;
                } else {

                }
            }
        }
        return false;
    }

    /**
     * 是否包含epc
     *
     * @return
     */
    public static boolean getVosType3(List<InventoryVo> vos, String epc, int mOperationType) {
        if (vos != null) {
            int size = vos.size();
            for (int x = size - 1; x >= 0; x--) {
                String epc1 = vos.get(x).getEpc();
                if (epc == null || epc.equals("0") || epc1.equals(epc)) {
                    return true;
                } else {
                    InventoryVo vo = vos.get(x);
                    if ((mOperationType == 3 && vo.getOperationStatus() != 98) || mOperationType == 4) {
                        if (vo.getIsErrorOperation() != 1 ||
                                (vo.getIsErrorOperation() == 1 && vo.getExpireStatus() == 0)) {
                            vo.setStatus(mOperationType + "");
                        }
                        if (mOperationType == 4 && vo.isDateNetType()) {
                            vo.setOperationStatus(3);
                        }
                    } else {
                        if (vo.isDateNetType() || !App.mTitleConn) {
                            vo.setIsErrorOperation(1);
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 是否包含epc
     *
     * @return
     */
    public static boolean getVosType2(List<InventoryVo> vos, String epc, int mOperationType) {
        if (vos != null) {
            int size = vos.size();
            for (int x = size - 1; x >= 0; x--) {
                String epc1 = vos.get(x).getEpc();
                if (epc == null || epc.equals("0") || epc1.equals(epc)) {
                    return true;
                } else {
                    InventoryVo vo = vos.get(x);
                    if (mOperationType == 9 || mOperationType == 8 ||
                            (mOperationType == 3 && vo.getOperationStatus() != 98) ||
                            (mOperationType == 4 && vo.isDateNetType())) {
                        if (vo.getIsErrorOperation() != 1 ||
                                (vo.getIsErrorOperation() == 1 && vo.getExpireStatus() == 0)) {
                            vo.setStatus(mOperationType + "");
                        }
                        if (mOperationType == 4) {
                            vo.setOperationStatus(3);
                        }
                    } else {
                        if (vo.isDateNetType() || !App.mTitleConn) {
                            vo.setIsErrorOperation(1);
                        }
                    }
                }
            }

            //	   for (int i = 0; i < size; i++) {
            //		if (epc==null||epc.equals("0")||vos.get(i).getEpc().equals(epc)) {
            //		   return true;
            //		}
            //	   }
        }
        return false;
    }

    /**
     * 是否包含柜号
     *
     * @return
     */
    public static boolean getVosBoxId(List<DeviceInventoryVo> vos, String box_id) {
        if (vos != null) {
            for (int i = 0; i < vos.size(); i++) {
                if (vos.get(i).getDeviceId().equals(box_id)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否包含柜号
     *
     * @return
     */
    public static boolean getVosBoxIdVo(List<InventoryVo> vos, String box_id) {
        if (vos != null) {
            for (int i = 0; i < vos.size(); i++) {
                if (null == vos.get(i).getDeviceId() || vos.get(i).getDeviceId().equals(box_id)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是套餐内的
     *
     * @return
     */
    public static boolean getVosRemark(List<BillStockResultBean.OrderDetailVo> vos, String box_id) {
        if (vos != null) {
            for (int i = 0; i < vos.size(); i++) {
                if (vos.get(i).getCstId().equals(box_id)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 给显示的vos赋值（区分各个柜子）
     *
     * @param box_id
     */
    public static List<InventoryVo> setAllBoxVosDate(
            List<InventoryVo> mBoxInventoryVos, String box_id) {
        List<InventoryVo> cstVos = getLocalAllCstVos();
        int size = cstVos.size();
        if (size > 0) {
            Iterator<InventoryVo> iterator = cstVos.iterator();
            while (iterator.hasNext()) {
                InventoryVo next = iterator.next();
                if (!box_id.equals(next.getDeviceId())) {
                    iterator.remove();
                }
            }
            mBoxInventoryVos.addAll(cstVos);
            int size1 = mBoxInventoryVos.size();
            for (int i = 0; i < size1 - 1; i++) {
                int size2 = mBoxInventoryVos.size();
                for (int x = size2 - 1; x > i; x--) {
                    if (mBoxInventoryVos.get(x).getEpc().equals(mBoxInventoryVos.get(i).getEpc())) {
                        mBoxInventoryVos.remove(x);
                    }
                }
            }
        }
        return mBoxInventoryVos;
    }

    /**
     * 重新扫描
     */
    public static void moreStartScan(
            List<InventoryVo> mBoxInventoryVos, RxUtils.BaseEpcObservable mObs) {
        for (String deviceInventoryVo : mEthDeviceIdBack) {
            String deviceCode = deviceInventoryVo;
            startScan(mBoxInventoryVos, mObs, deviceCode);
        }
    }

    /**
     * 重新扫描
     */
    public static void stopScan() {
      /*  List<String> readerDeviceId = DevicesUtils.getReaderDeviceId();
        for (String deviceInventoryVo : readerDeviceId) {
            String deviceCode = deviceInventoryVo;
            ReaderManager.getManager().stopScan(deviceCode);
        }*/
    }

    /**
     * 开始扫描
     *
     * @param mBoxInventoryVos
     * @param mObs
     * @param deviceIndentify
     */
    public static void startScan(
            List<InventoryVo> mBoxInventoryVos, RxUtils.BaseEpcObservable mObs,
            String deviceIndentify) {
        List<BoxIdBean> boxIdBeans;
        String cabinetType = "";
        String deviceIdWhich = deviceIndentify.substring(deviceIndentify.length() - 1);
        String deviceId = deviceIndentify.substring(0, deviceIndentify.length() - 1);
        Log.i("onDoorState", "deviceIdWhich    " + deviceIdWhich);
        Log.i("onDoorState", "deviceId    " + deviceId);

        boxIdBeans = LitePal.where("device_id = ? and name = ?", deviceId,
                CONSUMABLE_TYPE).find(BoxIdBean.class);
        Log.i("onDoorState", "boxIdBeansss    " + boxIdBeans.size());
        if (boxIdBeans.size() == 2) {
            if (deviceIdWhich.equals("0")) {
                cabinetType = "1";
            } else if (deviceIdWhich.equals("1")) {
                cabinetType = "2";
            }
        } else if (boxIdBeans.size() == 1) {
            cabinetType = "0";
        }

        /*for (BoxIdBean boxIdBean : boxIdBeans) {
            if (cabinetType.equals(boxIdBean.getCabinetType())) {
                String box_id = boxIdBean.getBox_id();
                List<BoxIdBean> deviceBean = LitePal.where("box_id = ? and name = ?", box_id,
                        READER_TYPE).find(BoxIdBean.class);
                for (BoxIdBean deviceid : deviceBean) {
                    String device_id = deviceid.getDevice_id();
                    int i = ReaderManager.getManager().startScan(device_id, READER_TIME);
                    Log.i("dfafaeeee", "startScan  " + i);
                    if (i == 2) {
                        Log.i("aalldf", "mRunnable   开始  " + device_id);
                        EventBusUtils.post(new Event.StartScanType(true, false));
                        ToastUtils.showShortToast("扫描中，请扫描结束后再进行操作！");
                    }
                    if (i == 1) {
                        EventBusUtils.post(new Event.StartScanType(true, false));
                        EventBusUtils.postSticky(new Event.EventLoadingX(false));
                        initReaderUtil();
                    }
                    if (i == 0) {
                        EventBusUtils.post(new Event.StartScanType(false, true));
                        EventBusUtils.postSticky(new Event.EventLoadingX(true));
                        setAllBoxVosDate(mBoxInventoryVos, box_id);
                        if (mObs != null && mDoorStatusType) {
                            mObs.removeVos();
                        }
                    }
                }
            }
        }*/
        //	return mBoxInventoryVos;
    }

    public static void setInventoryVoDate(
            List<InventoryVo> mBoxInventoryVos, List<DeviceInventoryVo> vos, int x) {
        List<Inventory> list = vos.get(x).getInventories();
        for (int i = 0; i < list.size(); i++) {
            if (!getVosType(mBoxInventoryVos, list.get(i).getEpc())) {
                InventoryVo inventoryVo = new InventoryVo();
                inventoryVo.setEpc(list.get(i).getEpc());
                inventoryVo.setDeviceId(vos.get(x).getDeviceId());
                inventoryVo.setOperationStatus(99);
                inventoryVo.setStatus("禁止放入");
                inventoryVo.setIsErrorOperation(1);
                inventoryVo.setRenewTime(getDates());
                inventoryVo.setAccountId(SPUtils.getString(App.getAppContext(), KEY_ACCOUNT_ID));
                inventoryVo.setUserName(SPUtils.getString(App.getAppContext(), KEY_USER_NAME));
                mBoxInventoryVos.add(inventoryVo);
            }
        }
    }

    /**
     * 无网的扫描后的EPC信息赋值 不绑定患者
     *
     * @param toJson
     */
    public static InventoryDto setUnNetDate(
            Context mContext, Gson mGson, int mOperationType, String toJson, String result, int type) {
        if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1") &&
                (mOperationType == type)) {
            return getInventoryDto(mContext, mGson, mOperationType, toJson);
        }
        return null;
    }

    /**
     * 无网的扫描后的EPC信息赋值  绑定患者
     *
     * @param toJson
     */
    public static InventoryDto setUnNetDate(
            Context mContext, Gson mGson, int mOperationType, String toJson, String result) {
        if (SPUtils.getString(mContext, SAVE_SEVER_IP) != null && result.equals("-1")) {
            return getInventoryDto(mContext, mGson, mOperationType, toJson);
        }
        return null;
    }

    @NonNull
    private static InventoryDto getInventoryDto(
            Context mContext, Gson mGson, int mOperationType, String toJson) {
        List<InventoryVo> mInVo = new ArrayList<>();
        InventoryDto cc = LitePal.findFirst(InventoryDto.class);
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setOperation(mOperationType);
        inventoryDto.setThingId(cc.getThingId());
        InventoryDto dto = mGson.fromJson(toJson, InventoryDto.class);
        if (dto.getDeviceInventoryVos().size() > 0) {
            List<Inventory> list = dto.getDeviceInventoryVos().get(0).getInventories();
            String deviceId = dto.getDeviceInventoryVos().get(0).getDeviceId();
            List<InventoryVo> vos = LitePal.where("deviceid = ? and status = ?", deviceId, "2")
                    .find(InventoryVo.class);
            BoxIdBean boxIdBean = LitePal.where("device_id = ? ", deviceId).findFirst(BoxIdBean.class);
            mInVo.addAll(vos);
            if (list.size() != 0) {
                for (Inventory s : list) {
                    InventoryVo first = LitePal.where("epc = ? and deviceid = ?", s.getEpc(), deviceId)
                            .findFirst(InventoryVo.class);
                    if (!getVosType(vos, s.getEpc())) {//无网放入
                        InventoryVo inventoryVo = new InventoryVo();
                        inventoryVo.setEpc(s.getEpc());
                        inventoryVo.setDeviceId(deviceId);
                        inventoryVo.setDeviceName(boxIdBean.getName());
                        inventoryVo.setAccountId(SPUtils.getString(mContext, KEY_ACCOUNT_ID));
                        inventoryVo.setUserName(SPUtils.getString(mContext, KEY_USER_NAME));
                        inventoryVo.setIsErrorOperation(0);
                        inventoryVo.setOperationStatus(99);
                        inventoryVo.setStatus("2");
                        inventoryVo.setRenewTime(getDates());
                        if (!getVosType(mInVo, s.getEpc())) {//避免重复加入
                            mInVo.add(inventoryVo);
                        }
                    } else {
                        if (first != null) {
                            mInVo.remove(first);
                        }
                    }
                }
            }
        }
        inventoryDto.setInventoryVos(mInVo);
        return inventoryDto;
    }

    /**
     * 请求结束后的数据放入显示在界面上
     *
     * @param vos
     */
    public static void setBoxVosDate(List<InventoryVo> mBoxInventoryVos, List<InventoryVo> vos) {
        if (mBoxInventoryVos.size() > 0) {
            for (int x = 0; x < vos.size(); x++) {
                if (!getVosType(mBoxInventoryVos, vos.get(x).getEpc())) {
                    InventoryVo inventoryVo = vos.get(x);
                    inventoryVo.setDateNetType(false);
                    mBoxInventoryVos.add(inventoryVo);
                }
            }
        } else {
            for (int x = 0; x < vos.size(); x++) {
                InventoryVo inventoryVo = vos.get(x);
                inventoryVo.setDateNetType(false);
                mBoxInventoryVos.add(inventoryVo);
            }
        }
    }

    /**
     * 重新开柜的逻辑3.0
     */
    public static void setMoreOpenDoor() {
        //	String string = SPUtils.getString(mAppContext, THING_MODEL);
        for (int i = 0; i < mBomDoorDeviceIdList.size(); i++) {
            String deviceIds = mBomDoorDeviceIdList.get(i);
            int size = 0;
            String deviceIdWhich = "";
            for (String deviceInventoryVo : mEthDeviceIdBack) {
                String deviceId = deviceInventoryVo.substring(0, deviceInventoryVo.length() - 1);
                if (mBomDoorDeviceIdList.get(i).equals(deviceId)) {
                    deviceIdWhich = deviceInventoryVo.substring(deviceInventoryVo.length() - 1);
                    size++;
                }
            }
            if (size > 1) {
                int i1 = ConsumableManager.getManager().openDoor(deviceIds);
                Log.i("ssffff", "openDoor：：" + deviceIds + "    v    " + i1);
            } else if (size == 1) {
                int Which = -1;
                if (deviceIdWhich.equals("0")) {
                    Which = 0;
                } else if (deviceIdWhich.equals("1")) {
                    Which = 1;
                }
                ConsumableManager.getManager().openDoor(deviceIds, Which);
            }
        }
    }

    /**
     * 断连后重连
     */
    public static void initReaderUtil() {
       /* new Thread(() -> {
            NetReaderManager.getManager().stopService();
            if (SPUtils.getString(mAppContext, READER_NAME) == null ||
                    SPUtils.getString(mAppContext, READER_NAME).equals(READER_NAME_RODINBELL)) {
                ReaderManager.getManager().connectReader(ReaderProducerType.TYPE_NET_RODINBELL);
            } else if (SPUtils.getString(mAppContext, READER_NAME) != null &&
                    SPUtils.getString(mAppContext, READER_NAME).equals(READER_NAME_COLU)) {
                ReaderManager.getManager().connectReader(ReaderProducerType.TYPE_NET_COLU);
            }
        }).start();*/
    }
}

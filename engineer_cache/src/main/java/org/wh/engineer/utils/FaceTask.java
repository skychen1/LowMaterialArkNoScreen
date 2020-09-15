package org.wh.engineer.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.baidu.idl.main.facesdk.utils.FileUitls;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.ruihua.libfacerecognitionv3.main.presenter.FaceManager;

import org.wh.engineer.R;
import org.wh.engineer.bean.AllFacePhotoBean;
import org.wh.engineer.http.BaseResult;
import org.wh.engineer.http.NetRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ruihua.libfacerecognitionv3.main.presenter.FaceManager.CODE_SUCCESS;
import static org.wh.engineer.cont.Constants.FACE_UPDATE_TIME;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/6/10
 * 描述：1.获取所有待注册人脸照，
 * 2.已注册但不是最新图片，删除已注册的底照，再次注册
 * 3.未注册，注册
 * 人脸注册不能批量注册，只能每次注册一张，注册完成后再注册下一张
 */
public class FaceTask {

    private String TAG = "FaceTask";

    public interface CallBack {
        void finishRegister(boolean hasRegister, String msg);
    }

    //记录当前缓存的图片
    private int index;
    //人脸照缓存路径
    private File faceLibrary;
    //待注册的人脸
    private List<AllFacePhotoBean.UsersBean> updatePhotoList;
    //注册完成后缓存最新的人脸照数据
    private String startTime;
    private Gson mGson;
    private Activity _mActivity;
    private CallBack callBack;

    public FaceTask(Activity _mActivity) {
        this._mActivity = _mActivity;
        index = 0;
        mGson = new Gson();
        updatePhotoList = new ArrayList<>();
        faceLibrary = FileUitls.getBatchFaceDirectory("cache_face_library");
        //上次更新时间
        startTime=SPUtils.getString(UIUtils.getContext(), FACE_UPDATE_TIME, "");

    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 获取所有人脸信息，缓存至本地，注册至本地人脸照底库
     */
    public void getAllFaceAndRegister() {
        NetRequest.getInstance().getAllFace(startTime, _mActivity, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                updatePhotoList.clear();
                AllFacePhotoBean facePhotoBean = mGson.fromJson(result, AllFacePhotoBean.class);
                if (facePhotoBean.isOperateSuccess()){
                    //缓存本次更新时间
                    SPUtils.putString(UIUtils.getContext(), FACE_UPDATE_TIME, facePhotoBean.getEndDate());
                }
                if (null == facePhotoBean||!facePhotoBean.isOperateSuccess()||facePhotoBean.getUserVos()==null||facePhotoBean.getUserVos().isEmpty()) {
                    if (callBack != null)
                        //                        callBack.finishRegister(false,"暂时没有人脸照数据");
                        callBack.finishRegister(false,null);
                    return;
                }
                List<AllFacePhotoBean.UsersBean> facePhotoList = facePhotoBean.getUserVos();
                updatePhotoList.addAll(facePhotoList);
                cacheFace();
            }

            @Override
            public void onError(String result) {
                ToastUtils.showShort(R.string.connection_fails);
                if (callBack != null)
                    callBack.finishRegister(false,"获取人脸照链接失败，请检查网络！");
            }
        });
    }

    /**
     * 缓存人脸照
     */
    private void cacheFace() {
        String faceUrl = updatePhotoList.get(index).getFaceFileName();
        if (TextUtils.isEmpty(faceUrl)){
            next();
            return;
        }
        OkGo.<File>get(faceUrl).tag(_mActivity)//
              //                .params("systemType", SYSTEMTYPE)
              .execute(new FileCallback(faceLibrary.getAbsolutePath(), updatePhotoList.get(index).getFaceId() + ".jpg") {  //文件下载时，需要指定下载的文件目录和文件名
                  @Override
                  public void onSuccess(Response<File> response) {
                      //缓存至本地，注册人脸
                      registerFaceLibrary(updatePhotoList.get(index), response.body());
                  }

                  @Override
                  public void downloadProgress(Progress progress) {
                      //                        mDialog.setProgress((int) (progress.fraction / -1024));
                      super.downloadProgress(progress);

                  }

                  @Override
                  public void onError(Response<File> response) {
                      super.onError(response);
                      //继续注册下一张
                      next();
                  }
              });
    }

    /**
     * 注册至人脸底库
     */
    private void registerFaceLibrary(AllFacePhotoBean.UsersBean facePhotoBean, File file) {
        String userId = facePhotoBean.getFaceId();
        FaceManager.getManager().inportFaceImage(userId,userId, file.getAbsolutePath(), (code, msg) -> {
            LogUtils.d(TAG, "LoginFace:人脸注册结果：：code=" + code + ":::msg=" + msg + ":::FaceId=" + userId+ ":::userName=" +  facePhotoBean.getName());
            if (code==CODE_SUCCESS){
                //注册成功即可删除缓存照片
                file.delete();
            }
            //注册完成，继续注册下一张
            next();
        });
    }

    private void next() {
        if (updatePhotoList.size() - 1 > index) {
            index++;
            cacheFace();
        } else {
            FaceManager.getManager().initDatabases();
            if (callBack != null)
                callBack.finishRegister(true, "人脸照注册完成");
            _mActivity = null;
        }
    }
}

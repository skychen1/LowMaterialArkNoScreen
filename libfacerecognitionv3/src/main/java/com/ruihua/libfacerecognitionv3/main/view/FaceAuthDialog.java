package com.ruihua.libfacerecognitionv3.main.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.baidu.idl.main.facesdk.FaceAuth;
import com.baidu.idl.main.facesdk.utils.FileUitls;
import com.ruihua.face.recognition.R;
import com.ruihua.libfacerecognitionv3.main.listener.SdkInitListener;
import com.ruihua.libfacerecognitionv3.main.presenter.FaceManager;
import com.ruihua.libfacerecognitionv3.main.utils.T;

/**
 * 鉴权dialog
 */
public class FaceAuthDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private EditText etKey;
    private int lastKeyLen = 0;

    private SdkInitListener dialogClickListener = null;

    public FaceAuthDialog(@NonNull Context context) {
        this(context, 0);
    }

    public FaceAuthDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_face_auth, null);
        setContentView(view);

        etKey = findViewById(R.id.et_key);

        //测试
//        etKey.setText("P6ME-ZRHS-KALY-IK57");
        TextView deviceId =findViewById(R.id.device_id);
        deviceId.setText(new FaceAuth().getDeviceId(mContext));

        etKey.setTransformationMethod(new AllCapTransformationMethod(true));
        addLisenter();
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        // 在线激活按钮
        Button btOnLineActive = findViewById(R.id.bt_on_line_active);
        btOnLineActive.setOnClickListener(this);
        // 检查文件按钮
        Button btInspectSdcard = findViewById(R.id.bt_inspect_sdcard);
        btInspectSdcard.setOnClickListener(this);
        // 离线激活按钮
        Button btOffLineActive = findViewById(R.id.bt_off_line_active);
        btOffLineActive.setOnClickListener(this);
    }

//    private void refresh() {
//
//    }

    @Override
    public void show() {
        super.show();
//        refresh();
    }

    public void setDialogClickListener(SdkInitListener listener) {
        this.dialogClickListener = listener;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_on_line_active) { // 在线激活

            String key = etKey.getText().toString().trim().toUpperCase();
            if (TextUtils.isEmpty(key)) {
                Toast.makeText(mContext, "请输入激活序列号!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (dialogClickListener != null)
                FaceManager.getManager().initLicenseOnline(key, mContext, dialogClickListener);
            // 查看sdcard
        } else if (i == R.id.bt_inspect_sdcard) {
            String path = FileUitls.getSDPath();
            String sdCardDir = path + "/" + "License.zip";
            if (FileUitls.fileIsExists(sdCardDir)) {
                toast("读取到License.zip文件，文件地址为：" + sdCardDir, mContext);
            } else {
                toast("未查找到License.zip文件", mContext);
            }
            // 离线激活
        } else if (i == R.id.bt_off_line_active) {//离线激活
            T.d(FaceAuthDialog.class, "离线激活");
            if (dialogClickListener != null)
                FaceManager.getManager().initLicenseOffLine(mContext, dialogClickListener);
        } else if (i == R.id.tv_cancel) {//取消
            if (dialogClickListener != null)
                dialogClickListener.initLicenseFail(-1, "取消授权操作");
            dismiss();
        }

    }

    private void addLisenter() {
        etKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 19) {
                    etKey.setText(s.toString().substring(0, 19));
                    etKey.setSelection(etKey.getText().length());
                    lastKeyLen = s.length();
                    return;
                }
                if (s.toString().length() < lastKeyLen) {
                    lastKeyLen = s.length();
                    return;
                }
                String text = s.toString().trim();
                if (etKey.getSelectionStart() < text.length()) {
                    return;
                }
                if (text.length() == 4 || text.length() == 9 || text.length() == 14) {
                    etKey.setText(text + "-");
                    etKey.setSelection(etKey.getText().length());
                }

                lastKeyLen = s.length();
            }
        });
    }

    public static class AllCapTransformationMethod extends ReplacementTransformationMethod {

        private char[] lower = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
                'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        private char[] upper = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        private boolean allUpper = false;

        public AllCapTransformationMethod(boolean needUpper) {
            this.allUpper = needUpper;
        }

        @Override
        protected char[] getOriginal() {
            if (allUpper) {
                return lower;
            } else {
                return upper;
            }
        }

        @Override
        protected char[] getReplacement() {
            if (allUpper) {
                return upper;
            } else {
                return lower;
            }
        }
    }


    private void toast(final String text, final Context context) {

        Toast.makeText(context, text, Toast.LENGTH_LONG).show();


    }
}

package com.ruihua.libfacerecognitionv3.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ruihua.face.recognition.R;
import com.ruihua.libfacerecognitionv3.main.model.BaseConfig;
import com.ruihua.libfacerecognitionv3.main.model.SingleBaseConfig;


/**
 * 1：N 人脸注册，搜索，管理
 */
public class FaceMainSearchActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    public static final int PAGE_TYPE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

        mContext = this;
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化View 相关
     */
    private void initView() {
        //人脸搜索
        findViewById(R.id.ly_search).setOnClickListener(this);
        //人脸注册
        findViewById(R.id.ly_reigster).setOnClickListener(this);
        //人脸库管理
        findViewById(R.id.ly_user).setOnClickListener(this);
        // 批量导入
        findViewById(R.id.ly_import).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_setting).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();//人脸搜索(人脸匹配)
//人脸注册
//人脸库管理
        if (i == R.id.ly_search) {
            Intent intent = new Intent(mContext, FaceRGBCloseDebugSearchActivity.class);
//                intent.putExtra("page_type", "search");
            startActivity(intent);
        } else if (i == R.id.ly_reigster) {
            Intent intent2 = new Intent(mContext, FaceRegisterActivity.class);
//                intent2.putExtra("page_type", "register");
            startActivityForResult(intent2, PAGE_TYPE);
        } else if (i == R.id.ly_user) {
            startActivity(new Intent(mContext, FaceUserGroupListActivity.class));
        } else if (i == R.id.ly_import) {
            startActivity(new Intent(mContext, BatchImportActivity.class));
        } else if (i == R.id.btn_back) {
            finish();
        } else if (i == R.id.btn_setting) {//                startActivity(new Intent(mContext, SettingMainActivity.class));
        }
    }
}

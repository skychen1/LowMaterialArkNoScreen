package org.wh.engineer.base.mvp;

import android.os.Bundle;
import android.view.View;

/**
 * Created by wanglei on 2016/12/29.
 */

public interface IViewFrg<P> {
    void bindUI(View rootView);

    void bindEvent();
    
    int getLayoutId();
    void initDataAndEvent(Bundle savedInstanceState);

    void onBindViewBefore(View view);
    int getOptionsMenuId();
    
    boolean useEventBus();

    P newP();
}

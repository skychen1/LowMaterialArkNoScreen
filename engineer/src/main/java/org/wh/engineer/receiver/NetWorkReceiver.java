package org.wh.engineer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import org.wh.engineer.bean.Event;
import org.wh.engineer.utils.EventBusUtils;

import static org.wh.engineer.base.App.mTitleConn;

/**
 * 项目名称:    TCP_SC_Demo
 * 创建者:      Danniu
 * 创建时间:    2018/5/31 13:24
 * 描述:        监听网线、WIFI、无网
 * 包名:        com.danniu.myapp.receiver
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class NetWorkReceiver extends BroadcastReceiver {

    private boolean netConnected = true;
    private int calledCount = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean net = checkNet(context);
        if (calledCount == 0) {
            EventBusUtils.post(new Event.XmmppConnect(mTitleConn, net)); //第一次网络状态回调
            calledCount++;
            netConnected = net;
        } else {
            if (netConnected && !net) {
                netConnected = false;
                Toast.makeText(context, "网络连接断开", Toast.LENGTH_SHORT).show();
                EventBusUtils.post(new Event.XmmppConnect(mTitleConn, netConnected));
            } else if (!netConnected && net) {
                netConnected = true;
                Toast.makeText(context, "网络连接成功", Toast.LENGTH_SHORT).show();
                EventBusUtils.post(new Event.XmmppConnect(mTitleConn, netConnected));
            }
        }

    }

    public static boolean checkNet(Context context) {
        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                //Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                //Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                //Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
            } else {
                // Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
            //API大于23时使用下面的方式进行网络监听
        } else {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            //用于存放网络连接信息
            StringBuilder sb = new StringBuilder();
            //通过循环将网络信息逐个取出来
            for (int i = 0; i < networks.length; i++) {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                if (networkInfo.isConnected()) {
                    /*Toast.makeText(context, "网络连接成功", Toast.LENGTH_SHORT).show();
                    EventBusUtils.post(new Event.XmmppConnect(mTitleConn, true));*/
                    return true;
                }
            }
            if (networks.length == 0) {
               /* Toast.makeText(context, "网络连接断开", Toast.LENGTH_SHORT).show();
                EventBusUtils.post(new Event.XmmppConnect(mTitleConn, false));*/
            }
            return false;
        }
    }

    /**
     * 注册网络监听的广播
     */
    public static NetWorkReceiver initReceiver(Context context) {
        NetWorkReceiver mWorkReceiver = new NetWorkReceiver();
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction("android.net.ethernet.ETHERNET_STATE_CHANGED");
        timeFilter.addAction("android.net.ethernet.STATE_CHANGE");
        timeFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        timeFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        timeFilter.addAction("android.net.wifi.STATE_CHANGE");
        timeFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        context.registerReceiver(mWorkReceiver, timeFilter);
        return mWorkReceiver;
    }

}

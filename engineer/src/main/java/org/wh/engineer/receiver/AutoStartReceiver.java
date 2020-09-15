package org.wh.engineer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.wh.engineer.activity.SplashActivity;

/**
 * created by wh on 2020/8/13
 * desc
 */
public class AutoStartReceiver  extends BroadcastReceiver {
    private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Wmx logs::", intent.getAction());
        Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG).show();

        if (ACTION_BOOT.equals(intent.getAction())) {
            Intent intentMainActivity = new Intent(context, SplashActivity.class);
            intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentMainActivity);
        }
    }


}

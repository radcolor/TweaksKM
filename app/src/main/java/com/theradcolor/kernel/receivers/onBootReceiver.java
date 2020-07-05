package com.theradcolor.kernel.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.RootUtils;
import com.theradcolor.kernel.services.onBootService;

public class onBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if (RootUtils.rootAccess()) {
                Utils.startService(context, new Intent(context, onBootService.class));
            } else {
                RootUtils.closeSU();
            }
        }
    }
}

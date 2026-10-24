package com.laifu.kids;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** 开机后把「少儿电脑」桌面拉到前台（作为 HOME 的兜底）。 */
public class BootReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context c, Intent i) {
        String a = i == null ? null : i.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(a) ||
            "android.intent.action.QUICKBOOT_POWERON".equals(a)) {
            try {
                Intent s = new Intent(c, MainActivity.class);
                s.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(s);
            } catch (Exception e) { }
        }
    }
}

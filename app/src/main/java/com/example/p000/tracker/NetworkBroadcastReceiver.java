package com.example.p000.tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = C_Comm.getConnectivityStatusString(context);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status == C_Comm.NETWORK_STATUS_NOT_CONNECTED) {
                System.out.println("not connected");
            } else {
                System.out.println("connected");
            }
        }
    }
}

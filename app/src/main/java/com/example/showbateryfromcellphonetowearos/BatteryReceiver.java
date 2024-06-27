package com.example.showbateryfromcellphonetowearos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class BatteryReceiver extends BroadcastReceiver {
    private final DataClient dataClient;

    public BatteryReceiver(Context context) {
        dataClient = Wearable.getDataClient(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale * 100;

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/battery");
        putDataMapReq.getDataMap().putFloat("battery_pct", batteryPct);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        Task<DataItem> putDataTask = dataClient.putDataItem(putDataReq);
    }
}

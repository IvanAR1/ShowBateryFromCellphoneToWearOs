package com.example.showbateryfromcellphonetowearos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{

        TextView BatteryTextView;
        private DataClient dataClient;
        Context context = this;

        @SuppressLint("SetTextI18n")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            BatteryReceiver batteryBroadcastReceiver = new BatteryReceiver(this);
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

            dataClient = Wearable.getDataClient(this);
            BatteryTextView = findViewById(R.id.textView);
            Intent batteryStatus = this.registerReceiver(batteryBroadcastReceiver, intentFilter);
            dataClient = Wearable.getDataClient(this);

            if(batteryStatus != null) {
                int level = batteryStatus.getIntExtra("level", -1);
                int scale = batteryStatus.getIntExtra("scale", -1);
                float batteryPct = level / (float) scale * 100;
                BatteryTextView.setText(String.format("%.1f%%", batteryPct));
            }

            dataClient.addListener(new DataClient.OnDataChangedListener() {
                @Override
                public void onDataChanged(DataEventBuffer dataEvents) {
                    for (DataEvent event : dataEvents) {
                        if (event.getType() == DataEvent.TYPE_CHANGED &&
                                event.getDataItem().getUri().getPath().equals("/battery")) {
                            DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                            float batteryPct = dataMapItem.getDataMap().getFloat("battery_pct");

                            // Actualizar la interfaz de usuario con el porcentaje de batería
                            BatteryTextView.setText(String.format("%.1f%%", batteryPct));
                        }
                    }
                }
            });
        }
}

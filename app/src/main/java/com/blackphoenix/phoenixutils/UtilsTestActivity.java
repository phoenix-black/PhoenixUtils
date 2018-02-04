package com.blackphoenix.phoenixutils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.blackphoenix.phoenixutils.BatteryManager.PxBatteryException;
import com.blackphoenix.phoenixutils.BatteryManager.PxBatteryManager;
import com.blackphoenix.phoenixutils.NetworkManager.PxNetworkException;
import com.blackphoenix.phoenixutils.NetworkManager.PxNetworkManager;

public class UtilsTestActivity extends AppCompatActivity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utils_test);
        context = UtilsTestActivity.this;

        try {
            Log.e("MBattery",""+ PxBatteryManager.getBatteryLevelAsString(context));
        } catch (PxBatteryException e) {
            e.printStackTrace();
        }

        try {
            Log.e("MNetwork Dbm",""+ PxNetworkManager.getSignalStrengthDbm(context));
            Log.e("MNetwork Level",""+ PxNetworkManager.getSignalStrengthLevel(context));
        } catch (PxNetworkException e){
            e.printStackTrace();
        }


    }
}

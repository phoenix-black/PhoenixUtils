package com.blackphoenix.phoenixutils.toast;

import android.content.Context;
import android.widget.Toast;

import com.blackphoenix.phoenixutils.R;


/**
 * Created by Praba on 1/1/2018.
 */

public class IToast {

    public static void show(Context context, String message){
        Toast.makeText(context,""+message,Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Context context, String message){
        Toast.makeText(context,""+message,Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String message){
        Toast.makeText(context,""+message,Toast.LENGTH_LONG).show();
    }

    public static void debugShow(Context context, String message){
        Toast.makeText(context,""+message,Toast.LENGTH_SHORT).show();
    }

    public static void message(Context context, String message, long timeMilliSeconds){

        ToastDialog messageDialog = new ToastDialog(context, R.style.ToastDialogTheme,message,timeMilliSeconds) {
            @Override
            public void onInterfaceReady(ToastDialogDataInterface dialogInterface) {

            }

            @Override
            public void onTimedOut() {

            }

            @Override
            public void onDismissed() {

            }
        };

        messageDialog.show();
    }

    public static void message(Context context, String message){

        ToastDialog messageDialog = new ToastDialog(context, R.style.ToastDialogTheme,message) {
            @Override
            public void onInterfaceReady(ToastDialogDataInterface dialogInterface) {

            }

            @Override
            public void onTimedOut() {

            }

            @Override
            public void onDismissed() {

            }
        };

        messageDialog.show();
    }
}

package com.blackphoenix.phoenixutils;

import android.content.Context;
import android.widget.Toast;

import com.blackphoenix.phoenixwidgets.MessageDialog;
import com.blackphoenix.phoenixwidgets.listeners.ProgressDialogDataInterface;


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

        MessageDialog messageDialog = new MessageDialog(context, com.blackphoenix.phoenixwidgets.R.style.ProgressDialogTheme,message,timeMilliSeconds) {
            @Override
            public void onInterfaceReady(ProgressDialogDataInterface dialogInterface) {

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

        MessageDialog messageDialog = new MessageDialog(context, com.blackphoenix.phoenixwidgets.R.style.ProgressDialogTheme,message) {
            @Override
            public void onInterfaceReady(ProgressDialogDataInterface dialogInterface) {

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

package com.blackphoenix.phoenixutils.toast;

import android.content.Context;
import android.support.annotation.NonNull;
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


    /**
     *
     * @param context  Activity Context
     * @param infoMessage  A brief information to show to user
     */

    public static void message(Context context, String infoMessage){
        message(context,infoMessage,-1);
    }


    /**
     *
     * @param context Activity Context
     * @param infoMessage A brief information to show to user
     * @param timeMilliSeconds time in Milliseconds | Automatically closes the dialog in set time
     *                         time should be greater than 500ms other wise default time will be set (3 seconds)
     */

    public static void message(Context context, String infoMessage, long timeMilliSeconds){

        new AdvancedToastDialog(context,infoMessage) {
            @Override
            public void onInterfaceReady(ToastDialogDataInterface dialogInterface) {

            }

            @Override
            public void onTimedOut() {

            }

            @Override
            public void onDismissed() {

            }

            @Override
            public void onReport(String errorCode, String errorMessage) {
            }
        }
                .setTimeOutTimeInMills(timeMilliSeconds)
                .show();
    }


    /**
     *
     * @param context Activity Context
     * @param errorMessage A short description of Error Message
     */

    public static void showError(@NonNull Context context, @NonNull String errorMessage){
        showError(context,errorMessage,null,null,-1,null);
    }

    /**
     *
     * @param context Activity Context
     * @param errorMessage A short description of Error Message
     * @param listener setting this will display report button on the dialog and its click listener is maintained here
     */

    public static void showError(@NonNull Context context, @NonNull String errorMessage, OnReportListener listener){
        showError(context,errorMessage,null,null,-1,listener);
    }

    /**
     *
     * @param context  Activity Context
     * @param errorMessage  A short description of Error Message
     * @param timeout time in Milliseconds | Automatically closes the dialog in set time
     *                time should be greater than 500ms other wise default time will be set (3 seconds)
     */

    public static void showError(@NonNull Context context, @NonNull String errorMessage, long timeout){
        showError(context,errorMessage,null,null,timeout,null);
    }

    /**
     * This dialog will have "show more" button
     * @param context  Activity Context
     * @param errorMessage   A short description of Error Message
     * @param detailedError  A Detailed description of the Error Message. Will be displayed when user clicks show more button
     * @param errorCode Error Code for Developer Reference. Will be displayed when user clicks show more button
     */

    public static void showError(@NonNull Context context, @NonNull String errorMessage, String detailedError, String errorCode){
        showError(context,errorMessage,detailedError,errorCode,-1,null);
    }


    /**
     * This dialog will have "show more" button
     * @param context   Activity Context
     * @param errorMessage   A short description of Error Message
     * @param detailedError   A Detailed description of the Error Message. Will be displayed when user clicks show more button
     * @param errorCode  Error Code for Developer Reference. Will be displayed when user clicks show more button
     * @param timeout  time in Milliseconds | Automatically closes the dialog in set time
     *                 time should be greater than 500ms other wise default time will be set (3 seconds)
     */

    public static void showError(@NonNull Context context, @NonNull String errorMessage, String detailedError, String errorCode, long timeout){
        showError(context,errorMessage,detailedError,errorCode,timeout,null);
    }


    /**
     *
     * @param context  Activity Context
     * @param errorMessage   A short description of Error Message
     * @param listener  setting this will display report button on the dialog and its click listener is maintained here
     * @param timeout  time in Milliseconds | Automatically closes the dialog in set time
     *                 time should be greater than 500ms other wise default time will be set (3 seconds)
     */

    public static void showError(@NonNull Context context, @NonNull String errorMessage, OnReportListener listener, long timeout){
        showError(context,errorMessage,null,null,timeout,listener);
    }


    /**
     *
     * @param context  Activity Context
     * @param errorMessage   A short description of Error Message
     * @param detailedError   A Detailed description of the Error Message. Will be displayed when user clicks show more button
     * @param errorCode   Error Code for Developer Reference. Will be displayed when user clicks show more button
     * @param listener  setting this will display report button on the dialog and its click listener is maintained here
     */

    public static void showError(@NonNull Context context, @NonNull String errorMessage, String detailedError, String errorCode, OnReportListener listener){
        showError(context,errorMessage,detailedError,errorCode,-1,listener);
    }


    /**
     *
     * @param context  Activity Context
     * @param briefErrorMessage   A short description of Error Message
     * @param detailedErrorMessage   A Detailed description of the Error Message. Will be displayed when user clicks show more button
     * @param errorCode   Error Code for Developer Reference. Will be displayed when user clicks show more button
     * @param timeout   time in Milliseconds | Automatically closes the dialog in set time
     *                  time should be greater than 500ms other wise default time will be set (3 seconds)
     * @param listener  setting this will display report button on the dialog and its click listener is maintained here
     */

    public static void showError(@NonNull Context context, @NonNull String briefErrorMessage, String detailedErrorMessage, String errorCode, long timeout, final OnReportListener listener){

        new AdvancedToastDialog(context,briefErrorMessage) {
            @Override
            public void onInterfaceReady(ToastDialogDataInterface dialogInterface) {

            }

            @Override
            public void onTimedOut() {

            }

            @Override
            public void onDismissed() {

            }

            @Override
            public void onReport(String errorCode, String errorMessage) {
                if(listener!=null){
                    listener.onReportClicked(errorCode,errorMessage);
                }
            }
        }
            .setDialogType(AdvancedToastDialog.DIALOG_TYPE.DIALOG_ERROR)
            .setTimeOutTimeInMills(timeout)
            .setDetailedError(errorCode,detailedErrorMessage)
            .setReportEnabled(listener!=null)
            .show();

    }


    /**
     *
     * @param context Activity Context
     * @param warningMessage Warning Message to display to user
     */


    public static void showWarning(@NonNull Context context, @NonNull String warningMessage){
        showWarning(context,warningMessage,-1);
    }


    /**
     *
     * @param context Activity Context
     * @param warningMessage Warning Message to display to user
     * @param timeout  time in Milliseconds | Automatically closes the dialog in set time
     */

    public static void showWarning(@NonNull Context context, @NonNull String warningMessage, long timeout){

        new AdvancedToastDialog(context,warningMessage) {
            @Override
            public void onInterfaceReady(ToastDialogDataInterface dialogInterface) {

            }

            @Override
            public void onTimedOut() {

            }

            @Override
            public void onDismissed() {

            }

            @Override
            public void onReport(String errorCode, String errorMessage) {

            }
        }
                .setDialogType(AdvancedToastDialog.DIALOG_TYPE.DIALOG_WARNING)
                .setTimeOutTimeInMills(timeout)
                .show();

    }

}

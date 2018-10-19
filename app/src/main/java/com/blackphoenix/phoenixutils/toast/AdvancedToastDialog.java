package com.blackphoenix.phoenixutils.toast;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
/*import android.support.annotation.IntDef;*/
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blackphoenix.phoenixutils.R;

/*import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;*/


/**
 * Created by Praba on 08-02-2017.
 */

public abstract class AdvancedToastDialog extends AlertDialog {

    protected TextView briefContentView;
    protected TextView detailedContentView;
    protected LinearLayout detailedContentLayout;
    protected LinearLayout errorCodeLayout;
    protected LinearLayout parentLayout;
    protected TextView errorCodeView;
    protected TextView showMoreButton;
    protected ImageView dialogIcon;
    protected Button closeButton;
    protected Button reportButton;

    protected Drawable dialogIconDrawable = null;
    protected int dialogIconId = -1;
    protected int dialogType = DIALOG_INFO;
    protected boolean isReportEnabled = false;



    private ToastDialogDataInterface progressDialogDataInterface;
    private static String DEFAULT_ERROR_TEXT = "Error!";
    private String briefErrorMessage = DEFAULT_ERROR_TEXT;
    private String detailedErrorMessage = null;
    private String errorCode = null;
    private long WAIT_TIME = 180*1000; // 3 minutes
    protected Handler timerHandler;

    public abstract void onInterfaceReady(ToastDialogDataInterface dialogInterface);
    public abstract void onTimedOut();
    public abstract void onDismissed();
    public abstract void onReport(String errorCode, String errorMessage);

    public static final int DIALOG_ERROR = 1;
    public static final int DIALOG_WARNING = 2;
    public static final int DIALOG_INFO = 3;
    public static final int DIALOG_CUSTOM = 4;

 /*   @Retention(RetentionPolicy.SOURCE)
    @IntDef ({DIALOG_ERROR,DIALOG_WARNING,DIALOG_INFO,DIALOG_CUSTOM})
    public @interface DIALOG_TYPE {}*/


    public AdvancedToastDialog(Context context, String errorBrief){
        super(context, R.style.ToastDialogTheme);
        this.briefErrorMessage = errorBrief;
    }

    public AdvancedToastDialog(Context context, int themeResId, String text) {
        super(context, themeResId);
        this.briefErrorMessage = text;
    }

    public AdvancedToastDialog(Context context, int themeResId, String text, long timeout /* TIMEOUT in Milliseconds*/) {
        super(context, themeResId);
        this.briefErrorMessage = text;
        this.WAIT_TIME = timeout;
    }

    public AdvancedToastDialog setDetailedError(String errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.detailedErrorMessage = errorMessage;
        return this;
    }

    public AdvancedToastDialog setDialogIcon(Drawable dialogIcon){
        dialogIconDrawable = dialogIcon;
        return this;
    }

    public AdvancedToastDialog setDialogIcon(int dialogIconId){
        this.dialogIconId = dialogIconId;
        return this;
    }

    public AdvancedToastDialog setDialogType(/*@DIALOG_TYPE*/ int dialogType){
        this.dialogType = dialogType;
        return this;
    }

    public AdvancedToastDialog setReportEnabled(boolean status){
        this.isReportEnabled = status;
        return this;
    }

    public AdvancedToastDialog setTimeOutTimeInMills(long timeInMills){
        if(timeInMills>500) {
            this.WAIT_TIME = timeInMills;
        }
        return this;
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pxutils_dialog_advanced_toast);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        briefContentView = (TextView)findViewById(R.id.toastAdvanced_briefContent);
        detailedContentView = (TextView)findViewById(R.id.toastAdvanced_detailedContent);
        errorCodeView = (TextView)findViewById(R.id.toastAdvanced_errorCode);
        detailedContentLayout = (LinearLayout) findViewById(R.id.toastAdvanced_detailedContentLayout);
        errorCodeLayout = (LinearLayout) findViewById(R.id.toastAdvanced_errorCodeLayout);
        showMoreButton = (TextView)findViewById(R.id.toastAdvanced_showMore);
        dialogIcon = (ImageView) findViewById(R.id.toastAdvanced_icon);
        parentLayout = (LinearLayout)findViewById(R.id.toastAdvanced_parentLayout);
        closeButton = (Button)findViewById(R.id.toastAdvanced_close);
        reportButton = (Button)findViewById(R.id.toastAdvanced_report);


        initViews();

        progressDialogDataInterface = new ToastDialogDataInterface() {
            @Override
            public void updateData(String newData) {
                briefContentView.setText(newData);
            }
        };

        onInterfaceReady(progressDialogDataInterface);

        errorCodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorCodeView.setSelected(!errorCodeView.isSelected());
            }
        });

        showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!showMoreButton.isSelected()) {
                    showMoreButton.setText("Show Less");
                    detailedContentLayout.setVisibility(View.VISIBLE);

                    if(errorCode != null && errorCode.length()>0) {
                        errorCodeLayout.setVisibility(View.VISIBLE);
                        errorCodeView.setSelected(true);
                    } else {
                        errorCodeLayout.setVisibility(View.GONE);
                        errorCodeView.setSelected(false);
                    }

                    if(isReportEnabled){
                        reportButton.setVisibility(View.VISIBLE);
                    }

                } else {
                    showMoreButton.setText("Show More");
                    detailedContentLayout.setVisibility(View.GONE);
                    errorCodeView.setSelected(false);
                    reportButton.setVisibility(View.INVISIBLE);
                }
                showMoreButton.setSelected(!showMoreButton.isSelected());
            }
        });


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(timerHandler!=null){
                    timerHandler.removeCallbacksAndMessages(null);
                }

                onDismissed();
                dismiss();
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerHandler!=null){
                    timerHandler.removeCallbacksAndMessages(null);
                }
                onReport(errorCode,detailedErrorMessage);
            }
        });
    }

    private void initViews(){

        briefContentView.setMovementMethod(new ScrollingMovementMethod());
        briefContentView.setText(briefErrorMessage);

        initDialogType();

    }


    private void initDialogType(){
        switch (dialogType){
            case DIALOG_CUSTOM:
                parentLayout.setBackgroundResource(R.drawable.pxutils_toast_shape_rounded_rect_view);
                if (dialogIconDrawable != null) {
                    dialogIcon.setImageDrawable(dialogIconDrawable);
                    break;
                }
                if (dialogIconId > 0) {
                    dialogIcon.setImageResource(dialogIconId);
                }
                break;

            case DIALOG_WARNING:
                dialogIcon.setImageResource(R.drawable.pxutils_ic_warning_yellow_48dp);
                parentLayout.setBackgroundResource(R.drawable.pxutils_toast_shape_rounded_rect_view_yellow);
                break;

            case DIALOG_ERROR:
                dialogIcon.setImageResource(R.drawable.pxutils_ic_error_red_48dp);
                parentLayout.setBackgroundResource(R.drawable.pxutils_toast_shape_rounded_rect_view_red);
                initErrorMessage();
                break;

            case DIALOG_INFO:
            default:
                dialogIcon.setImageResource(R.drawable.pxutils_toast_icon_info_2);
                parentLayout.setBackgroundResource(R.drawable.pxutils_toast_shape_rounded_rect_view);

        }
    }

    private void initErrorMessage(){

        if(detailedErrorMessage == null){
            showMoreButton.setVisibility(View.GONE);
            detailedContentLayout.setVisibility(View.GONE);

            if(isReportEnabled){
                reportButton.setVisibility(View.VISIBLE);
            }
            return;
        }

        showMoreButton.setSelected(false);
        showMoreButton.setVisibility(View.VISIBLE);
        //detailedContentLayout.setVisibility(View.VISIBLE);

        if(errorCode!=null){
            errorCodeView.setText(errorCode);
            errorCodeView.setSelected(false);
        }

        detailedContentView.setText(detailedErrorMessage);
        detailedContentView.setMovementMethod(new ScrollingMovementMethod());
    }


    public ToastDialogDataInterface getProgressDialogDataInterface(){
        return this.progressDialogDataInterface;
    }

    public boolean isInterfaceReady(){
        return progressDialogDataInterface!=null;
    }

    @Override
    public void show(){
        super.show();
        if(timerHandler==null) {
            timerHandler = new Handler();
        }

        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isShowing()) {
                    onTimedOut();
                    dismiss();
                }
            }
        },WAIT_TIME);

    }
}

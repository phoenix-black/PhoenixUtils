package com.blackphoenix.phoenixutils.toast;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blackphoenix.phoenixutils.R;


/**
 * Created by Praba on 08-02-2017.
 */

public abstract class ToastDialog extends AlertDialog {

    protected TextView textViewContent;
    private ToastDialogDataInterface progressDialogDataInterface;
    public static String DEFAULT_PROGRESS_TEXT = "Please Wait...";
    private String progressText = DEFAULT_PROGRESS_TEXT;
    private long WAIT_TIME = 180*1000;
    protected Handler timerHandler;

    public abstract void onInterfaceReady(ToastDialogDataInterface dialogInterface);
    public abstract void onTimedOut();
    public abstract void onDismissed();

    public ToastDialog(Context context, int themeResId, String text) {
        super(context, themeResId);
        this.progressText = text;
    }


    public ToastDialog(Context context, int themeResId, String text, long timeout /* TIMEOUT in Milliseconds*/) {
        super(context, themeResId);
        this.progressText = text;
        this.WAIT_TIME = timeout;
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pxutils_dialog_toast);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        textViewContent = (TextView)findViewById(R.id.toastMessage_content);
        textViewContent.setMovementMethod(new ScrollingMovementMethod());
        textViewContent.setText(progressText);

        progressDialogDataInterface = new ToastDialogDataInterface() {
            @Override
            public void updateData(String newData) {
                textViewContent.setText(newData);
            }
        };

        onInterfaceReady(progressDialogDataInterface);

        Button closeButton = findViewById(R.id.toastMessage_close);
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

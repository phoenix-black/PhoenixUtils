package com.blackphoenix.phoenixutils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Praba on 1/2/2018.
 */

public class ILog {

    private static String logFileName = "ilogcat.log";

    public static void print(String title, String message){
        Log.e(""+title,""+message);
    }

    public static void error(String errorTitle, String errorMessage){
        Log.e(""+errorTitle,""+errorMessage);
    }

    public static void error(Context context, String errorTitle, String errorMessage){
        Log.e(""+errorTitle,""+errorMessage);
        appendLog(context,errorTitle,errorMessage);
    }

    public static void save(Context context, String errorTitle, String errorMessage){
        Log.e(""+errorTitle,""+errorMessage);
        appendLog(context,errorTitle,errorMessage);
    }


    public static void d(String title, String message) {
        String logMessage = ""+getClassName(4)+"."+getMethodName(4)+"()."+getLineNumber(4)+": "+title + " -> " + message;
        Log.e("PxLog",""+logMessage);
    }

    public static File getLogFile(@NonNull Context context){
        String filesDir = context.getFilesDir().toString();
        File logFile = new File(filesDir,logFileName);

        if (!logFile.exists()) {
            try {
                if(!logFile.createNewFile()){
                    return null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }

        return logFile;
    }

    private static void appendLog(@NonNull Context mContext, String title, String message) {

        File logFile = getLogFile(mContext);

        if(logFile==null){
            Log.e("ILog","Unable to get LogFile! Null Value returned");
            return;
        }

        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            String logMessage = ""+getClassName(5)+"."+getMethodName(5)+"()."+getLineNumber(5)+": "+title + " -> " + message;
            buf.append(logMessage);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String getClassName(int index){
        String fullClassName = Thread.currentThread().getStackTrace()[index].getClassName(); // default: index = 3
        return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
    }

    private static String getMethodName(int index){
        return Thread.currentThread().getStackTrace()[index].getMethodName(); // default: index = 4
    }

    private static int getLineNumber(int index){
        return Thread.currentThread().getStackTrace()[index].getLineNumber(); // default: index = 4
    }

}

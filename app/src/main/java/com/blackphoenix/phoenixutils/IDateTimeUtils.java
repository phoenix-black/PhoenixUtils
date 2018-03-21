package com.blackphoenix.phoenixutils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Praba on 3/20/2018.
 *
 */

public class IDateTimeUtils {

    public static String getCurrentDateTimeString(){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("MM-dd-yy_hh:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentDateTimeString(String format){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    public static long getCurrentTimeMilliSec(){
        return System.currentTimeMillis();
    }

    public static boolean compareTimeMilliSec(long compareTime, long duration){
        return ((getCurrentTimeMilliSec() - compareTime) >= duration);
    }
}

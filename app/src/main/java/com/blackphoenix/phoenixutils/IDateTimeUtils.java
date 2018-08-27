package com.blackphoenix.phoenixutils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Praba on 3/20/2018.
 * ToDo : Handle Parse Exception Properly
 */

public class IDateTimeUtils {

    private static final String DEFAULT_DATE_TIME_PATTERN = "MM-dd-yy_hh:mm:ss";

    /**
     *
     * @return DateTimeString in the format MM-dd-yy_hh:mm:ss
     */
    public static String getCurrentDateTimeString(){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(DEFAULT_DATE_TIME_PATTERN, Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    /**
     *
     * @param format : format of the Date Time String that require in return
     * @return DateTimeString in the specified format
     */
    public static String getCurrentDateTimeString(String format){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }



    /**
     *
     * @return return Current Time in milliseconds
     * the difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.
     */
    public static long getCurrentTimeMilliSec(){
        return System.currentTimeMillis();
    }




    /**
     * check the time difference between input compareTime and current Times
     * @param compareTime : Time is milliseconds to be compared
     * @param duration : Duration / time difference that need to checked
     * @return  "true" if the time difference is greater than the specified duration else "false"
     */
    public static boolean compareTimeMilliSec(long compareTime, long duration){
        return ((getCurrentTimeMilliSec() - compareTime) >= duration);
    }




    /**
     *
     * @param dateTimeString reference to the TimeStamp which need to compared with current time
     *                       Pattern of the string should be : MM-dd-yy_hh:mm:ss
     * @param durationMilliSec difference limit that need to be checked between the two timestamps
     * @return "true" if the difference time is less than or equal to the specified duration
     * "false" if its greater
     * Uses Default Pattern MM-dd-yy_hh:mm:ss to compare the timestamp
     */
    public static boolean isDateTimeWithinTheDuration(String dateTimeString, long durationMilliSec){
        return isDateTimeWithinTheDuration(dateTimeString,durationMilliSec,DEFAULT_DATE_TIME_PATTERN);
    }





    /**
     *
     * @param dateTimeString reference to the TimeStamp which need to compared with current time
     *                        Pattern of the string should be the specified dateTimePattern
     * @param durationMilliSec difference limit that need to be checked between the two timestamps
     * @param dateTimePattern Pattern of the DateTimeStamps
     * @return "true" if the difference time is less than or equal to the specified duration
     * "false" if its greater
     */
    public static boolean isDateTimeWithinTheDuration(String dateTimeString, long durationMilliSec, String dateTimePattern){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(dateTimePattern, Locale.getDefault());
        try {
            Date savedDate = simpleDateFormat.parse(dateTimeString);

            long timeDifference = getCurrentTimeMilliSec() - savedDate.getTime();
            return (timeDifference <= durationMilliSec);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }





    /**
     *
     * @param dateTimeString Time for which the difference need to calculated with respect to current time
     *                     It Should be of Default pattern : MM-dd-yy_hh:mm:ss
     * @return the time difference of specified time and current time in milliseconds
     */

    public long getDifferenceTimeFromNow(String dateTimeString){
        return getDifferenceTimeFromNow(dateTimeString,DEFAULT_DATE_TIME_PATTERN);
    }





    /**
     *
     * @param dateTimeString Time for which the difference need to calculated with respect to current time
     * @param dateTimePattern the pattern of the specified date Time String
     * @return the time difference of specified time and current time in milliseconds
     */

    public long getDifferenceTimeFromNow(String dateTimeString, String dateTimePattern){

        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(dateTimePattern, Locale.getDefault());

        try {
            Date startDate = simpleDateFormat.parse(dateTimeString);
            return getCurrentTimeMilliSec() - startDate.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }





    /**
     * To find the time difference between two times
     * @param startDateString Start Time String, should be in the pattern  MM-dd-yy_hh:mm:ss
     * @param endDateString End Time String, should be in the pattern  MM-dd-yy_hh:mm:ss
     * @return  the time difference of specified times in milliseconds
     */
    public long getDifferenceTime(String startDateString, String endDateString){
        return getDifferenceTime(startDateString,endDateString,DEFAULT_DATE_TIME_PATTERN);
    }





    /**
     *
     * @param startDateString Start Time String
     * @param endDateString End Time String
     * @param dateTimePattern PAttern of the start and end time
     * @return the time difference of specified times in milliseconds
     */
    public long getDifferenceTime(String startDateString, String endDateString, String dateTimePattern){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(dateTimePattern, Locale.getDefault());
        try {
            Date startDate = simpleDateFormat.parse(startDateString);
            Date endDate = simpleDateFormat.parse(endDateString);

            return endDate.getTime() - startDate.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}

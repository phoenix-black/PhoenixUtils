package com.blackphoenix.phoenixutils.NetworkManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;


import com.blackphoenix.phoenixutils.R;
import com.blackphoenix.phoenixwidgets.CustomProgressDialog;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;


/**
 * Created by Praba on 12/19/2017.
 */
public class PxNetworkManager {

    private static String LOG_TITLE = PxNetworkManager.class.getSimpleName();

    Context context;
    TelephonyManager mTelephonyManager;
    private int signalStrength;
    private PhoneStateListener phoneStateListener;


    private static int SIGNAL_STRENGTH_GOOD = 3;
    private static int SIGNAL_STRENGTH_GREAT = 4;
    private static int SIGNAL_STRENGTH_MODERATE = 2;
    private static int SIGNAL_STRENGTH_NONE_OR_UNKNOWN = 0;
    private static int SIGNAL_STRENGTH_POOR = 1;

    public PxNetworkManager(Context context) {
        this.context = context;
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener(){
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);

                if (signalStrength.getGsmSignalStrength() == 0 || signalStrength.getGsmSignalStrength() == 99) {
                    PxNetworkManager.this.signalStrength = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
                } else if (signalStrength.getGsmSignalStrength() > 12) {
                    PxNetworkManager.this.signalStrength = SIGNAL_STRENGTH_GREAT;
                } else if (signalStrength.getGsmSignalStrength() > 8) {
                    PxNetworkManager.this.signalStrength = SIGNAL_STRENGTH_GOOD;
                } else if (signalStrength.getGsmSignalStrength() >= 5) {
                    PxNetworkManager.this.signalStrength = SIGNAL_STRENGTH_MODERATE;
                } else if (signalStrength.getGsmSignalStrength() < 5) {
                    PxNetworkManager.this.signalStrength = SIGNAL_STRENGTH_POOR;
                }
            }
        };

    }

    public void registerSignalStrengthListener(){
        if(mTelephonyManager!=null)
        mTelephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        // ToDo : Handle the else Part Here
    }

    public void unRegisterSignalStrengthListener(){
        if(mTelephonyManager!=null)
        mTelephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        // ToDo : Handle the else Part Here
    }


    public int getSignalStrength(){
        return signalStrength;
    }

    public void destroy(){
        if(mTelephonyManager!=null) {
            mTelephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
            mTelephonyManager = null;
        }
    }

    public static boolean isConnected(Context context) throws PxNetworkException {

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connManager == null) {
            throw new PxNetworkException("Unable to Get/Initialize Connectivity Service");
        }

        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

       /*
        Get the Type of Network Connected to (WiFi/Mobile/Ethernet/Bluetooth)
        @Parm: Context
        @Ret: Boolean
     */

    public static String getNetworkType(Context context) throws PxNetworkException {

        if (isConnected(context)) {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();

            switch (activeNetwork.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return "WiFi";
                case ConnectivityManager.TYPE_MOBILE:
                    return "Mobile";
                case ConnectivityManager.TYPE_ETHERNET:
                    return "Ethernet";
                case ConnectivityManager.TYPE_BLUETOOTH:
                    return "Bluetooth";
                default:
                    return "Unknown Network";
            }
        }
        return "No Network Available";
    }


    public static boolean isConnectionStrong(final Context context, final PxNetworkConnectivity networkInterface) throws PxNetworkException, IOException {

        if (isConnected(context)) {

            final CustomProgressDialog pa_progressDialog = new CustomProgressDialog(context, R.style.ProgressDialogTheme);
            pa_progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        if (InetAddress.getByName("www.google.com").isReachable(100)) {
                            //return true;
                            Log.e("NW MNGR", "Internet Connection is Strong");
                            networkInterface.onFinished(true);
                            //Toast.makeText(context,"Internet Connection is Strong",Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("NW MNGR", "Internet Connection is Weak");
                            //Toast.makeText(context,"Internet Connection is Weak",Toast.LENGTH_SHORT).show();
                            networkInterface.onFinished(false);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("NW MNGR", "IO Exception " + e.toString());
                        networkInterface.onFinished(false);
//                        Toast.makeText(context,"Internet Connection is Weak",Toast.LENGTH_SHORT).show();
                    }
                    if (pa_progressDialog.isShowing()) {
                        pa_progressDialog.dismiss();
                    }
                }
            }).start();

        }
        return false;
    }

    public static int getSignalStrengthDbm(Context context){

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            String countryCode = telephonyManager.getSimCountryIso();
            Log.e(LOG_TITLE, "CountryCode " + countryCode);
        }catch (NullPointerException e){
            Log.e(LOG_TITLE,"Null pointer while getting country code : "+e.getMessage());
        }

        List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();   //This will give info of all sims present inside your mobile
        if(cellInfos!=null){
            for (int i = 0 ; i<cellInfos.size(); i++){
                if (cellInfos.get(i).isRegistered()){
                    if(cellInfos.get(i) instanceof CellInfoWcdma){
                        CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) telephonyManager.getAllCellInfo().get(0);
                        CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                        Log.e(LOG_TITLE,"WCDMA Cell network found: ");
                        return cellSignalStrengthWcdma.getDbm();

                    }else if(cellInfos.get(i) instanceof CellInfoGsm){
                        CellInfoGsm cellInfogsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
                        CellSignalStrengthGsm cellSignalStrengthGsm = cellInfogsm.getCellSignalStrength();
                        //strength = String.valueOf(cellSignalStrengthGsm.getDbm());
                        Log.e(LOG_TITLE,"GSM Cell network found: ");
                        return cellSignalStrengthGsm.getDbm();

                    }else if(cellInfos.get(i) instanceof CellInfoLte){
                        CellInfoLte cellInfoLte = (CellInfoLte) telephonyManager.getAllCellInfo().get(0);
                        CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                        //strength = String.valueOf(cellSignalStrengthLte.getDbm());
                        Log.e(LOG_TITLE,"LTE Cell network found: ");
                        return cellSignalStrengthLte.getDbm();

                    }
                }
            }
        } else {
            Log.e(LOG_TITLE,"No Cell Network found");
            return -1;
        }

        return -1;
    }

    public static int getSignalStrengthLevel(Context context){

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            String countryCode = telephonyManager.getSimCountryIso();
            Log.e(LOG_TITLE, "CountryCode " + countryCode);
        }catch (NullPointerException e){
            Log.e(LOG_TITLE,"Null pointer while getting country code : "+e.getMessage());
        }

        List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();   //This will give info of all sims present inside your mobile
        if(cellInfos!=null){
            for (int i = 0 ; i<cellInfos.size(); i++){
                if (cellInfos.get(i).isRegistered()){
                    if(cellInfos.get(i) instanceof CellInfoWcdma){
                        CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) telephonyManager.getAllCellInfo().get(0);
                        CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                        Log.e(LOG_TITLE,"WCDMA Cell network found: ");
                        return cellSignalStrengthWcdma.getLevel();

                    }else if(cellInfos.get(i) instanceof CellInfoGsm){
                        CellInfoGsm cellInfogsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
                        CellSignalStrengthGsm cellSignalStrengthGsm = cellInfogsm.getCellSignalStrength();
                        //strength = String.valueOf(cellSignalStrengthGsm.getDbm());
                        Log.e(LOG_TITLE,"GSM Cell network found: ");
                        return cellSignalStrengthGsm.getLevel();

                    }else if(cellInfos.get(i) instanceof CellInfoLte){
                        CellInfoLte cellInfoLte = (CellInfoLte) telephonyManager.getAllCellInfo().get(0);
                        CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                        //strength = String.valueOf(cellSignalStrengthLte.getDbm());
                        Log.e(LOG_TITLE,"LTE Cell network found: ");
                        return cellSignalStrengthLte.getLevel();

                    }
                }
            }
        } else {
            Log.e(LOG_TITLE,"No Cell Network found");
            return -1;
        }

        return -1;
    }
}



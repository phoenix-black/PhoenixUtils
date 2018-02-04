package com.blackphoenix.phoenixutils.BatteryManager;

/**
 * Created by Praba on 02-02-2017.
 */

public class PxBatteryException extends Exception {
    public PxBatteryException(String message) {
        super("Phoenix Battery Exception: "+message);
    }
}

package com.blackphoenix.phoenixutils.NetworkManager;

/**
 * Created by Praba on 02-02-2017.
 */

public class PxNetworkException extends Exception {
    public PxNetworkException(String message) {
        super("Hoyo Network Exception: "+message);
    }
}

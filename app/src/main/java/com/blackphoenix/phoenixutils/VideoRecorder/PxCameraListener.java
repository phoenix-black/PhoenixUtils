package com.blackphoenix.phoenixutils.VideoRecorder;

import java.io.File;

/**
 * Created by w on 05-03-2017.
 */

public abstract class PxCameraListener {

    private File outputFile = null;
    public PxCameraListener(){

    }

    public void setOutputFile(File file){
        outputFileCreated(file);
        outputFile = file;
    }

    public boolean setRecordingFinished(){
        if(outputFile!=null){
            recordingFinished(outputFile);
            return true;
        } else {
            recordingError("Output File is Empty");
            return false;
        }
    }

    public abstract void recordingStarted();
    public abstract void recordingFinished(File file);
    public abstract void recordingError(String error);
    public abstract void recordingInterrupted(String message);
    public abstract void outputFileCreated(File outputFile);
}

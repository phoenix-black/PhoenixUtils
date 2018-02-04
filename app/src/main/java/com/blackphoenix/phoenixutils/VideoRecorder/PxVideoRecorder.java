package com.blackphoenix.phoenixutils.VideoRecorder;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.blackphoenix.phoenixwidgets.ILog;

import java.io.File;
import java.io.IOException;

/**
 * Created by w on 07-11-2016.
 */

public class PxVideoRecorder implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    public MediaRecorder mediaRecorder;
    public VideoSurfaceView surfaceView;
    private Camera camera;

   // public Semaphore semRecording  = new Semaphore(1,true);

    //private File deviceHomeDir = Environment.getExternalStorageDirectory();
    private File deviceHomeDir = null;
    private String appHomeDir = "/hoyo/";
    private final int MIN_RECORD_TIME = 5000;
    private File recordedVideoFile;
    private String videoRecordPath = deviceHomeDir+appHomeDir;
    //private int videoRecordTime = 10000; // 10 seconds
    public boolean recordStatus = false;
    public boolean mediaRecorderStatus = false;
    private boolean cameraStatus = false;

    private PxCameraListener videoRecordingListener;

    public PxVideoRecorder(Context context) {

        recordStatus = false;
        surfaceView = new VideoSurfaceView(context);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        // commented because its depreciated in latest library
        // Also it will be automatically done in latest API
        //surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);



        mediaRecorder = new MediaRecorder();
        camera = getCameraInstance();

        // ToDo : Create Proper Video Storing Directory
        deviceHomeDir = context.getFilesDir();

        // ToDo : Create Proper Video File Path
        videoRecordPath = deviceHomeDir+appHomeDir;

        File f = new File(deviceHomeDir, appHomeDir);

        if(f.isDirectory()) {
            ILog.print("VRec","Is Directory "+deviceHomeDir + appHomeDir);
        } else {
            if(!f.exists()) {
                if(f.mkdirs()) {
                    ILog.print("VRec","Is Directory "+deviceHomeDir + appHomeDir);
                } else {
                    ILog.print("VRec_E","Make Dir Error");
                }
            } else {
                ILog.print("VRec_E","Dir Exists");
            }
        }
        ILog.print("VRec","Recorder Init Done");
    }

    public void setCameraListener(PxCameraListener pxCameraListener){
        this.videoRecordingListener = pxCameraListener;
    }

    /*
     * Setup Media Recorder
     */

    private MediaRecorder setupMediaRecorder(){
        recordedVideoFile = null;

        MediaRecorder localMediaRecorder = new MediaRecorder();
        camera.unlock();

        localMediaRecorder.setCamera(camera);
        localMediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        localMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        localMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        localMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        localMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        localMediaRecorder.setAudioChannels(1);
        localMediaRecorder.setAudioSamplingRate(48000);
        localMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        localMediaRecorder.setVideoFrameRate(20);
        localMediaRecorder.setVideoSize(640,480);

        recordedVideoFile = prepareVideoFile();

        if(recordedVideoFile == null){
            videoRecordingListener.recordingInterrupted("Unable to Create Video File");
            return null;
        }

        localMediaRecorder.setOutputFile(recordedVideoFile.getAbsolutePath());

        ILog.print("VRec","Recorder Configuration Done");

        localMediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());


        try {
            localMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            localMediaRecorder.release();
            e.printStackTrace();
            videoRecordingListener.recordingInterrupted("Unable to Create Video File");
            return null;
        } catch (IOException e) {
            localMediaRecorder.release();
            e.printStackTrace();
            videoRecordingListener.recordingInterrupted("Unable to Create Video File");
            return null;
        }


        return localMediaRecorder;
    }

    /*
     * Prepare Output file for video
     */

    private File prepareVideoFile(){

        // ToDo : Create Proper Video File Name

        File tempVideoFile = new File(videoRecordPath);
        try {
            return File.createTempFile("E_Video",".mp4",tempVideoFile);
        } catch (IOException e) {
            e.printStackTrace();
            ILog.print("VRec_E","Unable to Create a Video File in "+tempVideoFile.getAbsolutePath() +"ERROR: "+e.toString());
            return null;
        }
    }

    /*
     * Runnable for Stop Recording
     */

    private Runnable stopRecordingRunnable = new Runnable() {
        @Override
        public void run() {
            ILog.print("VRec","Recording Finished");
            recordStatus = true;
            videoRecordingListener.setOutputFile(recordedVideoFile);
            StopRecording();
        }
    };

    /*
     * Start Video Recording
     */


    public File StartRecording(int mSeconds) {

        if(mSeconds<=MIN_RECORD_TIME){
            mSeconds = MIN_RECORD_TIME;
        }

        if(camera == null) {
            ILog.print("VRec_E","No Camera Instance Available");
            videoRecordingListener.recordingError("Camera is Busy");
            return null;
        }

        mediaRecorder = setupMediaRecorder();

        if(mediaRecorder == null){
            videoRecordingListener.recordingInterrupted("MediaRecorder setup initialize Failed");
            return null;
        }

        mediaRecorder.start();

        mediaRecorderStatus = true;
        videoRecordingListener.recordingStarted();

        /*
         * Set Timer based Stop Function
         */

        Handler handler = new Handler();
        handler.postDelayed(stopRecordingRunnable,mSeconds);

        ILog.print("VRec","Recorder Started");

        return recordedVideoFile;
    }

    /*
    * Stop Recording
     */

    private void StopRecording() {
        ILog.print("VRec","Recording Stopped");
        if(mediaRecorder != null) {

            if(mediaRecorderStatus) {
                mediaRecorder.stop();
            }
            mediaRecorder.release();
            mediaRecorder = null;
            mediaRecorderStatus = false;
            ILog.print("VRec","mediaRecorder stopped");
        }
        videoRecordingListener.setRecordingFinished();
    }

     /*
    * Release Camera
     */

    private void ReleaseCamera() {
        if (camera != null) {
            camera.lock();
            camera.stopPreview();
            camera.setPreviewCallback(null);
            surfaceHolder.removeCallback(this);
            camera.release();
            camera = null;
            ILog.print("VRec", "Camera stopped");

        }
    }

     /*
    * Destroy Recorder
     */

    private void Destroy() {
        ILog.print("VRec","Recording Destroy");
        if(mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }


    private Camera getCameraInstance() {
        Camera c = null;

        try {
            c = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
            ILog.print("VRec", "Error Exception" + e);
        }

        return c;
    }




    /*
     * Auto Generated **********************************************************************
     */


    private class VideoSurfaceView extends SurfaceView {
        public VideoSurfaceView(Context context) {
            super(context);
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        ILog.print("VRec","SurFace Created");
        if(camera == null) {
            camera = getCameraInstance();
        }

        if(camera != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                //camera.setPreviewCallback();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        ILog.print("VRec","SurFace Changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        ILog.print("VRec","Surface Destroyed");

        this.surfaceHolder.removeCallback(this);

        if(mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            camera.lock();
        }

        if(camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}

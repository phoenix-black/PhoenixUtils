# Video Recorder Usage:

 
## In OnCreate
-------------
```java
FrameLayout frame = (FrameLayout)findViewById(R.id.perm_cameraView);

PxVideoRecorder videoRecorder = new PxVideoRecorder(context);
frame.addView(videoRecorder.surfaceView);

CameraListener cameraListener = new CameraListener() {
            @Override
            public void recordingStarted() {
            }

            @Override
            public void recordingFinished(File file) {
            }

            @Override
            public void recordingError(String error) {
            }

            @Override
            public void recordingInterrupted(String message) {
            }

            @Override
            public void outputFileCreated(File outputFile) {
            }
};
```

Add Listener to the recorder
```java
videoRecorder.setCameraListener(cameraListener);
```
To Start Recording

```java
videoRecorder.StartRecording(10000); // Time in Milliseconds
```

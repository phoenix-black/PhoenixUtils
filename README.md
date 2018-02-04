# Video Recorder Usage:

public FrameLayout frame;

In OnCreate()

frame = (FrameLayout)findViewById(R.id.perm_cameraView);
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


//Map
videoRecorder.setCameraListener(cameraListener);

videoRecorder.StartRecording(10000); // Time in Milliseconds

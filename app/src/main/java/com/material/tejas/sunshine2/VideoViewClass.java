package com.material.tejas.sunshine2;

import android.app.Activity;
import android.app.ProgressDialog;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * Created by tejas on 20/6/15.
 */
public class VideoViewClass extends Activity implements GestureDetection.SimpleGestureListener {


    private  String video_name;
    private VideoView mVideoView;
    private int position = 0;
    private int currentPosition;
    private int currentVolume;
    private AudioManager audioManager;
    private GestureDetection detector;

    private ProgressDialog progressDialog;

    private MediaController mediaControls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);
        detector = new GestureDetection(this, this);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        Log.w("folder_name161", "inside");
        if (getIntent().hasExtra("video_name")) {
            video_name = getIntent().getStringExtra("video_name");
            Log.w("folder_name111", video_name);
        }

        //set the media controller buttons

        if (mediaControls == null) {

            mediaControls = new MediaController(VideoViewClass.this);

        }

        mVideoView = (VideoView) findViewById(R.id.video_view);


        // create a progress bar while the video file is loading

        progressDialog = new ProgressDialog(VideoViewClass.this);

        // set a title for the progress bar

        progressDialog.setTitle("JavaCodeGeeks Android Video View Example");

        // set a message for the progress bar

        progressDialog.setMessage("Loading...");

        //set the progress bar not cancelable on users' touch

        progressDialog.setCancelable(false);

        // show the progress bar

        progressDialog.show();

        Log.w("video path", video_name);
        mVideoView.setVideoPath(video_name);
        mVideoView.setMediaController(mediaControls);
        mVideoView.requestFocus();
        mVideoView.start();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {


            public void onPrepared(MediaPlayer mediaPlayer) {

                // close the progress bar and play the video

                progressDialog.dismiss();

                //if we have a position on savedInstanceState, the video playback should start from here

                mVideoView.seekTo(position);

                mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        //TODO: Your code here
                        mVideoView.start();
                    }
                });

                if (position == 0) {

                    mVideoView.start();

                } else {

                    //if we come from a resumed activity, video playback will be paused

                    mVideoView.pause();

                }



            }

        });

    }


    @Override

    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);

        //we use onSaveInstanceState in order to store the video playback position for orientation change

        savedInstanceState.putInt("Position", mVideoView.getCurrentPosition());

        mVideoView.pause();

    }



    @Override

    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        //we use onRestoreInstanceState in order to play the video playback from the stored position

        position = savedInstanceState.getInt("Position");

        mVideoView.seekTo(position);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        this.detector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onSwipe(int direction) {
        // TODO Auto-generated method stub
        String str = "";
    Log.w("direction", String.valueOf(direction));
        switch (direction) {

            case GestureDetection.SWIPE_LEFT:

                currentPosition = mVideoView.getCurrentPosition();
                Log.w("swipe left", String.valueOf(currentPosition));
                currentPosition = mVideoView.getCurrentPosition() - 10000;
                Log.w("after swipe left", String.valueOf(currentPosition));
                mVideoView.pause();
                mVideoView.seekTo(currentPosition);
                str = "Swipe Left";
                break;

            case GestureDetection.SWIPE_RIGHT:

                currentPosition = mVideoView.getCurrentPosition();
                Log.w("swipe right", String.valueOf(currentPosition));
                currentPosition = mVideoView.getCurrentPosition() + 10000;
                Log.w("after swipe right", String.valueOf(currentPosition));
                mVideoView.seekTo(currentPosition);
                str = "Swipe Right";
                break;

            case GestureDetection.SWIPE_DOWN:

                currentVolume = audioManager
                        .getStreamVolume(AudioManager.STREAM_MUSIC);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        currentVolume - 1, 0);
                str = "Swipe Down";
                break;
            case GestureDetection.SWIPE_UP:

                currentVolume = audioManager
                        .getStreamVolume(AudioManager.STREAM_MUSIC);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        currentVolume + 1, 0);
                str = "Swipe Up";
                break;

        }
Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

}

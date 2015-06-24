package com.material.tejas.sunshine2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import  com.material.tejas.*;
/**
 * Created by tejas on 19/6/15.
 */
public class VideoList extends Activity {

    protected static MyDatabase db;
    private static List<VideoInformation> mainListOfVideos;
    private RecyclerView recyclerViewVideo;
    private VideoListAdapter videoListAdapter;
    private String folder_name;
    static List<String> VideoList = new ArrayList<>();
    static Context mcontext;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list);
        db = new MyDatabase(this);


        /*
        new Thread(new Runnable() {
            public void run() {
                createThumbnails();
                });
            }
        }).start();
        */

        recyclerViewVideo = (RecyclerView) findViewById(R.id.list_main);
        if (getIntent().hasExtra("folder_name")) {
            folder_name = getIntent().getStringExtra("folder_name");
            Log.w("folder_name", folder_name);
        }
        mcontext = this;
        mainListOfVideos = listAllVideosOnFolder(this, folder_name);
        videoListAdapter = new VideoListAdapter(this, mainListOfVideos);
        /*if(!MainActivity.replayDone) {
            mainListOfVideos = listAllVideosOnFolder(this, folder_name);
            videoListAdapter = new VideoListAdapter(this, mainListOfVideos);
        }
        else {
        mainListOfVideos = listAllVideosOnFolderwithThumbnail(folder_name);
            videoListAdapter = new VideoListAdapter(this, mainListOfVideos);
            Log.w("notifydataset "," called");
            videoListAdapter.notifyDataSetChanged();
        }*/


        Log.w("tejas", "3");
        recyclerViewVideo.setAdapter(videoListAdapter);

        recyclerViewVideo.setLayoutManager(new LinearLayoutManager(this));
        Log.w("tejas", "5");
        recyclerViewVideo.addOnItemTouchListener(new MainActivity.RecyclerTouchListener(this, recyclerViewVideo, new MainActivity.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                showVideo(position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    public static List<VideoInformation> listAllVideosOnFolder(Context context, String folder_name) {


        Log.w("VideoList", "inside listAllVideosOnFolder");
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA};

        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + folder_name + "%"};
        Cursor c = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
        int vidsCount = 0;

        List<VideoInformation> data = new ArrayList<>();
        if (c != null) {
            vidsCount = c.getCount();
            while (c.moveToNext()) {
                Log.w("VideoList123", c.getString(0));
                VideoInformation current = new VideoInformation();
                String thumbNailName;
                current.title = c.getString(0);
                /*
                thumbNailName = db.getThumbnailName(videoList.get(i));
                String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/PhysicsSketchpad" + thumbNailName;
                File f = new File(file_path);
                if(f != null) {
                    Log.w("listallvideosonfolder", "passed null check");
                    Uri imageUri = Uri.fromFile(f);
                    current.iconId = BitmapFactory.decodeFile(imageUri.getPath());
                }
                else
                {
                    Log.w("listallvideosonfolder", "file is null");
                }*/
                data.add(current);
            }
            c.close();
        }
        return data;
    }

    public void showVideo(int position) {
        Intent mVideoListIntent = new Intent(this, VideoViewClass.class);
        mVideoListIntent.putExtra("video_name", mainListOfVideos.get(position).title);
        Log.w("showvideo", mainListOfVideos.get(position).title);
        startActivity(mVideoListIntent);
    }

    public  static List<VideoInformation> listAllVideosOnFolderwithThumbnail(String folder_name)
    {
        List<String> videoList123 = new ArrayList<>(db.getListOfAllVideosOnFolder(folder_name));
        videoList123 = db.getListOfAllVideosOnFolder(folder_name);
        mainListOfVideos = listAllVideosOnFolder(mcontext, folder_name);

        for(int i=0; i<videoList123.size(); i++)
        {
            Log.w("jksdfh", videoList123.get(i));
        }
        String thumbNailName;

        for(int i=0; i<videoList123.size()-1; i++)
        {
            Log.w("sdf", videoList123.get(i));
            thumbNailName = db.getThumbnailName(videoList123.get(i));
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/PhysicsSketchpad" + thumbNailName;
            VideoInformation current= new VideoInformation();
            current.title = videoList123.get(i);
                File f = new File(file_path);
                if(f != null) {
                    Log.w("listallvideosonfolder", "passed null check");
                    Uri imageUri = Uri.fromFile(f);
                    Log.w(" mainlistofvideos size" , String.valueOf(mainListOfVideos.size()));
                    for(int j=0; j<mainListOfVideos.size(); j++) {
                        if(mainListOfVideos.get(j).equals(current)) {
                            current.iconId = BitmapFactory.decodeFile(imageUri.getPath());
                        }
                    }
                }
                else
                {
                    Log.w("listallvideosonfolder", "file is null");
                }

        }
        return mainListOfVideos;

    }

    /*public static List<String> listAllVideosOnSDCard(Context context) {
        Log.w("VIDEO", "inside listAllVideosOnSDCard");
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.VideoColumns.DISPLAY_NAME};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        int vidsCount = 0;

        if (c != null) {
            vidsCount = c.getCount();
            while (c.moveToNext()) {
                Log.w("VIDEO", c.getString(0));
                String current = new String();
                current = c.getString(0);

                Log.w("foldername: ", current);
                if (!VideoList.contains(current)) {
                    VideoList.add(current);
                }
            }
            c.close();
        }
        return VideoList;
    }*/





}

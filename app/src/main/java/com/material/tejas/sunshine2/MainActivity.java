package com.material.tejas.sunshine2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private List<String> folderList;
    private RecyclerView recyclerView;
    private VideoFolderListAdapter adapter;
    public static boolean replayDone;
    protected static MyDatabase db;
    static List<String> VideoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        folderList = listAllVideosOnSDCard(this);
        folderList.addAll(listAllVideosOnInternalCard(this));
        Log.w("tejas", "1");
        recyclerView = (RecyclerView) findViewById(R.id.list_folder);
        Log.w("tejas", "2");
        adapter = new VideoFolderListAdapter(this, folderList);
        Log.w("tejas", "3");
        recyclerView.setAdapter(adapter);
        Log.w("tejas", "4");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.w("tejas", "5");
        db = new MyDatabase(this);
        db.addBaseFolderList(folderList);
        BigComputationTask t = new BigComputationTask();
        t.execute();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                showVideoList(position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public static boolean isReplayDone(){
        if(replayDone) return true;
        else return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static List<String> listAllVideosOnSDCard(Context context) {
        Log.w("VIDEO", "inside listAllVideosOnSDCard");
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.VideoColumns.DISPLAY_NAME};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        int vidsCount = 0;
        List<String> folderList = new ArrayList<>();
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
                String displayName = new String();
                displayName = c.getString(1);
                String folderName = new String();
                folderName = current.replace(displayName, "");
                Log.w("foldername: ", folderName);
                if (!folderList.contains(folderName)) {
                    folderList.add(folderName);
                }
            }
            c.close();
        }
        return folderList;
    }

    private class BigComputationTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            // Runs on UI thread
            Log.d("preexecute", "About to start...");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // Runs on the background thread
            createThumbNails();
            return true;

        }

        @Override
        protected void onPostExecute(Boolean t) {
            // Runs on the UI thread
            Log.d("onpostexecute", "Big computation finished");
            replayDone =true;

        }

    }

    public static void createThumbNails() {

        {
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/PhysicsSketchpad";
            File dir = new File(file_path);
            if (!dir.exists())
                dir.mkdirs();


            Log.w("videolistsize", String.valueOf(VideoList.size()));
            for (int i = 0; i < VideoList.size(); i++) {
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(VideoList.get(i),
                        MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);

                Log.w("videolistsize", String.valueOf(i));

                File file = new File(dir, "sketchpad" + i + ".png");
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(file);


                    thumb.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                    try {
                        fOut.flush();
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int pos = VideoList.get(i).lastIndexOf("/") + 1;
                    Log.w("pos", String.valueOf(pos));
                    Log.w("folder name",VideoList.get(i).substring(0, pos) );
                    db.addVideoList(VideoList.get(i).substring(0, pos), VideoList.get(i), "sketchpad" + i + ".png");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }

    }


    public void showVideoList(int position) {
        Intent mVideoListIntent = new Intent(this, VideoList.class);
        mVideoListIntent.putExtra("folder_name", folderList.get(position));
        startActivity(mVideoListIntent);
    }

    public static List<String> listAllVideosOnInternalCard(Context context) {
        Log.w("VIDEO", "inside listAllVideosOnSDCard");
        Uri uri = MediaStore.Video.Media.INTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.VideoColumns.DISPLAY_NAME};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        int vidsCount = 0;
        List<String> folderList = new ArrayList<>();
        if (c != null) {
            vidsCount = c.getCount();
            while (c.moveToNext()) {
                Log.w("VIDEO", c.getString(0));
                String current = new String();
                current = c.getString(0);
                String displayName = new String();
                displayName = c.getString(1);
                String folderName = new String();
                folderName = current.replace(displayName, "");
                Log.w("foldername: ", folderName);
                if (!folderList.contains(folderName)) {
                    folderList.add(folderName);
                }
            }
            c.close();
        }
        return folderList;
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
    }

}

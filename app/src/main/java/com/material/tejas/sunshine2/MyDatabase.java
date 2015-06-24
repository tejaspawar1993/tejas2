package com.material.tejas.sunshine2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by tejas on 20/6/15.
 */

/*
    There are 2 tables.
    1. Folder_list table contains list of folders containing videos
    2. Video_list table contains list of videos with their folder_name, thumbnail_file_name,

 */
public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "new.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // you can use an alternate constructor to specify a database location
        // (such as a folder on the sd card)
        // you must ensure that this folder is available and you have permission
        // to write to it
        //super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);

    }

    public boolean addBaseFolderList(List<String> arg_folder_list) {
        Log.w("database", "addWorkout called");
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("CREATE TABLE IF NOT EXISTS Folder_list ( _id INTEGER PRIMARY KEY AUTOINCREMENT, folder_names VARCHAR );");

        for (int i = 0; i < arg_folder_list.size(); i++) {
            db.execSQL("INSERT INTO Folder_list (folder_names) VALUES ('" + arg_folder_list.get(i) + "');");
        }
        return true;
    }

    public List<String> getBaseFolderList() {

        SQLiteDatabase db = getWritableDatabase();
        Cursor mCursor;
        List<String> folder_list = new Vector<String>();
        ;
        mCursor = db.rawQuery("SELECT  * from Folder_list", null);
        mCursor.moveToFirst();
        int colNum = mCursor.getColumnIndex("folder_names");
        Log.w("column name", String.valueOf(colNum));
        while (mCursor.moveToNext()) {
            folder_list.add(mCursor.getString(colNum));
        }
        return folder_list;
    }

    public String getThumbnailName(String video_name){
        SQLiteDatabase db = getWritableDatabase();
        Log.w("QQQ getthumbnail", video_name);
        Cursor thumbNameCursor = db.rawQuery("SELECT thumbnail_name from Video_list where video_names='" + video_name + "'", null);
        if(thumbNameCursor.moveToFirst()) {
            return thumbNameCursor.getString(0);
        }
        else return  "NA";
    }

    public void addVideoList(String folder_name, String video_name, String thumb_name)
    {
        Log.w("tejas folder123", folder_name);
        Log.w("QQQ videoname", video_name);
        Log.w("thumb", thumb_name);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS Video_list ( _id INTEGER PRIMARY KEY AUTOINCREMENT, folder_name VARCHAR, video_names VARCHAR, thumbnail_name VARCHAR );");
        db.execSQL("INSERT INTO Video_list (folder_name, video_names, thumbnail_name) VALUES ('" + folder_name + "','"+ video_name+"','"+thumb_name +"');");

    }

    public List<String> getListOfAllVideosOnFolder( String folder_name)
    {
            List<String> videoList1 = new ArrayList<>();
            SQLiteDatabase db = getWritableDatabase();
        Cursor vidCursor;
        Log.w("tejas dfs", folder_name);
            vidCursor = db.rawQuery("SELECT * from Video_list where folder_name ='"+ folder_name+"'", null);
        int colNum = vidCursor.getColumnIndex("video_names");
        vidCursor.moveToFirst();
        while (vidCursor.moveToNext())
            {
                Log.w("tejas dfs5465", vidCursor.getString(colNum));
                videoList1.add(vidCursor.getString(colNum));
            }

        return  videoList1;
    }

    //save thumbnail name, file name,

}

package com.material.tejas.sunshine2;

import android.graphics.Bitmap;

/**
 * Created by tejas on 19/6/15.
 */
public class VideoInformation {

    Bitmap iconId;
    String title;


    public boolean equals(VideoInformation o) {
        if(this.title == o.title)
            return true;
        else
            return false;
    }
}

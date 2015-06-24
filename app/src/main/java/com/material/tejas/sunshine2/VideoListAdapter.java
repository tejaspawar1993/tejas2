package com.material.tejas.sunshine2;

/**
 * Created by tejas on 19/6/15.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by tejas on 19/6/15.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> {
    List<VideoInformation> data= Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    public VideoListAdapter(Context context, List<VideoInformation> data){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.data=data;
    }

    public void delete(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.w("tejas", "oncreateviewholder");
        View view=inflater.inflate(R.layout.video_list_row2, parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        VideoInformation current=data.get(position);
        Log.w("tejas", "onbindviewholder");
        holder.title.setText(current.title);
        //holder.icon.setImageBitmap(current.iconId);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.video_info2);
            //icon= (ImageView) itemView.findViewById(R.id.video_thumbnail);
        }
    }
}

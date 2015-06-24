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

public class VideoFolderListAdapter extends RecyclerView.Adapter<VideoFolderListAdapter.MyViewHolder> {
    List<String> data= Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    public VideoFolderListAdapter(Context context, List<String> data){
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
        View view=inflater.inflate(R.layout.folder_list_row, parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String current=data.get(position);
        Log.w("tejas", "onbindviewholder");
        holder.folderName.setText(current);

    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView folderName;
        public MyViewHolder(View itemView) {
            super(itemView);
            folderName = (TextView) itemView.findViewById(R.id.folder_info);
        }
    }
}
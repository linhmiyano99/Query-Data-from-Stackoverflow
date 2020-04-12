package com.example.fossilandroidexam;

import android.content.Context;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.security.KeyStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    List<User> data;
    Context context;
    Map<String, Boolean> mapBookmark;
    Map<String, Bitmap> mapImage;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textDisplay;
        public ImageView imageProfile;
        public ImageView imageBookmark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDisplay = itemView.findViewById(R.id.displayInfo);
            imageProfile = itemView.findViewById(R.id.profile_image);
            imageBookmark = itemView.findViewById(R.id.bookmark);
            imageBookmark.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(!mapBookmark.containsKey(v.getTag())){
                        mapBookmark.put((String) v.getTag(), false);
                    }else {
                        if (mapBookmark.get(v.getTag())
                        ) {
                            mapBookmark.put((String) v.getTag(), false);

                        } else {
                            mapBookmark.put((String) v.getTag(), true);
                        }
                    }
                    //chua tim ra cach update 1 itemview
                    notifyDataSetChanged();
                    UpdateBoorkmark task = new UpdateBoorkmark((String) v.getTag(),  mapBookmark.get(v.getTag()));
                    task.execute(context);
                }
            });

        }
    }

    public RecyclerViewAdapter(Context context, List<User> data) {
        this.data = data;
        this.context = context;
        mapBookmark = new HashMap<>();
        mapImage = new HashMap<>();
        LoadBookmarkTask task = new LoadBookmarkTask(this);
        task.execute(context);
        for (User user: data) {
            LoadImageTask task2 = new LoadImageTask(this);
            task2.execute(user.profile_image);
        }
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View view =
                inflater.inflate(R.layout.item_recylerview, parent, false);
        // Get the app's shared preferences


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        User user =  data.get(position);
        holder.textDisplay.setText(user.toString() );
        holder.itemView.setTag(user.user_id);
        //loadImage(holder, user.profile_image);
        /*if(!mapImage.containsKey(user.profile_image)){
            LoadImageTask task = new LoadImageTask(this);

            task.execute(user.profile_image);
        }*/
        holder.imageProfile.setImageBitmap(mapImage.get(user.profile_image));



        holder.imageBookmark.setTag(user.user_id);


        // load bookmark
        if(!mapBookmark.containsKey(user.user_id)){
            holder.imageBookmark.setImageResource(R.drawable.bookmark_border);
        }
        else {
            if (mapBookmark.get(user.user_id)){
                holder.imageBookmark.setImageResource(R.drawable.bookmark_black);

            }
            else{
                holder.imageBookmark.setImageResource(R.drawable.bookmark_border);

            }

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    /**
     *  load Image
     */
    public void loadImage(final RecyclerViewAdapter.ViewHolder holder, String imageUrl) {
        // Create a task to download and display image.///
        DownloadImageTask task = new DownloadImageTask(holder.imageProfile);
        // Execute task (Pass imageUrl).
        task.execute(imageUrl);
    }
    public void setMapBookmark(Map<String, Boolean> list){
        mapBookmark = list;
    }
    public void setMapImage(Map<String, Bitmap> list){
        mapImage = list;
    }
    public void addMapImage(String key, Bitmap value){
        mapImage.put(key, value);
    }

}

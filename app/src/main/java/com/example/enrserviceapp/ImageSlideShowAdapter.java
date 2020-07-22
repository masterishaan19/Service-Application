package com.example.enrserviceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ImageSlideShowAdapter extends RecyclerView.Adapter <ImageSlideShowAdapter.ImageSlideShowViewHolder > {
    Context context;
    ArrayList < Integer > arr;
    public ImageSlideShowAdapter(Context context, ArrayList<Integer> imageList) {
        this.context = context;
        this.arr = imageList;
    }

    @NonNull
    @Override
    public ImageSlideShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageSlideShowViewHolder(
                LayoutInflater.from(context).inflate(R.layout.image_slideshow, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSlideShowViewHolder holder, int position) {
        holder.imageView.setImageResource(arr.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ImageSlideShowViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ImageSlideShowViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliding_image);
        }
    }
}

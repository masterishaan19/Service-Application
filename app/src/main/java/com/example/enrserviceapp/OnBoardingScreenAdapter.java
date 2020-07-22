package com.example.enrserviceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class OnBoardingScreenAdapter extends RecyclerView.Adapter <OnBoardingScreenAdapter.OnBoardingViewHolder> {

    ArrayList < ScreenItemClass > arr;
    Context context;
    public OnBoardingScreenAdapter(ArrayList<ScreenItemClass> arr, Context context) {
        this.arr = arr;
        this.context = context;
    }
    @NonNull
    @Override
    public OnBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnBoardingViewHolder(
                LayoutInflater.from(context)
                            .inflate(R.layout.layout_screen, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardingViewHolder holder, int position) {
        holder.setOnBoardingData(arr.get(position));
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    class OnBoardingViewHolder extends RecyclerView.ViewHolder{
        private TextView title, description;
        private ImageView imageView;

        public OnBoardingViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.intro_title);
            description = itemView.findViewById(R.id.intro_description);
            imageView = itemView.findViewById(R.id.intro_image);
        }

        public void setOnBoardingData(ScreenItemClass data){
            title.setText(data.getTitle());
            description.setText(data.getDescription());
            imageView.setImageResource(data.getImageUrl());
        }
    }
}

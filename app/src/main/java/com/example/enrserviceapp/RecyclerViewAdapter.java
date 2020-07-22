package com.example.enrserviceapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

class RecyclerViewAdapter extends RecyclerView.Adapter < RecyclerViewAdapter.myViewHolder > {

    Context myContext;
    ArrayList <ServicesDetails> arr;
    public RecyclerViewAdapter(Context context, ArrayList <ServicesDetails> list){
        this.arr = list;
        this.myContext = context;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.services_card, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {
        holder.text.setText(arr.get(position).getName());
        holder.image.setImageResource(arr.get(position).getImage());
        CardView cardView = holder.cardView;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.text.getText().equals("More Coming Soon")){
                    Intent intent = new Intent(myContext, OrderPlacingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("from", holder.text.getText().toString());
                    myContext.startActivity(intent);
                }else{
                    Toast.makeText(myContext, "Adding Soon..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        ImageView image;
        CardView cardView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.service_name);
            image = itemView.findViewById(R.id.service_image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}

package com.example.enrserviceapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class customAdapterServiceRequests extends FirebaseRecyclerAdapter < ServiceRequestClass, customAdapterServiceRequests.customViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
//    RecyclerView.AdapterDataObserver
    Context context;
    public customAdapterServiceRequests(@NonNull FirebaseRecyclerOptions<ServiceRequestClass> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull customViewHolder holder, final int position, @NonNull ServiceRequestClass model) {
        holder.image.setImageResource(model.getImage());
        holder.username.setText(model.getName());
        holder.address.setText(model.getAddress());
        holder.mobileNumber.setText(model.getMobileNumber());
        holder.heading.setText(model.getWorktype());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirmDialog = new AlertDialog.Builder(context);
                confirmDialog.setTitle("Are you sure")
                        .setMessage("You want to delete the service request")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference("Request")
                                        .child(getRef(position).getKey())
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(context, "Service Request Deleted", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(context, "Unable to delete request", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        })
                        .setCancelable(false)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("TAG", "onClick: Cancel button presses");
                            }
                        })
                        .show();
            }
        });
        holder.markAsComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Adding Soon", Toast.LENGTH_SHORT).show();
                /*TODO: Functionality for marking the abjective complete us to be added to the app !!*/
//                FirebaseDatabase.getInstance().getReference("Request")
//                        .child(getRef(position).getKey())
//                        .removeValue()
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful()){
//                                    Toast.makeText(context, "Service Request Deleted", Toast.LENGTH_SHORT).show();
//                                }else{
//                                    Toast.makeText(context, "Unable to delete request", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                FirebaseDatabase.getInstance().getReference()
            }
        });
    }

    @NonNull
    @Override
    public customViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_reques_card, parent, false);
        return new customViewHolder(view);
    }

    class customViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView username, address, heading, mobileNumber;
        ImageButton delete, markAsComplete;
        public customViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.workImage);
            username = itemView.findViewById(R.id.requestUserName);
            address = itemView.findViewById(R.id.requestUserAddress);
            mobileNumber = itemView.findViewById(R.id.requestUserMobileNumber);
            heading = itemView.findViewById(R.id.requestTitle);
            delete = itemView.findViewById(R.id.delete);
            markAsComplete = itemView.findViewById(R.id.markAsComplete);
        }
    }
}

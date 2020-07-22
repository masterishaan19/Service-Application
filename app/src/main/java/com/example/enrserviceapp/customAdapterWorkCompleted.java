package com.example.enrserviceapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class customAdapterWorkCompleted extends FirebaseRecyclerAdapter < WorkDoneClass , customAdapterWorkCompleted.customAdapterViewHolder>{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public customAdapterWorkCompleted(@NonNull FirebaseRecyclerOptions<WorkDoneClass> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull customAdapterViewHolder holder, int position, @NonNull WorkDoneClass model) {
        holder.work.setText(model.getTitle());
        holder.startTime.setText(model.startTime() + " on " + model.getStartingDate());
        holder.endTime.setText(model.endingTime() + " on " + model.getEndingDate());
        holder.worker.setText(model.getWorkerId());
        holder.customer.setText(model.getCustomerId());
        holder.breakTime.setText(String.valueOf(model.getBreakTime()) + " minutes");
        holder.workDescription.setText(model.getWorkType());
    }

    @NonNull
    @Override
    public customAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.completed_word_details, parent, false);
        return new customAdapterViewHolder(view);
    }

    class customAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView work, startTime, worker, customer, endTime, breakTime, workDescription;
        public customAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            work = itemView.findViewById(R.id.Work);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            worker = itemView.findViewById(R.id.worker);
            customer = itemView.findViewById(R.id.customer);
            breakTime = itemView.findViewById(R.id.breaktime);
            workDescription = itemView.findViewById(R.id.workDescription);
        }

    }
}

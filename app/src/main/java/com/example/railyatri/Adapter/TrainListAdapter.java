package com.example.railyatri.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railyatri.Activity.LiveStatusActivity;
import com.example.railyatri.Model.TrainModel;
import com.example.railyatri.R;

import java.util.List;

public class TrainListAdapter extends RecyclerView.Adapter<TrainListAdapter.TrainListViewHolder> {

    private List<TrainModel> trains;
    private Activity activity;
    private String date;

    public TrainListAdapter(Activity a, List<TrainModel> trains, String date) {
        this.trains = trains;
        this.activity = a;
        this.date = date;
    }

    class TrainListViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_train_number, tv_train_name, tv_train_type, tv_arival_time, tv_dest_time;
        private CardView layout_main;

        TrainListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_train_number = itemView.findViewById(R.id.tv_train_number);
            tv_train_name = itemView.findViewById(R.id.tv_train_name);
            tv_train_type = itemView.findViewById(R.id.tv_train_type);
            tv_arival_time = itemView.findViewById(R.id.tv_arival_time);
            tv_dest_time = itemView.findViewById(R.id.tv_dest_time);
            layout_main = itemView.findViewById(R.id.layout_main);
        }
    }

    @NonNull
    @Override
    public TrainListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.row_trains, parent, false);
        return new TrainListViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TrainListViewHolder holder, final int position) {
        holder.tv_train_number.setText(trains.get(position).getNumber());
        holder.tv_train_name.setText(trains.get(position).getName());
        switch (trains.get(position).getTrainType()) {
            case "EXP":
                holder.tv_train_type.setText("Express");
                break;
            case "PASS":
                holder.tv_train_type.setText("Passenger");
                break;
            case "RAJ":
                holder.tv_train_type.setText("Rajdhani");
                break;
            case "SF":
                holder.tv_train_type.setText("Super Fast");
                break;
        }
        holder.tv_arival_time.setText(trains.get(position).getSrc_departure_time());
        holder.tv_dest_time.setText(trains.get(position).getDest_arrival_time());
        holder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, LiveStatusActivity.class);
                i.putExtra("TrainNumber", trains.get(position).getNumber());
                i.putExtra("date", date);
                i.putExtra("TrainName", trains.get(position).getName());
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trains.size();
    }
}

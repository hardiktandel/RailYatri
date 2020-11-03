package com.example.railyatri.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.railyatri.Model.TrainLiveStatusModel;
import com.example.railyatri.R;

import java.util.List;

public class TrainStatusAdapter extends BaseAdapter {

    private List<TrainLiveStatusModel.Route> trains;
    private Activity activity;

    public TrainStatusAdapter(Activity a, List<TrainLiveStatusModel.Route> trains) {
        this.trains = trains;
        this.activity = a;
    }

    @Override
    public int getCount() {
        return trains.size();
    }

    @Override
    public Object getItem(int position) {
        return trains.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        TrainStatusViewHolder viewHolder = new TrainStatusViewHolder();
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.row_live_station, null);
            viewHolder.tv_station_name = (TextView) convertView.findViewById(R.id.tv_station_name);
            viewHolder.tv_train_status = (TextView) convertView.findViewById(R.id.tv_train_status);
            viewHolder.tv_schedule_time = (TextView) convertView.findViewById(R.id.tv_schedule_time);
            viewHolder.tv_actual_time = (TextView) convertView.findViewById(R.id.tv_actual_time);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (TrainStatusViewHolder) convertView.getTag();
        }

        viewHolder.tv_station_name.setText(trains.get(position).getStationName() + "(" + trains.get(position).getStationCode() + ")");
        viewHolder.tv_train_status.setText(trains.get(position).getDelayInArrival());
        viewHolder.tv_schedule_time.setText(trains.get(position).getScheduleArrival());
        viewHolder.tv_actual_time.setText(trains.get(position).getActualArrival());

        return convertView;
    }

    public class TrainStatusViewHolder {
        private TextView tv_station_name, tv_train_status, tv_schedule_time, tv_actual_time;
    }
}

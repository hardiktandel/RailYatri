package com.example.railyatri.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.railyatri.Adapter.TrainStatusAdapter;
import com.example.railyatri.Model.TrainLiveStatusModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.HttpDAO;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.example.railyatri.Utils.Const.API_KEY;

public class LiveStatusActivity extends AppCompatActivity {

    private Activity activity;
    private TextView tv_status;
    private TextView tv_station_name;
    private TextView tv_arrival_status;
    private TextView tv_departure_status;
    private TextView tv_schedule_arrival;
    private TextView tv_actual_arrival;
    private TextView tv_schedule_departure;
    private TextView tv_actual_departure;
    private ListView listView;
    private RelativeLayout layout_main;
    private LinearLayout linear_current;
    private LinearLayout linear_current1;
    private Toolbar toolbar;
    private View mProgressView;
    private String TrainNumber;
    private String TrainName;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_status);

        tv_status = findViewById(R.id.tv_status);
        tv_station_name = findViewById(R.id.tv_station_name);
        tv_arrival_status = findViewById(R.id.tv_arrival_status);
        tv_departure_status = findViewById(R.id.tv_departure_status);
        tv_schedule_arrival = findViewById(R.id.tv_schedule_arrival);
        tv_actual_arrival = findViewById(R.id.tv_actual_arrival);
        tv_schedule_departure = findViewById(R.id.tv_schedule_departure);
        tv_actual_departure = findViewById(R.id.tv_actual_departure);
        linear_current1 = findViewById(R.id.layout_current1);
        linear_current = findViewById(R.id.layout_current);
        layout_main = findViewById(R.id.layout_main);
        mProgressView = findViewById(R.id.progress);
        listView = findViewById(R.id.listView);
        toolbar = findViewById(R.id.toolbar);

        activity = this;

        TrainNumber = getIntent().getStringExtra("TrainNumber");
        TrainName = getIntent().getStringExtra("TrainName");
        date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(TrainName);

        new AsyncTrainName().execute("livetrainstatus/apikey/" + API_KEY + "/trainnumber/" + TrainNumber + "/date/" + date + "/");
    }

    @SuppressLint("StaticFieldLeak")
    class AsyncTrainName extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layout_main.setVisibility(View.GONE);
            showProgress();

        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            result = new HttpDAO().LiveStatus(params[0]);
            return result;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {

            Log.d("result", "" + result);
            Gson gson = new Gson();
            stopProgress();
            layout_main.setVisibility(View.VISIBLE);

            try {

                TrainLiveStatusModel tsm = gson.fromJson(result, TrainLiveStatusModel.class);
                TrainStatusAdapter adapter = new TrainStatusAdapter(LiveStatusActivity.this, tsm.getRoute());
                listView.setAdapter(adapter);

                if (tsm.getCurrent_station() != null) {
                    tv_station_name.setText(tsm.getCurrent_station().getStationName() + "(" + tsm.getCurrent_station().getStationCode() + ")");
                    tv_arrival_status.setText(tsm.getCurrent_station().getDelayInArrival());
                    tv_departure_status.setText(tsm.getCurrent_station().getDelayInDeparture());
                    tv_schedule_arrival.setText(tsm.getCurrent_station().getScheduleArrival());
                    tv_actual_arrival.setText(tsm.getCurrent_station().getActualArrival());
                    tv_schedule_departure.setText(tsm.getCurrent_station().getScheduleDeparture());
                    tv_actual_departure.setText(tsm.getCurrent_station().getActualDeparture());
                    tv_status.setVisibility(View.GONE);
                } else {
                    tv_status.setVisibility(View.VISIBLE);
                    linear_current1.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                layout_main.setVisibility(View.GONE);
            }
        }
    }

    private void showProgress() {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mProgressView.setVisibility(View.VISIBLE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void stopProgress() {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mProgressView.setVisibility(View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}

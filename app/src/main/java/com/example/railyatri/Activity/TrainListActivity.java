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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railyatri.Adapter.TrainListAdapter;
import com.example.railyatri.Model.TrainSearchModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.HttpDAO;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.example.railyatri.Utils.Const.API_KEY;

public class TrainListActivity extends AppCompatActivity {

    private TextView tv_from;
    private TextView tv_to;
    private TextView tv_date;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private View mProgressView;
    private Activity activity;
    private String to;
    private String toCode;
    private String from;
    private String fromCode;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);

        activity = this;
        tv_from = findViewById(R.id.tv_from);
        tv_to = findViewById(R.id.tv_to);
        tv_date = findViewById(R.id.tv_date);
        toolbar = findViewById(R.id.toolbar);
        mProgressView = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        to = getIntent().getStringExtra("To");
        from = getIntent().getStringExtra("From");
        toCode = getIntent().getStringExtra("ToCode");
        fromCode = getIntent().getStringExtra("FromCode");
        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        tv_from.setText(from);
        tv_to.setText(to);
        tv_date.setText(date);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        new AsyncTrainName().execute("TrainBetweenStation/apikey/" + API_KEY + "/From/" + fromCode + "/To/" + toCode);
    }

    @SuppressLint("StaticFieldLeak")
    class AsyncTrainName extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            result = new HttpDAO().TrainList(params[0]);
            return result;
        }

        protected void onPostExecute(String result) {

            Log.d("result", "" + result);
            Gson gson = new Gson();
            try {
                TrainSearchModel tsm = gson.fromJson(result, TrainSearchModel.class);
                if (tsm.getTrains() != null) {
                    TrainListAdapter tnba = new TrainListAdapter(activity, tsm.getTrains(), date);
                    recyclerView.setAdapter(tnba);
                }
                stopProgress();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Invalid Server", Toast.LENGTH_LONG).show();
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

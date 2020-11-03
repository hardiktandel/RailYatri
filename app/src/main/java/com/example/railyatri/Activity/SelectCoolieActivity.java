package com.example.railyatri.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railyatri.Adapter.CoolieAdapter;
import com.example.railyatri.Model.CoolieListModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.Utils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectCoolieActivity extends AppCompatActivity {

    private Spinner spinner;
    private RecyclerView recyclerView;
    private Utils utils;
    private ApiInterface apiInterface;
    private CoolieAdapter adapter;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_coolie);

        activity = this;
        utils = new Utils(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Coolie");

        spinner = (Spinner) findViewById(R.id.spinner);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.station));
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(12);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = spinner.getSelectedItem().toString();
                if (adapter != null) {
                    adapter.filter(item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (Connectivity.getInstance(activity).isOnline()) {
            getCoolie();
        } else {
            Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCoolie() {

        utils.startProgressDialog();

        Call<CoolieListModel> call = apiInterface.getCoolie();
        call.enqueue(new Callback<CoolieListModel>() {
            public void onResponse(@NonNull Call<CoolieListModel> call, @NonNull Response<CoolieListModel> response) {

                utils.stopProgressDialog();
                try {
                    assert response.body() != null;
                    adapter = new CoolieAdapter(activity, response.body().getData());
                    adapter.filter("Anand Junction");
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    Toast.makeText(activity, "Please Try Again Later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CoolieListModel> call, @NonNull Throwable t) {
                call.cancel();
                utils.stopProgressDialog();
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}

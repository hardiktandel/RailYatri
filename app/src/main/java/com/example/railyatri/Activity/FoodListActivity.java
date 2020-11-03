package com.example.railyatri.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railyatri.Adapter.FoodListAdapter;
import com.example.railyatri.Model.FoodListModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.Utils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ApiInterface apiInterface;
    public static Activity activity;
    private Utils utils;
    private String shopId;
    private String categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        activity = this;
        utils = new Utils(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        shopId = getIntent().getStringExtra("shopId");
        categoryId = getIntent().getStringExtra("categoryId");
        categoryName = getIntent().getStringExtra("categoryName");

        getSupportActionBar().setTitle(categoryName);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        if (Connectivity.getInstance(activity).isOnline()) {
            getFood();
        } else {
            Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFood() {

        utils.startProgressDialog();

        Call<FoodListModel> call = apiInterface.getFoodList(shopId, categoryId);
        call.enqueue(new Callback<FoodListModel>() {
            public void onResponse(@NonNull Call<FoodListModel> call, @NonNull Response<FoodListModel> response) {

                utils.stopProgressDialog();
                try {
                    assert response.body() != null;
                    FoodListAdapter foodListAdapter = new FoodListAdapter(activity, categoryName, shopId, response.body().getData());
                    recyclerView.setAdapter(foodListAdapter);
                } catch (Exception e) {
                    Toast.makeText(activity, "Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FoodListModel> call, @NonNull Throwable t) {
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

package com.example.railyatri.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railyatri.Adapter.FoodCategoryAdapter;
import com.example.railyatri.Model.FoodCategoryModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.Utils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodCategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ApiInterface apiInterface;
    public static Activity activity;
    private Utils utils;
    private String shopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_category);

        activity = this;
        utils = new Utils(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Category");
        shopId = getIntent().getStringExtra("shopId");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));

        if (Connectivity.getInstance(activity).isOnline()) {
            geCategory();
        } else {
            Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void geCategory() {

        utils.startProgressDialog();

        Call<FoodCategoryModel> call = apiInterface.getFoodCategory(shopId);
        call.enqueue(new Callback<FoodCategoryModel>() {
            public void onResponse(@NonNull Call<FoodCategoryModel> call, @NonNull Response<FoodCategoryModel> response) {

                utils.stopProgressDialog();
                try {
                    assert response.body() != null;
                    FoodCategoryAdapter foodCategoryAdapter = new FoodCategoryAdapter(activity, shopId, response.body().getData());
                    recyclerView.setAdapter(foodCategoryAdapter);
                } catch (Exception e) {
                    Toast.makeText(activity, "Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FoodCategoryModel> call, @NonNull Throwable t) {
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

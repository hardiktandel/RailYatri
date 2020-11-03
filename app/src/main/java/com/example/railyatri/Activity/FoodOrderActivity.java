package com.example.railyatri.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.railyatri.Adapter.FoodOrderAdapter;
import com.example.railyatri.Model.FoodOrderModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.BottomOffsetDecoration;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.SharedPref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe_to_refresh;
    private FloatingActionButton fab_add;
    private Activity activity;
    private ApiInterface apiInterface;
    private SharedPref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order);

        fab_add = findViewById(R.id.fab_add);

        fab_add.setOnClickListener(v -> {
            Intent i = new Intent(activity, ShopListActivity.class);
            startActivityForResult(i, 100);
        });

        activity = this;
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);
        pref = new SharedPref(activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Food Order History");

        swipe_to_refresh = findViewById(R.id.swipe_to_refresh);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new BottomOffsetDecoration(150));

        swipe_to_refresh.setOnRefreshListener(this::getFoodOrderDetails);
        getFoodOrderDetails();
    }

    private void getFoodOrderDetails() {
        if (Connectivity.getInstance(activity).isOnline()) {
            getOrders(pref.getID(), pref.getTYPE());
        } else {
            Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getOrders(String id, String type) {

        swipe_to_refresh.setRefreshing(true);

        Call<FoodOrderModel> call = apiInterface.getFoodOrder(id, type);
        call.enqueue(new Callback<FoodOrderModel>() {
            public void onResponse(@NonNull Call<FoodOrderModel> call, @NonNull Response<FoodOrderModel> response) {

                swipe_to_refresh.setRefreshing(false);
                try {
                    assert response.body() != null;
                    FoodOrderAdapter adapter = new FoodOrderAdapter(activity, response.body().getData());
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    Toast.makeText(activity, "Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FoodOrderModel> call, @NonNull Throwable t) {
                call.cancel();
                swipe_to_refresh.setRefreshing(false);
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            getFoodOrderDetails();
        }
    }
}

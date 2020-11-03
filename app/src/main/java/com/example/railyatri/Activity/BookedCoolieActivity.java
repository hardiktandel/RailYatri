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

import com.example.railyatri.Adapter.CoolieOrderHistoryAdapter;
import com.example.railyatri.Model.CoolieOrderListModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.BottomOffsetDecoration;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.RecyclerViewEdgeEffectFactory;
import com.example.railyatri.Utils.SharedPref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookedCoolieActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe_to_refresh;
    private Activity activity;
    private ApiInterface apiInterface;
    private SharedPref pref;
    private FloatingActionButton fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_coolie);

        fab_add = findViewById(R.id.fab_add);

        fab_add.setOnClickListener(v -> {
            Intent i = new Intent(activity, SelectCoolieActivity.class);
            startActivityForResult(i, 100);
        });

        activity = this;
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);
        pref = new SharedPref(activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Booked Coolie History");

        swipe_to_refresh = findViewById(R.id.swipe_to_refresh);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setEdgeEffectFactory(new RecyclerViewEdgeEffectFactory());
        recyclerView.addItemDecoration(new BottomOffsetDecoration(150));

        swipe_to_refresh.setOnRefreshListener(this::getOrderDetails);
        getOrderDetails();
    }

    private void getOrderDetails() {
        if (Connectivity.getInstance(activity).isOnline()) {
            getOrders(pref.getID());
        } else {
            Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getOrders(String userId) {

        swipe_to_refresh.setRefreshing(true);

        Call<CoolieOrderListModel> call = apiInterface.getCoolieOrderHistory(userId);
        call.enqueue(new Callback<CoolieOrderListModel>() {
            public void onResponse(@NonNull Call<CoolieOrderListModel> call, @NonNull Response<CoolieOrderListModel> response) {

                swipe_to_refresh.setRefreshing(false);
                try {
                    assert response.body() != null;
                    CoolieOrderHistoryAdapter adapter = new CoolieOrderHistoryAdapter(activity, response.body().getData());
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    Toast.makeText(activity, "Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CoolieOrderListModel> call, @NonNull Throwable t) {
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
            getOrderDetails();
        }
    }
}

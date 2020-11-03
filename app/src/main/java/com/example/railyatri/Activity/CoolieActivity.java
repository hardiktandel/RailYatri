package com.example.railyatri.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.railyatri.Adapter.CoolieOrderAdapter;
import com.example.railyatri.Model.CommonModel;
import com.example.railyatri.Model.CoolieOrderListModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.SharedPref;
import com.example.railyatri.Utils.Utils;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoolieActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe_to_refresh;
    private Activity activity;
    private Utils utils;
    private ApiInterface apiInterface;
    private SharedPref pref;
    private SwitchMaterial iv_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coolie);

        activity = this;
        utils = new Utils(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);
        pref = new SharedPref(activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Coolie");

        iv_switch = findViewById(R.id.iv_switch);
        swipe_to_refresh = findViewById(R.id.swipe_to_refresh);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        iv_switch.setOnClickListener(view -> {
            String status;
            if (iv_switch.isChecked()) {
                status = "1";
            } else {
                status = "0";
            }
            updateDialog(status, iv_switch.isChecked());
        });

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

    private void updateDialog(String status, boolean checked) {

        Dialog dialogActivate = new Dialog(activity);
        dialogActivate.setContentView(R.layout.dialog_delete);
        Objects.requireNonNull(dialogActivate.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        dialogActivate.getWindow().setGravity(17);
        dialogActivate.setCancelable(false);
        dialogActivate.show();

        Button btn_yes = dialogActivate.findViewById(R.id.btn_yes);
        Button btn_no = dialogActivate.findViewById(R.id.btn_no);
        TextView tv_info = dialogActivate.findViewById(R.id.tv_info);

        tv_info.setText("Are you sure \nYou want to update Availability Status");

        btn_yes.setOnClickListener(view -> {
            dialogActivate.dismiss();
            if (Connectivity.getInstance(activity).isOnline()) {
                updateStatus(pref.getID(), status);
            } else {
                Toast.makeText(activity, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
            }
        });

        btn_no.setOnClickListener(view -> {
            dialogActivate.dismiss();
            if (checked) {
                iv_switch.setChecked(false);
            } else {
                iv_switch.setChecked(true);
            }
        });
    }

    public void updateStatus(String id, String status) {

        utils.startProgressDialog();

        Call<CommonModel> call = apiInterface.updateCoolieAvailableStatus(id, status);
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                utils.stopProgressDialog();
                try {
                    Toast.makeText(activity, "Update Successfully", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(activity, "Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                call.cancel();
                utils.stopProgressDialog();
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOrders(String coolieId) {

        swipe_to_refresh.setRefreshing(true);

        Call<CoolieOrderListModel> call = apiInterface.getCoolieOrder(coolieId);
        call.enqueue(new Callback<CoolieOrderListModel>() {
            public void onResponse(@NonNull Call<CoolieOrderListModel> call, @NonNull Response<CoolieOrderListModel> response) {

                swipe_to_refresh.setRefreshing(false);
                try {
                    assert response.body() != null;
                    CoolieOrderAdapter adapter = new CoolieOrderAdapter(activity, response.body().getData());
                    recyclerView.setAdapter(adapter);

                    if (response.body().status.equals("1")) {
                        iv_switch.setChecked(true);
                    } else {
                        iv_switch.setChecked(false);
                    }
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
}

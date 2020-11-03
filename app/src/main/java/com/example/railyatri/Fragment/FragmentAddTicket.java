package com.example.railyatri.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.railyatri.Activity.PassTicketActivity;
import com.example.railyatri.Model.CommonModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.SharedPref;
import com.example.railyatri.Utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAddTicket extends Fragment {

    private View mFragmentView;
    private Button btn_apply;
    private Activity activity;
    private ApiInterface apiInterface;
    private Utils utils;
    private Spinner spinner_from;
    private Spinner spinner_to;
    private EditText et_qty;
    private RadioGroup radio_group;
    private SharedPref pref;
    private String type = "local";
    private String qty = "0";
    private String price = "10";
    private int local = 15;
    private int express = 30;
    private int superFast = 45;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(R.layout.fragment_add_ticket, null);
            init(mFragmentView);
        }
        return mFragmentView;
    }

    public void init(View view) {

        activity = getActivity();
        utils = new Utils(activity);
        pref = new SharedPref(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);

        spinner_from = view.findViewById(R.id.spinner_from);
        spinner_to = view.findViewById(R.id.spinner_to);
        et_qty = view.findViewById(R.id.et_qty);
        radio_group = view.findViewById(R.id.radio_group);
        btn_apply = view.findViewById(R.id.btn_apply);

        ArrayAdapter<String> adapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.station));
        spinner_from.setAdapter(adapter);
        spinner_to.setAdapter(adapter);

        spinner_from.setSelection(12);

        radio_group.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_local) {
                price = "" + local;
                type = "local";
            } else if (i == R.id.rb_express) {
                price = "" + express;
                type = "express";
            } else if (i == R.id.rb_superfast) {
                price = "" + superFast;
                type = "superfast";
            }
        });

        btn_apply.setOnClickListener(view1 -> {

            if (!TextUtils.isEmpty(et_qty.getText().toString())) {
                qty = et_qty.getText().toString().trim();
            }
            int total;
            if (type.equals("local")) {
                total = Integer.parseInt(qty) * local;
            } else if (type.equals("express")) {
                total = Integer.parseInt(qty) * express;
            } else {
                total = Integer.parseInt(qty) * superFast;
            }

            if (TextUtils.isEmpty(et_qty.getText().toString())) {
                et_qty.requestFocus();
                et_qty.setError("Please enter no of ticket");
            } else if (Integer.parseInt(et_qty.getText().toString()) == 0) {
                et_qty.requestFocus();
                et_qty.setError("Please enter valid no of ticket");
            } else if (spinner_from.getSelectedItemPosition() == spinner_to.getSelectedItemPosition()) {
                Toast.makeText(activity, "Please select different station", Toast.LENGTH_SHORT).show();
            } else {
                if (Connectivity.getInstance(activity).isOnline()) {
                    addTicket(pref.getID(), pref.getTYPE(), type, spinner_from.getSelectedItem().toString(), spinner_to.getSelectedItem().toString(), qty, price, "" + total);
                } else {
                    Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addTicket(String uid, String utype, String ttype, String from, String to, String qty, String price, String total) {

        utils.startProgressDialog();

        Call<CommonModel> call = apiInterface.addTicket(uid, utype, ttype, from, to, qty, price, total);
        call.enqueue(new Callback<CommonModel>() {
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                utils.stopProgressDialog();
                try {
                    assert response.body() != null;
                    if (response.body().getResult().equals("200")) {
                        PassTicketActivity.activity.recreate();
                        activity.finish();
                    } else {
                        Toast.makeText(activity, "Please Try Again", Toast.LENGTH_SHORT).show();
                    }

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
}
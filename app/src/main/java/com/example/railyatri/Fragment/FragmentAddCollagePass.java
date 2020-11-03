package com.example.railyatri.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
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

public class FragmentAddCollagePass extends Fragment {

    private View mFragmentView;
    private EditText et_collage;
    private EditText et_enroll;
    private Button btn_apply;
    private Spinner spinner_from;
    private Spinner spinner_to;
    private RadioGroup radio_group;
    private RadioGroup radio_group_class;
    private Activity activity;
    private Utils utils;
    private ApiInterface apiInterface;
    private SharedPref pref;
    private String enroll;
    private String college;
    private String to;
    private String from;
    private String month = "1 Month";
    private String trainClass = "First Class";
    private int month1first = 600;
    private int month3first = 1500;
    private int month1second = 200;
    private int month3second = 500;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(R.layout.fragment_add_collage_pass, null);
            init(mFragmentView);
        }
        return mFragmentView;
    }

    public void init(View view) {

        activity = getActivity();
        utils = new Utils(activity);
        pref = new SharedPref(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);

        et_collage = view.findViewById(R.id.et_collage);
        et_enroll = view.findViewById(R.id.et_enroll);
        btn_apply = view.findViewById(R.id.btn_apply);
        spinner_from = view.findViewById(R.id.spinner_from);
        spinner_to = view.findViewById(R.id.spinner_to);
        radio_group = view.findViewById(R.id.radio_group);
        radio_group_class = view.findViewById(R.id.radio_group_class);

        et_collage.setText(pref.getCollage());
        et_enroll.setText(pref.getEnroll());

        ArrayAdapter<String> adapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.station));
        spinner_from.setAdapter(adapter);
        spinner_to.setAdapter(adapter);

        spinner_from.setSelection(12);

        radio_group_class.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.radio_fc) {
                trainClass = "First Class";
            } else {
                trainClass = "Second Class";
            }
        });

        radio_group.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.radio_1) {
                month = "1 Month";
            } else {
                month = "3 Month";
            }
        });

        btn_apply.setOnClickListener(view1 -> {

            college = et_collage.getText().toString().trim();
            enroll = et_enroll.getText().toString().trim();
            to = spinner_to.getSelectedItem().toString();
            from = spinner_from.getSelectedItem().toString();

            String payment;
            if (month.equalsIgnoreCase("1 Month")) {
                if (trainClass.equalsIgnoreCase("First Class")) {
                    payment = month1first + "";
                } else {
                    payment = month1second + "";
                }
            } else {
                if (trainClass.equalsIgnoreCase("First Class")) {
                    payment = month3first + "";
                } else {
                    payment = month3second + "";
                }
            }

            if (to.equalsIgnoreCase(from)) {
                Toast.makeText(activity, "Please Select Different Station", Toast.LENGTH_SHORT).show();
            } else {
                if (Connectivity.getInstance(activity).isOnline()) {
                    String[] path = pref.getPhoto().split("/");
                    applyCollagePass(enroll, pref.getCollageId(), to, from, month, trainClass, payment, path[path.length - 1]);
                } else {
                    Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void applyCollagePass(String enroll, String collage, String to, String from, String month, String trainClass, String payment, String fileName) {

        utils.startProgressDialog();

        Call<CommonModel> call = apiInterface.applyCollagePass(enroll, collage, to, from, month, trainClass, payment, fileName);
        call.enqueue(new Callback<CommonModel>() {
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                utils.stopProgressDialog();
                try {
                    assert response.body() != null;
                    if (response.body().getResult().equals("200")) {
                        PassTicketActivity.activity.recreate();
                        activity.finish();
                    } else {
                        Toast.makeText(activity, "Please Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(activity, "Please Try Again Later", Toast.LENGTH_SHORT).show();
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

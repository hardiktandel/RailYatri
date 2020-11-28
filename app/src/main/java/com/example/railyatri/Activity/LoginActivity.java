package com.example.railyatri.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.railyatri.Model.LoginModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.SharedPref;
import com.example.railyatri.Utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText et_mobile;
    private TextInputEditText et_password;
    private Activity activity;
    private Button btn_login;
    private Button btn_signup;
    private TextView tv_skip;
    private RadioGroup radio;
    private String mobile;
    private String password;
    private String type = "user";
    private Utils utils;
    private SharedPref pref;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_mobile = findViewById(R.id.et_mobile);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        tv_skip = findViewById(R.id.tv_skip);
        radio = findViewById(R.id.radio);
        activity = this;
        utils = new Utils(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);
        pref = new SharedPref(activity);

        radio.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.radio_user) {
                type = "user";
            } else if (i == R.id.radio_student) {
                type = "student";
            } else if (i == R.id.radio_coolie) {
                type = "coolie";
            } else {
                type = "shop";
            }
        });

        btn_login.setOnClickListener(v -> {

            mobile = Objects.requireNonNull(et_mobile.getText()).toString().trim();
            password = Objects.requireNonNull(et_password.getText()).toString().trim();

            if (TextUtils.isEmpty(mobile)) {
                et_mobile.setError("Please Enter Mobile No.");
                et_mobile.requestFocus();
            } else if (mobile.length() != 10) {
                et_mobile.setError("Please Enter Valid Number");
                et_mobile.requestFocus();
            } else if (TextUtils.isEmpty(password)) {
                et_password.setError("Please Enter Password");
                et_password.requestFocus();
            } else {
                getLogin();
            }
        });

        btn_signup.setOnClickListener(v -> {
            Intent i = new Intent(activity, MobileVerificationActivity.class);
            startActivityForResult(i, 100);
        });

        if (pref.getFirstName().equals("Guest User")) {
            tv_skip.setVisibility(View.GONE);
        }

        tv_skip.setOnClickListener(v -> {
            pref.setFirstName("Guest User");
            Intent homeIntent = new Intent(activity, HomeActivity.class);
            startActivity(homeIntent);
            finish();
        });
    }

    private void getLogin() {

        utils.startProgressDialog();

        Call<LoginModel> call = apiInterface.login(mobile, password, type);
        call.enqueue(new Callback<LoginModel>() {
            public void onResponse(@NonNull Call<LoginModel> call, @NonNull Response<LoginModel> response) {

                utils.stopProgressDialog();
//                if (response.isSuccessful()) {
                try {
                    assert response.body() != null;
                    if (response.body().getResult() == 200) {

                        pref.setPhoto(response.body().getPhoto());
                        pref.setID(response.body().getId());
                        pref.setTYPE(type);
                        pref.setFirstName(response.body().getFirstName());
                        pref.setLastName(response.body().getLastName());
                        pref.setMobile(response.body().getMobile());
                        pref.setAddress(response.body().getAddress());
                        pref.setEnroll(response.body().getEnroll());
                        pref.setCollage(response.body().getCollage());
                        pref.setCollageId(response.body().getCollageId());
                        createShortcuts(pref.getTYPE());
                        Intent i = new Intent(activity, HomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                        Toast.makeText(activity, "Successfully Login!!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(activity, "Wrong Mobile or Password", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(activity, "Please Try Again", Toast.LENGTH_SHORT).show();
                }
//                } else {
//                    ApiErrorModel apiError = ErrorUtils.parseError(response);
//                    Toast.makeText(activity, apiError.getMessage(), Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginModel> call, @NonNull Throwable t) {
                call.cancel();
                utils.stopProgressDialog();
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createShortcuts(String type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            switch (type.toLowerCase()) {
                case "user":
                case "student":
                    createDynamicShortcuts(
                            createShortcut("bookingCoolie", "Booking Coolie", "Booking Coolie", R.drawable.coolie, BookedCoolieActivity.class),
                            createShortcut("foodOrder", "Food Orders", "Food Orders", R.drawable.ic_room_service_black_24dp, FoodOrderActivity.class),
                            createShortcut("passTicket", "Pass And Ticket", "Pass And Ticket", R.drawable.ic_receipt_black_24dp, PassTicketActivity.class)
                    );
                    break;
                case "coolie":
                    createDynamicShortcuts(
                            createShortcut("coolie", "Coolie", "Coolie Admin", R.drawable.bag, CoolieActivity.class)
                    );
                    break;
                case "shop":
                    createDynamicShortcuts(
                            createShortcut("foodShop", "Food Shop", "Food Shop Admin", R.drawable.ic_restaurant_menu_black_24dp, FoodShopActivity.class)
                    );
                    break;
            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @NotNull
    private ShortcutInfo createShortcut(String shortcutId, String shortLabel, String longLabel, int iconResource, Class<?> targetActivityClass) {
        return new ShortcutInfo.Builder(getApplicationContext(), shortcutId)
                .setShortLabel(shortLabel)
                .setLongLabel(longLabel)
                .setIcon(Icon.createWithResource(getApplicationContext(), iconResource))
                .setIntents(new Intent[]{
                        new Intent(Intent.ACTION_VIEW, Uri.EMPTY, activity, HomeActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),
                        new Intent(Intent.ACTION_VIEW, Uri.EMPTY, activity, targetActivityClass)
                })
                .build();
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void createDynamicShortcuts(ShortcutInfo... shortcuts) {
        List<ShortcutInfo> shortcutList = new ArrayList<>();
        Collections.addAll(shortcutList, shortcuts);
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
        Objects.requireNonNull(shortcutManager).addDynamicShortcuts(shortcutList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                mobile = data.getStringExtra("Mobile");
                password = data.getStringExtra("Password");
                type = "user";
                getLogin();
            }
        }
    }
}
package com.example.railyatri.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.railyatri.Model.CommonModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.Const;
import com.example.railyatri.Utils.SharedPref;
import com.example.railyatri.Utils.Utils;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFoodOrderActivity extends AppCompatActivity {

    private Button btn_add;
    private ShapeableImageView img_icon;
    private TextView tv_name;
    private TextView tv_description;
    private TextView tv_price;
    private EditText et_qty;
    private RadioGroup radio_group;
    private RadioButton rbtn_small;
    private RadioButton rbtn_medium;
    private RadioButton rbtn_large;
    private Activity activity;
    private Utils utils;
    private ApiInterface apiInterface;
    private SharedPref pref;
    private String name;
    private String description;
    private String price;
    private String price2;
    private String price3;
    private String foodId;
    private String shopId;
    private String category;
    private String photo;
    private String size = "1";
    private int total = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_order);

        activity = this;
        utils = new Utils(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);
        pref = new SharedPref(activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Item");

        img_icon = findViewById(R.id.img_icon);
        tv_name = findViewById(R.id.tv_name);
        tv_description = findViewById(R.id.tv_description);
        tv_price = findViewById(R.id.tv_price);
        et_qty = findViewById(R.id.et_qty);
        radio_group = findViewById(R.id.radio_group);
        rbtn_small = findViewById(R.id.rbtn_small);
        rbtn_medium = findViewById(R.id.rbtn_medium);
        rbtn_large = findViewById(R.id.rbtn_large);
        btn_add = findViewById(R.id.btn_add);

        name = getIntent().getStringExtra("name");
        description = getIntent().getStringExtra("description");
        price = getIntent().getStringExtra("price");
        price2 = getIntent().getStringExtra("price2");
        price3 = getIntent().getStringExtra("price3");
        foodId = getIntent().getStringExtra("foodId");
        photo = getIntent().getStringExtra("photo");
        shopId = getIntent().getStringExtra("shopId");
        category = getIntent().getStringExtra("category");

        Picasso.get().load(Const.URL + photo).into(img_icon);

        tv_name.setText(name);
        tv_price.setText("₹ " + price);
        tv_description.setText(description);

        if (category.equalsIgnoreCase("pizza")) {
            radio_group.setVisibility(View.VISIBLE);
            tv_price.setVisibility(View.GONE);
            rbtn_small.setText("Small Size ( ₹ " + price + " )");
            rbtn_medium.setText("Medium Size ( ₹ " + price2 + " )");
            rbtn_large.setText("Large Size ( ₹ " + price3 + " )");
        } else {
            radio_group.setVisibility(View.GONE);
            tv_price.setVisibility(View.VISIBLE);
        }

        radio_group.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rbtn_small) {
                size = "1";
            } else if (i == R.id.rbtn_medium) {
                size = "2";
            } else {
                size = "3";
            }
        });

        btn_add.setOnClickListener(view -> {
            if (TextUtils.isEmpty(et_qty.getText().toString())) {
                et_qty.setError("Please Enter Quantity");
                et_qty.requestFocus();
            } else {
                int qty = Integer.parseInt(et_qty.getText().toString());
                if (qty == 0) {
                    et_qty.setError("Please Enter Valid Quantity");
                    et_qty.requestFocus();
                } else {
                    if (category.equalsIgnoreCase("pizza")) {
                        if (size.equals("1")) {
                            total = qty * Integer.parseInt(price);
                        } else if (size.equals("2")) {
                            total = qty * Integer.parseInt(price2);
                        } else {
                            total = qty * Integer.parseInt(price3);
                        }
                    } else {
                        total = qty * Integer.parseInt(price);
                    }
                    if (Connectivity.getInstance(activity).isOnline()) {
                        String[] path = photo.split("/");
                        addFoodOrder(pref.getID(), pref.getTYPE(), name, category, shopId, size, et_qty.getText().toString(), "" + total, path[path.length - 1]);
                    } else {
                        Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void addFoodOrder(String userId, String userType, String foodName, String categoryName, String shopId, String size, String qty, String total, String fileName) {

        utils.startProgressDialog();

        Call<CommonModel> call = apiInterface.addFoodOrder(userId, userType, foodName, categoryName, shopId, size, qty, total, fileName);
        call.enqueue(new Callback<CommonModel>() {
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                utils.stopProgressDialog();
                try {
                    assert response.body() != null;
                    if (response.body().getResult().equals("200")) {
                        setResult(RESULT_OK);
                        finish();
                        FoodListActivity.activity.finish();
                        FoodCategoryActivity.activity.finish();
                        ShopListActivity.activity.finish();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}

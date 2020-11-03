package com.example.railyatri.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.railyatri.Model.CommonModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.SharedPref;
import com.example.railyatri.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.railyatri.Utils.Const.URL;

public class AddCoolieOrderActivity extends AppCompatActivity {

    private TextView tv_station;
    private TextView tv_coolie;
    private TextView tv_mo_no;
    private EditText et_place;
    private CircleImageView img_coolie_icon;
    private Button btn_add;
    private Activity activity;
    private Utils utils;
    private ApiInterface apiInterface;
    private SharedPref pref;
    private String station;
    private String coolieId;
    private String coolieName;
    private String coolieContact;
    private String place;
    private String photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coolie_order);

        activity = this;
        utils = new Utils(activity);
        pref = new SharedPref(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Book Coolie");

        coolieId = getIntent().getStringExtra("id");
        coolieName = getIntent().getStringExtra("name");
        coolieContact = getIntent().getStringExtra("contact");
        station = getIntent().getStringExtra("station");
        photo = getIntent().getStringExtra("photo");

        et_place = findViewById(R.id.et_place);
        tv_station = findViewById(R.id.tv_station);
        tv_coolie = findViewById(R.id.tv_coolie);
        tv_mo_no = findViewById(R.id.tv_mo_no);
        btn_add = findViewById(R.id.btn_add);
        img_coolie_icon = findViewById(R.id.img_icon);

        if (photo.equals("")) {
            img_coolie_icon.setImageResource(R.drawable.people);
        } else {
            Picasso.get().load(URL + photo).into(img_coolie_icon);
        }
        tv_coolie.setText(coolieName);
        tv_station.setText(station);
        tv_mo_no.setText(coolieContact);

        btn_add.setOnClickListener(view -> {
            place = et_place.getText().toString().trim();
            if (TextUtils.isEmpty(place)) {
                et_place.setError("Please Enter Place");
                et_place.requestFocus();
            } else {
                if (Connectivity.getInstance(activity).isOnline()) {
                    bookCoolie(pref.getID(), coolieId, station, place);
                } else {
                    Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void bookCoolie(String userId, String coolieId, String station, String place) {

        utils.startProgressDialog();

        Call<CommonModel> call = apiInterface.bookCoolie(coolieId, userId, station, place);
        call.enqueue(new Callback<CommonModel>() {
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                utils.stopProgressDialog();
                try {
                    assert response.body() != null;
                    if (response.body().getResult().equals("200")) {
                        setResult(RESULT_OK);
                        SelectCoolieActivity.activity.finish();
                        finish();
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

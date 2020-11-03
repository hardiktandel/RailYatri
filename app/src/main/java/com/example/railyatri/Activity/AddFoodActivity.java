package com.example.railyatri.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.railyatri.Model.CommonModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.Const;
import com.example.railyatri.Utils.SharedPref;
import com.example.railyatri.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.railyatri.Utils.Const.URL;

public class AddFoodActivity extends AppCompatActivity {

    private CircleImageView img_icon;
    private EditText et_name;
    private EditText et_description;
    private EditText et_price;
    private EditText et_price2;
    private EditText et_price3;
    private Button btn_add;
    private Spinner spinner;
    private Activity activity;
    private Utils utils;
    private ApiInterface apiInterface;
    private SharedPref pref;
    private Uri imageUri;
    private Bitmap bitmap;
    private String id;
    private String name;
    private String description;
    private String price;
    private String price2;
    private String price3;
    private String photo;
    private String selectedPhoto = "";
    private int category;
    private boolean forUpdate;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        img_icon = findViewById(R.id.img_icon);
        et_name = findViewById(R.id.et_name);
        et_description = findViewById(R.id.et_description);
        et_price = findViewById(R.id.et_price);
        et_price2 = findViewById(R.id.et_price2);
        et_price3 = findViewById(R.id.et_price3);
        btn_add = findViewById(R.id.btn_add);
        spinner = findViewById(R.id.spinner);

        activity = this;
        utils = new Utils(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);
        pref = new SharedPref(activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Food Item");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.food_category));
        spinner.setAdapter(adapter);

        forUpdate = getIntent().getBooleanExtra("update", false);

        if (forUpdate) {
            btn_add.setText("Update");
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            description = getIntent().getStringExtra("description");
            price = getIntent().getStringExtra("price");
            price2 = getIntent().getStringExtra("price2");
            price3 = getIntent().getStringExtra("price3");
            photo = getIntent().getStringExtra("photo");
            category = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("category")));

            Picasso.get().load(URL + photo).into(img_icon);
            et_name.setText(name);
            et_description.setText(description);
            et_price.setText(price);
            et_price2.setText(price2);
            et_price3.setText(price3);

            spinner.setSelection(category - 1);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    et_price.setHint("Enter Price(Small Size Pizza)");
                    et_price2.setVisibility(View.VISIBLE);
                    et_price3.setVisibility(View.VISIBLE);
                } else {
                    et_price.setHint("Enter Price");
                    et_price2.setVisibility(View.GONE);
                    et_price3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        img_icon.setOnClickListener(v -> {
            if (isPermissionGranted()) {
                openImagePicker();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                }
            }
        });

        btn_add.setOnClickListener(v -> {

            name = et_name.getText().toString().trim();
            description = et_description.getText().toString().trim();
            price = et_price.getText().toString().trim();
            price2 = et_price2.getVisibility() == View.GONE ? "0" : et_price2.getText().toString().trim();
            price3 = et_price3.getVisibility() == View.GONE ? "0" : et_price3.getText().toString().trim();
            category = spinner.getSelectedItemPosition() + 1;

            RequestBody foodImage = null;
            if (!selectedPhoto.equals("")) {
                File file = new File(selectedPhoto);
                foodImage = RequestBody.create(file, MediaType.parse("image/*"));
            } else {
                if (!forUpdate) {
                    Toast.makeText(activity, "Select Food Image", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (TextUtils.isEmpty(name)) {
                et_name.setError("Please Enter Name");
                et_name.requestFocus();
            } else if (TextUtils.isEmpty(description)) {
                et_description.setError("Please Enter Description");
                et_description.requestFocus();
            } else if (TextUtils.isEmpty(price)) {
                et_price.setError("Please Enter Description");
                et_price.requestFocus();
            } else {
                if (category == 1) {
                    if (TextUtils.isEmpty(price2)) {
                        et_price2.setError("Please Enter Price");
                        et_price2.requestFocus();
                        return;
                    } else if (TextUtils.isEmpty(price3)) {
                        et_price3.setError("Please Enter Price");
                        et_price3.requestFocus();
                        return;
                    }
                }

                RequestBody categoryId = RequestBody.create("" + category, MediaType.parse("text/plain"));
                RequestBody foodName = RequestBody.create(name, MediaType.parse("text/plain"));
                RequestBody foodDescription = RequestBody.create(description, MediaType.parse("text/plain"));
                RequestBody foodPrice = RequestBody.create(price, MediaType.parse("text/plain"));
                RequestBody foodPrice2 = RequestBody.create(price2, MediaType.parse("text/plain"));
                RequestBody foodPrice3 = RequestBody.create(price3, MediaType.parse("text/plain"));

                if (Connectivity.getInstance(activity).isOnline()) {
                    if (forUpdate) {
                        RequestBody foodId = RequestBody.create(id, MediaType.parse("text/plain"));
                        updateFood(foodId, categoryId, foodName, foodDescription, foodPrice, foodPrice2, foodPrice3, foodImage);
                    } else {
                        RequestBody shopId = RequestBody.create(pref.getID(), MediaType.parse("text/plain"));
                        addFood(shopId, categoryId, foodName, foodDescription, foodPrice, foodPrice2, foodPrice3, foodImage);
                    }
                } else {
                    Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addFood(RequestBody shopId, RequestBody categoryId, RequestBody name, RequestBody description, RequestBody price, RequestBody price2, RequestBody price3, RequestBody foodImage) {

        utils.startProgressDialog();

        Call<CommonModel> call = apiInterface.addFood(shopId, name, description, price, price2, price3, categoryId, foodImage);
        call.enqueue(new Callback<CommonModel>() {
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                utils.stopProgressDialog();
                try {
                    assert response.body() != null;
                    if (response.body().getResult().equals("200")) {
                        Toast.makeText(activity, "Succeed", Toast.LENGTH_SHORT).show();
                        finish();
                        FoodShopActivity.activity.recreate();
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

    private void updateFood(RequestBody foodId, RequestBody categoryId, RequestBody name, RequestBody description, RequestBody price, RequestBody price2, RequestBody price3, RequestBody foodImage) {

        utils.startProgressDialog();

        Call<CommonModel> call = apiInterface.updateFood(foodId, name, description, price, price2, price3, categoryId, foodImage);
        call.enqueue(new Callback<CommonModel>() {
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                utils.stopProgressDialog();
                try {
                    assert response.body() != null;
                    if (response.body().getResult().equals("200")) {
                        Toast.makeText(activity, "Succeed", Toast.LENGTH_SHORT).show();
                        finish();
                        FoodShopActivity.activity.recreate();
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

    private void openImagePicker() {

        TextView textView = new TextView(activity);
        textView.setText("Select Food Image");
        textView.setPadding(40, 30, 40, 30);
        textView.setTextSize(20F);
        textView.setBackgroundColor(getResources().getColor(R.color.grey));
        textView.setTextColor(Color.WHITE);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setCustomTitle(textView);
        CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        dialogBuilder.setItems(options, (dialog, which) -> {
            if (options[which].equals("Take Photo")) {
                imageUri = utils.getPhotoFileUri(activity);
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePicture, Const.CAPTURE_IMAGE_INTENT);
            } else if (options[which].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, Const.CHOOSE_PHOTO_INTENT);
            } else if (options[which].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        dialogBuilder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.CAPTURE_IMAGE_INTENT:
                if (resultCode == RESULT_OK) {
                    this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri));
                    imageUri = utils.performCrop(activity, imageUri, false, null);
                } else {
                    utils.deleteFile(activity, imageUri);
                    Toast.makeText(activity, "Cancelled", Toast.LENGTH_LONG).show();
                }
                break;
            case Const.CHOOSE_PHOTO_INTENT:
                if (resultCode == RESULT_OK && data != null) {
                    imageUri = data.getData();
                    imageUri = utils.performCrop(activity, imageUri, false, null);
                } else {
                    Toast.makeText(activity, "Cancelled", Toast.LENGTH_LONG).show();
                }
                break;
            case Const.CROP_IMAGE_INTENT:
                if (resultCode == RESULT_OK) {
                    Uri croppedImageUri = data.getData();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    try {
                        BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(imageUri), null, options);
                        options.inSampleSize = utils.calculateInSampleSize(options, 400, 400);
                        options.inJustDecodeBounds = false;
                        bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(Objects.requireNonNull(croppedImageUri)), null, options);
                        imageUri = utils.getImageUri(activity, Objects.requireNonNull(bitmap));
                        selectedPhoto = utils.getRealPathFromUri(imageUri);
                        img_icon.setImageBitmap(bitmap);
                        utils.deleteFile(activity, croppedImageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    utils.deleteFile(activity, imageUri);
                    Toast.makeText(activity, "Cancelled", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            }
        }
    }
}

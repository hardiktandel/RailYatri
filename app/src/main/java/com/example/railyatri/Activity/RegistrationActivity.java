package com.example.railyatri.Activity;

import android.Manifest;
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
import android.widget.Button;
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
import com.example.railyatri.Utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private CircleImageView img_icon;
    private TextView tv_logIn;
    private TextInputEditText et_firstName;
    private TextInputEditText et_lastName;
    private TextInputEditText et_mobile;
    private TextInputEditText et_address;
    private TextInputEditText et_password;
    private TextInputEditText et_confirm_password;
    private Button btn_signUp;
    private String firstName;
    private String lastName;
    private String mobile;
    private String address;
    private String password;
    private String confirm_password;
    private String selectedPhoto = "";
    private ApiInterface apiInterface;
    private Activity activity;
    private Utils utils;
    private Uri imageUri;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        img_icon = findViewById(R.id.img_icon);
        et_firstName = findViewById(R.id.et_fname);
        et_lastName = findViewById(R.id.et_lname);
        et_mobile = findViewById(R.id.et_mobile);
        et_address = findViewById(R.id.et_address);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        btn_signUp = findViewById(R.id.btn_signup);
        tv_logIn = findViewById(R.id.tv_login);

        activity = this;
        utils = new Utils(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);

        mobile = getIntent().getStringExtra("Mobile");
        et_mobile.setText(mobile);
        et_mobile.setEnabled(false);

        tv_logIn.setOnClickListener(v -> finish());

        img_icon.setOnClickListener(v -> {
            if (isPermissionGranted()) {
                openImagePicker();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                }
            }
        });

        btn_signUp.setOnClickListener(v -> {
            firstName = Objects.requireNonNull(et_firstName.getText()).toString().trim();
            lastName = Objects.requireNonNull(et_lastName.getText()).toString().trim();
            mobile = Objects.requireNonNull(et_mobile.getText()).toString().trim();
            password = Objects.requireNonNull(et_password.getText()).toString().trim();
            confirm_password = Objects.requireNonNull(et_confirm_password.getText()).toString().trim();
            address = Objects.requireNonNull(et_address.getText()).toString().trim();

            RequestBody userImage = null;
            if (!selectedPhoto.equals("")) {
                File file = new File(selectedPhoto);
                userImage = RequestBody.create(file, MediaType.parse("image/*"));
            }

            if (TextUtils.isEmpty(firstName)) {
                et_firstName.setError("Please Enter First Name");
                et_firstName.requestFocus();
            } else if (TextUtils.isEmpty(lastName)) {
                et_lastName.setError("Please enter last name");
                et_lastName.requestFocus();
            } else if (TextUtils.isEmpty(address)) {
                et_lastName.setError("Please enter address");
                et_lastName.requestFocus();
            } else if (mobile.length() != 10) {
                et_mobile.setError("please enter valid number");
                et_mobile.requestFocus();
            } else if (!password.equals(confirm_password)) {
                et_confirm_password.setError("Mismatch Password");
                et_confirm_password.requestFocus();
            } else {
                RequestBody userFirstName = RequestBody.create(firstName, MediaType.parse("text/plain"));
                RequestBody userLastName = RequestBody.create(lastName, MediaType.parse("text/plain"));
                RequestBody userMobile = RequestBody.create(mobile, MediaType.parse("text/plain"));
                RequestBody userPassword = RequestBody.create(password, MediaType.parse("text/plain"));
                RequestBody userAddress = RequestBody.create(address, MediaType.parse("text/plain"));
                if (Connectivity.getInstance(this).isOnline()) {
                    getRegistration(userFirstName, userLastName, userMobile, userPassword, userAddress, userImage);
                } else {
                    Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getRegistration(RequestBody userFirstName, RequestBody userLastName, RequestBody userMobile, RequestBody userPassword, RequestBody userAddress, RequestBody userImage) {

        utils.startProgressDialog();

        Call<CommonModel> call = apiInterface.registration(userFirstName, userLastName, userMobile, userPassword, userAddress, userImage);
        call.enqueue(new Callback<CommonModel>() {
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                utils.stopProgressDialog();
                try {
                    assert response.body() != null;
                    if (response.body().getResult().equals("200")) {
                        Intent succeedIntent = new Intent(activity, SignUpSucceedActivity.class);
                        succeedIntent.putExtra("Mobile", mobile);
                        succeedIntent.putExtra("Password", password);
                        succeedIntent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                        startActivity(succeedIntent);
                        finish();
                    } else if (response.body().getResult().equals("400")) {
                        Toast.makeText(activity, "Mobile No is already register", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity, "Please Try Again!!", Toast.LENGTH_LONG).show();
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

    private void openImagePicker() {

        TextView textView = new TextView(this);
        textView.setText("Select User Image");
        textView.setPadding(40, 30, 40, 30);
        textView.setTextSize(20F);
        textView.setBackgroundColor(getResources().getColor(R.color.grey));
        textView.setTextColor(Color.WHITE);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCustomTitle(textView);
        CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        dialogBuilder.setItems(options, (dialog, which) -> {
            if (options[which].equals("Take Photo")) {
                imageUri = utils.getPhotoFileUri(this);
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

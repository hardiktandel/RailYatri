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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.railyatri.Model.UpdateUserModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.Const;
import com.example.railyatri.Utils.SharedPref;
import com.example.railyatri.Utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

public class ViewProfileActivity extends AppCompatActivity {

    private LinearLayout layout_main;
    private CircleImageView img_icon;
    private TextInputLayout etFNameLayout;
    private TextInputLayout etLNameLayout;
    //    private TextInputLayout etMobileLayout;
    private TextInputLayout etAddressLayout;
    private TextInputLayout etCollageLayout;
    private TextInputLayout etEnrollLayout;
    private TextInputEditText et_fname;
    private TextInputEditText et_lname;
    private TextInputEditText et_mobile;
    private TextInputEditText et_address;
    private TextInputEditText et_collage;
    private TextInputEditText et_enroll;
    private Button btn_update;
    private SharedPref pref;
    private ApiInterface apiInterface;
    private Activity activity;
    private Utils utils;
    private MenuItem menuEdit;
    private Uri imageUri;
    private Bitmap bitmap;
    private String id;
    private String userType;
    private String fName;
    private String lName;
    private String mobile;
    private String address;
    private String collage;
    private String enroll;
    private String photo;
    private String selectedPhoto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        activity = this;
        layout_main = findViewById(R.id.layout_main);
        img_icon = findViewById(R.id.img_icon);
        etFNameLayout = findViewById(R.id.etFNameLayout);
        etLNameLayout = findViewById(R.id.etLNameLayout);
//        etMobileLayout = findViewById(R.id.etMobileLayout);
        etAddressLayout = findViewById(R.id.etAddressLayout);
        etCollageLayout = findViewById(R.id.etCollageLayout);
        etEnrollLayout = findViewById(R.id.etEnrollLayout);
        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        et_mobile = findViewById(R.id.et_mobile);
        et_address = findViewById(R.id.et_address);
        et_collage = findViewById(R.id.et_collage);
        et_enroll = findViewById(R.id.et_enroll);
        btn_update = findViewById(R.id.btn_update);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        utils = new Utils(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);
        showProfileDetails();

        img_icon.setOnClickListener(v -> {
            if (isPermissionGranted()) {
                openImagePicker();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                }
            }
        });

        btn_update.setOnClickListener(v -> {
            fName = Objects.requireNonNull(et_fname.getText()).toString().trim();
            lName = Objects.requireNonNull(et_lname.getText()).toString().trim();
            address = Objects.requireNonNull(et_address.getText()).toString().trim();

            RequestBody userImage = null;
            if (!selectedPhoto.equals("")) {
                File file = new File(selectedPhoto);
                userImage = RequestBody.create(file, MediaType.parse("image/*"));
            }

            if (TextUtils.isEmpty(fName)) {
                et_fname.setError("Enter First Name");
                et_fname.requestFocus();
            } else if (TextUtils.isEmpty(address)) {
                et_address.setError("Enter Address");
                et_address.requestFocus();
            } else {
                if (etLNameLayout.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(lName)) {
                        et_lname.setError("Enter Last Name");
                        et_lname.requestFocus();
                        return;
                    }
                }

                RequestBody userId = RequestBody.create(id, MediaType.parse("text/plain"));
                RequestBody uType = RequestBody.create(userType, MediaType.parse("text/plain"));
                RequestBody userFName = RequestBody.create(fName, MediaType.parse("text/plain"));
                RequestBody userLName = RequestBody.create(lName, MediaType.parse("text/plain"));
                RequestBody userAddress = RequestBody.create(address, MediaType.parse("text/plain"));

                if (Connectivity.getInstance(activity).isOnline()) {
                    utils.hideSoftKeyboard(activity, layout_main);
                    updateUser(userId, uType, userFName, userLName, userAddress, userImage);
                } else {
                    Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUser(RequestBody userId, RequestBody userType, RequestBody userFName, RequestBody userLName, RequestBody userAddress, RequestBody userImage) {

        utils.startProgressDialog();

        Call<UpdateUserModel> call = apiInterface.updateUser(userId, userType, userFName, userLName, userAddress, userImage);
        call.enqueue(new Callback<UpdateUserModel>() {
            public void onResponse(@NonNull Call<UpdateUserModel> call, @NonNull Response<UpdateUserModel> response) {

                utils.stopProgressDialog();
                try {
                    assert response.body() != null;
                    if (response.body().getResult().equals("200")) {
                        Toast.makeText(activity, "Succeed", Toast.LENGTH_SHORT).show();
                        storeDataToSharedPref(response.body().getPhoto());
                        showProfileDetails();
                        setResult(RESULT_OK);
                    } else {
                        Toast.makeText(activity, "Please Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(activity, "Please Try Again Later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateUserModel> call, @NonNull Throwable t) {
                call.cancel();
                utils.stopProgressDialog();
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeDataToSharedPref(String photoPath) {
        pref = new SharedPref(activity);
        pref.setID(id);
        pref.setTYPE(userType);
        pref.setPhoto(photoPath);
        pref.setFirstName(fName);
        pref.setLastName(lName);
        pref.setAddress(address);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (pref.getCollage().isEmpty()) {
            getMenuInflater().inflate(R.menu.activity_view_profile, menu);
            menuEdit = menu.findItem(R.id.edit_profile);
            menuEdit.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.edit_profile:
                editProfileDetails();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (btn_update.getVisibility() == View.GONE) {
            super.onBackPressed();
        } else {
            showProfileDetails();
        }
    }

    private void editProfileDetails() {
        img_icon.setEnabled(true);

        et_fname.setEnabled(true);
        et_fname.setTextColor(getResources().getColor(R.color.grey));
        etFNameLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);

        et_lname.setEnabled(true);
        et_lname.setTextColor(getResources().getColor(R.color.grey));
        etLNameLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);

        et_address.setEnabled(true);
        et_address.setTextColor(getResources().getColor(R.color.grey));
        etAddressLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);

        btn_update.setVisibility(View.VISIBLE);
        menuEdit.setVisible(false);

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.toggleSoftInputFromWindow(et_fname.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        et_fname.requestFocus();
        et_fname.setSelection(Objects.requireNonNull(et_fname.getText()).length());
    }

    private void showProfileDetails() {
        getDataFromSharedPref();
        if (photo.equals("")) {
            switch (userType.toLowerCase()) {
                case "shop":
                    img_icon.setImageResource(R.drawable.restaurant);
                    break;
                case "coolie":
                    img_icon.setImageResource(R.drawable.people);
                    break;
                default:
                    img_icon.setImageResource(R.drawable.ic_account_circle_200dp);
                    break;
            }
        } else {
            Picasso.get().load(Const.URL + photo)
                    .error(R.drawable.ic_account_circle_200dp)
                    .into(img_icon);
        }
        img_icon.setEnabled(false);

        et_fname.setEnabled(false);
        etFNameLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);

        et_lname.setEnabled(false);
        etLNameLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);

        et_address.setEnabled(false);
        etAddressLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);

        btn_update.setVisibility(View.GONE);
        if (menuEdit != null) {
            menuEdit.setVisible(true);
        }

        et_fname.setText(fName);
        et_fname.setTextColor(getResources().getColor(R.color.light_grey));

        et_mobile.setText(mobile);
        et_mobile.setTextColor(getResources().getColor(R.color.light_grey));

        et_address.setText(address);
        et_address.setTextColor(getResources().getColor(R.color.light_grey));

        if (lName.isEmpty()) {
            etLNameLayout.setVisibility(View.GONE);
        } else {
            et_lname.setText(lName);
            et_lname.setTextColor(getResources().getColor(R.color.light_grey));
        }

        if (collage.isEmpty()) {
            etCollageLayout.setVisibility(View.GONE);
            etEnrollLayout.setVisibility(View.GONE);
        } else {
            et_collage.setText(collage);
            et_collage.setTextColor(getResources().getColor(R.color.light_grey));
            et_enroll.setText(enroll);
            et_enroll.setTextColor(getResources().getColor(R.color.light_grey));
        }
        btn_update.setVisibility(View.GONE);
    }

    private void getDataFromSharedPref() {
        pref = new SharedPref(activity);
        id = pref.getID();
        userType = pref.getTYPE();
        photo = pref.getPhoto();
        fName = pref.getFirstName();
        lName = pref.getLastName();
        mobile = pref.getMobile();
        address = pref.getAddress();
        collage = pref.getCollage();
        enroll = pref.getEnroll();
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
                utils.hideSoftKeyboard(activity, layout_main);
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
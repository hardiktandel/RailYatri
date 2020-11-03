package com.example.railyatri.Fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.railyatri.Activity.PassTicketActivity;
import com.example.railyatri.Model.CommonModel;
import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.Connectivity;
import com.example.railyatri.Utils.Const;
import com.example.railyatri.Utils.SharedPref;
import com.example.railyatri.Utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class FragmentAddNormalPass extends Fragment {

    private View mFragmentView;
    private EditText et_name;
    private EditText et_age;
    private Button btn_apply;
    private ImageView img_sel_photo;
    private ImageView img_sel_proof;
    private ImageView img_photo;
    private Spinner spinner_from;
    private Spinner spinner_to;
    private RadioGroup radio_group_duration;
    private RadioGroup radio_group_class;
    private RadioGroup radio_group_gender;
    private RadioButton radio_1;
    private RadioButton radio_3;
    private Activity activity;
    private Utils utils;
    private SharedPref pref;
    private ApiInterface apiInterface;
    private Uri imageUri;
    private Bitmap bitmap;
    private String name;
    private String to;
    private String from;
    private String age;
    private String photo = "";
    private String idProof = "";
    private String month = "1 Month";
    private String gender = "Male";
    private String trainClass = "First Class";
    private int month1first = 600;
    private int month3first = 1500;
    private int month1second = 200;
    private int month3second = 500;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(R.layout.fragment_add_normal_pass, null);
            init(mFragmentView);
        }
        return mFragmentView;
    }

    @SuppressLint("SetTextI18n")
    private void init(View view) {

        activity = getActivity();
        utils = new Utils(activity);
        pref = new SharedPref(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);

        et_name = view.findViewById(R.id.et_name);
        et_age = view.findViewById(R.id.et_age);
        btn_apply = view.findViewById(R.id.btn_apply);
        img_sel_photo = view.findViewById(R.id.img_sel_photo);
        img_sel_proof = view.findViewById(R.id.img_sel_proof);
        spinner_from = view.findViewById(R.id.spinner_from);
        spinner_to = view.findViewById(R.id.spinner_to);
        radio_group_duration = view.findViewById(R.id.radio_group_duration);
        radio_1 = view.findViewById(R.id.radio_1);
        radio_3 = view.findViewById(R.id.radio_3);
        radio_group_class = view.findViewById(R.id.radio_group_class);
        radio_group_gender = view.findViewById(R.id.radio_group_gender);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.station));
        spinner_from.setAdapter(adapter);
        spinner_to.setAdapter(adapter);

        spinner_from.setSelection(12);

        radio_group_class.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.radio_fc) {
                trainClass = "First Class";
                radio_1.setText("1 Month (₹ " + 500 + ")");
                radio_3.setText("3 Month (₹ " + 3 * 500 + ")");
            } else {
                trainClass = "Second Class";
                radio_1.setText("1 Month (₹ " + 200 + ")");
                radio_3.setText("3 Month (₹ " + 3 * 200 + ")");
            }
        });

        radio_group_duration.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.radio_1) {
                month = "1 Month";
            } else {
                month = "3 Month";
            }
        });

        radio_group_gender.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.radio_f) {
                gender = "Female";
            } else {
                gender = "Male";
            }
        });

        img_sel_photo.setOnClickListener(v -> {
            if (isPermissionGranted()) {
                img_photo = (ImageView) v;
                openImagePicker();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            }
        });

        img_sel_proof.setOnClickListener(v -> {
            if (isPermissionGranted()) {
                img_photo = (ImageView) v;
                openImagePicker();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
            }
        });

        btn_apply.setOnClickListener(v -> {

            name = et_name.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                et_name.requestFocus();
                et_name.setError("Enter Name");
                return;
            }
            to = spinner_to.getSelectedItem().toString();
            from = spinner_from.getSelectedItem().toString();
            age = et_age.getText().toString().trim();
            if (TextUtils.isEmpty(age)) {
                et_age.requestFocus();
                et_age.setError("Enter Age");
                return;
            }

            RequestBody profileImage;
            RequestBody idProofImage;
            if (!photo.equals("")) {
                File file = new File(photo);
                profileImage = RequestBody.create(file, MediaType.parse("image/*"));
            } else {
                Toast.makeText(activity, "Select Photo", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!idProof.equals("")) {
                File file = new File(idProof);
                idProofImage = RequestBody.create(file, MediaType.parse("image/*"));
            } else {
                Toast.makeText(activity, "Select Id Proof", Toast.LENGTH_SHORT).show();
                return;
            }
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
            RequestBody userId = RequestBody.create(pref.getID(), MediaType.parse("text/plain"));
            RequestBody userType = RequestBody.create(pref.getTYPE(), MediaType.parse("text/plain"));
            RequestBody stationTo = RequestBody.create(to, MediaType.parse("text/plain"));
            RequestBody stationFrom = RequestBody.create(from, MediaType.parse("text/plain"));
            RequestBody userName = RequestBody.create(name, MediaType.parse("text/plain"));
            RequestBody duration = RequestBody.create(month, MediaType.parse("text/plain"));
            RequestBody userGender = RequestBody.create(gender, MediaType.parse("text/plain"));
            RequestBody trainClass = RequestBody.create(this.trainClass, MediaType.parse("text/plain"));
            RequestBody userAge = RequestBody.create(age, MediaType.parse("text/plain"));
            RequestBody pay = RequestBody.create(payment, MediaType.parse("text/plain"));

            if (Connectivity.getInstance(activity).isOnline()) {
                applyCollagePass(userId, userType, stationTo, stationFrom, userName, duration, userAge, trainClass, pay, userGender, profileImage, idProofImage);
            } else {
                Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyCollagePass(RequestBody userId, RequestBody type, RequestBody to, RequestBody from, RequestBody name, RequestBody month, RequestBody age, RequestBody trainClass, RequestBody payment, RequestBody gender, RequestBody photo, RequestBody idProof) {

        utils.startProgressDialog();

        Call<CommonModel> call = apiInterface.applyNormalPass(userId, type, to, from, name, month, trainClass, payment, age, gender, photo, idProof);
        call.enqueue(new Callback<CommonModel>() {
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                utils.stopProgressDialog();
                try {
                    assert response.body() != null;
                    if (response.body().getResult().equals("200")) {
                        Toast.makeText(activity, "Succeed", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("SetTextI18n")
    private void openImagePicker() {

        TextView textView = new TextView(activity);
        if (img_photo == img_sel_photo) {
            textView.setText("Select Photo");
        } else {
            textView.setText("Select Id Photo");
        }
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
                    activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri));
                    if (img_photo == img_sel_photo) {
                        imageUri = utils.performCrop(activity, imageUri, false, this);
                    } else {
                        imageUri = utils.performCrop(activity, imageUri, true, this);
                    }
                } else {
                    utils.deleteFile(activity, imageUri);
                    Toast.makeText(activity, "Cancelled", Toast.LENGTH_LONG).show();
                }
                break;
            case Const.CHOOSE_PHOTO_INTENT:
                if (resultCode == RESULT_OK && data != null) {
                    imageUri = data.getData();
                    if (img_photo == img_sel_photo) {
                        imageUri = utils.performCrop(activity, imageUri, false, this);
                    } else {
                        imageUri = utils.performCrop(activity, imageUri, true, this);
                    }
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
                        if (img_photo == img_sel_photo) {
                            photo = utils.getRealPathFromUri(imageUri);
                        } else {
                            idProof = utils.getRealPathFromUri(imageUri);
                        }
                        img_photo.setImageBitmap(bitmap);
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
        switch (requestCode) {
            case 101: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImagePicker();
                }
            }
            case 102: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImagePicker();
                }
            }
        }
    }
}
package com.example.railyatri.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.railyatri.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class Utils {

    private Dialog progressDialog;
    private Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public void startProgressDialog() {
        progressDialog = new Dialog(context);
        progressDialog.setContentView(R.layout.dialog_loading_progress);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void stopProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public Uri getImageUri(Activity activity, Bitmap bitmap) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        return Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "Title", null));
    }

    public String getRealPathFromUri(Uri tempUri) {
        try (Cursor cursor = context.getContentResolver().query(tempUri, new String[]{"_data"}, null, null, null)) {
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
    }

    public Uri getPhotoFileUri(Activity activity) {
        Uri uri = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        String fileName = "IMG_" + timeStamp + ".jpg";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = activity.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/Camera/");
            uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File photoFile = new File(storageDir, "/Camera/" + fileName);
            if (!Objects.requireNonNull(photoFile.getParentFile()).exists()) {
                photoFile.getParentFile().mkdir();
            }
            uri = FileProvider.getUriForFile(activity, "com.example.railyatri.provider", photoFile);
        }
        return uri;
    }

    public void deleteFile(Activity activity, Uri imageUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = activity.getContentResolver();
            int result = resolver.delete(imageUri, null, null);
            if (result > 0) {
//                Toast.makeText(activity, "File deleted", Toast.LENGTH_SHORT).show();
            }
        } else {
            File file = new File(getRealPathFromUri(imageUri));
            if (file.exists()) {
                file.delete();
//                Toast.makeText(activity, "File deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Uri performCrop(Activity activity, Uri uri, boolean isFreeCrop, Fragment fragment) {
        Uri photoUri = null;
        try {
            photoUri = getPhotoFileUri(activity);
            activity.grantUriPermission("com.android.camera", photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("crop", "true");
            if (!isFreeCrop) {
                cropIntent.putExtra("aspectX", 1);
                cropIntent.putExtra("aspectY", 1);
                cropIntent.putExtra("outputX", 500);
                cropIntent.putExtra("outputY", 500);
            }
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            if (fragment == null) {
                activity.startActivityForResult(cropIntent, Const.CROP_IMAGE_INTENT);
            } else {
                fragment.startActivityForResult(cropIntent, Const.CROP_IMAGE_INTENT);
            }
        } catch (ActivityNotFoundException ex) {
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
        }
        return photoUri;
    }

    public static String getTimeStamp(String dateStr) {
        String timestamp = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone("GMT-5"));
            Date d = format.parse(dateStr);
            format.setTimeZone(TimeZone.getDefault());
            assert d != null;
            dateStr = format.format(d);
            Date date = format.parse(dateStr);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
            assert date != null;
            timestamp = newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }


    public static Bitmap generateQRCode(String text) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 200;
        int height = 200;
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //expand view
    public static void expandView(View view) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int actualHeight = view.getMeasuredHeight();
        view.getLayoutParams().height = 0;
        view.setVisibility(View.VISIBLE);
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (actualHeight * interpolatedTime);
                view.requestLayout();
            }
        };
        animation.setDuration((long) (actualHeight / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }

    //collapse view
    public static void collapseView(View view) {
        int actualHeight = view.getMeasuredHeight();
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = actualHeight - (int) (actualHeight * interpolatedTime);
                    view.requestLayout();
                }
            }
        };
        animation.setDuration((long) (actualHeight / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }
}

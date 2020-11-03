package com.example.railyatri.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.railyatri.R;
import com.example.railyatri.RestApi.ApiClientClass;
import com.example.railyatri.RestApi.ApiInterface;
import com.example.railyatri.Utils.SharedPref;
import com.example.railyatri.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpSucceedActivity extends AppCompatActivity {

    private ImageView img_right;
    private Activity activity;
    private String mobile;
    private String password;
    private String type = "user";
    private Utils utils;
    private SharedPref pref;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupsucceed);

        img_right = (ImageView) findViewById(R.id.img_right);

        activity = this;
        utils = new Utils(activity);
        apiInterface = ApiClientClass.getClient().create(ApiInterface.class);
        pref = new SharedPref(activity);

        mobile = getIntent().getStringExtra("Mobile");
        password = getIntent().getStringExtra("Password");

//        AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getDrawable(R.drawable.animated_check);
//        img_right.setImageDrawable(drawable);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Objects.requireNonNull(drawable).registerAnimationCallback(new Animatable2.AnimationCallback() {
//                @Override
//                public void onAnimationEnd(Drawable drawable) {
//                    super.onAnimationEnd(drawable);
//                    ((AnimatedVectorDrawable) drawable).start();
//                }
//            });
//        }
//        if (drawable != null) {
//            drawable.start();
//        }

        Drawable checkedDrawable = img_right.getDrawable();
        if (checkedDrawable instanceof Animatable) {
            ((Animatable) checkedDrawable).start();
        }
        new Handler().postDelayed(() -> {
            Intent i = new Intent();
            i.putExtra("Mobile", mobile);
            i.putExtra("Password", password);
            setResult(RESULT_OK, i);
            finish();
        }, 2000);
    }


    @Override
    public void finish() {
        FirebaseAuth.getInstance().signOut();
        super.finish();
    }
}

package com.example.railyatri.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.railyatri.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MobileVerificationActivity extends AppCompatActivity {

    public static Activity activity;
    private TextInputEditText et_mobile;
    private Button btn_next;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);

        activity = this;

        et_mobile = findViewById(R.id.et_mobile);
        btn_next = findViewById(R.id.btn_next);

        btn_next.setOnClickListener(v -> {

            mobile = Objects.requireNonNull(et_mobile.getText()).toString().trim();

            if (TextUtils.isEmpty(mobile)) {
                et_mobile.setError("Please Enter Mobile No.");
                et_mobile.requestFocus();
            } else if (mobile.length() != 10) {
                et_mobile.setError("Please Enter Valid Number");
                et_mobile.requestFocus();
            } else {
                Intent i = new Intent(this, EnterVerificationCodeActivity.class);
                i.putExtra("Mobile", mobile);
                i.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(i);
            }
        });
    }
}
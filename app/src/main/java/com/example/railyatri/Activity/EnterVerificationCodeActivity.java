package com.example.railyatri.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.railyatri.R;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class EnterVerificationCodeActivity extends AppCompatActivity {

    private Button btn_submit;
    private TextInputEditText et_verification_code;
    private TextView tv_mobile_no;
    private String mobile;
    private String enteredCode;
    private String verificationId;
    private FirebaseAuth auth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_verification_code);

        et_verification_code = findViewById(R.id.et_verification_code);
        btn_submit = findViewById(R.id.btn_submit);
        tv_mobile_no = findViewById(R.id.tv_mobile_no);

        auth = FirebaseAuth.getInstance();

        mobile = getIntent().getStringExtra("Mobile");
        tv_mobile_no.setText("We have sent the verification code on \n +91 " + mobile);

        btn_submit.setOnClickListener(v -> {
            enteredCode = Objects.requireNonNull(et_verification_code.getText()).toString().trim();
            if (TextUtils.isEmpty(enteredCode) || enteredCode.length() < 6) {
                et_verification_code.setError("Enter Code");
                et_verification_code.requestFocus();
            } else {
                verifyCode(enteredCode);
            }
        });
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobile, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken
                forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            Toast.makeText(EnterVerificationCodeActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                et_verification_code.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(EnterVerificationCodeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        openRegistrationActivity();
                    } else {
                        Toast.makeText(EnterVerificationCodeActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openRegistrationActivity() {
        Intent i = new Intent(this, RegistrationActivity.class);
        i.putExtra("Mobile", Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber()).substring(3));
        i.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(i);
        finish();
        MobileVerificationActivity.activity.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).equalsIgnoreCase("+91" + mobile)) {
                openRegistrationActivity();
                return;
            }
        }
        sendVerificationCode("+91" + mobile);
    }
}
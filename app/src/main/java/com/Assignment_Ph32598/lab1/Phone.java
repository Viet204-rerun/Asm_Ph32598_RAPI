package com.Assignment_Ph32598.lab1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Phone extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    EditText edtOtp,edtNumber;
    Button btnOtp,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        edtOtp = findViewById(R.id.p_edtOtp);
        edtNumber = findViewById(R.id.p_edtNumber);
        btnLogin = findViewById(R.id.p_btnLogin);
        btnOtp = findViewById(R.id.p_btnOtp);

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // Lấy mã OTP và đặt vào EditText
                String otp = phoneAuthCredential.getSmsCode();
                if (otp != null) {
                    edtOtp.setText(otp);
                } else {
                    Log.d(TAG, "onVerificationCompleted: Cannot retrieve SMS code");
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // Xử lý khi xác thực thất bại
                Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(Phone.this, "Xác thực thất bại", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // Lưu trữ mã xác thực
                mVerificationId = verificationId;
            }
        };

        // Sự kiện click cho nút gửi mã OTP
        btnOtp.setOnClickListener(view -> {
            String phoneNumber = edtNumber.getText().toString().trim();
            if (!phoneNumber.isEmpty()) {
                getOTP(phoneNumber);
            } else {
                Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện click cho nút đăng nhập
        btnLogin.setOnClickListener(view -> {
            String otp = edtOtp.getText().toString().trim();
            if (!otp.isEmpty()) {
                verifyOTP(otp);
            } else {
                Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOTP(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOTP(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Phone.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Phone.this, DanhSach.class));
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(Phone.this, "Mã OTP không hợp lệ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Phone.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}

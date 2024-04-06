package com.Assignment_Ph32598.lab1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DangKy extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText edtUser,edtPass;
    Button btnSign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        edtUser = findViewById(R.id.dk_Username);
        edtPass = findViewById(R.id.dk_Password);
        btnSign = findViewById(R.id.dk_btnSign);
        String email = edtUser.getText().toString();
        String password = edtUser.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtUser.getText().toString();
                String password = edtPass.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(DangKy.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(email, password);
                }
            }
        });
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                FirebaseUser user = mAuth.getCurrentUser();
                Toast.makeText(DangKy.this, "Đăng ký thành công"+user.getEmail(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DangKy.this, DangNhap.class); // DangNhap là tên Activity của màn hình đăng nhập
                startActivity(intent);
                finish();
            }else{
                Log.w(TAG, "createUserWithEmail:faiure",task.getException() );
                Toast.makeText(DangKy.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    public void btnTroVeDangNhap(View view) {
        startActivity(new Intent(DangKy.this,DangNhap.class));

    }
}

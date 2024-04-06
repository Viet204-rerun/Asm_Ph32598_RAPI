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

public class DangNhap extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText edtUser,edtPass;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        edtUser = findViewById(R.id.dn_Username);
        edtPass = findViewById(R.id.dn_Password);
        btnLogin = findViewById(R.id.dn_btnLogin);

        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtUser.getText().toString();
                String password = edtPass.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(DangNhap.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email, password);
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(DangNhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DangNhap.this,DanhSachLaptop.class));
                }else{
                    Log.w(TAG, "signInWithEmail:faiure",task.getException() );
                    Toast.makeText(DangNhap.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void SignUp(View view) {
        startActivity(new Intent(DangNhap.this,DangKy.class));
    }

    public void forgetPassWord(View view) {
        startActivity(new Intent(DangNhap.this, QuenMatKhau.class));
    }
}

package com.Assignment_Ph32598.lab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class QuenMatKhau extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText edtEmail;
    Button btnKiemTra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);
        edtEmail = findViewById(R.id.qmk_email);
        btnKiemTra = findViewById(R.id.btnKiemTra);
        mAuth=  FirebaseAuth.getInstance();
        btnKiemTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtEmail.getText().toString();
                if (email.isEmpty() ) {
                    Toast.makeText(QuenMatKhau.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    send(email);
                }
            }
        });

    }


    public void send(String email){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(QuenMatKhau.this, "Vui lòng kiểm tra hộp thư để cập nhật lại mật khẩu", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(QuenMatKhau.this,DangNhap.class));
                }else{
                    Toast.makeText(QuenMatKhau.this, "Lỗi gửi email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void btnTroVeDangNhap(View view) {
        startActivity(new Intent(QuenMatKhau.this,DangNhap.class));
    }
}
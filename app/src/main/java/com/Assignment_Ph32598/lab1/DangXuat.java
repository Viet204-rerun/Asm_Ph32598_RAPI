package com.Assignment_Ph32598.lab1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lab1.R;
import com.google.firebase.auth.FirebaseAuth;

public class DangXuat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_xuat);
    }

    public void Logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(DangXuat.this,MainActivity.class));
    }
}
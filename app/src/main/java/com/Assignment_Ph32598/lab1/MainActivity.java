package com.Assignment_Ph32598.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lab1.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnEmail(View view) {
        startActivity(new Intent(MainActivity.this,DangNhap.class));
    }

    public void btnPhone(View view) {
        startActivity(new Intent(MainActivity.this,Phone.class));

    }
}
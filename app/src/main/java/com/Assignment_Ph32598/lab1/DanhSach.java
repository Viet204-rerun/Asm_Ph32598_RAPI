package com.Assignment_Ph32598.lab1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DanhSach extends AppCompatActivity {

    private static final String TAG = "DanhSach";
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private CityAdapter adapter;
    ArrayList<City> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CityAdapter(list);
        recyclerView.setAdapter(adapter);

        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moDialogNhapDuLieu();
            }
        });

        docDulieu();
    }

    private void docDulieu() {
        db.collection("city")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<City> citiesList = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                City city = document.toObject(City.class);
                                citiesList.add(city);
                            }
                            // Kiểm tra và cập nhật RecyclerView nếu adapter không null
                            if (adapter != null) {
                                adapter.updateData(citiesList);
                            } else {
                                Log.e(TAG, "Adapter is null, cannot update data");
                            }

                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void moDialogNhapDuLieu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_city, null);
        builder.setView(dialogView);

        EditText editTextCityName = dialogView.findViewById(R.id.editTextCityName);
        EditText editTextState = dialogView.findViewById(R.id.editTextState);
        EditText editTextCountry = dialogView.findViewById(R.id.editTextCountry);
        EditText editTextPopulation = dialogView.findViewById(R.id.editTextPopulation);

        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String cityName = editTextCityName.getText().toString().trim();
                String state = editTextState.getText().toString().trim();
                String country = editTextCountry.getText().toString().trim();
                int population = Integer.parseInt(editTextPopulation.getText().toString().trim());

                ghiDuLieu(cityName, state, country, population);
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void ghiDuLieu(String cityName, String state, String country, int population) {
        CollectionReference citiesRef = db.collection("city");

        Map<String, Object> city = new HashMap<>();
        city.put("name", cityName);
        city.put("state", state);
        city.put("country", country);
        city.put("population", population);
        citiesRef.add(city)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        // Cập nhật RecyclerView sau khi thêm dữ liệu mới thành công
                        docDulieu();

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    public void DangXuat(View view) {
        startActivity(new Intent(DanhSach.this, DangXuat.class));
    }
}

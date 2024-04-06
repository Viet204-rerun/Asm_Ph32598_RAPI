package com.Assignment_Ph32598.lab1;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lab1.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DanhSachLaptop extends AppCompatActivity {
    private RecyclerView recyclerView;
    List<Laptop> list;
     LaptopAdapter adapter ;
     Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_laptop);
        recyclerView = findViewById(R.id.rcv_dsLaptop);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiService = retrofit.create(APIService.class);
        Call<List<Laptop>> call = apiService.getLaptops();
        call.enqueue(new Callback<List<Laptop>>() {
            @Override
            public void onResponse(Call<List<Laptop>> call, Response<List<Laptop>> response) {
                if(response.isSuccessful()){
                    list = response.body();
                    adapter = new LaptopAdapter(context,list);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Laptop>> call, Throwable t) {

            }
        });
        Button buttonAdd = findViewById(R.id.btn_dsLaptopThem);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moDialogNhapDuLieu();
            }
        });


    }
    private void moDialogNhapDuLieu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_laptop, null);
        builder.setView(dialogView);

        EditText edtTen = dialogView.findViewById(R.id.edt_addTen);
        EditText edtGia = dialogView.findViewById(R.id.edt_addGia);
        EditText edtLoai = dialogView.findViewById(R.id.edt_addLoai);
        EditText edtAnh = dialogView.findViewById(R.id.edt_addAnh);

        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ten = edtTen.getText().toString().trim();
                        String giaStr = edtGia.getText().toString().trim();
                        String hang = edtLoai.getText().toString().trim();
                        String anh = edtAnh.getText().toString().trim();
                        // Kiểm tra nếu bất kỳ trường nào trống
                        if (ten.isEmpty() || giaStr.isEmpty() || hang.isEmpty() || anh.isEmpty()) {
                            Toast.makeText(DanhSachLaptop.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return; // Trả về mà không thực hiện các thao tác tiếp theo
                        }
                        int gia = Integer.parseInt(giaStr);
                        Laptop newLaptop = new Laptop(ten, gia, hang, anh);
                        // Gửi yêu cầu POST đến API để thêm sản phẩm mới
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(APIService.DOMAIN)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        APIService apiService = retrofit.create(APIService.class);
                        Call<Laptop> call = apiService.addLaptop(newLaptop);
                        call.enqueue(new Callback<Laptop>() {
                                         @Override
                                         public void onResponse(Call<Laptop> call, Response<Laptop> response) {
                                             if (response.isSuccessful()) {
                                                 // Nếu thêm thành công, thêm sản phẩm vào danh sách và cập nhật adapter
                                                 list.add(newLaptop);
                                                 adapter.notifyDataSetChanged();
                                                 Toast.makeText(DanhSachLaptop.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();

                                             } else {
                                                 // Xử lý khi thêm không thành công
                                                 Toast.makeText(DanhSachLaptop.this, "Thêm sản phẩm không thành công", Toast.LENGTH_SHORT).show();
                                             }
                                         }

                                         @Override
                                         public void onFailure(Call<Laptop> call, Throwable t) {
                                             Toast.makeText(DanhSachLaptop.this, "Có lỗi xảy ra khi thêm sản phẩm: " + t.toString(), Toast.LENGTH_SHORT).show();
                                             Log.e("DanhSachLaptop", "Có lỗi xảy ra khi thêm sản phẩm: " + t.toString());
                                         }
                                     }

                        );
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

    public void btnDangXuat(View view) {
        startActivity(new Intent(DanhSachLaptop.this, DangXuat.class));
    }
}
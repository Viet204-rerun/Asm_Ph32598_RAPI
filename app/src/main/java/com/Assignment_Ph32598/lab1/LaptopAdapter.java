package com.Assignment_Ph32598.lab1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LaptopAdapter extends RecyclerView.Adapter<LaptopAdapter.LaptopViewHolder> {
    private List<Laptop> list;
    private Context context ;

    public LaptopAdapter(Context context,List<Laptop> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LaptopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new LaptopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaptopViewHolder holder, int position) {
        Laptop laptop = list.get(position);
        holder.tv_ten.setText("Tên Laptop: " + laptop.getTen());
        holder.tv_gia.setText("Giá Laptop: " + laptop.getGia());
        holder.tv_loai.setText("Hãng Laptop: " + laptop.getHang());
        Picasso.get().load(laptop.getAnhUrl()).into(holder.img_anh);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LaptopViewHolder extends RecyclerView.ViewHolder {

        TextView tv_ten, tv_gia, tv_loai;
        ImageView img_anh;
        ImageButton imgXoa,imgSua;


        public LaptopViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ten = itemView.findViewById(R.id.tv_hTen);
            tv_gia = itemView.findViewById(R.id.tv_hGia);
            tv_loai = itemView.findViewById(R.id.tv_hLoai);
            img_anh = itemView.findViewById(R.id.img_hImg);
            imgXoa = itemView.findViewById(R.id.btn_hXoa);
            imgSua = itemView.findViewById(R.id.btn_hSua);
            imgXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Laptop laptop = list.get(position);
                        showDeleteConfirmationDialog(laptop.get_id()); // Hiển thị dialog xác nhận xóa
                    }
                }
            });
            imgSua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Laptop laptop = list.get(position);
                        showEditDialog(laptop); // Hiển thị dialog để chỉnh sửa thông tin laptop
                    }

                }
            });
        }
    }
    private void showEditDialog(final Laptop laptop) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_laptop, null);
        builder.setView(dialogView);

        EditText edtTen = dialogView.findViewById(R.id.edt_addTen);
        EditText edtGia = dialogView.findViewById(R.id.edt_addGia);
        EditText edtLoai = dialogView.findViewById(R.id.edt_addLoai);
        EditText edtAnh = dialogView.findViewById(R.id.edt_addAnh);

        // Hiển thị thông tin hiện tại của laptop trong các EditText
        edtTen.setText(laptop.getTen());
        edtGia.setText(String.valueOf(laptop.getGia()));
        edtLoai.setText(laptop.getHang());
        edtAnh.setText(laptop.getAnhUrl());

        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ten = edtTen.getText().toString().trim();
                String giaStr = edtGia.getText().toString().trim();
                String hang = edtLoai.getText().toString().trim();
                String anh = edtAnh.getText().toString().trim();
                // Kiểm tra nếu bất kỳ trường nào trống
                if (ten.isEmpty() || giaStr.isEmpty() || hang.isEmpty() || anh.isEmpty()) {
                    Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return; // Trả về mà không thực hiện các thao tác tiếp theo
                }
                int gia = Integer.parseInt(giaStr);
                // Tạo đối tượng Laptop mới với thông tin đã chỉnh sửa
                Laptop updatedLaptop = new Laptop(laptop.get_id(), ten, gia, hang, anh);
                // Gửi yêu cầu PUT đến API để cập nhật thông tin laptop
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(APIService.DOMAIN)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                APIService apiService = retrofit.create(APIService.class);
                Call<Laptop> call = apiService.updateLaptop(laptop.get_id(), updatedLaptop);
                call.enqueue(new Callback<Laptop>() {
                    @Override
                    public void onResponse(Call<Laptop> call, Response<Laptop> response) {
                        if (response.isSuccessful()) {
                            // Nếu cập nhật thành công, cập nhật thông tin trong danh sách và cập nhật adapter
                            int index = list.indexOf(laptop);
                            list.set(index, updatedLaptop);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Sửa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            // Xử lý khi cập nhật không thành công
                            Toast.makeText(context, "Sửa sản phẩm không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Laptop> call, Throwable t) {
                        Toast.makeText(context, "Có lỗi xảy ra khi sửa sản phẩm: " + t.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("DanhSachLaptop", "Có lỗi xảy ra khi sửa sản phẩm: " + t.toString());
                    }
                });
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

    private void showDeleteConfirmationDialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteLaptop(id);
                Toast.makeText(context, "Xóa Thành Công!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteLaptop(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiService = retrofit.create(APIService.class);
        Call<Laptop> call = apiService.deleteLaptop(id);
        call.enqueue(new Callback<Laptop>() {
            @Override
            public void onResponse(Call<Laptop> call, Response<Laptop> response) {
                if (response.isSuccessful()) {
                    // Xóa thành công, cập nhật RecyclerView
                    list.removeIf(laptop -> laptop.get_id().equals(id));
                    notifyDataSetChanged();
                } else {
                    // Xóa thất bại, xử lý thông báo hoặc log lỗi nếu cần
                }
            }

            @Override
            public void onFailure(Call<Laptop> call, Throwable t) {
                // Xử lý lỗi khi gọi API xóa laptop
            }
        });
    }
    public void updateData(List<Laptop> newData) {
        list.clear(); // Xóa dữ liệu cũ
        list.addAll(newData); // Thêm dữ liệu mới
        notifyDataSetChanged(); // Cập nhật RecyclerView
    }
}

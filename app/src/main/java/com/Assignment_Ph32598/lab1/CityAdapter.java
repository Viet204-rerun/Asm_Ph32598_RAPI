package com.Assignment_Ph32598.lab1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.R;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private List<City> citiesList;

    public CityAdapter(List<City> citiesList) {
        this.citiesList = citiesList;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = citiesList.get(position);
        holder.textViewCityName.setText("Thành Phố:"+city.getName());
        holder.textViewStateCountry.setText("Huyện:"+city.getState());
        holder.textViewStateCountry2.setText("Quốc Gia:" + city.getCountry());
        holder.textViewPopulation.setText("Dân Số: " + city.getPopulation());
        // Gán các giá trị khác của thành phố vào các TextView tương ứng
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCityName;
        TextView textViewStateCountry;
        TextView textViewPopulation;
        TextView textViewStateCountry2;


        // Khai báo các TextView khác tương ứng với các thông tin của thành phố

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCityName = itemView.findViewById(R.id.textViewCityName);
            textViewStateCountry = itemView.findViewById(R.id.textViewStateCountry);
            textViewStateCountry2 = itemView.findViewById(R.id.textViewStateCountry2);

            textViewPopulation = itemView.findViewById(R.id.textViewPopulation);
            // Khởi tạo các TextView khác
        }
    }
    public void updateData(List<City> newData) {
        citiesList.clear(); // Xóa dữ liệu cũ
        citiesList.addAll(newData); // Thêm dữ liệu mới
        notifyDataSetChanged(); // Cập nhật RecyclerView
    }
}

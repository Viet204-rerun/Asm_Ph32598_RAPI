package com.Assignment_Ph32598.lab1;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {
     String DOMAIN = "http://10.24.60.99:3000";
     @GET("/api/list")
     Call<List<Laptop>> getLaptops();
     @POST("/api/add")
     Call<Laptop> addLaptop(@Body Laptop laptop);

     @DELETE("/api/delete/{id}") // Sử dụng Path Variable để truyền ID của laptop cần xóa
     Call<Laptop> deleteLaptop(@Path("id") String id);

     @PUT("/api/update/{id}") // Sử dụng Path Variable để truyền ID của laptop cần cập nhật
     Call<Laptop> updateLaptop(@Path("id") String id, @Body Laptop laptop);
}

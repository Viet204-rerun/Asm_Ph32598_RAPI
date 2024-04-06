package com.Assignment_Ph32598.lab1;

public class Laptop {
    String _id;
    String ten;
    int gia;
    String hang, anhUrl;

    public Laptop() {
    }

    public Laptop(String ten, int gia, String hang, String anhUrl) {
        this.ten = ten;
        this.gia = gia;
        this.hang = hang;
        this.anhUrl = anhUrl;
    }

    public Laptop(String _id, String ten, int gia, String hang, String anhUrl) {
        this._id = _id;
        this.ten = ten;
        this.gia = gia;
        this.hang = hang;
        this.anhUrl = anhUrl;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public String getAnhUrl() {
        return anhUrl;
    }

    public void setAnhUrl(String anhUrl) {
        this.anhUrl = anhUrl;
    }
}
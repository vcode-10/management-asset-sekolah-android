package com.example.scanasetsekolah;

public class Ruangan {
    private int id;
    private String nama;

    public Ruangan(int id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    @Override
    public String toString() {
        return nama;
    }
}

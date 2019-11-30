package com.reggi.firebase.model;

public class Mahasiswa {

    private String nim;
    private String namaLengkap;
    private String jurusan;
    private String key;

    // Setter dan Getter
    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    //Membuat Konstruktor Kosong untuk membaca data snapshot
    public Mahasiswa(){
    }

    //Konstruktor dengan beberapa parameter, untuk mendapat Input Data dari pengguna
    public Mahasiswa(String nim, String nama, String jurusan){
        this.nim = nim;
        this.namaLengkap = nama;
        this.jurusan = jurusan;
    }
}

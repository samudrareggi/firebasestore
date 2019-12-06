package com.reggi.firebase.model;

public class Staff {
    private String namaLengkap;
    private String nip;
    private String key;

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Staff(String nip, String nama, String jurusan){
        this.nip = nip;
        this.namaLengkap = nama;
    }
}

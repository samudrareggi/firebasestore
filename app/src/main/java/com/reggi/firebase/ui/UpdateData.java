package com.reggi.firebase.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reggi.firebase.R;
import com.reggi.firebase.model.Mahasiswa;

public class UpdateData extends AppCompatActivity {

    //Deklarasi semua variabel
    private EditText nimBaru, namaBaru, jurusanBaru;
    private Button update;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private String cekNIM, cekNama, cekJurusan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        getSupportActionBar().setTitle("Update Data");
        nimBaru = findViewById(R.id.new_nim);
        namaBaru = findViewById(R.id.new_nama);
        jurusanBaru = findViewById(R.id.new_jurusan);
        update = findViewById(R.id.update);

        //Mendapatkan Instance autentikasi dan Referensi dari Database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
//        getData();
        Log.e("NIM ", "getData: "+ this.getIntent().getExtras().getString("dataNIM"));
        final Bundle Maha = getIntent().getBundleExtra("Mhs");
        final String getNIM = Maha.getString("dataNIM");
        final String getNama = Maha.getString("dataNama");
        final String getJurusan = Maha.getString("dataJurusan");
        if(getNIM == null){
            Toast.makeText(this, "DATA NIM KOSNG!", Toast.LENGTH_SHORT).show();
        }
        nimBaru.setText(getNIM);
        namaBaru.setText(getNama);
        jurusanBaru.setText(getJurusan);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Mendapatkan data mahasiswa yang akan dikonfirmasi
                cekNIM = nimBaru.getText().toString();
                cekNama = namaBaru.getText().toString();
                cekJurusan = jurusanBaru.getText().toString();

                //Memeriksa agar tidak ada data yang kosong, saat proses update
                if (isEmpty(cekNIM) || isEmpty(cekNama) || isEmpty(cekJurusan)){
                    Toast.makeText(UpdateData.this,"Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                }else {
                    //Menjalankan proses update data
                    Mahasiswa setMahasiswa = new Mahasiswa();
                    setMahasiswa.setNim(nimBaru.getText().toString());
                    setMahasiswa.setNamaLengkap(namaBaru.getText().toString());
                    setMahasiswa.setJurusan(jurusanBaru.getText().toString());
                    updateMahasiswa(setMahasiswa);
                }
            }
        });
    }

    //Memeriksa apakah adat data yang kosong, sebelum diupdate
    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }

    //Menampilkan data yang akan di update
    private void getData(){
        Log.e("NIM ", "getData: "+ getIntent().getExtras());
        final String getNIM = getIntent().getExtras().getString("dataNIM");
        final String getNama = getIntent().getExtras().getString("dataNama");
        final String getJurusan = getIntent().getExtras().getString("dataJurusan");
        if(getNIM == null){
            Toast.makeText(this, "DATA NIM KOSNG!", Toast.LENGTH_SHORT).show();
        }
        nimBaru.setText(getNIM);
        namaBaru.setText(getNama);
        jurusanBaru.setText(getJurusan);
    }

    //Proses update data yang sudah ditentukan
    private void updateMahasiswa(Mahasiswa mahasiswa){
        String userID = auth.getUid();
        String getKey = getIntent().getBundleExtra("Mhs").getString("getPrimaryKey");
        assert userID != null;
        assert getKey != null;

        database.child("Admin")
                .child(userID)
                .child("Mahasiswa")
                .child(getKey)
                .setValue(mahasiswa)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        nimBaru.setText("");
                        namaBaru.setText("");
                        jurusanBaru.setText("");
                        Toast.makeText(UpdateData.this,"Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}
package com.reggi.firebase.ui;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reggi.firebase.R;
import com.reggi.firebase.model.Mahasiswa;

import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Deklarasi variabel
    private ProgressBar pbLoading;
    private EditText etNim, etNama, etJurusan;
    private FirebaseAuth fbAuth;
    private Button btLogout, btSave, btLogin, btShowdata;

    //Membuat kode permintaan
    private int RC_SIGNIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pbLoading = findViewById(R.id.progress);
        pbLoading.setVisibility(View.GONE);

        //Inisialisasi ID (Button)
        btLogout = findViewById(R.id.logout);
        btLogout.setOnClickListener(this);
        btSave = findViewById(R.id.save);
        btSave.setOnClickListener(this);
        btLogin = findViewById(R.id.login);
        btLogin.setOnClickListener(this);
        btShowdata = findViewById(R.id.showdata);
        btShowdata.setOnClickListener(this);

        fbAuth = FirebaseAuth.getInstance(); //Mendapatkan Instance Firebase Autentifikasi

        //Inisialisasi ID
        etNim = findViewById(R.id.nim);
        etNama = findViewById(R.id.namaLengkap);
        etJurusan = findViewById(R.id.jurusan);

        /*
         * Memeriksa keaktifan pengguna. Jika pengguna tidak aktif, maka setiap komponen UI akan dinonaktifkan
         * Kecuali tombol btLogin. Namun jika ada user yang terautentikasi, semua fungsi/komponen
         * didalam User Interface dapat digunakan kecuali btLogout
         */
        if(fbAuth.getCurrentUser() == null){
            defaultUI();
        }else{
            updateUI();
        }
    }

    //Tampilan default pada activity jika user belum terautentikasi
    private void defaultUI(){
        btLogout.setVisibility(View.GONE);
        btSave.setVisibility(View.GONE);
        btShowdata.setVisibility(View.GONE);
        etJurusan.setVisibility(View.GONE);
        etNim.setVisibility(View.GONE);
        etNama.setVisibility(View.GONE);
        btLogin.setEnabled(true);
    }

    //Tampilan user interface setelah terautentukasi
    private void updateUI(){
        btLogout.setEnabled(true);
        btSave.setEnabled(true);
        btShowdata.setEnabled(true);
        etNama.setEnabled(true);
        etNim.setEnabled(true);
        etJurusan.setEnabled(true);
        btLogin.setEnabled(false);
        pbLoading.setVisibility(View.GONE);
    }

    //Memeriksa apakah ada data yang kosong
    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //RC_SIGNIN adalah kode permintaan yang diberikan ke staartActivityForResult,
        //saat menjalankan alur program.
        if (requestCode == RC_SIGNIN){

            //Berhasil masuk
            if (resultCode == RESULT_OK){
                Toast.makeText(MainActivity.this,"btLogin Berhasil", Toast.LENGTH_SHORT).show();
                updateUI();
            }else{
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"btLogin Dibatalkan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                //Statement program untuk login
                startActivityForResult(AuthUI.getInstance()
                                .createSignInIntentBuilder()
                //Memilih method yg digunakan
                        .setAvailableProviders(Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false)
                        .build(),
                        RC_SIGNIN);
                pbLoading.setVisibility(View.VISIBLE);
                break;

            case R.id.save:
                //Mendapatkan UserID dari pengguna yg terautentikasi
                String getUserID = fbAuth.getCurrentUser().getUid();

                //Mendapatkan Instance dari Databse
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference getReference;

                //Menyimpan data yang dimasukan pengguna ke variabel
                String getNIM = etNim.getText().toString();
                String getNama = etNama.getText().toString();
                String getJurusan = etJurusan.getText().toString();

                getReference = database.getReference(); //Mendapatkan referensi dari Database

                //Memeriksa apakah ada data yg kosong
                if (isEmpty(getNIM) || isEmpty(getNama) || isEmpty(getJurusan)){
                    //Jika ada
                    Toast.makeText(MainActivity.this,"Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                }else {
                    /*
                     *Jika tidak, maka diproses
                     * Menyimpan data referensi pada Database berdasarkan user ID
                     */
                    getReference.child("Admin").child(getUserID).child("Mahasiswa").push()
                            .setValue(new Mahasiswa(getNIM, getNama, getJurusan))
                            .addOnSuccessListener(this, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    //Peristiwa ini terjadi saat user berhasil menimpan data ke databse
                                    etNim.setText("");
                                    etNama.setText("");
                                    etJurusan.setText("");
                                    Toast.makeText(MainActivity.this,"Data Tersimpan", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                break;

            case R.id.logout:
                //Statement untuk logout
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Toast.makeText(MainActivity.this,"btLogout Berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                break;
            case R.id.showdata:
                startActivity(new Intent(MainActivity.this, ListDataMahasiswa.class));
                break;
        }
    }
}

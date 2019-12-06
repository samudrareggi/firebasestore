package com.reggi.firebase.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reggi.firebase.R;
import com.reggi.firebase.adapter.RecyclerViewAdapter;
import com.reggi.firebase.model.Mahasiswa;

import java.util.ArrayList;

public class ListDataMahasiswa extends AppCompatActivity implements RecyclerViewAdapter.dataListener {

    //Deklarasi variable untuk RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    //Deklarasi variabel Database Reference dan ArrayList dengan Parameter Class Model
    private DatabaseReference reference;
    private ArrayList<Mahasiswa> dataMahasiswa;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list_data);
        recyclerView = findViewById(R.id.datalist);
        getSupportActionBar().setTitle("Data Mahasiswa");
        auth = FirebaseAuth.getInstance();
        MyRecyclerView();
        GetData();
    }

    //Mengambil data dari Database dan menampilkannya kedalam Adapter
    private void GetData(){
        //Mendapatkan referensi Database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Admin").child(auth.getUid()).child("Mahasiswa")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList
                        dataMahasiswa = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                            Mahasiswa mahasiswa = snapshot.getValue(Mahasiswa.class);

                            //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                            mahasiswa.setKey(snapshot.getKey());
                            dataMahasiswa.add(mahasiswa);
                        }

                        //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                        adapter = new RecyclerViewAdapter(dataMahasiswa, ListDataMahasiswa.this);

                        //Memasang Adapter pada RecyclerView
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        /*
                        Dijalankan ketika ada error dan,
                        pengambilan data error tersebut ditampilkan errornya
                        ke LogCat
                         */
                        Toast.makeText(getApplicationContext(),"Data Gagal Dimuat", Toast.LENGTH_SHORT).show();
                        Log.e("ListDataMahasiswa", databaseError.getDetails()+" "+databaseError.getMessage());
                    }
                });
    }

    //Pengaturan RecyclerView
    private void MyRecyclerView(){
        //Menggunakan Layout Manager, membuat list secara vertical
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Membuat underline pada setiap item dalam list
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onDeleteData(Mahasiswa data, int position){
        /*
         * Dijalankan ketika method onDeleteData
         * dipanggil dari adapter pada RecyclerView melalui interface.
         * Berfungsi menghapus data berdasarkan primary key dari data tersebut
         * Jika berhasil, maka akan muncul Toast
         */
        String userID = auth.getUid();
        if (reference != null){
            reference.child("Admin")
                    .child(userID)
                    .child("Mahasiswa")
                    .child(data.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ListDataMahasiswa.this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}

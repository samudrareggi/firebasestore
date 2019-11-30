package com.reggi.firebase.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.reggi.firebase.R;
import com.reggi.firebase.ui.ListDataMahasiswa;
import com.reggi.firebase.ui.UpdateData;
import com.reggi.firebase.model.Mahasiswa;

import java.util.ArrayList;

//Kelas adaptar untuk menampilkan data interaktif
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    //Deklarasi Vseluruh Variabel
    private ArrayList<Mahasiswa> listMahasiswa;
    private Context mContext;

    //Membuat Interface
    public interface dataListener{
        void onDeleteData(Mahasiswa data, int position);
    }

    //Deklarasi objek dari Interface
    private dataListener listener;

    //Membuat Konstruktor, untuk menerima input dari Database
    public RecyclerViewAdapter(ArrayList<Mahasiswa> listMahasiswa, Context context){
        this.listMahasiswa = listMahasiswa;
        this.mContext = context;
        listener = (ListDataMahasiswa) mContext;
    }

    //ViewHolder untuk menyimpan referensi dari semua view
    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvNim, tvNama, tvJurusan;
        private LinearLayout llListItem;

        ViewHolder(View itemView){
            super(itemView);
            //Menginisialisasi view-view yang terpasang pada layout recyclerview
            tvNim = itemView.findViewById(R.id.nim);
            tvNama = itemView.findViewById(R.id.namaLengkap);
            tvJurusan = itemView.findViewById(R.id.jurusan);

            llListItem = itemView.findViewById(R.id.list_item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //Membuat view untuk menyimpan dan memasang layout yang akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false);
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder (ViewHolder holder, final int position){
        //Mengambil nilai pada RecyclerView berdasarkan posisi tertentu
        final String strNim = listMahasiswa.get(position).getNim();
        final String strNama = listMahasiswa.get(position).getNamaLengkap();
        final String strJurusan = listMahasiswa.get(position).getJurusan();

        //Memasukan nilai kedalam view
        holder.tvNim.setText("tvNim: "+ strNim);
        holder.tvNama.setText("tvNama: "+ strNama);
        holder.tvJurusan.setText("tvJurusan: "+ strJurusan);

        //Menampilkan menu update dan delete saat user melakukan long klik
        holder.llListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final String[] action = {"Update", "Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                /*
                                Berpindah Activity pada halaman layout UpdateData
                                dan mengambil data pada listMahasiswa, berdasarkan posisinya
                                untuk dikirim pada activity UpdataData
                                 */
                                Bundle bundle = new Bundle();
                                bundle.putString("dataNIM", listMahasiswa.get(position).getNim());
                                bundle.putString("dataNama", listMahasiswa.get(position).getNamaLengkap());
                                bundle.putString("dataJurusan", listMahasiswa.get(position).getJurusan());
                                bundle.putString("getPrimaryKey", listMahasiswa.get(position).getKey());
                                Intent intent = new Intent(view.getContext(), UpdateData.class);
                                intent.putExtra("", bundle);
                                mContext.startActivity(intent);
                                break;
                            case 1:
                                //Menggunakan interface untuk mengirim data mahasiswa, yang akan dihapus
                                listener.onDeleteData(listMahasiswa.get(position), position);
                                break;
                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount(){
        //Menghitung jumlah data yang akan ditampilkan pada RecyclerView
        return listMahasiswa.size();
    }

}

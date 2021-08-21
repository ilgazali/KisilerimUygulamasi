package com.example.kisilerimuygulamasi;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class KisilerAdapter extends RecyclerView.Adapter<KisilerAdapter.CardTasarimTutucu> {

    private Context mContext;
    private ArrayList<Kisiler> kisilerArrayList;
    Kisiler kisi;
    VeriTabani vt;

    public KisilerAdapter(Context mContext, ArrayList<Kisiler> kisilerArrayList) {
        this.mContext = mContext;
        this.kisilerArrayList = kisilerArrayList;
    }

    @NonNull
    @Override
    public CardTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tasarim,parent,false);

        return new CardTasarimTutucu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardTasarimTutucu holder, int position) {

        final Kisiler kisi = kisilerArrayList.get(position);



        holder.textViewBilgi.setText(kisi.getKisi_ad()+" : "+kisi.getKisi_tel());

        holder.imageViewNokta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(mContext,holder.imageViewNokta);
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case R.id.action_sil:
                                Snackbar.make(holder.imageViewNokta,"Emin misin?",Snackbar.LENGTH_SHORT)
                                        .setAction("Evet", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                vt = new VeriTabani(mContext);
                                                new KisilerDao().kisiSil(vt,kisi.getKisi_id());

                                                kisilerArrayList = new KisilerDao().tumKisiler(vt);
                                                notifyDataSetChanged();

                                            }
                                        }).show();


                                return true;
                            case R.id.action_guncelle:
                                alertGoster(kisi);
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popupMenu.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return kisilerArrayList.size();
    }

    public class CardTasarimTutucu extends RecyclerView.ViewHolder{

       private  TextView textViewBilgi;
       private ImageView imageViewNokta;

        public CardTasarimTutucu(@NonNull View itemView) {
            super(itemView);

            textViewBilgi = itemView.findViewById(R.id.textViewBilgi);
            imageViewNokta = itemView.findViewById(R.id.imageViewNokta);
        }
    }
    public void alertGoster(Kisiler kisi){

        LayoutInflater layout = LayoutInflater.from(mContext);
        View view = layout.inflate(R.layout.alert_tasarim,null);

        EditText editTextAd = view.findViewById(R.id.editTextAd);
        EditText editTextTel = view.findViewById(R.id.editTextTel);

        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setView(view);
        alert.setTitle("Kişi Güncelle");

        editTextAd.setText(kisi.getKisi_ad());
        editTextTel.setText(kisi.getKisi_tel());

        alert.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                String kisi_ad = editTextAd.getText().toString().trim();
                String kisi_tel= editTextTel.getText().toString().trim();

                if (TextUtils.isEmpty(kisi_ad)){
                    Snackbar.make(editTextAd,"Adı giriniz",Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(kisi_tel)){
                    Snackbar.make(editTextTel,"Telefon numarasını giriniz!",Snackbar.LENGTH_LONG).show();
                    return;
                }

                vt = new VeriTabani(mContext);
                new KisilerDao().kisiGuncelle(vt,kisi.getKisi_id(),kisi_ad,kisi_tel);
                kisilerArrayList = new KisilerDao().tumKisiler(vt);
                notifyDataSetChanged();

            }
        });

        alert.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alert.create().show();
    }
}

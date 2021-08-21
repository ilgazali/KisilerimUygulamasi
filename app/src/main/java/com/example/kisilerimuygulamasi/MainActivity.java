package com.example.kisilerimuygulamasi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private Toolbar toolbar;
    private RecyclerView rv;
    private FloatingActionButton fab;
    private ArrayList<Kisiler> kisiler;

    private VeriTabani veriTabani;

    private KisilerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        rv = findViewById(R.id.rv);
        fab = findViewById(R.id.fab);

        toolbar.setTitle("Kişilerim");
        setSupportActionBar(toolbar);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        veriTabani = new VeriTabani(getApplicationContext());
        kisiler = new KisilerDao().tumKisiler(veriTabani);

        adapter = new KisilerAdapter(this,kisiler);
        rv.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertGoster();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        MenuItem item = menu.findItem(R.id.action_ara);

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {


        kisiler = new KisilerDao().kelimeAra(veriTabani,query);

        adapter = new KisilerAdapter(this,kisiler);
        rv.setAdapter(adapter);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        veriTabani = new VeriTabani(getApplicationContext());

        kisiler = new KisilerDao().kelimeAra(veriTabani,newText);

        adapter = new KisilerAdapter(this,kisiler);
        rv.setAdapter(adapter);

        return true;
    }

    public void alertGoster(){

        LayoutInflater layout = LayoutInflater.from(this);
        View view = layout.inflate(R.layout.alert_tasarim,null);

        EditText editTextAd = view.findViewById(R.id.editTextAd);
        EditText editTextTel = view.findViewById(R.id.editTextTel);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(view);
        alert.setTitle("Kişi Ekle");

        alert.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String kisi_ad = editTextAd.getText().toString().trim();
                String kisi_tel= editTextTel.getText().toString().trim();

                if (TextUtils.isEmpty(kisi_ad)){
                    Snackbar.make(editTextAd,"Adı giriniz!",Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(kisi_tel)){
                    Snackbar.make(editTextTel,"Telefon numarasını giriniz!",Snackbar.LENGTH_LONG).show();
                    return;
                }

                new KisilerDao().kisiEkle(veriTabani,kisi_ad,kisi_tel);
                kisiler = new KisilerDao().tumKisiler(veriTabani);

                adapter = new KisilerAdapter(getApplicationContext(),kisiler);
                rv.setAdapter(adapter);


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
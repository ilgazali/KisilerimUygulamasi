package com.example.kisilerimuygulamasi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class KisilerDao {

    public void kisiEkle( VeriTabani veriTabani,String kisi_ad, String kisi_tel){

        SQLiteDatabase sqLiteDatabase = veriTabani.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("kisi_ad",kisi_ad);
        values.put("kisi_tel",kisi_tel);

        sqLiteDatabase.insertOrThrow("kisiler",null,values);
        sqLiteDatabase.close();
    }


    public void kisiGuncelle( VeriTabani veriTabani,int kisi_id,String kisi_ad, String kisi_tel){

         SQLiteDatabase sqLiteDatabase = veriTabani.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("kisi_ad",kisi_ad);
        values.put("kisi_tel",kisi_tel);

        sqLiteDatabase.update("kisiler",values,"kisi_id=?",new String[]{String.valueOf(kisi_id)});
        sqLiteDatabase.close();
    }

    public void kisiSil(VeriTabani veriTabani, int kisi_id){ //Silmek için verinin id numarası lazım.
        SQLiteDatabase sqLiteDatabase = veriTabani.getWritableDatabase();
        sqLiteDatabase.delete("kisiler","kisi_id=?",new String[]{String.valueOf(kisi_id)});
        sqLiteDatabase.close();
    }
    public ArrayList<Kisiler> tumKisiler(VeriTabani veriTabani){

        ArrayList<Kisiler> kisilerArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = veriTabani.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM kisiler",null);

        while (c.moveToNext()){

            Kisiler kisiler = new Kisiler(c.getInt(c.getColumnIndex("kisi_id")),c.getString(c.getColumnIndex("kisi_ad"))
                    ,c.getString(c.getColumnIndex("kisi_tel")));
            kisilerArrayList.add(kisiler);
        }

        sqLiteDatabase.close();
        return kisilerArrayList;
    }

    public ArrayList<Kisiler> kelimeAra(VeriTabani veriTabani, String aramaKelime){
        ArrayList<Kisiler> kisilerArrayList = new ArrayList<>();
        SQLiteDatabase db = veriTabani.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM kisiler WHERE kisi_ad like '%"+aramaKelime+"%'",null);

        while(c.moveToNext()){

            Kisiler kisiler = new Kisiler(c.getInt(c.getColumnIndex("kisi_id")),c.getString(c.getColumnIndex("kisi_ad"))
                    ,c.getString(c.getColumnIndex("kisi_tel")));
            kisilerArrayList.add(kisiler);
        }
        db.close();
        return kisilerArrayList;
    }
}

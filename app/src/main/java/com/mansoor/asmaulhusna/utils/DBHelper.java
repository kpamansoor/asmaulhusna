package com.mansoor.asmaulhusna.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mansoor.asmaulhusna.models.Prayers;
import com.mansoor.asmaulhusna.models.Verse;

import java.util.ArrayList;
import java.util.HashMap;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String VERSES_TABLE_NAME = "verses";
    public static final String PRAYER_TABLE_NAME = "prayer";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " +VERSES_TABLE_NAME+
                        "(id integer primary key, ayath text,surath text,surath_e text, arab_text text,eng_text text,date DATETIME DEFAULT CURRENT_TIMESTAMP)"
        );
        db.execSQL(
                "create table " +PRAYER_TABLE_NAME+
                        "(id integer primary key, date text not null unique,fajr text,sunrise text, dhuhr text,asr text,sunset text,maghrib text, isha text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+VERSES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+PRAYER_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertVerse (String ayath, String surath, String surath_e, String arab_text, String eng_text) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("ayath", ayath);
            contentValues.put("surath", surath);
            contentValues.put("surath_e", surath_e);
            contentValues.put("arab_text", arab_text);
            contentValues.put("eng_text", eng_text);
            db.insert(VERSES_TABLE_NAME, null, contentValues);
            return true;
        }catch (Exception e){
            return false;
        }

    }
    public boolean insertPrayerTime (String date, String fajr, String sunrise , String  dhuhr , String asr , String sunset , String maghrib , String  isha) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("date", date);
            contentValues.put("fajr", fajr);
            contentValues.put("sunrise", sunrise);
            contentValues.put("dhuhr", dhuhr);
            contentValues.put("asr", asr);
            contentValues.put("sunset", sunset);
            contentValues.put("maghrib", maghrib);
            contentValues.put("isha", isha);

            db.insert(PRAYER_TABLE_NAME, null, contentValues);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+VERSES_TABLE_NAME+" where id="+id+"", null );
        return res;
    }

    public int numberOfVerses(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, VERSES_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteAllVerses () {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(VERSES_TABLE_NAME,null,null);
    }
    public Integer deleteAllPrayerTimes () {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PRAYER_TABLE_NAME,null,null);
    }

    public Prayers getPrayerTimes(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Prayers prayers = null;
        Cursor res =  db.rawQuery( "select * from "+PRAYER_TABLE_NAME+" where date='"+date+"'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            prayers = new Prayers();
            prayers.setFajr(res.getString(2));
            prayers.setSunrise(res.getString(3));
            prayers.setDhuhr(res.getString(4));
            prayers.setAsr(res.getString(5));
            prayers.setSunset(res.getString(6));
            prayers.setMaghrib(res.getString(7));
            prayers.setIsha(res.getString(8));
            res.moveToNext();
        }
        return prayers;
    }

    public ArrayList<Verse> getAllVerses() {
        ArrayList<Verse> array_list = new ArrayList<Verse>();
        Verse verse = null;
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+VERSES_TABLE_NAME+" ORDER BY id DESC", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            verse =  new Verse();
            verse.setId(res.getInt(0));
            verse.setAyath(res.getString(1));
            verse.setSurath(res.getString(2)+"("+res.getString(3)+")");
            verse.setArabicText(res.getString(4));
            verse.setEnglishText(res.getString(5));
            array_list.add(verse);
            res.moveToNext();
        }
        return array_list;
    }
    public Integer deleteVerse (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(VERSES_TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
}

package com.example.diaconescu_andrei_alexandru_1088;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseController extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dazzle.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_LOCATII = "locatii";
    private static final String COL_ID = "id";
    private static final String COL_NUME = "nume";
    private static final String COL_RATING = "rating";
    private static final String COL_PUNCTE = "puncte";
    private static final String COL_TIP = "tip";
    private static final String COL_ADRESA = "adresa";

    private static DatabaseController instance;

    public static synchronized DatabaseController getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseController(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_LOCATII + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NUME + " TEXT, " +
                COL_RATING + " REAL, " +
                COL_PUNCTE + " INTEGER, " +
                COL_TIP + " TEXT, " +
                COL_ADRESA + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATII);
        onCreate(db);
    }

    public void addLocatie(Locatie locatie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NUME, locatie.getNume());
        values.put(COL_RATING, locatie.getRating());
        values.put(COL_PUNCTE, locatie.getPuncte());
        values.put(COL_TIP, locatie.getTip());
        values.put(COL_ADRESA, locatie.getAdresa());

        db.insert(TABLE_LOCATII, null, values);
        db.close();
    }

    public void updateLocatie(Locatie locatie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NUME, locatie.getNume());
        values.put(COL_RATING, locatie.getRating());
        values.put(COL_PUNCTE, locatie.getPuncte());
        values.put(COL_TIP, locatie.getTip());
        values.put(COL_ADRESA, locatie.getAdresa());
        
        if (locatie.getId() != 0) {
             db.update(TABLE_LOCATII, values, COL_ID + " = ?", new String[]{String.valueOf(locatie.getId())});
        }
        db.close();
    }

    public List<Locatie> getAllLocatii() {
        List<Locatie> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LOCATII, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String nume = cursor.getString(cursor.getColumnIndexOrThrow(COL_NUME));
                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(COL_RATING));
                int puncte = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PUNCTE));
                String tip = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIP));
                String adresa = cursor.getString(cursor.getColumnIndexOrThrow(COL_ADRESA));

                Locatie locatie = new Locatie(adresa, tip, puncte, rating, nume);
                locatie.setId(id);
                list.add(locatie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    
    public void deleteLocatie(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCATII, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllLocatii() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCATII, null, null);
        db.close();
    }
}

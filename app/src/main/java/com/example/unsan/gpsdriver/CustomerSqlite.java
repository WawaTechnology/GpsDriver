package com.example.unsan.gpsdriver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Unsan on 25/4/18.
 */

public class CustomerSqlite extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "delivery_driver";

    public static final String COLUMN_ID = "id";
    public static final String photo = "photo";
    public static final int version=1;


    private static final String DATABASE_NAME = "delivery_db";
    public CustomerSqlite(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_NAME + "("
                        + COLUMN_ID + " TEXT PRIMARY KEY ,"
                        + photo + " TEXT"
                        + ")"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
    public boolean insertContact (String KEY,String Photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, KEY);
        contentValues.put(photo, Photo);

        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }
    public Cursor getData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM delivery_driver WHERE id =?", new String[] {id});
        return res;
    }
    public Cursor getData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME,null );
        return res;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }
    public Integer deleteDelivery (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[] { id });
    }




}

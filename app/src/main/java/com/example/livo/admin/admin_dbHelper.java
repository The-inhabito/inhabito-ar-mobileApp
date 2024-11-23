package com.example.livo.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class admin_dbHelper extends SQLiteOpenHelper{

    public static final String databaseName = "Admin.db";

    public admin_dbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "Admin.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase Admin_Database) {
        Admin_Database.execSQL("create Table Admin.db (email TEXT primary key, password TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase Admin_Database, int i, int i1) {
        Admin_Database.execSQL("drop table if exists Admin.db");
    }

    public Boolean insertData(String email, String password){
        SQLiteDatabase Admin_Database = this.getWritableDatabase();
        ContentValues ContentValues = new ContentValues();
        ContentValues.put("email", email);
        ContentValues.put("password", password);
        long result = Admin_Database.insert("Admin", null, ContentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkEmail(String email){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ?", new String[]{email});
        if(cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
}


package com.ahmadabuhasan.appgithubuser.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.ahmadabuhasan.appgithubuser.db.DatabaseContract.FavoriteColumns.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_FAVORITE = String.format(
            "CREATE TABLE %s" +
                    " (%s INTEGER PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_NAME,
            DatabaseContract.FavoriteColumns.ID,
            DatabaseContract.FavoriteColumns.AVATAR,
            DatabaseContract.FavoriteColumns.HTML,
            DatabaseContract.FavoriteColumns.USERNAME
    );

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
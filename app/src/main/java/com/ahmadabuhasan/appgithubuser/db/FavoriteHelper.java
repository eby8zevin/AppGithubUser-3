package com.ahmadabuhasan.appgithubuser.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ahmadabuhasan.appgithubuser.model.UserDetail;

import java.util.ArrayList;

import static com.ahmadabuhasan.appgithubuser.db.DatabaseContract.FavoriteColumns.ID;
import static com.ahmadabuhasan.appgithubuser.db.DatabaseContract.FavoriteColumns.TABLE_NAME;
import static com.ahmadabuhasan.appgithubuser.db.DatabaseContract.FavoriteColumns.USERNAME;

public class FavoriteHelper {

    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper databaseHelper;
    private static FavoriteHelper favoriteHelper;
    private static SQLiteDatabase db;

    public FavoriteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getFavoriteHelper(Context context) {
        if (favoriteHelper == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (favoriteHelper == null) {
                    favoriteHelper = new FavoriteHelper(context);
                }
            }
        }
        return favoriteHelper;
    }

    public void open() throws SQLException {
        db = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
        if (db.isOpen())
            db.close();
    }

    public ArrayList<UserDetail> getAllFavorite() {
        ArrayList<UserDetail> arrayList = new ArrayList<>();
        Cursor cursor = db.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                ID + " ASC",
                null);
        cursor.moveToFirst();
        UserDetail userDetail;
        if (cursor.getCount() > 0) {
            do {
                userDetail = new UserDetail();
                userDetail.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                userDetail.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(USERNAME)));
                arrayList.add(userDetail);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertFavorite(UserDetail response) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, response.getId());
        contentValues.put(USERNAME, response.getUsername());

        return db.insert(DATABASE_TABLE, null, contentValues);
    }

    public int deleteFavorite(String username) {
        return db.delete(TABLE_NAME, USERNAME + " = '" + username + "'", null);
    }
}
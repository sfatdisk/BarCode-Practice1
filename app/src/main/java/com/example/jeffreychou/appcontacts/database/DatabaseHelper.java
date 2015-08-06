package com.example.jeffreychou.appcontacts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jeffreychou on 7/29/15.
 * A Helper class to create/update the database
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    // Names of Tables
    public static final String TABLE_USER                   = "users";

    // Names of Columns
    public static final String COLUMN_USER_ID               = "_id";
    public static final String COLUMN_USER_FIRSTNAME        = "userFirstName";
    public static final String COLUMN_USER_SURNAME          = "userSurName";
    public static final String COLUMN_USER_PHONE            = "userPhone";
    public static final String COLUMN_USER_EMAIL            = "userEmail";


    // Database
    private static final String DATABASE_NAME = "AppContact";
    private static final int DATABASE_VERSION = 1;

    // Create Table
    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_USER +" ("
            + COLUMN_USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_FIRSTNAME+" TEXT,"
            + COLUMN_USER_SURNAME+" TEXT,"
            + COLUMN_USER_PHONE +" TEXT,"
            + COLUMN_USER_EMAIL+" TEXT);" ;

    // constructor, no modifier means package-private
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO -- create necessary tables
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO -- ship current database legacy to next generation
    }

}

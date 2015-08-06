package com.example.jeffreychou.appcontacts.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.jeffreychou.appcontacts.database.DatabaseHelper;

/**
 * Created by jeffreychou on 8/2/15.
 */

public class ContactProvider extends ContentProvider {

    private SQLiteDatabase db;

    // Content Provider Uri and Authority
    public static final String AUTHORITY= //same as AndroidManifest
            "com.example.jeffreychou.appcontacts.provider.ContactProvider";

    public static final Uri CONTENT_URI=
            Uri.parse("content://"+AUTHORITY+"/user"); //

    // List all or one query in DB
    private static final String USERS_MIME_TYPE= // plural: lists
            ContentResolver.CURSOR_DIR_BASE_TYPE+ "/vnd.com.example.jeffreychou.appcontacts.users";

    private static final String USER_MIME_TYPE=
            ContentResolver.CURSOR_ITEM_BASE_TYPE+"/vnd.com.example.jeffreychou.appcontacts.user";

    // UriMatcher stuff
    private static final int LIST_USER = 0;
    private static final int ITEM_USER = 1;
    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    /**
     * Builds up a UriMatcher for search
     * suggestion and shortcut refresh queries.
     */
    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        // to get definitions... list and item
        matcher.addURI(AUTHORITY, "user", LIST_USER);
        matcher.addURI(AUTHORITY, "user/#", ITEM_USER);

        return matcher;
    }

    // --

    @Override
    public boolean onCreate() {
        // grab a connection to our database
        db= new DatabaseHelper(getContext()).getWritableDatabase();
        return false;
    }

    @Override
    public String getType(Uri uri) {
        switch( URI_MATCHER.match(uri) ){

            case LIST_USER:{ return USERS_MIME_TYPE; }

            case ITEM_USER:{ return USER_MIME_TYPE; }

            default:{
                throw new IllegalArgumentException("Unknown Uri: "+uri);
            }
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) { // Uri = CONTENT_URI
        //inserting a row into the database return the row id

        if( values.containsKey(DatabaseHelper.COLUMN_USER_ID)){
            throw new UnsupportedOperationException();
        }

        long id= db.insertOrThrow(DatabaseHelper.TABLE_USER,null, values);
        // notify the UI the table users changed, so UI refreshed
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // the third and forth param can be ignored because of SQLite advanced usages
        if( values.containsKey(DatabaseHelper.COLUMN_USER_ID) ){
            throw new UnsupportedOperationException();
        }

        int count= db.update(
                DatabaseHelper.TABLE_USER,
                values,
                DatabaseHelper.COLUMN_USER_ID + "=?", // WHERE _id=?, ?= String[] {_id, _id, _id };
                new String[]{Long.toString(ContentUris.parseId(uri))}); // ? be replaced

        if(count>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count= db.delete(
                DatabaseHelper.TABLE_USER,
                DatabaseHelper.COLUMN_USER_ID+"=?",
                new String[]{ Long.toString( ContentUris.parseId(uri) )} );

        if(count>0){ // count==0 no delete, otherwise count==1
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String[] mProjection = new String[]{ // Columns in Table User
                DatabaseHelper.COLUMN_USER_ID,
                DatabaseHelper.COLUMN_USER_FIRSTNAME,
                DatabaseHelper.COLUMN_USER_SURNAME,
                DatabaseHelper.COLUMN_USER_PHONE,
                DatabaseHelper.COLUMN_USER_EMAIL };

        Cursor read;
        switch( URI_MATCHER.match(uri) ){
            case LIST_USER:{
                read=db.query(
                        DatabaseHelper.TABLE_USER,
                        mProjection,
                        selection, // WHERE title =?
                        selectionArgs,null,null,sortOrder);
                // read contains each of columns specified in the projection
                break;
            }

            case ITEM_USER:{
                read=db.query(
                        DatabaseHelper.TABLE_USER,
                        mProjection,
                        DatabaseHelper.COLUMN_USER_ID+"=?",
                        new String[]{Long.toString( ContentUris.parseId(uri))},
                        null,null,null, null);

                if(read.getCount()>0){
                    read.moveToFirst();
                }
                break;
            }
            default:{
                throw new IllegalArgumentException("Unknown Uri: "+ uri);
            }
        }
        // Loader uses the notification to automatically refresh UI
        read.setNotificationUri(getContext().getContentResolver(), uri);
        return read;
    }

}

package com.example.ph_data01221240053.movies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.net.URL;

/**
 * Created by PH-Data™ 01221240053 on 03/03/2017.
 */
public class MyProvider extends ContentProvider {
    public static final String _ID = "id";
    public static final String posterPath = "posterpath";
    public static final String overView = "overview";
    public static final String videoPath = "videopath";
    public static final String date= "date";
    public static final String favorir = "favorit";
    public static final String reviews= "reviews";
    public static final String vote= "vote";
    public static final String title= "title";
    public static final String TABLE_NAME = "MoviDetails";

    Moviebase base;
    private class Moviebase extends SQLiteOpenHelper {





        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        title + " TEXT," +
                        posterPath + " Text,"+
                        overView + " Text,"+
                        date + " Text,"+
                        vote + " Double,"+
                        favorir + " Boolean,"+
                        videoPath + " Text,"+
                        reviews + " TEXT)";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Movies";

        public Moviebase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

    }
    final static int movies=1;
    final static int movies_id=2;
    public final static  String providerName="com.example.ph_data01221240053.movies";
    public final static  String URL="content://"+providerName+"/"+"movies";
    public final static Uri myUrl = Uri.parse(URL);
    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(providerName, "movies", movies);
        sURIMatcher.addURI(providerName,  "movies/#", movies_id);
    }
    @Override
    public boolean onCreate() {
        base=new Moviebase(getContext());

        if(base==null)
            return false;
        else
            return true;

    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteQueryBuilder qp=new SQLiteQueryBuilder();
        qp.setTables(MyProvider.TABLE_NAME);
        SQLiteDatabase dp=base.getWritableDatabase();
        Cursor c=qp.query(dp,strings,null,null,null,null,null);
        c.setNotificationUri(getContext().getContentResolver(),uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri))
        {
            case movies:
                return "vnd.android.cursor.dir/vnd.example.movies";
            case movies_id:
                return "vnd.android.cursor.item/vnd.example.movies";
            default:
                throw new IllegalArgumentException("illegal");
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db=base.getWritableDatabase();
        long row= db.insert(MyProvider.TABLE_NAME, null, contentValues);
        Uri _uri;

        _uri= ContentUris.withAppendedId(myUrl, row);
        getContext().getContentResolver().notifyChange(_uri,null);


        return _uri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db=base.getWritableDatabase();
        int row=db.delete(MyProvider.TABLE_NAME,s,null);
        getContext().getContentResolver().notifyChange(uri, null);
        return row;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

}

package com.app.boshra.NovelMessage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {
    public static final int version = 1;
    public static final String database_name = "Message";
    public static final String MTable = "MTable";

    public static final String Text = "Text";
    public static final String RICHText = "RICHText";

    public Database(Context context) {
        super(context, database_name,null, version);

    }


    public void onCreate(SQLiteDatabase db) {
        final String t2 = "CREATE TABLE " +MTable+ "("+
                Text + " TEXT," + RICHText + " TEXT"+")";

        db.execSQL(t2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS MTable");
        onCreate(db);
    }
}
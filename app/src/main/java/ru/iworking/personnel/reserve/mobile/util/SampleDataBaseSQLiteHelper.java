package ru.iworking.personnel.reserve.mobile.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static ru.iworking.personnel.reserve.mobile.util.SqlProperties.*;

public class SampleDataBaseSQLiteHelper extends SQLiteOpenHelper {

    public SampleDataBaseSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Resume.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL(Resume.DROP_TABLE);
            this.onCreate(sqLiteDatabase);
        }
    }
}

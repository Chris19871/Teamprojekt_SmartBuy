package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Christian Meisberger on 21.03.2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "smartbuy.db";
    private static final int DATABASE_VERSION = 1;

    //table names
    private static final String VORAUSWAHL_TABLE = "vorauswahllisten";
    private static final String EINKAUFSARTIKEL_TABLE = "einkaufsartikel";
    private static final String EINKAUFSLISTEN_TABLE = "einkaufslisten";

    private final static String VORAUSWAHL_ID = "_id";
    private final static String EINKAUFSARTIKEL_ID = "_id";
    private final static String EINKAUFSLISTEN_ID = "_id";


    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}

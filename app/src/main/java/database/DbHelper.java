package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "smartbuy.db";
    private static final int DATABASE_VERSION = 1;

    //table names
    private static final String VORAUSWAHL_TABLE = "vorauswahllisten";
    private static final String EINKAUFSARTIKEL_TABLE = "einkaufsartikel";
    private static final String EINKAUFSLISTEN_TABLE = "einkaufslisten";

    private final static String VORAUSWAHL_ID = "id";
    private final static String EINKAUFSARTIKEL_ID = "id";
    private final static String EINKAUFSLISTEN_ID = "id";



    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create a new Database with default tables.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_VORAUSWAHL_TABLE = "CREATE TABLE " + VORAUSWAHL_TABLE + " (" +
                 VORAUSWAHL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT);";

        String CREATE_EINKAUFSARTIKEL_TABLE = "CREATE TABLE " + EINKAUFSARTIKEL_TABLE + " (" +
                EINKAUFSARTIKEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, "+
                        "desc TEXT, "+
                        "image INTEGER);";

        String CREATE_EINKAUFSLISTEN_TABLE = "CREATE TABLE " + EINKAUFSLISTEN_TABLE + " (" +
                EINKAUFSLISTEN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, "+
                        "best_time TEXT, "+
                        "start_time TEXT);";

        // create tables
        db.execSQL(CREATE_VORAUSWAHL_TABLE);
        db.execSQL(CREATE_EINKAUFSARTIKEL_TABLE);
        db.execSQL(CREATE_EINKAUFSLISTEN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
}
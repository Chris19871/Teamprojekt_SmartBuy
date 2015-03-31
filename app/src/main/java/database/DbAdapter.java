package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


/**
 * Created by Christian Meisberger on 25.03.2015.
 */
public class DbAdapter
{
    private DbHelper dbHelper;
    private SQLiteDatabase database;

    private String[] einkaufsArtikelAllColumns = { "id", "name",
            "desc", "image", "bought"};

    private String[] einkaufslistenAllColums = { "id", "name",
            "best_time", "start_time"};

    private String[] vorauswahllistenAllColums = { "id" , "name"};

    private int bought = 0;

    public DbAdapter(Context context)
    {
        dbHelper = new DbHelper(context);
    }

    public void openWrite() throws SQLException
    {
        database = dbHelper.getWritableDatabase();
    }

    public void openRead() throws SQLException
    {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public EinkaufsArtikel createEntryEinkaufsArtikel(String name, String desc, int image)
    {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("desc", desc);
        values.put("image", image);
        values.put("bought", bought);

        long insertId = database.insert("einkaufsartikel", null, values);

        Cursor cursor = database.query("einkaufsartikel", einkaufsArtikelAllColumns, "id = " + insertId, null, null, null, null);
        cursor.moveToFirst();

        return cursorToEntryArtikel(cursor);
    }

    public Einkaufsliste createEntryEinkaufsliste(String name, int bestTime, int startTime)
    {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("best_time", bestTime);
        values.put("start_time", startTime);

        long insertId = database.insert("einkaufslisten", null, values);

        Cursor cursorList = database.query("einkaufslisten", einkaufslistenAllColums, "id = " + insertId, null, null, null, null);
        cursorList.moveToFirst();

        return cursorToEntryEinkaufsliste(cursorList);
    }

    public Vorauswahl createEntryVorauswahlliste(String name)
    {
        ContentValues values = new ContentValues();
        values.put("name", name);

        long insertId = database.insert("vorauswahllisten", null, values);

        Cursor cursor = database.query("vorauswahllisten", vorauswahllistenAllColums, "id = " + insertId, null, null, null, null);
        cursor.moveToFirst();

        return cursorToEntryVorauswahlliste(cursor);
    }


    public ArrayList<EinkaufsArtikel> getAllEntriesArtikel(String table) {
        ArrayList<EinkaufsArtikel> EntriesList = new ArrayList<>();

        Cursor cursor = database.query(table, einkaufsArtikelAllColumns, null, null, null, null, null);
        cursor.moveToFirst();

        if(cursor.getCount() == 0) return EntriesList;

        while (!cursor.isAfterLast()) {
            EinkaufsArtikel artikel = cursorToEntryArtikel(cursor);
            EntriesList.add(artikel);
            cursor.moveToNext();
        }
        cursor.close();

        return EntriesList;
    }

    public ArrayList<Vorauswahl> getAllEntriesVorauswahlListe() {
        ArrayList<Vorauswahl> EntriesList = new ArrayList<>();

        Cursor cursor = database.query("vorauswahllisten", vorauswahllistenAllColums, null, null, null, null, null);
        cursor.moveToFirst();

        if(cursor.getCount() == 0) return EntriesList;

        while (!cursor.isAfterLast()) {
            Vorauswahl vorauswahl = cursorToEntryVorauswahlliste(cursor);
            EntriesList.add(vorauswahl);
            cursor.moveToNext();
        }
        cursor.close();

        return EntriesList;
    }

    public ArrayList<Einkaufsliste> getAllEntriesEinkaufsliste() {
        ArrayList<Einkaufsliste> EntriesList = new ArrayList<>();

        Cursor cursor = database.query("einkaufslisten", einkaufslistenAllColums, null, null, null, null, null);
        cursor.moveToFirst();

        if(cursor.getCount() == 0) return EntriesList;

        while (!cursor.isAfterLast()) {
            Einkaufsliste einkaufsliste = cursorToEntryEinkaufsliste(cursor);
            EntriesList.add(einkaufsliste);
            cursor.moveToNext();
        }
        cursor.close();

        return EntriesList;
    }


    private EinkaufsArtikel cursorToEntryArtikel(Cursor cursor) {
        EinkaufsArtikel artikel = new EinkaufsArtikel();
        artikel.setId(cursor.getLong(0));
        artikel.setName(cursor.getString(1));
        artikel.setDesc(cursor.getString(2));
        artikel.setPic(cursor.getInt(3));

        return artikel;
    }

    private Einkaufsliste cursorToEntryEinkaufsliste(Cursor cursor) {
        Einkaufsliste einkaufsliste = new Einkaufsliste();
        einkaufsliste.setId(cursor.getLong(0));
        einkaufsliste.setName(cursor.getString(1));
        einkaufsliste.setBestTime(cursor.getInt(2));
        einkaufsliste.setStartTime(cursor.getInt(3));
        return einkaufsliste;
    }

    private Vorauswahl cursorToEntryVorauswahlliste(Cursor cursor) {
        Vorauswahl vorauswahl = new Vorauswahl();
        vorauswahl.setId(cursor.getLong(0));
        vorauswahl.setName(cursor.getString(1));
        return vorauswahl;
    }

    public void addListe(String name)
    {
        database.execSQL("CREATE TABLE "+ name + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, desc TEXT, image INTEGER, bought INTEGER);");
    }

    public EinkaufsArtikel createEntryEinkaufArtikeltoTable(String table, String name, String desc, int image)
    {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("desc", desc);
        values.put("image", image);
        values.put("bought", bought);

        long insertId = database.insert(table, null, values);

        Cursor cursor = database.query(table, einkaufsArtikelAllColumns, "id = " + insertId, null, null, null, null);
        cursor.moveToFirst();

        return cursorToEntryArtikel(cursor);
    }


}

package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import smartbuy.teamproject.application.R;

public class DbAdapter
{
    private DbHelper dbHelper;
    private SQLiteDatabase database;

    private String[] einkaufsArtikelAllColumns = { "id", "name",
            "desc", "image", "bought"};

    private String[] einkaufslistenAllColums = { "id", "name",
            "best_time", "start_time"};

    private String[] vorauswahllistenAllColums = { "id" , "name"};

    private String[] vorauswahllisten = {"Party","Geburtstag"};
    private String[] partyArtikel = {
            "Fleisch",
            "Bier",
            "Limo",
            "Cola",
            "Holzkohle",
            "Grillanzünder",
            "Grillbesteck",
            "Feuerzeug",
            "Sprudel",
            "Würstchen"

    };
    private int[] partArtikelImage = {
            R.mipmap.image_fleisch,
            R.mipmap.smartbuy_logo,
            R.mipmap.image_limonade,
            R.mipmap.image_limonade,
            R.mipmap.smartbuy_logo,
            R.mipmap.smartbuy_logo,
            R.mipmap.smartbuy_logo,
            R.mipmap.smartbuy_logo,
            R.mipmap.image_limonade,
            R.mipmap.image_fleisch

    };
    private String[] geburstagArtikel = {
            "Partyhüte",
            "Plastikbesteck",
            "Limo",
            "Cola",
            "Sprudel",
            "Bier",
            "Pizza",
            "Fleisch",
            "Tomaten",
            "Nudeln"


    };
    private int[] geburstagArtikelImage = {
            R.mipmap.smartbuy_logo,
            R.mipmap.smartbuy_logo,
            R.mipmap.image_limonade,
            R.mipmap.image_limonade,
            R.mipmap.smartbuy_logo,
            R.mipmap.smartbuy_logo,
            R.mipmap.smartbuy_logo,
            R.mipmap.image_fleisch,
            R.mipmap.image_gemuse,
            R.mipmap.smartbuy_logo

    };

    /**
     * Constructor -
     *
     * create a new DbAdapter instance attached to a target.
     *
     * @param context context for this view.
     */
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

    /**
     * create a new entry into table "einkaufslisten" .
     *
     * @param name Name of the entry.
     *
     */
    public Einkaufsliste createEntryEinkaufsliste(String name, String bestTime, long startTime)
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

    /**
     * create a new entry into table "vorauswahllisten" .
     *
     * @param name Name of the entry.
     *
     */
    public Vorauswahl createEntryVorauswahlliste(String name)
    {
        ContentValues values = new ContentValues();
        values.put("name", name);

        long insertId = database.insert("vorauswahllisten", null, values);

        Cursor cursor = database.query("vorauswahllisten", vorauswahllistenAllColums, "id = " + insertId, null, null, null, null);
        cursor.moveToFirst();

        return cursorToEntryVorauswahlliste(cursor);
    }

    /**
     * Get all entries <EinkaufsArtikel> from a table.
     *
     * @param table Name of the table.
     *
     */
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

    /**
     * Get all entries from preselection table.
     *
     */
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

    /**
     * Get all entries from shoppinglist table.
     *
     */
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

    public EinkaufsArtikel getArtikel(String table, String name)
    {
        EinkaufsArtikel artikel = null;
        ArrayList<EinkaufsArtikel> artikelList = getAllEntriesArtikel(table);
        for(int i = 0; i < artikelList.size(); i++)
        {
            if(artikelList.get(i).getName().equals(name))
            {
                artikel = artikelList.get(i);
            }
        }
        return artikel;
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
        einkaufsliste.setBestTime(cursor.getString(2));
        einkaufsliste.setStartTime(cursor.getString(3));
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
        database.execSQL("CREATE TABLE IF NOT EXISTS " + name + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, desc TEXT, image INTEGER, bought INTEGER);");
    }

    /**
     * Create a new entry of EinkaufsArtikel to a specific table.
     */
    public EinkaufsArtikel createEntryEinkaufArtikeltoTable(String table, String name, String desc, int image)
    {
        int bought = 0;

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

    public void createVorauswahllisten()
    {
        String desc = "";

        for (String aVorauswahllisten : vorauswahllisten)
        {
            createEntryVorauswahlliste(aVorauswahllisten);
        }

        ArrayList<Vorauswahl> list = getAllEntriesVorauswahlListe();
        for (int i = 0; i < list.size(); i++)
        {
            addListe("'" + String.valueOf(list.get(i).getId()) + "'");
        }

        for(int i=0; i < partyArtikel.length; i++)
        {
            createEntryEinkaufArtikeltoTable("'" + String.valueOf(list.get(0).getId())+ "'",partyArtikel[i],desc,partArtikelImage[i]);
        }

        for(int i=0; i < geburstagArtikel.length; i++)
        {
            createEntryEinkaufArtikeltoTable("'" + String.valueOf(list.get(1).getId()) + "'",geburstagArtikel[i],desc,geburstagArtikelImage[i]);
        }
    }

    public void createNewVorauswahlliste(String tableName)
    {
        String listname = "";
        createEntryVorauswahlliste(tableName);
        ArrayList<Vorauswahl> list = getAllEntriesVorauswahlListe();
        for (int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getName().equals(tableName))
            {
                listname = "'" + String.valueOf(list.get(i).getId()) + "'";
            }

        }
        addListe(listname);
    }

    public void createEinkaufsliste(String tableName, ArrayList<EinkaufsArtikel> artikel)
    {
        String listname = "";
        createEntryEinkaufsliste(tableName,"00:00:00", 0);
        ArrayList<Einkaufsliste> list = getAllEntriesEinkaufsliste();
        for (int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getName().equals(tableName))
            {
                listname = "'" + String.valueOf(list.get(i).getId()) + "'";
            }

        }
        addListe(listname);

        for(int i = 0; i < artikel.size(); i++)
        {
            String name = artikel.get(i).getName();
            String desc = artikel.get(i).getDesc();
            int image = artikel.get(i).getPic();

            createEntryEinkaufArtikeltoTable(listname,name,desc,image);
        }
    }

    public void buyArtikel(String table, long id)
    {
        database.execSQL("UPDATE " + table +" set bought = 1 WHERE id = " + id +";");
    }

    public void undoBuyArtikel(String table, long id)
    {
        database.execSQL("UPDATE " + table +" set bought = 0 WHERE id = " + id +";");
    }

    public void deleteArtikel(String table, long id)
    {
        database.execSQL("DELETE FROM " + table + " WHERE id = " + id + ";");
    }

    public void changeArtikel(String table, String artikel, String desc, int image, long id)
    {
        database.execSQL("UPDATE " + table + " set name = '" + artikel +"', desc = '" + desc +
                "', image = " + image + " WHERE id = " + id + ";");
    }

    public void resetList(String table)
    {
        database.execSQL("UPDATE " + table + " set bought = 0 WHERE bought = 1;");
    }

    /**
     * Get all entries of EinkaufsArtikel from a specific table
     * where bought = 1.
     */
    public ArrayList<EinkaufsArtikel> getAllItemsBought(String table)
    {
        ArrayList<EinkaufsArtikel> artikelBought = new ArrayList<>();

        Cursor cursor = database.query(table, einkaufsArtikelAllColumns, "bought = 1", null, null, null, null);
        cursor.moveToFirst();

        if(cursor.getCount() == 0) return artikelBought;

        while (!cursor.isAfterLast()) {
            EinkaufsArtikel artikel = cursorToEntryArtikel(cursor);
            artikelBought.add(artikel);
            cursor.moveToNext();
        }
        cursor.close();

        return  artikelBought;
    }

    /**
     * Get all entries of EinkaufsArtikel from a specific table
     * where bought = 0.
     */
    public ArrayList<EinkaufsArtikel> getAllItemsNotBought(String table)
    {
        ArrayList<EinkaufsArtikel> artikelNotBought = new ArrayList<>();

        Cursor cursor = database.query(table, einkaufsArtikelAllColumns, "bought = 0", null, null, null, null);
        cursor.moveToFirst();

        if(cursor.getCount() == 0) return artikelNotBought;

        while (!cursor.isAfterLast()) {
            EinkaufsArtikel artikel = cursorToEntryArtikel(cursor);
            artikelNotBought.add(artikel);
            cursor.moveToNext();
        }
        cursor.close();

        return  artikelNotBought;
    }

    public String getStartTime(String table)
    {
        ArrayList<Einkaufsliste> EntriesList = new ArrayList<>();

        Cursor cursor = database.query("einkaufslisten", einkaufslistenAllColums, "name = " + table , null, null, null, null);
        cursor.moveToFirst();

        if(cursor.getCount() == 0) return "0";

        while (!cursor.isAfterLast()) {
            Einkaufsliste einkaufsliste = cursorToEntryEinkaufsliste(cursor);
            EntriesList.add(einkaufsliste);
            cursor.moveToNext();
        }
        cursor.close();

        return EntriesList.get(0).getStartTime();
    }

    public void setStartTime(String table, String startTime)
    {
        database.execSQL("UPDATE einkaufslisten set start_time = '" + startTime +"' WHERE name = " + table + ";");
    }

    public String getBestTime(String table)
    {
        ArrayList<Einkaufsliste> EntriesList = new ArrayList<>();

        Cursor cursor = database.query("einkaufslisten", einkaufslistenAllColums, "name = '" + table + "'" , null, null, null, null);
        cursor.moveToFirst();

        if(cursor.getCount() == 0) return "00:00:00";

        while (!cursor.isAfterLast()) {
            Einkaufsliste einkaufsliste = cursorToEntryEinkaufsliste(cursor);
            EntriesList.add(einkaufsliste);
            cursor.moveToNext();
        }
        cursor.close();

        return EntriesList.get(0).getBestTime();
    }

    public void setBestTime(String table, String bestTime)
    {
        database.execSQL("UPDATE einkaufslisten set best_time = '" + bestTime + "' WHERE name = '" + table + "';");
    }

    public void resetStartTime(String table)
    {
        database.execSQL("UPDATE einkaufslisten set start_time = '0' WHERE name = '" + table + "';");
    }

    public void resetBestTime(String table)
    {
        database.execSQL("UPDATE einkaufslisten set best_time = '00:00:00' WHERE name = '" + table + "';");
    }

    public void deleteTableEinkaufliste(String tableName, String tableId)
    {
        database.execSQL("DROP TABLE IF EXISTS '" + tableId + "'");
        database.execSQL("DELETE FROM einkaufslisten WHERE name = '" + tableName +"';");
    }

    public void deleteTableVorauswahl(String tableName, String tableId)
    {
        database.execSQL("DROP TABLE IF EXISTS '" + tableId + "'");
        database.execSQL("DELETE FROM vorauswahllisten WHERE name = '" + tableName +"';");
    }

    public void changeListNameEinkaufsliste(String oldName, String newName)
    {
        database.execSQL("UPDATE einkaufslisten set name = '" + newName +"' WHERE name = '" + oldName + "';");
    }

    public void changeListNameVorauswahlliste(String oldName, String newName)
    {
        database.execSQL("UPDATE vorauswahllisten set name = '" + newName +"' WHERE name = '" + oldName + "';");
    }
}

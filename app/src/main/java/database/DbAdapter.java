package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Array;
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

    private int bought = 0;
    private String desc = "";
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

    public Einkaufsliste createEntryEinkaufsliste(String name, String bestTime, String startTime)
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

    public void createVorauswahllisten()
    {
        for(int i=0; i < vorauswahllisten.length; i++)
        {
            addListe(vorauswahllisten[i]);
            createEntryVorauswahlliste(vorauswahllisten[i]);
        }
        for(int i=0; i < partyArtikel.length; i++)
        {
            createEntryEinkaufArtikeltoTable(vorauswahllisten[0],partyArtikel[i],desc,partArtikelImage[i]);
        }
        for(int i=0; i < geburstagArtikel.length; i++)
        {
            createEntryEinkaufArtikeltoTable(vorauswahllisten[1],geburstagArtikel[i],desc,geburstagArtikelImage[i]);
        }
    }

    public void createEinkaufsliste(String tableName, ArrayList<EinkaufsArtikel> artikel)
    {
        createEntryEinkaufsliste(tableName,"00:00:00", "00:00:00");
        addListe(tableName);
        for(int i = 0; i < artikel.size(); i++)
        {
            String name = artikel.get(i).getName();
            String desc = artikel.get(i).getDesc();
            int image = artikel.get(i).getPic();

            createEntryEinkaufArtikeltoTable(tableName,name,desc,image);
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

    public void deleteArtikel(String table, String artikel)
    {
        database.execSQL("DELETE FROM " + table + " WHERE name = " + artikel + ";");
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

    public ArrayList<EinkaufsArtikel> getAllItemsBought(String table)
    {
        ArrayList<EinkaufsArtikel> artikel = getAllEntriesArtikel(table);
        ArrayList<EinkaufsArtikel> artikelBought = new ArrayList<>();
        for(int i = 0; i < artikel.size(); i++)
        {
            if(artikel.get(i).getBought() == 1)
                artikelBought.add(artikel.get(i));
        }
        return  artikelBought;
    }

    public ArrayList<EinkaufsArtikel> getAllItemsNotBought(String table)
    {
        ArrayList<EinkaufsArtikel> artikel = getAllEntriesArtikel(table);
        ArrayList<EinkaufsArtikel> artikelNotBought = new ArrayList<>();
        for(int i = 0; i < artikel.size(); i++)
        {
            if(artikel.get(i).getBought() == 0)
                artikelNotBought.add(artikel.get(i));
        }
        return  artikelNotBought;
    }

    public void deleteTableEinkaufliste(String table)
    {
        database.execSQL("DROP TABLE IF EXISTS " + table);
        database.execSQL("DELETE FROM einkaufslisten WHERE name = '" + table +"';");
    }

    public void deleteTableVorauswahl(String table)
    {
        database.execSQL("DROP TABLE IF EXISTS " + table);
        database.execSQL("DELETE FROM vorauswahllisten WHERE name = '" + table +"';");
    }

    public void changeListNameEinkaufsliste(String oldName, String newName)
    {
        Einkaufsliste tmp = null;
        ArrayList<Einkaufsliste> liste = getAllEntriesEinkaufsliste();
        for (int i = 0; i < liste.size(); i++)
        {
            if (liste.get(i).getName().equals(oldName))
            {
                tmp = liste.get(i);
            }
        }

        long id = tmp.getId();

        database.execSQL("ALTER TABLE " + oldName + " RENAME TO " + newName +";");
        database.execSQL("UPDATE einkaufslisten set name = '" + newName +"' WHERE name = '" + oldName + "';");
    }

    public void changeListNameVorauswahlliste(String oldName, String newName)
    {
        Vorauswahl tmp = null;
        ArrayList<Vorauswahl> liste = getAllEntriesVorauswahlListe();
        for (int i = 0; i < liste.size(); i++)
        {
            if (liste.get(i).getName().equals(oldName))
            {
                tmp = liste.get(i);
            }
        }

        long id = tmp.getId();

        database.execSQL("ALTER TABLE " + oldName + " RENAME TO " + newName +";");
        database.execSQL("UPDATE vorauswahllisten set name = '" + newName +"' WHERE name = '" + oldName + "';");
    }
}

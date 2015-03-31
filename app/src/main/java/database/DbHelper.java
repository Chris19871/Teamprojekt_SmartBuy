package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import smartbuy.teamproject.application.R;


/**
 * Created by Christian Meisberger on 21.03.2015.
 */
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

    private DbAdapter dbAdapter;
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
            "Würstchen",
            "Tomaten",
            "Mayo",
            "Salat",
            "Getränkeuntersetzer",
            "Servietten",
            "Gläser",
            "Baguette",
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
            R.mipmap.image_fleisch,
            R.mipmap.image_gemuse,
            R.mipmap.smartbuy_logo,
            R.mipmap.image_gemuse,
            R.mipmap.smartbuy_logo,
            R.mipmap.smartbuy_logo,
            R.mipmap.smartbuy_logo,
            R.mipmap.smartbuy_logo
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
            "Nudeln",
            "Mayo",
            "Salat",
            "Paprika",
            "Gurken",
            "Käsehappchen",
            "Süßigkeiten",
            "Partydeko",

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
            R.mipmap.smartbuy_logo,
            R.mipmap.smartbuy_logo,
            R.mipmap.image_gemuse,
            R.mipmap.image_gemuse,
            R.mipmap.image_gemuse,
            R.mipmap.image_kase,
            R.mipmap.smartbuy_logo,
            R.mipmap.smartbuy_logo
    };


    public DbHelper(Context context, DbAdapter dbAdapter)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.dbAdapter = dbAdapter;
    }

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
                        "best_time INTEGER, "+
                        "start_time INTEGER);";



        // create tables
        db.execSQL(CREATE_VORAUSWAHL_TABLE);
        db.execSQL(CREATE_EINKAUFSARTIKEL_TABLE);
        db.execSQL(CREATE_EINKAUFSLISTEN_TABLE);

        createVorauswahllisten();


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
    public void createVorauswahllisten()
    {
        for(int i=0; i < vorauswahllisten.length; i++)
        {
            dbAdapter.addListe(vorauswahllisten[i]);

        }
        for(int i=0; i < partyArtikel.length; i++)
        {
            dbAdapter.createEntryEinkaufArtikeltoTable(vorauswahllisten[0],partyArtikel[i],desc,partArtikelImage[i]);
        }
        for(int i=0; i < geburstagArtikel.length; i++)
        {
            dbAdapter.createEntryEinkaufArtikeltoTable(vorauswahllisten[1],geburstagArtikel[i],desc,geburstagArtikelImage[i]);
        }

    }
}

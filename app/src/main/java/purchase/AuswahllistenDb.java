package purchase;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

import database.DbAdapter;

/**
 * Created by Christian Meisberger on 26.03.2015.
 */
public class AuswahllistenDb
{
    private ArrayList<VorauswahlListe> vorauswahllisten = new ArrayList<>();

    private DbAdapter dbAdapter;
    private String[] partyArtikel = {"Fleisch", "Bier", "Limo", "Cola"};
    String[] geburstagArtikel = {"Partyhüte", "Besteck", "Limo", "Cola"};

    public void AuswahllistenDb()
    {

        // SmartBuy Vorauswahllisten erstellen

        ArrayList<EinkaufsArtikel> gebItems = new ArrayList<>();
        EinkaufsArtikel artikel;
        for (int i = 0; i < geburstagArtikel.length; i++)
        {
            artikel = new EinkaufsArtikel(geburstagArtikel[i], "", null);
            gebItems.add(artikel);

        }

        VorauswahlListe geburtstag = new VorauswahlListe("Geburtstag", gebItems);
        vorauswahllisten.add(geburtstag);


        ArrayList<EinkaufsArtikel> partyItems = new ArrayList<>();
        for (int i = 0; i < partyArtikel.length; i++)
        {
            artikel = new EinkaufsArtikel(partyArtikel[i], "", null);
            partyItems.add(artikel);

        }
        VorauswahlListe party = new VorauswahlListe("Party", partyItems);
        vorauswahllisten.add(party);
    }

    public ArrayList<VorauswahlListe> getVorauswahllisten()
    {
        return vorauswahllisten;
    }

    public void addArtikelToDb()
    {
        try {
            dbAdapter.openWrite();
            for(int i = 0; i < partyArtikel.length; i++)
            {
                dbAdapter.createEntryEinkaufsArtikel(partyArtikel[i], null, 0);
            }
            for(int i = 0; i < geburstagArtikel.length; i++)
            {
                dbAdapter.createEntryEinkaufsArtikel(geburstagArtikel[i], null, 0);
            }

            dbAdapter.close();
        }
        catch (Exception ex) {
          //  Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void setDbAdapter(DbAdapter dbAdapter)
    {
        this.dbAdapter = dbAdapter;
    }
}

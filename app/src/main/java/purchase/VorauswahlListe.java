package purchase;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Christian Meisberger on 11.03.2015.
 */
public class VorauswahlListe
{
    private String name;
    private ArrayList<EinkaufsArtikel> items;

    public VorauswahlListe(String name, ArrayList<EinkaufsArtikel> items)
    {
        this.name = name;
        this.items = items;
    }

    public ArrayList<EinkaufsArtikel> getItems() {
        return items;
    }

    public void setItems(ArrayList<EinkaufsArtikel> items) {
        this.items = items;
    }

    @Override
    public String toString()
    {
        return name;
    }
}

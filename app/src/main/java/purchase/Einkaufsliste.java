package purchase;

import java.util.ArrayList;

/**
 * Created by Christian Meisberger on 09.03.2015.
 */
public class Einkaufsliste
{
    private ArrayList<EinkaufsArtikel> items;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Einkaufsliste(String name, ArrayList<EinkaufsArtikel> items)
    {

        this.items = items;
        this.name = name;
    }

    public void addItem(EinkaufsArtikel pItem)
    {
        items.add(pItem);
    }

    public String toString()
    {
        return name;
    }
}

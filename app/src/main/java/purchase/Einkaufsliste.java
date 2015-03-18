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

    public void addItemPos(EinkaufsArtikel pItem, int pos)
    {
        items.add(pos, pItem);
    }

    public void delItem(int pos)
    {
        if(!items.isEmpty())
        {
            items.remove(pos);
        }
    }

    public String toString()
    {
        return name;
    }

    public ArrayList<EinkaufsArtikel> getItems()
    {
        return items;
    }

    public void setItems(ArrayList<EinkaufsArtikel> items)
    {
        this.items = items;
    }
}

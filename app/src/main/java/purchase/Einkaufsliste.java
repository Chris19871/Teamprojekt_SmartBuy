package purchase;

import java.util.ArrayList;

public class Einkaufsliste
{
    private ArrayList<EinkaufsArtikel> allItems = new ArrayList<>();
    private ArrayList<EinkaufsArtikel> items;
    private ArrayList<EinkaufsArtikel> itemsBought;
    private String name;

    public Einkaufsliste(String name, ArrayList<EinkaufsArtikel> items, ArrayList<EinkaufsArtikel> itemsBought)
    {

        this.items = items;
        this.name = name;
        this.itemsBought = itemsBought;
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

    public ArrayList<EinkaufsArtikel> getItemsBought()
    {
        return itemsBought;
    }

    public void setItemsBought(ArrayList<EinkaufsArtikel> itemsBought)
    {
        this.itemsBought = itemsBought;
    }

    public ArrayList<EinkaufsArtikel> getAllItems()
    {
        for(int i = 0; i < items.size(); i++)
        {
            if(!allItems.contains(items.get(i))) {
                allItems.add(items.get(i));
            }
        }

        for(int i = 0; i < itemsBought.size(); i++) {
            if(!allItems.contains(itemsBought.get(i))) {
                allItems.add(itemsBought.get(i));
            }
        }

        return allItems;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

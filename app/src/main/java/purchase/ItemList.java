package purchase;

import java.util.ArrayList;

/**
 * Created by Christian Meisberger on 09.03.2015.
 */
public class ItemList {
    private ArrayList<PurchaseItems> items;
    private String name;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemList(String name, ArrayList<PurchaseItems> items)
    {

        this.items = items;
        this.name = name;
    }

    public void addItem(PurchaseItems pItem)
    {
        items.add(pItem);
    }


}

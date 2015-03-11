package purchase;

/**
 * Created by Christian Meisberger on 11.03.2015.
 */
public class PreselectionItem
{
    private String name;
    private PurchaseItems[] items;

    public PreselectionItem(String name, PurchaseItems[] items)
    {
        this.name = name;
        this.items = items;
    }


}

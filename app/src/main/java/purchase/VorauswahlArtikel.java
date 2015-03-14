package purchase;

/**
 * Created by Christian Meisberger on 11.03.2015.
 */
public class VorauswahlArtikel
{
    private String name;
    private EinkaufsArtikel[] items;

    public VorauswahlArtikel(String name, EinkaufsArtikel[] items)
    {
        this.name = name;
        this.items = items;
    }

    public EinkaufsArtikel[] getItems() {
        return items;
    }

    public void setItems(EinkaufsArtikel[] items) {
        this.items = items;
    }

    @Override
    public String toString()
    {
        return name;
    }
}

package purchase;

import java.util.ArrayList;

public class VorauswahlListe
{
    private String name;
    private ArrayList<EinkaufsArtikel> items;

    public VorauswahlListe(String name, ArrayList<EinkaufsArtikel> items)
    {
        this.name = name;
        this.items = items;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public ArrayList<EinkaufsArtikel> getItems() {
        return items;
    }

    public void setItems(ArrayList<EinkaufsArtikel> items) {
        this.items = items;
    }

}

package purchase;

import java.util.ArrayList;

/**
 * Created by Christian Meisberger on 12.03.2015.
 */
public class PreselectionList {

    private String name;
    private ArrayList<PreselectionItem> items;

    public PreselectionList(String name, ArrayList<PreselectionItem> items)
    {
        this.items = items;
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}

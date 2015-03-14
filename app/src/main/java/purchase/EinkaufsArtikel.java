package purchase;

import android.graphics.Picture;

/**
 * Created by Christian Meisberger on 09.03.2015.
 */
public class EinkaufsArtikel
{
    private String name;
    private String desc;
    private Picture pic;

    public EinkaufsArtikel(String name, String desc, Picture pic)
    {
        this.name = name;
        this.desc = desc;
        this.pic = pic;
    }

    public String getName()
    {
        return name;
    }

    public String getDesc()
    {
        return desc;
    }

    public Picture getPic()
    {
        return pic;
    }
}

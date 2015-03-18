package purchase;

import android.graphics.Picture;
import android.widget.ImageView;

/**
 * Created by Christian Meisberger on 09.03.2015.
 */
public class EinkaufsArtikel
{
    private String name;
    private String desc;
    private ImageView pic;

    public EinkaufsArtikel(String name, String desc, ImageView pic)
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

    public ImageView getPic()
    {
        return pic;
    }

    public String toString()
    {
        return name;
    }
}

package purchase;

import android.widget.ImageView;

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
    @Override
    public String toString()
    {
        return name;
    }

    public String getName()
    {
        return name;
    }

    public String getDesc()
    {
        return desc;
    }

    public ImageView getImage()
    {
        return pic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setPic(ImageView pic) {
        this.pic = pic;
    }


}


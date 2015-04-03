package database;

public class EinkaufsArtikel
{
    private long id;
    private String name;
    private String desc;
    private int pic;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public String getDesc()
    {
        return desc;
    }

    public int getPic()
    {
        return pic;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}


package database;

import java.util.ArrayList;

/**
 * Created by Christian Meisberger on 11.03.2015.
 */
public class Vorauswahl
{
    private String name;
    private long id;


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

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}

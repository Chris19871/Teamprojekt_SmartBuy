package database;

import java.util.ArrayList;

/**
 * Created by Christian Meisberger on 09.03.2015.
 */
public class Einkaufsliste
{
    private long id;
    private String name;
    private int bestTime;
    private int startTime;

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

    @Override
    public String toString()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getStartTime()
    {
        return startTime;
    }

    public void setStartTime(int startTime)
    {
        this.startTime = startTime;
    }

    public int getBestTime()
    {
        return bestTime;
    }

    public void setBestTime(int bestTime)
    {
        this.bestTime = bestTime;
    }
}

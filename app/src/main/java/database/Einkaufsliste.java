package database;

public class Einkaufsliste
{
    private long id;
    private String name;
    private String bestTime;
    private String startTime;

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

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getBestTime()
    {
        return bestTime;
    }

    public void setBestTime(String bestTime)
    {
        this.bestTime = bestTime;
    }
}

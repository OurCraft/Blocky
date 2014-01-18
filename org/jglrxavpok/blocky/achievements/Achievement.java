package org.jglrxavpok.blocky.achievements;

import java.util.HashMap;

public class Achievement
{

    private static final HashMap<String, Achievement> ids = new HashMap<String, Achievement>();
    private String name;
    private String id;
    private String desc;

    public Achievement(String id, String name, String desc)
    {
        this.name = name;
        this.id = id;
        ids.put(id, this);
        this.desc = desc;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getID()
    {
        return id;
    }

    public static Achievement getAchievementByID(String string)
    {
        return ids.get(string);
    }
}

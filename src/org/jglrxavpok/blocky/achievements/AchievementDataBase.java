package org.jglrxavpok.blocky.achievements;

import java.util.ArrayList;

import org.jglrxavpok.blocky.lexa.LexaManager;

public class AchievementDataBase
{

    private static AchievementDataBase instance;

    public AchievementDataBase()
    {
        AchievementList.beginNewWorld.getID(); // Init list
    }
    
    /**
     * Returns true if was already toggled
     * @param playerName
     * @param a
     * @return
     */
    public boolean activateAchievement(String playerName, Achievement a)
    {
        ArrayList<String> keys = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();
        keys.add("action");
        values.add("activate");
        keys.add("username");
        values.add(playerName);
        keys.add("achievement");
        values.add(a.getID());
        String result = LexaManager.doLexaRequest("achievements", keys, values);
        if(result.contains("true"))
            return true;
        return false;
    }
    
    public ArrayList<Achievement> retrieveAchievementsOf(String playerName)
    {
        ArrayList<Achievement> achievements = new ArrayList<Achievement>();
        ArrayList<String> keys = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();
        keys.add("action");
        values.add("get");
        keys.add("username");
        values.add(playerName);
        String result = LexaManager.doLexaRequest("achievements", keys, values);
        String[] parts = result.split(":");
        for(int i = 0;i<parts.length;i++)
        {
            Achievement a = Achievement.getAchievementByID(parts[i]);
            if(a != null)
                achievements.add(a);
        }
        return achievements;
    }
    
    public boolean doesPlayerHaveAchievement(Achievement a, String username)
    {
        return retrieveAchievementsOf(username).contains(a);
    }
    
    public static AchievementDataBase getInstance()
    {
        if(instance == null)
            instance = new AchievementDataBase();
        return instance;
    }
}

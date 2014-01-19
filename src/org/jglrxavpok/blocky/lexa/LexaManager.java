package org.jglrxavpok.blocky.lexa;

import java.util.ArrayList;

import org.jglrxavpok.blocky.utils.NetworkUtils;

public class LexaManager implements LexaConstants
{

    public static String doLexaRequest(String actionType, String[] keys, String[] values)
    {
        ArrayList<String> keysList = new ArrayList<String>();
        ArrayList<String> valuesList = new ArrayList<String>();
        for(int i = 0;i<keys.length;i++)
        {
            keysList.add(keys[i]);
            valuesList.add(values[i]);
        }
        return doLexaRequest(actionType, keysList, valuesList);
    }
    
    public static String doLexaRequest(String actionType, ArrayList<String> keys, ArrayList<String> values)
    {
        try
        {
            String result = NetworkUtils.post(LEXA_ACCESS+"?actionType="+actionType, keys, values);
            return result;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public static SkinObject getSkinForPlayer(String username)
    {
        SkinObject o = new SkinObject();
        String skin = doLexaRequest("skin", new String[]{"username","action"}, new String[]
                {
                username,"get"
                });
        o.parse(skin);
        return o;
    }
    
    public static void main(String[] args)
    {
        ArrayList<String> keys = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();
        keys.add("request");
        values.add("check");
        keys.add("username");
        values.add("jglrxavpok");
        keys.add("password");
        values.add("jglrxavpok");
        String s = doLexaRequest("login", keys, values);
        System.out.println(s);
    }
}

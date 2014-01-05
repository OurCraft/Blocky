package org.jglrxavpok.blocky.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;

import org.jglrxavpok.blocky.BlockyMain;

public class Lang
{

	private static String	language = "en_UK";
	private static ArrayList<String>	loaded = new ArrayList<String>();
	private static HashMap<String, Hashtable<?, ?>> langs = new HashMap<String, Hashtable<?, ?>>();

	public static void load()
	{
		load("fr_FR");
		load("en_UK");
//		load("es_ES");
//		load("ger_GER");
//		load("nl_NL");
		load("bitrott");
	}
	
	public static void setCurrentLanguage(String id)
	{
		language = id;
	}
	
	public static String[] getLoadedLanguages()
	{
		return langs.keySet().toArray(new String[0]);
	}
	
	public static String getCurrentLanguage()
	{
		return language;
	}
	
	public static String getLocalized(String txt, String language)
	{
		if(!loaded.contains(language))
		{
			load(language);
		}
		String s = (String) langs.get(language).get(txt);
		if(s != null)
		{
			try
			{
				return new String(s.getBytes(), "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		return txt;
	}

	public static void load(String lang)
	{
		if(loaded.contains(lang))
			loaded.remove(lang);
		BlockyMain.console("Loading language: "+lang);
		Properties props = new Properties();
		try
		{
			if(lang.equals("bitrott"))
				props.load(Lang.class.getResourceAsStream("/assets/lang/fr_FR.lang"));
			else
				props.load(Lang.class.getResourceAsStream("/assets/lang/"+lang+".lang"));
			langs.put(lang, props);
			loaded.add(lang);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static String getLocalized(String string)
	{
		return Lang.getLocalized(string, getCurrentLanguage());
	}
}

package org.jglrxavpok.blocky.utils;

public enum TextFormatting
{

	OBFUSCATED("§k"),
	UNDERLINE("§u"),
	ITALIC("§i"),
	RESET("§r");
	
	private String	s;

	TextFormatting(String s)
	{
		this.s =s ;
	}
	
	public String toString()
	{
		return s;
	}
}

package org.jglrxavpok.blocky.lexa;


public class SkinObject
{

    public int headColor = 0xFFFFBE;
    public int handsColor = 0xFFFFBE;
    public int bodyColor = 0x4800FF;
    public int legsColor = 0x404040;
    public int feetColor = 0x000000;

    public SkinObject()
    {}

    public void parse(String skin)
    {
        if(skin.equals(""))
            return;
        String parts[] = skin.split("/");
        int l = parts.length;
        if(l > 0)
        headColor = part(parts, 0);
        if(l > 1)
        handsColor = part(parts, 1);
        if(l > 2)
        bodyColor = part(parts, 2);
        if(l > 3)
        legsColor = part(parts, 3);
        if(l > 4)
        feetColor = part(parts, 4);
    }
    
    public String toString()
    {
        return headColor+"/"+handsColor+"/"+bodyColor+"/"+legsColor+"/"+feetColor;
    }
    
    private int part(String[] parts, int index)
    {
        if(index >= parts.length || index < 0)
        {
            return 0;
        }
        int i = 0;
        try
        {
            i = Integer.parseInt(parts[index], 16);
        }
        catch(NumberFormatException e)
        {
            e.printStackTrace();
        }
        return i;
    }

    public int getColor(String part)
    {
        if(part.equals("head"))
        {
            return headColor;
        }
        if(part.equals("hands"))
        {
            return handsColor;
        }
        if(part.equals("body"))
        {
            return bodyColor;
        }
        if(part.equals("legs"))
        {
            return legsColor;
        }
        if(part.equals("feet"))
        {
            return feetColor;
        }
        return 0;
    }
    
    
}

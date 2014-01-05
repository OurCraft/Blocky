package org.jglrxavpok.blocky.block;

import java.io.Serializable;

public class BlockInfo implements Serializable
{

    private static final long serialVersionUID = -3610659174623823896L;

    public static final int ID = 0;

    public static final int ATTACK_VALUE = 1;
    
    public String data;
    public int type;
    public int y;
    public int x;

    public BlockInfo(int x, int y, int type, String data)
    {
        this.x = x;
        this.y = y;
        this.type = type;
        this.data = data;
    }
}

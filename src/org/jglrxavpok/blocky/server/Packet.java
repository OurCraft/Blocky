package org.jglrxavpok.blocky.server;

import java.io.Serializable;

public class Packet implements Serializable
{

    private static final long serialVersionUID = -8863066537098218591L;
    
    public String name;
    public byte[] data;
//    public String playerName = null;
    
    public Packet(String name, byte[] data)
    {
        this.name = name;
        this.data = data;
    }
}

package org.jglrxavpok.blocky.server;

import com.esotericsoftware.kryonet.Connection;

import org.jglrxavpok.opengl.TextFormatting;

public class BlockyClient
{

    private int clientID;
    private String name;
    private Connection connection;

    public BlockyClient(int id, String name, Connection c)
    {
        this.clientID = id;
        this.name = TextFormatting.escapeString(name);
        this.connection = c;
    }

    public String getName()
    {
        return name;
    }
    
    public int getID()
    {
        return clientID;
    }
    
    public Connection getConnection()
    {
        return connection;
    }
}

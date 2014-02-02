package org.jglrxavpok.blocky.server;

import com.esotericsoftware.kryonet.Connection;

public class BlockyClient
{

    private int clientID;
    private String name;
    private Connection connection;

    public BlockyClient(int id, String name, Connection c)
    {
        this.clientID = id;
        this.name = name;
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

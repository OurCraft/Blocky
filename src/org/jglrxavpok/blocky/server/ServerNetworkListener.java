package org.jglrxavpok.blocky.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ServerNetworkListener extends Listener
{

    public void connected(Connection c)
    {
        System.out.println("Connection from: "+c.getRemoteAddressTCP());
    }
    
    public void disconnected(Connection c)
    {
        
    }
    
    public void received(Connection c, Object o)
    {
        
    }
}

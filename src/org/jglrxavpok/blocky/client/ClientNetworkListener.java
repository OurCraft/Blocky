package org.jglrxavpok.blocky.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientNetworkListener extends Listener
{

    public void connected(Connection c)
    {
        System.out.println("Client connected to: "+c.getRemoteAddressTCP());
    }
    
    public void disconnected(Connection c)
    {
        
    }
    
    public void received(Connection c, Object o)
    {
        
    }
}


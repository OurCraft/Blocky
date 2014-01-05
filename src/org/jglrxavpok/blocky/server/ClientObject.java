package org.jglrxavpok.blocky.server;

import java.net.InetAddress;
import java.util.ArrayList;

public class ClientObject
{

    private static ArrayList<ClientObject> clients = new ArrayList<ClientObject>();
    private InetAddress address;
    private int port;
    public String playerName = null;

    public ClientObject(InetAddress address, int port)
    {
        this.address = address;
        this.port = port;
    }
    
    public InetAddress getAddress()
    {
        return address;
    }
    
    public int getPort()
    {
        return port;
    }
    
    public boolean equals(Object o)
    {
        if(o instanceof ClientObject)
        {
            ClientObject c = (ClientObject)o;
            return c.address.equals(address) && c.port == port;
        }
        return false;
    }

    public static ClientObject get(InetAddress address2, int port2)
    {
        for(int i = 0;i<clients.size();i++)
        {
            ClientObject c = clients.get(i);
            if(c != null && c.address.equals(address2) && c.port == port2)
                return c;
        }
        ClientObject o = new ClientObject(address2,port2);
        clients.add(o);
        return o;
    }
}

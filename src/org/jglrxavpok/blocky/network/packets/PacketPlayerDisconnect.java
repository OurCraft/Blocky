package org.jglrxavpok.blocky.network.packets;

public class PacketPlayerDisconnect extends PacketMessage
{

    PacketPlayerDisconnect(){}
    
    public PacketPlayerDisconnect(String playerName)
    {
        super("PlayerDisconnection", playerName);
    }
}

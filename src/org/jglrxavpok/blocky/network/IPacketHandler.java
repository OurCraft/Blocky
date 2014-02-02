package org.jglrxavpok.blocky.network;

import com.esotericsoftware.kryonet.Connection;

import org.jglrxavpok.blocky.network.packets.Packet;

public interface IPacketHandler
{
    public void handlePacket(Packet p, Connection c);
}

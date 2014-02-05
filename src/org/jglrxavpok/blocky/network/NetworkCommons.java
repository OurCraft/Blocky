package org.jglrxavpok.blocky.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;

import org.jglrxavpok.blocky.network.packets.Packet;
import org.jglrxavpok.blocky.network.packets.PacketBlockUpdate;
import org.jglrxavpok.blocky.network.packets.PacketChat;
import org.jglrxavpok.blocky.network.packets.PacketDisconnect;
import org.jglrxavpok.blocky.network.packets.PacketKeepAlive;
import org.jglrxavpok.blocky.network.packets.PacketKick;
import org.jglrxavpok.blocky.network.packets.PacketMessage;
import org.jglrxavpok.blocky.network.packets.PacketRequestChunk;
import org.jglrxavpok.blocky.network.packets.PacketWorldChunk;

public class NetworkCommons
{

    public static void registerClassesFor(EndPoint p)
    {
        Kryo kryo = p.getKryo();
        kryo.register(Packet.class);
        kryo.register(PacketMessage.class);
        kryo.register(PacketChat.class);
        kryo.register(PacketKick.class);
        kryo.register(PacketBlockUpdate.class);
        kryo.register(PacketKeepAlive.class);
        kryo.register(PacketDisconnect.class);
        kryo.register(PacketRequestChunk.class);
        kryo.register(PacketWorldChunk.class);
        kryo.register(String.class);
        kryo.register(byte[].class);
    }
    
    public static void sendPacketToExcept(Packet packet, boolean udp, Connection except, Connection... list)
    {
        for(Connection c : list)
        {
            if(c == except)
                continue;
            if(udp)
            {
                c.sendUDP(packet);
            }
            else
            {
                c.sendTCP(packet);
            }
        }
    }
    
    public static void sendPacketToExcept(Packet packet, boolean udp, Connection except, Iterable<Connection> list)
    {
        for(Connection c : list)
        {
            if(c == except)
                continue;
            if(udp)
            {
                c.sendUDP(packet);
            }
            else
            {
                c.sendTCP(packet);
            }
        }
    }
    
    public static void sendPacketTo(Packet packet, boolean udp, Connection... list)
    {
        for(Connection c : list)
        {
            if(udp)
            {
                c.sendUDP(packet);
            }
            else
            {
                c.sendTCP(packet);
            }
        }
    }
    
    public static void sendPacketTo(Packet packet, boolean udp, Iterable<Connection> list)
    {
        for(Connection c : list)
        {
            if(udp)
            {
                c.sendUDP(packet);
            }
            else
            {
                c.sendTCP(packet);
            }
        }
    }

    public static void sendPacketTo(Packet packet, boolean udp, Connection c)
    {
        if(udp)
        {
            c.sendUDP(packet);
        }
        else
        {
            c.sendTCP(packet);
        }
    }
}

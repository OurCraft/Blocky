package org.jglrxavpok.blocky.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;

import org.jglrxavpok.blocky.network.packets.Packet;
import org.jglrxavpok.blocky.network.packets.PacketBlockUpdate;
import org.jglrxavpok.blocky.network.packets.PacketChat;
import org.jglrxavpok.blocky.network.packets.PacketDisconnect;
import org.jglrxavpok.blocky.network.packets.PacketEntitiesState;
import org.jglrxavpok.blocky.network.packets.PacketEntityState;
import org.jglrxavpok.blocky.network.packets.PacketHotbarSelection;
import org.jglrxavpok.blocky.network.packets.PacketKeepAlive;
import org.jglrxavpok.blocky.network.packets.PacketKick;
import org.jglrxavpok.blocky.network.packets.PacketMessage;
import org.jglrxavpok.blocky.network.packets.PacketPlayerConnection;
import org.jglrxavpok.blocky.network.packets.PacketPlayerDisconnect;
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
        kryo.register(PacketEntityState.class);
        kryo.register(PacketDisconnect.class);
        kryo.register(PacketRequestChunk.class);
        kryo.register(PacketWorldChunk.class);
        kryo.register(PacketPlayerConnection.class);
        kryo.register(PacketPlayerDisconnect.class);
        kryo.register(PacketHotbarSelection.class);
        kryo.register(PacketEntitiesState.class);
        kryo.register(String.class);
        kryo.register(byte[].class);
    }
    
    public static void sendPacketToExcept(final Packet packet, final boolean udp, final Connection except, final Connection... list)
    {
        new Thread()
        {
            public void run()
            {
                packet.compressData();
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
        }.start();
    }
    
    public static void sendPacketToExcept(final Packet packet, final boolean udp, final Connection except, final Iterable<Connection> list)
    {
        new Thread()
        {
            public void run()
            {
                packet.compressData();
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
        }.start();
    }
    
    public static void sendPacketTo(final Packet packet, final boolean udp, final Connection... list)
    {
        new Thread()
        {
            public void run()
            {
                packet.compressData();
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
        }.start();
    }
    
    public static void sendPacketTo(final Packet packet, final boolean udp, final Iterable<Connection> list)
    {
        new Thread()
        {
            public void run()
            {
                packet.compressData();
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
        }.start();
    }

    public static void sendPacketTo(final Packet packet, final boolean udp, final Connection c)
    {
        new Thread()
        {
            public void run()
            {
                packet.compressData();
                if(udp)
                {
                    c.sendUDP(packet);
                }
                else
                {
                    c.sendTCP(packet);
                }
            }
        }.start();
    }
}

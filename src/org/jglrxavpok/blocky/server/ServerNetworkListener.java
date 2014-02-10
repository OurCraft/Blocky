package org.jglrxavpok.blocky.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import org.jglrxavpok.blocky.network.ConnectionType;
import org.jglrxavpok.blocky.network.EntityState;
import org.jglrxavpok.blocky.network.NetworkCommons;
import org.jglrxavpok.blocky.network.packets.Packet;
import org.jglrxavpok.blocky.network.packets.PacketChat;
import org.jglrxavpok.blocky.network.packets.PacketPlayerConnection;
import org.jglrxavpok.blocky.network.packets.PacketPlayerDisconnect;

public class ServerNetworkListener extends Listener
{

    private ArrayList<Integer> queuingConnections = new ArrayList<Integer>();
    private ArrayList<BlockyClient> ingameConnections = new ArrayList<BlockyClient>();
    private Server server;
    private ServerPacketHandler packetHandler;
    
    public ServerNetworkListener(Server s)
    {
        this.server = s;
        packetHandler = new ServerPacketHandler(server, this);
    }
    
    public void connected(Connection c)
    {
        System.out.println("Connection from: "+c.getRemoteAddressTCP());
        queuingConnections.add(c.getID());
        NetworkCommons.sendPacketTo(new Packet("AuthPacket", null), false, c);
    }
    
    public void idle(Connection c)
    {
        
    }
    
    public void disconnected(Connection c)
    {
        if(queuingConnections.contains((Integer)c.getID()))
        {
            queuingConnections.remove((Integer)c.getID());
        }
        else
        {
            BlockyClient client = getClientByID(c.getID());
            if(client != null)
            {
                ingameConnections.remove(client);
                BlockyMainServer.console("Player "+client.getName()+" just disconnected.");
                NetworkCommons.sendPacketToExcept(new PacketPlayerDisconnect(client.getName()), false, c, getIngameConnectionsFromClients());
            }
        }
    }
    
    public BlockyClient getClientByID(int id)
    {
        for(BlockyClient c : ingameConnections)
        {
            if(c.getID() == id)
                return c;
        }
        return null;
    }

    public void received(Connection c, Object o)
    {
        if(o instanceof Packet)
        {
            Packet packet = ((Packet) o);
            packet.decompressData();
            if(packet.name.equals("Ping+infos request"))
            {
                sendServerInfos(c);
            }
            else if(packet.name.equals("AuthPacket") && queuingConnections.contains(c.getID()))
            {
                try
                {
                    if(packet.data == null)
                        return;
                    ByteArrayInputStream rawData = new ByteArrayInputStream(packet.data);
                    DataInputStream in = new DataInputStream(rawData);
                    int i = in.readInt();
                    if(!(i < 0 || i >= ConnectionType.values().length))
                    {
                        ConnectionType type = ConnectionType.values()[i];
                        if(type == ConnectionType.INFOS)
                        {
                            sendServerInfos(c);
                            queuingConnections.remove(c.getID());
                            c.close();
                        }
                        else if(type == ConnectionType.GAME)
                        {
                            queuingConnections.remove((Integer)c.getID());
                            BlockyClient client = new BlockyClient(c.getID(), in.readUTF(), c);
                            ingameConnections.add(client);
                            BlockyMainServer.console("Player "+client.getName()+" just connected!");
                            NetworkCommons.sendPacketToExcept(new PacketChat("Player "+client.getName()+" just connected!"), false, c, getIngameConnectionsFromClients());
                            NetworkCommons.sendPacketTo(new PacketPlayerConnection(BlockyMainServer.world.getCorrectID(), EntityState.createFromEntity(BlockyMainServer.world.getPlayerByName(client.getName()))), false, c);
                        }
                        else
                        {
                            BlockyMainServer.console("Don't know connection type :o");
                        }
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                packetHandler.handlePacket(packet, c);
            }
        }
    }
    
    public Connection[] getIngameConnectionsFromClients()
    {
        Connection[] connections = new Connection[ingameConnections.size()];
        int i = 0;
        for(BlockyClient client : ingameConnections)
        {
            connections[i++] = client.getConnection();
        }
        return connections;
    }

    public void sendServerInfos(Connection c)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);
            int nbrPlayers = ingameConnections.size()+1;
            int maxPlayers = 0;
            String serverName = "Test server";
            out.writeInt(nbrPlayers);
            out.writeInt(maxPlayers);
            out.writeUTF(serverName);
            out.close();
            baos.close();
            Packet response = new Packet("Ping+infos response", baos.toByteArray());
            NetworkCommons.sendPacketTo(response, false, c);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public BlockyClient[] getClients()
    {
        return ingameConnections.toArray(new BlockyClient[0]);
    }

    public void removeClient(BlockyClient client)
    {
        ingameConnections.remove(client);
    }
}

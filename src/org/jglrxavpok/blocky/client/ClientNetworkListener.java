package org.jglrxavpok.blocky.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.entity.EntityPlayerClientMP;
import org.jglrxavpok.blocky.network.ConnectionType;
import org.jglrxavpok.blocky.network.NetworkCommons;
import org.jglrxavpok.blocky.network.packets.Packet;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldGenerator.WorldType;

public class ClientNetworkListener extends Listener
{

    private boolean pingRequest;
    private long pingSentAt;
    private ClientPacketHandler packetHandler;
    private World world;
    private Client client;
    private boolean seemsConnected;
    private EntityPlayerClientMP player;

    public ClientNetworkListener(Client c)
    {
        this(false, c);
    }
    
    public ClientNetworkListener(boolean pingRequest, Client c)
    {
        client = c;
        world = new World("Client");
        world.isRemote = true;
        world.worldType = WorldType.CLIENT;
        this.pingRequest = pingRequest;
        this.packetHandler = new ClientPacketHandler(this);
    }

    public World getWorld()
    {
        return world;
    }
    
    public void idle(Connection c)
    {
//        if(seemsConnected)
//        {
//            NetworkCommons.sendPacketTo(new PacketKeepAlive(), false, c);
//        }
    }
    
    public void connected(Connection c)
    {
        System.out.println("Client connected to: "+c.getRemoteAddressTCP());
//        if(pingRequest)
        {
            NetworkCommons.sendPacketTo(new Packet("Ping+infos request", null), false, c);
            pingSentAt = System.currentTimeMillis();
        }
        BlockyMain.instance.loadLevel(world);
    }
    
    public void disconnected(Connection c)
    {
        System.err.println("Disconnected");
    }
    
    public void received(Connection c, Object o)
    {
        if(o instanceof Packet)
        {
            Packet p = ((Packet)o);
            p.decompressData();
//            BlockyMain.console("Received packet "+p.name);
            if(p.name.equals("Ping+infos response"))
            {
                try
                {
                    long ping = System.currentTimeMillis()-this.pingSentAt;
                    ByteArrayInputStream input = new ByteArrayInputStream(p.data);
                    DataInputStream in = new DataInputStream(input);
                    int nbrPlayers = in.readInt();
                    int maxPlayers = in.readInt();
                    String serverName = in.readUTF();
                    in.close();
                    input.close();
                    System.out.println("Ping request on server: "+serverName);
                    System.out.println("===================================");
                    System.out.println("Number of connected players: "+nbrPlayers);
                    System.out.println("Max players: "+maxPlayers);
                    System.out.println("Ping: "+ping);
                    System.out.println("===================================");
                    if(pingRequest)
                        c.close();
                    pingSentAt = System.currentTimeMillis();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                if(p.name.equals("AuthPacket"))
                {
                    NetworkCommons.sendPacketTo(createAuthPacket(pingRequest ? ConnectionType.INFOS : ConnectionType.GAME), false, c);
                    seemsConnected = true;
                }
                else
                {
                    packetHandler.handlePacket(p, c);
                }
            }
        }
    }
    
    private Packet createAuthPacket(ConnectionType t)
    {
        Packet p = new Packet("AuthPacket", null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        try
        {
            out.writeInt(t.ordinal());
            String s = BlockyMain.username;
            if(s == null)
                s = "Player";
            out.writeUTF(s);
            out.close();
            baos.close();
            p.data = baos.toByteArray();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return p;
    }

    public static void main(String[] args)
    {
        try
        {
            Client c = new Client();
            NetworkCommons.registerClassesFor(c);
            c.addListener(new ClientNetworkListener(true, c));
            c.start();
            c.connect(5000, "localhost", 35565);
            c.sendTCP(new Packet("Ping+infos request", null));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Connection getClientConnection()
    {
        return client;
    }

    public EntityPlayerClientMP getPlayer()
    {
        return player;
    }

    public void setPlayer(EntityPlayerClientMP player)
    {
        this.player = player;
    }
}


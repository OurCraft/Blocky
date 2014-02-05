package org.jglrxavpok.blocky.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import org.jglrxavpok.blocky.block.BlockInfo;
import org.jglrxavpok.blocky.network.IPacketHandler;
import org.jglrxavpok.blocky.network.NetworkCommons;
import org.jglrxavpok.blocky.network.packets.Packet;
import org.jglrxavpok.blocky.network.packets.PacketBlockUpdate;
import org.jglrxavpok.blocky.network.packets.PacketChat;
import org.jglrxavpok.blocky.network.packets.PacketDisconnect;
import org.jglrxavpok.blocky.network.packets.PacketRequestChunk;
import org.jglrxavpok.blocky.network.packets.PacketWorldChunk;

public class ServerPacketHandler implements IPacketHandler
{

    private Server server;
    private ServerNetworkListener listener;

    public ServerPacketHandler(Server server, ServerNetworkListener listener)
    {
        this.server = server;
        this.listener = listener;
    }

    @Override
    public void handlePacket(Packet p, Connection c)
    {
        if(p instanceof PacketRequestChunk)
        {
            int id = ((PacketRequestChunk)p).getChunkID();
            PacketWorldChunk packet = new PacketWorldChunk(BlockyMainServer.world.getChunkByID(id, true));
            NetworkCommons.sendPacketTo(packet, false, c);
        }
        else if(p instanceof PacketDisconnect)
        {
            BlockyClient client = listener.getClientByID(c.getID());
            NetworkCommons.sendPacketToExcept(new PacketChat("Player "+client.getName()+" just disconnected."), false, client.getConnection(), listener.getIngameConnectionsFromClients());
            BlockyMainServer.console("Player "+client.getName()+" just disconnected.");
            listener.removeClient(client);
            client.getConnection().close();
        }
        else if(p instanceof PacketBlockUpdate)
        {
            BlockInfo info = ((PacketBlockUpdate) p).getBlockInfo();
            if(info != null)
            {
                BlockyMainServer.world.handleBlockUpdate(info);
                NetworkCommons.sendPacketTo(p, false, listener.getIngameConnectionsFromClients());
            }
        }
    }

}

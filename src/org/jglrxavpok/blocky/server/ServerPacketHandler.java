package org.jglrxavpok.blocky.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import org.jglrxavpok.blocky.block.BlockInfo;
import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.entity.EntityPlayerClientMP;
import org.jglrxavpok.blocky.entity.EntityPlayerMP;
import org.jglrxavpok.blocky.network.EntityState;
import org.jglrxavpok.blocky.network.IPacketHandler;
import org.jglrxavpok.blocky.network.NetworkCommons;
import org.jglrxavpok.blocky.network.packets.Packet;
import org.jglrxavpok.blocky.network.packets.PacketBlockUpdate;
import org.jglrxavpok.blocky.network.packets.PacketDisconnect;
import org.jglrxavpok.blocky.network.packets.PacketEntityState;
import org.jglrxavpok.blocky.network.packets.PacketHotbarSelection;
import org.jglrxavpok.blocky.network.packets.PacketPlayerConnection;
import org.jglrxavpok.blocky.network.packets.PacketPlayerDisconnect;
import org.jglrxavpok.blocky.network.packets.PacketRequestChunk;
import org.jglrxavpok.blocky.network.packets.PacketWorldChunk;
import org.jglrxavpok.blocky.utils.Reflect;
import org.jglrxavpok.storage.TaggedStorageChunk;

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
        else if(p instanceof PacketHotbarSelection)
        {
            float index = Float.parseFloat(((PacketHotbarSelection) p).getMessage());
            BlockyClient client = listener.getClientByID(c.getID());
            if(client != null)
            {
                EntityPlayer player = BlockyMainServer.world.getPlayerByName(client.getName());
                if(player != null)
                {
                    player.invIndex = index;
                    EntityState state = new EntityState();
                    TaggedStorageChunk chunk = new TaggedStorageChunk("PlayerHotbarSelectionUpdate_"+player.entityID);
                    chunk.setFloat("invIndex", index);
                    state.setEntityState(chunk);
                    NetworkCommons.sendPacketToExcept(new PacketEntityState(player.entityID, state), false, c, listener.getIngameConnectionsFromClients());
                }
            }
        }
        else if(p instanceof PacketDisconnect)
        {
            BlockyClient client = listener.getClientByID(c.getID());
            NetworkCommons.sendPacketToExcept(new PacketPlayerDisconnect(client.getName()), false, c, listener.getIngameConnectionsFromClients());
            BlockyMainServer.console("Player "+client.getName()+" just disconnected!");
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
        else if(p instanceof PacketEntityState && !(p instanceof PacketPlayerConnection))
        {
            TaggedStorageChunk chunk = ((PacketEntityState)p).getState();
            if(chunk != null)
            {
                NetworkCommons.sendPacketToExcept(p, false, c, listener.getIngameConnectionsFromClients());
                String s = chunk.getChunkName().replaceFirst("Entity_", "");
                String s1 = s.substring(0, s.lastIndexOf("_"));
                try
                {
                    Class<?> class1 = Class.forName(s1);
                    if(Reflect.isInstanceof(class1, EntityPlayer.class))
                    {
                        Entity e = BlockyMainServer.world.getEntityByID(Integer.parseInt(s.substring(s.lastIndexOf("_")+1)));
                        if(e != null)
                            e.readFromChunk(chunk);
                        else
                        {
                            try
                            {
                                if(class1 == EntityPlayerMP.class)
                                    class1 = EntityPlayerClientMP.class;
                                e = (Entity) class1.newInstance();
                                e.entityID = Integer.parseInt(s.substring(s.lastIndexOf("_")+1));
                                e.readFromChunk(chunk);
                                BlockyMainServer.world.addEntityWithID(e.entityID, e);
                            }
                            catch (Exception e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}

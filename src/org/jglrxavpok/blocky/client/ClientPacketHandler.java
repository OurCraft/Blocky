package org.jglrxavpok.blocky.client;

import java.util.List;

import com.esotericsoftware.kryonet.Connection;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.block.BlockInfo;
import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.entity.EntityPlayerClientMP;
import org.jglrxavpok.blocky.network.IPacketHandler;
import org.jglrxavpok.blocky.network.packets.Packet;
import org.jglrxavpok.blocky.network.packets.PacketBlockUpdate;
import org.jglrxavpok.blocky.network.packets.PacketChat;
import org.jglrxavpok.blocky.network.packets.PacketEntitiesState;
import org.jglrxavpok.blocky.network.packets.PacketEntityState;
import org.jglrxavpok.blocky.network.packets.PacketKick;
import org.jglrxavpok.blocky.network.packets.PacketPlayerConnection;
import org.jglrxavpok.blocky.network.packets.PacketPlayerDisconnect;
import org.jglrxavpok.blocky.network.packets.PacketWorldChunk;
import org.jglrxavpok.blocky.world.WorldChunk;
import org.jglrxavpok.storage.TaggedStorageChunk;

public class ClientPacketHandler implements IPacketHandler
{

    private ClientNetworkListener listener;

    public ClientPacketHandler(ClientNetworkListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void handlePacket(Packet p, Connection c)
    {
        if(p instanceof PacketPlayerDisconnect)
        {
            String playerName = ((PacketPlayerDisconnect)p).getMessage();
            String s = "Player "+playerName+" just disconnected!";
            BlockyMain.console(s);
            BlockyMain.instance.addToChat(s);
            listener.getWorld().removeEntity(listener.getWorld().getPlayerByName(playerName));
        }
        else if(p instanceof PacketKick)
        {
            String reason = ((PacketKick) p).getMessage();
            BlockyMain.console("You have been kicked because of: \""+reason+"\"");
            c.close();
        }
        else if(p instanceof PacketChat)
        {
            String msg = ((PacketChat)p).getMessage();
            BlockyMain.instance.addToChat(msg);
            BlockyMain.console(msg);
        }
        else if(p instanceof PacketPlayerConnection)
        {
            TaggedStorageChunk chunk = ((PacketPlayerConnection) p).getState();
            EntityPlayerClientMP player = new EntityPlayerClientMP();
            player.move(0*Block.BLOCK_WIDTH, 250*Block.BLOCK_HEIGHT);
            if(chunk != null)
            {
                player.readFromChunk(chunk);
            }
            listener.setPlayer(player);
            listener.getWorld().addEntityWithID(((PacketPlayerConnection) p).getEntityID(), player);
            listener.getWorld().centerOfTheWorld = player;
        }
        else if(p instanceof PacketEntitiesState)
        {
            List<?>[] lists = ((PacketEntitiesState) p).getStates();
            for(int i = 0;i<lists[0].size();i++)
            {
                Entity e = listener.getWorld().getEntityByID((Integer) lists[0].get(i));
                TaggedStorageChunk chunk = (TaggedStorageChunk) lists[1].get(i);
                if(e != null)
                {
                    e.readFromChunk(chunk);
                }
                else
                {
                    try
                    {
                        String s = chunk.getChunkName().replaceFirst("Entity_", "");
                        String s1 = s.substring(0, s.lastIndexOf("_"));
                        Class<?> class1 = Class.forName(s1);
                        if(class1 != EntityPlayerClientMP.class)
                            e = (Entity) class1.newInstance();
                        else
                            e = (Entity) EntityPlayer.class.newInstance();
                        e.readFromChunk(chunk);
                        listener.getWorld().addEntityWithID((Integer) lists[0].get(i), e);
                    }
                    catch (Exception e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }
        }
        else if(p instanceof PacketWorldChunk)
        {
            WorldChunk chunk = ((PacketWorldChunk)p).readChunk(listener.getWorld());
            listener.getWorld().putChunk(chunk);
        }
        else if(p instanceof PacketBlockUpdate)
        {
            BlockInfo info = ((PacketBlockUpdate) p).getBlockInfo();
            if(info != null)
                listener.getWorld().handleBlockUpdate(info);
        }
        else if(p instanceof PacketEntityState && !(p instanceof PacketPlayerConnection))
        {
            TaggedStorageChunk chunk = ((PacketEntityState)p).getState();
            if(chunk != null)
            {
                String s = chunk.getChunkName().replaceFirst("Entity_", "");
                String s1 = s.substring(0, s.lastIndexOf("_"));
                try
                {
                    {
                        Entity e = listener.getWorld().getEntityByID(Integer.parseInt(s.substring(s.lastIndexOf("_")+1)));
                        if(e != null)
                        {
                            e.readFromChunk(chunk);
                        }
                        else
                        {
                            try
                            {
                                Class<?> class1 = Class.forName(s1);
                                if(class1 != EntityPlayerClientMP.class)
                                    e = (Entity) class1.newInstance();
                                else
                                    e = (Entity) EntityPlayer.class.newInstance();
                                e.readFromChunk(chunk);
                                listener.getWorld().addEntityWithID(Integer.parseInt(s.substring(s.lastIndexOf("_")+1)), e);
                            }
                            catch (Exception e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}

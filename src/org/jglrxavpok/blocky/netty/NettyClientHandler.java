package org.jglrxavpok.blocky.netty;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.block.BlockInfo;
import org.jglrxavpok.blocky.client.ClientPlayerInputHandler;
import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.entity.EntityPlayerSP;
import org.jglrxavpok.blocky.gui.UIMainMenu;
import org.jglrxavpok.blocky.server.Packet;
import org.jglrxavpok.blocky.server.PacketBlockInfos;
import org.jglrxavpok.blocky.server.PacketChatContent;
import org.jglrxavpok.blocky.server.PacketEntityUpdate;
import org.jglrxavpok.blocky.server.PacketPlayer;
import org.jglrxavpok.blocky.server.PacketServerInfos;
import org.jglrxavpok.blocky.server.PacketTime;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.ui.UIErrorMenu;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldChunk;
import org.jglrxavpok.blocky.world.WorldGenerator.WorldType;
import org.jglrxavpok.storage.TaggedStorageChunk;

public class NettyClientHandler extends SimpleChannelHandler
{

    public static NettyClientHandler current;
    public Channel serverChannel;
    private boolean ping;
    private World level;
    private boolean waitingForAnwser;

    public NettyClientHandler(boolean pingOnly)
    {
        this.ping = pingOnly;
        current = this;
        if(!ping)
        {
        }
    }
    
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
    {
        serverChannel = e.getChannel();
        if(ping)
        {
            try
            {
                NettyCommons.sendPacket(new Packet("Get server infos", null), serverChannel);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
        else
        {
            waitingForAnwser = true;
            try
            {
                NettyCommons.sendPacket(new Packet("Connection", null), serverChannel);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
    }
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) 
    {
        try
        {
            Packet p = NettyCommons.readPacket((ChannelBuffer)e.getMessage());
            if(p instanceof PacketServerInfos)
            {
                PacketServerInfos infos = (PacketServerInfos)p;
                if(ping)
                {
                    e.getChannel().disconnect();
                    e.getChannel().close();
                }
                System.out.println("Ping server "+infos.serverName+"->"+infos.playerCount+"/"+infos.maxPlayers+" "+(System.currentTimeMillis()-infos.pingTime));
            }
            else if(p.name.startsWith("Connected "))
            {
                if(waitingForAnwser)
                {
                    waitingForAnwser = false;
                    level = new World("Server handled level");
                    level.worldType = WorldType.CLIENT;
                    EntityPlayerSP playerSp = new EntityPlayerSP();
                    playerSp.move(25000*Block.BLOCK_WIDTH, 252*Block.BLOCK_HEIGHT);
                    level.addEntity(playerSp);
                    playerSp.entityID = Integer.parseInt(p.name.replace("Connected ", ""));
                    level.centerOfTheWorld = playerSp;
                    BlockyMain.instance.loadLevel(level);
                    BlockyMain.instance.addInputProcessor(new ClientPlayerInputHandler(playerSp));
                    UI.displayMenu(null);
                }
            }
            dispatchPacket(p);
        }
        catch (ClassNotFoundException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e1)
        {
            if(e1 instanceof EOFException)
            {
                ;
            }
            else if(e1.getMessage() != null && e1.getMessage().contains("invalid stream header"))
                ;
            else
                e1.printStackTrace();
        }
    }

    private void dispatchPacket(Packet p)
    {
        System.out.println(p.name);
        if(p.name.startsWith("Response "))
        {
            String responseType = p.name.replaceFirst("Response ", "");
            if(responseType.startsWith("ChunkContent "))
            {
                int chunkID = Integer.parseInt(responseType.replaceFirst("ChunkContent ", ""));
                WorldChunk chunk = new WorldChunk(level, chunkID);
                try
                {
                    chunk.readStorageChunk(BlockyMain.saveSystem.readChunk(p.data));
                    level.putChunk(chunk);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if(p instanceof PacketTime)
        {
            level.time = Long.parseLong(p.name.replace("Time: ", ""));
        }
        else if(p instanceof PacketEntityUpdate)
        {
            try
            {
                TaggedStorageChunk chunk = Packet.tagSystem.readChunk(p.data);
                int entityID = Integer.parseInt(chunk.getChunkName().substring(chunk.getChunkName().length()-1, chunk.getChunkName().length()));
                Entity e = level.getEntityByID(entityID);
                if(e == null)
                {
                    try
                    {
                        String entityClass = chunk.getString("class");
                        e = (Entity) Class.forName(entityClass).newInstance();
                        e.readFromChunk(chunk);
                        level.addEntity(e);
                        e.entityID = entityID;
                    }
                    catch(Exception e1)
                    {
                        ;
                    }
                }
                else
                {
                    if(e instanceof EntityPlayer)
                        ;
                    else
                        e.readFromChunk(chunk);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if(p instanceof PacketBlockInfos)
        {
            level.handlingChanges = true;
            PacketBlockInfos infosPacket = (PacketBlockInfos)p;
            if(infosPacket.id == BlockInfo.ID)
            {
                level.setBlock(infosPacket.x, infosPacket.y, infosPacket.data);
            }
            else if(infosPacket.id == BlockInfo.ATTACK_VALUE)
            {
                try
                {
                    String parts[] = infosPacket.data.split("\001");
                    int value = Integer.parseInt(parts[0]);
                    level.setAttackValue(infosPacket.x, infosPacket.y, value, parts[1]);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            level.handlingChanges = false;
        }
        else if(p instanceof PacketChatContent)
        {
            String content = new String(p.data);
            BlockyMain.instance.addToChat(content);
        }
        else if(p instanceof PacketPlayer)
        {
            try
            {
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(p.data));
                float x = in.readFloat();
                float y = in.readFloat();
                float vx = in.readFloat();
                float vy = in.readFloat();
                String n = in.readUTF();
                EntityPlayer player = level.getPlayerByName(n);
                if(player == null)
                {
                    player=new EntityPlayer();
                    level.addEntity(player);
                }
                player.x = x;
                player.y = y;
                player.vx = vx;
                player.vy = vy;
                player.username = n;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) 
    {
        e.getCause().printStackTrace();
        UI.displayMenu(new UIErrorMenu(new UIMainMenu(), null, e.getCause()));
        BlockyMain.instance.loadLevel(null);
        Channel ch = e.getChannel();
        ch.close();
    }
    
}

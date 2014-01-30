package org.jglrxavpok.blocky.netty;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import org.jglrxavpok.blocky.block.BlockInfo;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.server.Packet;
import org.jglrxavpok.blocky.server.PacketBlockInfos;
import org.jglrxavpok.blocky.server.PacketChatContent;
import org.jglrxavpok.blocky.server.PacketPlayer;
import org.jglrxavpok.blocky.server.PacketServerInfos;
import org.jglrxavpok.blocky.world.World;

public class NettyServerHandler extends SimpleChannelHandler
{

    private String serverName;
    private World level;
    
    public NettyServerHandler(String serverName)
    {
        this.serverName = serverName;
    }
    
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
    {
    }
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) 
    {
        boolean flag = true;
        try
        {
            Packet p = NettyCommons.readPacket((ChannelBuffer)e.getMessage());
            if(p.name.equals("Get server infos"))
            {
                flag = false;
                NettyCommons.sendPacket(new PacketServerInfos(serverName, 0, 0), e.getChannel());
            }
            else if(p.name.equals("Connection"))
            {
                NettyCommons.sendPacket(new Packet("Connected "+level.entityID++,null), e.getChannel());
            }
            else
                dispatchPacket(p, e.getChannel());
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

    private void dispatchPacket(Packet packetReceived, Channel client)
    {
        if(packetReceived.name.startsWith("Request "))
        {
            handleRequestPacket(packetReceived, client);
        }
        else if(packetReceived instanceof PacketChatContent)
        {
            this.sendPacketToAllExcept(packetReceived, null);
        }
        else if(packetReceived instanceof PacketBlockInfos)
        {
            level.handlingChanges = true;
            PacketBlockInfos infosPacket = (PacketBlockInfos)packetReceived;
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
            try
            {
                NettyCommons.sendPacket(new PacketChatContent("Testing chat"), client);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            sendPacketToAllExcept(packetReceived, null);
        }
        else if(packetReceived instanceof PacketPlayer)
        {
            try
            {
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(packetReceived.data));
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
                sendPacketToAllExcept(packetReceived, client);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private void handleRequestPacket(Packet p, Channel sender)
    {
        String request = p.name.replaceFirst("Request ", "");
        if(request.startsWith("ChunkContent "))
        {
            int chunkID = Integer.parseInt(request.replaceFirst("ChunkContent ", ""));
            try
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(Packet.tagSystem.writeChunk(level.getChunkAt(chunkID*16, 0, true).createStorageChunk()));
                baos.close();
                NettyCommons.sendPacket(new Packet("Response ChunkContent "+chunkID, baos.toByteArray()), sender);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void sendPacketToAllExcept(Packet packetReceived, Channel client)
    {
//        for(int i = 0;i<clients.size();i++)
//        {
//            if(!clients.get(i).equals(client))
//                try
//                {
//                    NettyCommons.sendPacket(packetReceived, clients.get(i));
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) 
    {
        e.getCause().printStackTrace();
        Channel ch = e.getChannel();
        ch.close();
    }
    
}

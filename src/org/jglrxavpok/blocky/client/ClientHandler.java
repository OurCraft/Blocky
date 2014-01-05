package org.jglrxavpok.blocky.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import net.java.games.input.Component;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.block.BlockInfo;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.entity.EntityPlayerSP;
import org.jglrxavpok.blocky.gui.UIPauseMenu;
import org.jglrxavpok.blocky.input.InputProcessor;
import org.jglrxavpok.blocky.items.ItemBlock;
import org.jglrxavpok.blocky.server.Packet;
import org.jglrxavpok.blocky.server.PacketBlockInfos;
import org.jglrxavpok.blocky.server.PacketChatContent;
import org.jglrxavpok.blocky.server.PacketPlayer;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldChunk;
import org.jglrxavpok.blocky.world.WorldGenerator.WorldType;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class ClientHandler
{


    private DatagramSocket client;
    private String address;
    private int port;
    private World level;
    private EntityPlayerSP player;
    private DatagramPacket bufferPacket;
    private InetAddress inetAddress;
    
    public ClientHandler(DatagramSocket socket, String serverAddress, int serverPort)
    {
        this.client = socket;
        try
        {
            this.inetAddress= InetAddress.getByName(serverAddress);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        this.address = serverAddress;
        this.port = serverPort;
        level = new World("Server level");
        level.worldType = WorldType.CLIENT;
        player = new EntityPlayerSP();
        BlockyMain.instance.addInputProcessor(new ClientPlayerInputHandler(player));
        player.world = level;
        level.centerOfTheWorld = player.move(0, 256*Block.BLOCK_HEIGHT);
        level.addEntity(player);
        bufferPacket = new DatagramPacket(new byte[65565], 65565);
        new Thread(new Runnable()
        {
            public void run()
            {
                while(!ClientHandler.this.client.isConnected())
                    pollInput();
            }
        }).start();
    }
    
    public World getLevel()
    {
        return level;
    }
    
    protected void pollInput()
    {
        try
        {
            try
            {
                client.receive(bufferPacket);
                ByteArrayInputStream bais = new ByteArrayInputStream(bufferPacket.getData());
                ObjectInputStream input = new ObjectInputStream(bais);
                Packet packet = (Packet)input.readObject();
                handlePacket(packet);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            if(e instanceof EOFException)
            {
                ;
            }
            else if(e.getMessage().contains("invalid stream header"))
            {
                ;
            }
            else
                e.printStackTrace();
        }
    }

    public void handlePacket(Packet p)
    {
        if(p.name.startsWith("Response "))
            handleResponsePacket(p);
        else if(p instanceof PacketBlockInfos)
        {
            level.handlingChanges = true;
            PacketBlockInfos infosPacket = (PacketBlockInfos)p;
            if(infosPacket.id == BlockInfo.ID)
            {
                level.setBlock(infosPacket.x, infosPacket.y, infosPacket.data);
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

    private void handleResponsePacket(Packet p)
    {
        String response = p.name.replaceFirst("Response ", "");
        if(response.startsWith("ChunkContent "))
        {
            level.handlingChanges = true;
            WorldChunk chunk = new WorldChunk(level, Integer.parseInt(response.replaceFirst("ChunkContent ", "")));
            try
            {
                chunk.readStorageChunk(BlockyMain.saveSystem.readChunk(p.data));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            level.putChunk(chunk);
            level.handlingChanges = false;
        }
    }

    public void sendPacketToServer(Packet p)
    {
        try
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            p.playerName = player.username;
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(p);
            oos.close();
            byte[] data = out.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, inetAddress, port);
            client.send(packet);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

}

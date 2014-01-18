package org.jglrxavpok.blocky.server;

import java.io.IOException;
import java.util.ArrayList;

import org.jboss.netty.channel.Channel;
import org.jglrxavpok.blocky.netty.NettyCommons;
import org.jglrxavpok.blocky.world.World;

public class ThreadUpdateLevel extends Thread
{

    private World world;
    private ArrayList<Channel> clients;
    private long lastTime;
    private int frame;
    private int fps;

    public ThreadUpdateLevel(World w, ArrayList<Channel> clients)
    {
        this.world = w;
        this.clients = clients;
    }
    
    public void run()
    {
        while(true)
        {
            frame++;
            if(frame <= 60)
            {
                world.tick();
                ArrayList<Packet> packets = world.getUpdatePackets();
                for(Packet packet : packets)
                {
                    for(Channel c : clients)
                        try
                        {
                            NettyCommons.sendPacket(packet, c);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                }
            }
            if(System.currentTimeMillis()-lastTime>=1000)
            {
                fps = frame;
                frame = 0;
                lastTime = System.currentTimeMillis();
            }
        }
    }
}

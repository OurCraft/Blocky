package org.jglrxavpok.blocky.server;

import java.util.ArrayList;

import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.network.NetworkCommons;
import org.jglrxavpok.blocky.network.packets.PacketEntitiesState;
import org.jglrxavpok.blocky.world.World;

public class ThreadUpdateServerWorld extends Thread
{

    private World world;
    private ServerNetworkListener listener;
    private int frame;
    private long lastTime;
    private int fps;

    public ThreadUpdateServerWorld(World world, ServerNetworkListener listener)
    {
        super("WorldUpdate");
        this.world = world;
        this.listener = listener;
    }
    
    public void run()
    {
        while(BlockyMainServer.instance.running)
        {
            frame++;
            if(frame <= 60)
            {
                world.tick();
                ArrayList<Entity> ents = new ArrayList<Entity>();
                for(Entity e : world.entities)
                {
                    if(e.needUpdate)
                    {
                        ents.add(e);
                        e.needUpdate = false;
                    }
                }
                NetworkCommons.sendPacketTo(new PacketEntitiesState(ents, new Class<?>[]{EntityPlayer.class}), false, BlockyMainServer.instance.getNetwork().getIngameConnectionsFromClients());
            }
            if(System.currentTimeMillis()-lastTime >= 1000)
            {
                lastTime = System.currentTimeMillis();
                fps = frame;
                System.out.println(fps);
                frame = 0;
            }
        }
    }

}

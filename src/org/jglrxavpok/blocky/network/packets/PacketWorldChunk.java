package org.jglrxavpok.blocky.network.packets;

import java.io.IOException;

import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldChunk;

public class PacketWorldChunk extends Packet
{

    PacketWorldChunk(){}
    public PacketWorldChunk(WorldChunk chunk)
    {
        super("WorldChunk", null);
        try
        {
            data = tagSystem.writeChunk(chunk.createStorageChunk());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public WorldChunk readChunk(World world)
    {
        WorldChunk w = new WorldChunk(world, 0);
        try
        {
            w.readStorageChunk(tagSystem.readChunk(data));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return w;
    }
}

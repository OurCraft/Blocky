package org.jglrxavpok.blocky.network.packets;

import java.io.IOException;

import org.jglrxavpok.blocky.network.EntityState;
import org.jglrxavpok.storage.TaggedStorageChunk;

public class PacketEntityState extends Packet
{
    private int entityID;

    PacketEntityState()
    {
    }
    
    public PacketEntityState(int entityID, EntityState state)
    {
        super("EntityState", null);
        this.entityID = entityID;
        if(state != null)
        try
        {
            data = tagSystem.writeChunk(state.getEntityState());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public TaggedStorageChunk getState()
    {
        if(data != null)
        try
        {
            return tagSystem.readChunk(data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public int getEntityID()
    {
        return entityID;
    }
}

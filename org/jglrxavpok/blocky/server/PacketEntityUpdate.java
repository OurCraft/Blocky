package org.jglrxavpok.blocky.server;

import java.io.IOException;

import org.jglrxavpok.blocky.entity.Entity;

public class PacketEntityUpdate extends Packet
{

    private static final long serialVersionUID = 2003303026110256369L;

    public PacketEntityUpdate(Entity e, int id)
    {
        super("EntityUpdate", null);
        try
        {
            data = Packet.tagSystem.writeChunk(e.writeTaggedStorageChunk(id));
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }

}

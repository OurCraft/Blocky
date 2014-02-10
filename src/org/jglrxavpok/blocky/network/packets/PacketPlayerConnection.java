package org.jglrxavpok.blocky.network.packets;

import org.jglrxavpok.blocky.network.EntityState;

public class PacketPlayerConnection extends PacketEntityState
{

    PacketPlayerConnection(){}
    public PacketPlayerConnection(int entityID, EntityState state)
    {
        super(entityID, state);
    }
}

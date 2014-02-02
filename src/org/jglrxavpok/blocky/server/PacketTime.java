package org.jglrxavpok.blocky.server;

public class PacketTime extends OldPacket
{

    private static final long serialVersionUID = -9088892073289990818L;

    public PacketTime(long time)
    {
        super("Time: "+time, null);
    }

}

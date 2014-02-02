package org.jglrxavpok.blocky.network.packets;


public class PacketKick extends PacketMessage
{
    PacketKick(){}
    public PacketKick(String reason)
    {
        super("Kick", reason);
    }
}

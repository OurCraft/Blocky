package org.jglrxavpok.blocky.network.packets;

public class PacketHotbarSelection extends PacketMessage
{

    PacketHotbarSelection(){}
    
    public PacketHotbarSelection(float index)
    {
        super("Hotbar Selection", ""+getValid(index));
    }

    private static float getValid(float index)
    {
        while(index < 0.f)
        {
            index+=10.f;
        }
        while(index > 0.f)
        {
            index-=10.f;
        }
        return index;
    }
}

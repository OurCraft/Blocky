package org.jglrxavpok.blocky.network.packets;


public class PacketChat extends PacketMessage
{

    PacketChat(){}
    public PacketChat(String string)
    {
        super("ChatMessage", string);
    }
}

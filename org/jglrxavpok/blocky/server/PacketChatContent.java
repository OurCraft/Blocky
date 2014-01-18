package org.jglrxavpok.blocky.server;

public class PacketChatContent extends Packet
{

    private static final long serialVersionUID = 2465726553519802201L;

    public PacketChatContent(String content)
    {
        super("Chat content", content.getBytes());
    }

}

package org.jglrxavpok.blocky.server;

public class PacketClientConnection extends Packet
{

    private static final long serialVersionUID = -652218709357551632L;
    public String playerName;

    public PacketClientConnection(String playerName)
    {
        super("Client connection", null);
        this.playerName = playerName;
    }

}

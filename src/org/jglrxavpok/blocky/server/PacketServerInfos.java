package org.jglrxavpok.blocky.server;

public class PacketServerInfos extends OldPacket
{

    private static final long serialVersionUID = 679098010024625251L;
    public String serverName;
    public int playerCount;
    public int maxPlayers;
    public long pingTime;
    
    public PacketServerInfos(String serverName, int playerCount, int maxPlayers)
    {
        super("Server infos", null);
        this.serverName = serverName;
        this.playerCount = playerCount;
        this.maxPlayers = maxPlayers;
        pingTime = System.currentTimeMillis();
    }

}

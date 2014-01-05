package org.jglrxavpok.blocky.server;

import org.jglrxavpok.blocky.block.BlockInfo;


public class PacketBlockInfos extends Packet
{

    private static final long serialVersionUID = -6030776033005045060L;
    public int x;
    public int y;
    public int id;
    public String data;

    public PacketBlockInfos(BlockInfo infos)
    {
        super("BlockInfos packet", null);
        x = infos.x;
        y = infos.y;
        id = infos.type;
        data = infos.data;
    }

}

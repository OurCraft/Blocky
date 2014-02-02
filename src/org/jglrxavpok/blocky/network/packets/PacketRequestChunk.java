package org.jglrxavpok.blocky.network.packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class PacketRequestChunk extends Packet
{

    PacketRequestChunk(){}
    public PacketRequestChunk(int chunkID)
    {
        super("ChunkRequest", null);
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);
            out.writeInt(chunkID);
            out.close();
            baos.close();
            data = baos.toByteArray();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public int getChunkID()
    {
        try
        {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            DataInputStream in = new DataInputStream(bais);
            int id = in.readInt();
            in.close();
            bais.close();
            return id;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }
}

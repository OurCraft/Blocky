package org.jglrxavpok.blocky.network.packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.jglrxavpok.blocky.block.BlockInfo;

public class PacketBlockUpdate extends Packet
{

    PacketBlockUpdate(){}
    
    public PacketBlockUpdate(int x, int y, int id, String data)
    {
        super("BlockUpdate", null);
        try
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream dataOutput = new DataOutputStream(out);
            dataOutput.writeInt(x);
            dataOutput.writeInt(y);
            dataOutput.writeInt(id);
            dataOutput.writeUTF(data);
            dataOutput.close();
            out.close();
            this.data = out.toByteArray();
        }
        catch(Exception e)
        {
            e.printStackTrace();  
        }
    }
    
    public BlockInfo getBlockInfo()
    {
        try
        {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
            int x = in.readInt();
            int y = in.readInt();
            int id = in.readInt();
            String data = in.readUTF();
            BlockInfo info = new BlockInfo(x,y,id,data);
            return info;   
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}

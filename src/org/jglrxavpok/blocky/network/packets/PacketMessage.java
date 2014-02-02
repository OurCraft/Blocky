package org.jglrxavpok.blocky.network.packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketMessage extends Packet
{

    PacketMessage(){}
    public PacketMessage(String id, String msg)
    {
        super(id, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        try
        {
            out.writeUTF(msg);
            out.close();
            baos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        data = baos.toByteArray();
    }
    
    public String getMessage()
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream in = new DataInputStream(bais);
        try
        {
            String s = in.readUTF();
            in.close();
            bais.close();
            return s;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}

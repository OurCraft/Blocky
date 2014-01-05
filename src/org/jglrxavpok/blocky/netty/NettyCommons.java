package org.jglrxavpok.blocky.netty;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.ClosedChannelException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jglrxavpok.blocky.server.Packet;

public class NettyCommons
{

    public static Packet readPacket(ChannelBuffer buf) throws IOException, ClassNotFoundException
    {
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream input = new ObjectInputStream(bais);
        Object o = input.readObject();
        bais.close();
        return (Packet)o;
    }
    
    public static void sendPacket(Packet p, Channel c) throws IOException
    {
        if(c == null)
            return;
        try
        {
            ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(p);
            baos.close();
            buf.writeBytes(baos.toByteArray());
            c.write(buf);
        }
        catch(Exception e)
        {
            if(e instanceof ClosedChannelException)
            {
            }
            else
                e.printStackTrace();
        }
    }

}

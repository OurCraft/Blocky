package org.jglrxavpok.blocky.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Random;

import org.jglrxavpok.blocky.entity.EntityPlayer;

public class PacketPlayer extends OldPacket
{

    private static final long serialVersionUID = 3947776726070866132L;

    public PacketPlayer(EntityPlayer player)
    {
        super("Player packet", null);
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);
            out.writeFloat(player.x);
            out.writeFloat(player.y);
            out.writeFloat(player.vx);
            out.writeFloat(player.vy);
            if(player.username == null)
                player.username = "Player_"+new Random().nextInt(20000);
            out.writeUTF(player.username);
            out.close();
            data = baos.toByteArray();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}

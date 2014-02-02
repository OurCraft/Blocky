package org.jglrxavpok.blocky.client;

import com.esotericsoftware.kryonet.Connection;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.gui.UIConnectingToServer;
import org.jglrxavpok.blocky.network.IPacketHandler;
import org.jglrxavpok.blocky.network.packets.Packet;
import org.jglrxavpok.blocky.network.packets.PacketChat;
import org.jglrxavpok.blocky.network.packets.PacketKick;
import org.jglrxavpok.blocky.network.packets.PacketWorldChunk;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.world.WorldChunk;

public class ClientPacketHandler implements IPacketHandler
{

    private ClientNetworkListener listener;

    public ClientPacketHandler(ClientNetworkListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void handlePacket(Packet p, Connection c)
    {
        if(p instanceof PacketKick)
        {
            String reason = ((PacketKick) p).getMessage();
            BlockyMain.console("You have been kicked because of: \""+reason+"\"");
            c.close();
        }
        else if(p instanceof PacketChat)
        {
            String msg = ((PacketChat)p).getMessage();
            BlockyMain.instance.addToChat(msg);
            BlockyMain.console(msg);
        }
        else if(p instanceof PacketWorldChunk)
        {
            WorldChunk chunk = ((PacketWorldChunk)p).readChunk(listener.getWorld());
            listener.getWorld().putChunk(chunk);
        }
    }

}

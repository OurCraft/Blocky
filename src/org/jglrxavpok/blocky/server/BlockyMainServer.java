package org.jglrxavpok.blocky.server;

import javax.swing.UIManager;

import com.esotericsoftware.kryonet.Server;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.utils.Fluid;
import org.jglrxavpok.blocky.world.World;

public class BlockyMainServer
{

    private static int serverPort;

    public static void main(String[] args)
    {
        if(args != null && args.length > 0)
        {
            serverPort = Integer.parseInt(args[0]);
        }
        else
        {
            serverPort = 35565;
        }
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        BlockyMain.instance = new BlockyMain();
        Fluid.load();
        Block.loadAll();
        new BlockyMainServer().startServer(serverPort);;
    }

    public static BlockyMainServer instance;
    public static World level;
    
    public BlockyMainServer()
    {
        instance = this;
    }
    
    public static void console(String s)
    {
        System.out.println("[BlockyServer "+BlockyMain.version()+"] "+s);
    }
    
    private void startServer(int port)
    {
        try
        {
            Server server = new Server();
            server.start();
            server.bind(port);
            server.addListener(new ServerNetworkListener());
            console("Server started on port "+port);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

package org.jglrxavpok.blocky.server;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

import javax.swing.UIManager;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.network.NetworkCommons;
import org.jglrxavpok.blocky.network.packets.PacketChat;
import org.jglrxavpok.blocky.network.packets.PacketKick;
import org.jglrxavpok.blocky.utils.Fluid;
import org.jglrxavpok.blocky.world.World;

public class BlockyMainServer
{

    private static int serverPort;
    public static WorldServer world;

    public static void injectIntoClasspath(URL path)
    {
        try
        {
            URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class<?>[]{URL.class}); 
            addURL.setAccessible(true);
            addURL.invoke(classLoader, new Object[]{path});
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
        try
        {
            if(args != null)
            {
                if(args.length > 0)
                    serverPort = Integer.parseInt(args[0]);
                else
                {
                    serverPort = 35565;
                }
                if(args.length > 1)
                {
                    File f = new File(args[1]);
                    File[] list = f.listFiles();
                    if(list != null)
                    for(File file : list)
                    {
                        try
                        {
                            injectIntoClasspath(file.toURI().toURL());
                        }
                        catch (MalformedURLException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
    //            else
    //            {
    //                System.out.println("Librairies not found, can't continue. :'(");
    //            }
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
            world = new WorldServer("Server");
            new BlockyMainServer().startServer(serverPort);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static BlockyMainServer instance;
    private ServerNetworkListener listener;
    public boolean running;
    
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
            final Server server = new Server(6556500, 6556500);
            NetworkCommons.registerClassesFor(server);
            server.start();
            server.bind(port);
            listener = new ServerNetworkListener(server);
            server.addListener(listener);
            running = true;
            new ThreadUpdateServerWorld(world, instance.listener).start();
            console("Server started on port "+port);
            Thread thread = new Thread("Commands")
            {
                public void run()
                {
                    Scanner sc = new Scanner(System.in);
                    while(true)
                    {
                        loop(sc, server, listener);
                    }
                }
            };
            thread.setDaemon(true);
            thread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void loop(Scanner sc, Server server, ServerNetworkListener listener)
    {
        String line = sc.nextLine();
        if(line.startsWith("/"))
        {
            String command = line.substring(1);
            if(command.equals("stop"))
            {
                running = false;
                server.stop();
                BlockyMainServer.console("Server closed");
                System.exit(0);
            }
            else if(command.equals("list"))
            {
                BlockyClient[] clients = listener.getClients();
                BlockyMainServer.console("Player list");
                BlockyMainServer.console("============");
                for(BlockyClient c : clients)
                {
                    BlockyMainServer.console(c.getName()+"("+c.getID()+") @ "+c.getConnection().getRemoteAddressTCP());
                }
                BlockyMainServer.console("============");
            }
            else if(command.startsWith("kick "))
            {
                String username = command.replace("kick ", "");
                String parts[] = username.split(" ");
                username = parts[0];
                String reason = "Kicked by admin";
                try
                {
                    reason = command.substring(6+username.length());
                }
                catch(Exception e)
                {
//                    e.printStackTrace();
                }
                BlockyClient[] clients = listener.getClients();
                BlockyClient toKick = null;
                for(BlockyClient c : clients)
                {
                    if(c.getName().equals(username))
                    {
                        toKick = c;
                        break;
                    }
                }
                if(toKick != null)
                {
                    NetworkCommons.sendPacketTo(new PacketKick(reason), false, toKick.getConnection());
                    toKick.getConnection().close();
                    BlockyMainServer.console("Player "+toKick.getName()+" has been kicked for: \""+reason+"\"");
                }
                else
                {
                    BlockyMainServer.console("Player "+username+" doesn't exist.");
                }
            }
        }
        else
        {
            server.sendToAllTCP(new PacketChat("{Server} "+line));
        }
    }

    public ServerNetworkListener getNetwork()
    {
        return listener;
    }

}

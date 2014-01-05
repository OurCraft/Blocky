package org.jglrxavpok.blocky.gui;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jglrxavpok.blocky.netty.NettyClientHandler;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.ui.UIButton;
import org.jglrxavpok.blocky.ui.UIComponentBase;
import org.jglrxavpok.blocky.ui.UILabel;
import org.jglrxavpok.blocky.ui.UILabel.LabelAlignment;
import org.jglrxavpok.blocky.ui.UIMenu;

public class UIConnectingToServer extends UIBlockyMenu
{

    private UIMenu parent;
    private UIButton backButton;
    private boolean connecting;
    private String serverIP;

    public UIConnectingToServer(UIMenu parent, String serverIP)
    {
        this.parent = parent;
        this.serverIP = serverIP;
    }
    
    public void initMenu()
    {
        comps.add(new UILabel("Connecting to server: "+serverIP, w/2-75, h/2, LabelAlignment.CENTERED).setColor(0xFFFFFF));
        backButton = new UIButton(this, w/2-75, h/2-30, 150, 30,"Cancel");
        comps.add(backButton);
        if(!connecting)
        new Thread(new Runnable()
        {
            public void run()
            {
                if(serverIP == null || serverIP.trim().length() == 0)
                    return;
                String serverAddress = "";
                int serverPort = 35565;
                if(serverIP.contains(":"))
                {
                    serverAddress = serverIP.split(":")[0];
                    serverPort = Integer.parseInt(serverIP.split(":")[1]);
                }
                else
                {
                    serverAddress = serverIP;
                }
                try
                {
//                    DatagramSocket socket = new DatagramSocket();
////                    socket.connect(InetAddress.getByName(serverAddress), serverPort);
//                    BlockyMain.instance.clientHandler = new ClientHandler(socket, serverAddress, serverPort);
                    ChannelFactory factory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
                    ClientBootstrap bootstrap = new ClientBootstrap(factory);
                    bootstrap.setPipelineFactory(new ChannelPipelineFactory() 
                    {
                        public ChannelPipeline getPipeline() 
                        {
                            return Channels.pipeline(new NettyClientHandler(false));
                        }
                    });
                    bootstrap.setOption("tcpNoDelay", true);
                    bootstrap.setOption("keepAlive", true);
                    bootstrap.connect(new InetSocketAddress(serverAddress, serverPort));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                
            }
        }).start();
        connecting = true;
    }
    
    public void componentClicked(UIComponentBase b)
    {
        if(backButton == b)
        {
            UI.displayMenu(parent);
        }
    }
}

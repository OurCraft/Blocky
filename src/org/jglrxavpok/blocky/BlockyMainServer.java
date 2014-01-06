package org.jglrxavpok.blocky;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.UIManager;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.netty.NettyServerHandler;
import org.jglrxavpok.blocky.server.ThreadUpdateLevel;
import org.jglrxavpok.blocky.utils.Fluid;
import org.jglrxavpok.blocky.world.World;

public class BlockyMainServer
{

    public static void main(String[] args)
    {
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
        new BlockyMainServer();
    }

    public static BlockyMainServer instance;
    public static World level;
    
    public BlockyMainServer()
    {
        instance = this;
        final JFrame portFrame = new JFrame("Port number");
        JPanel pan = new JPanel(new FlowLayout());
        pan.add(new JLabel("Port: "));
        final JSpinner spinner = new JSpinner();
        spinner.setValue(35565);
        pan.add(spinner);
        JButton button = new JButton("OK");
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                portFrame.dispose();
                startServer((Integer)spinner.getValue());
            }
        });
        pan.add(button);
        portFrame.add(pan);
        portFrame.pack();
        portFrame.setLocationRelativeTo(null);
        portFrame.setVisible(true);
    }
    
    public static ArrayList<Channel> clients = new ArrayList<Channel>();
    
    private void startServer(int port)
    {
        try
        {
            level = new World("Server level");
            new ThreadUpdateLevel(level, clients).start();
            final String serverName = "Blocky server";
            ChannelFactory factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),Executors.newCachedThreadPool());
            ServerBootstrap bootstrap = new ServerBootstrap(factory);
            bootstrap.setPipelineFactory(new ChannelPipelineFactory() 
            {
                public ChannelPipeline getPipeline() 
                {
                    return Channels.pipeline(new NettyServerHandler(serverName));
                }
            });

            bootstrap.setOption("child.tcpNoDelay", true);
            bootstrap.setOption("child.keepAlive", true);
            bootstrap.bind(new InetSocketAddress(port));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

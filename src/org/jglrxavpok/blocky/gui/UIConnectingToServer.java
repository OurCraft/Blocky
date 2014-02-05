package org.jglrxavpok.blocky.gui;

import com.esotericsoftware.kryonet.Client;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.client.ClientNetworkListener;
import org.jglrxavpok.blocky.entity.EntityPlayerClientMP;
import org.jglrxavpok.blocky.network.NetworkCommons;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.ui.UIButton;
import org.jglrxavpok.blocky.ui.UIComponentBase;
import org.jglrxavpok.blocky.ui.UIErrorMenu;
import org.jglrxavpok.blocky.ui.UILabel;
import org.jglrxavpok.blocky.ui.UILabel.LabelAlignment;
import org.jglrxavpok.blocky.ui.UIMenu;

public class UIConnectingToServer extends UIBlockyMenu
{

    private UIMenu parent;
    private UIButton backButton;
    private boolean connecting;
    private String serverIP;
    private Client client;

    public UIConnectingToServer(UIMenu parent, String serverIP)
    {
        this.parent = parent;
        this.serverIP = serverIP;
    }
    
    public void initMenu()
    {
        comps.add(new UILabel("Connecting to server: "+serverIP, w/2, h/2, LabelAlignment.CENTERED).setColor(0xFFFFFF));
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
                    client = new Client(6556500, 6556500);
                    client.start();
                    NetworkCommons.registerClassesFor(client);
                    ClientNetworkListener listener = new ClientNetworkListener(client);
                    BlockyMain.instance.setClientNetwork(listener);
                    client.addListener(listener);
                    client.connect(8001, serverAddress, serverPort);
                }
                catch (Exception e)
                {
                    UI.displayMenu(new UIErrorMenu(new UIMainMenu(), UIConnectingToServer.this, e));
                    e.printStackTrace();
                }
                
            }
        }).start();
        connecting = true;
    }
    
    public void update(int mx, int my, boolean[] buttons)
    {
        if(BlockyMain.instance.getClientNetwork() != null)
        {
            if(BlockyMain.instance.getClientNetwork().getWorld().getRandomChunk() != null)
            {
                UI.displayMenu(null);
                EntityPlayerClientMP player = new EntityPlayerClientMP();
                player.move(0*Block.BLOCK_WIDTH, 250*Block.BLOCK_HEIGHT);
                BlockyMain.instance.getClientNetwork().getWorld().centerOfTheWorld = player;
                BlockyMain.instance.getClientNetwork().getWorld().addEntity(player);
            }
        }
    }
    
    public void componentClicked(UIComponentBase b)
    {
        if(backButton == b)
        {
            if(client != null)
                client.close();
            UI.displayMenu(parent);
        }
    }
}

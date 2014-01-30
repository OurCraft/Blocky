package org.jglrxavpok.blocky.gui;

import com.esotericsoftware.kryonet.Client;

import org.jglrxavpok.blocky.client.ClientNetworkListener;
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
                    Client client = new Client();
                    client.start();
                    client.addListener(new ClientNetworkListener());
                    client.connect(8001, serverAddress, serverPort);
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

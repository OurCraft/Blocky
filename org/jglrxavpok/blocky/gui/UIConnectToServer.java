package org.jglrxavpok.blocky.gui;

import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.ui.UIButton;
import org.jglrxavpok.blocky.ui.UIComponentBase;
import org.jglrxavpok.blocky.ui.UIMenu;
import org.jglrxavpok.blocky.ui.UITextField;
import org.jglrxavpok.opengl.Textures;
import org.lwjgl.opengl.GL11;

public class UIConnectToServer extends UIMenu
{

    private UIMenu parent;
    private UIButton playButton;
    private UIButton backButton;
    private UITextField serverIP;

    public UIConnectToServer(UIMenu parent)
    {
        this.parent = parent;
    }
    
    public void initMenu()
    {
        playButton = new UIButton(this, w/2-75,h/4,150,30,"Connect");
        backButton = new UIButton(this, w/2-75,h/4-50,150,30,"Back");
        serverIP = new UITextField(w/2-75, h/4+40, 150, 30,"");
        serverIP.setPlaceholder("Server IP");
        comps.add(playButton);
        comps.add(backButton);
        comps.add(serverIP);
    }
    
    public void componentClicked(UIComponentBase c)
    {
        super.componentClicked(c);
        if(c == backButton)
        {
            UI.displayMenu(parent);
        }
        else if(c == playButton)
        {
            UI.displayMenu(new UIConnectingToServer(this, serverIP.getText()));
        }
    }
    
    public void renderBackground(int mx, int my, boolean[] buttons)
    {
        UIMainMenu.background.render();
        super.renderBackground(mx,my,buttons);
    }
    
    public void update(int mx, int my, boolean[] buttons)
    {
        UIMainMenu.background.tick();
        super.update(mx, my, buttons);
    }
    
    public void renderOverlay(int mx, int my, boolean[] buttons)
    {
        float x = w/2-223;
        float y = h/2+61;
        float w = 223*2;
        float h = 61*2;
        GL11.glColor3f(0.75f, 0.75f, 0.75f);
        Textures.render(Textures.getFromClasspath("/assets/textures/logo.png"), x+2, y-2, w, h);
        GL11.glColor3f(1, 1, 1);
        Textures.render(Textures.getFromClasspath("/assets/textures/logo.png"), x, y, w, h);
    }

}

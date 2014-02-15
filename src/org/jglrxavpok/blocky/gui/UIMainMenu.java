package org.jglrxavpok.blocky.gui;

import java.io.File;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.entity.EntityDemoPlayer;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.ui.UIButton;
import org.jglrxavpok.blocky.ui.UIComponentBase;
import org.jglrxavpok.blocky.utils.SoundManager;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.opengl.Textures;
import org.lwjgl.opengl.GL11;

public class UIMainMenu extends UIBlockyMenu
{

    private UIButton quitButton;
    private UIButton playButton;
    private UIButton serverButton;
    public static final World background = new World("Background");

    static
    {
        Entity p = new EntityDemoPlayer().move(50*Block.BLOCK_WIDTH, 120*Block.BLOCK_HEIGHT);
        background.centerOfTheWorld = p;
        background.addEntity(p);
    }
    
    public void initMenu()
    {
        playButton = new UIButton(this, w/2-75,h/4,150,30,"Play");
        serverButton = new UIButton(this, w/2-75,h/4-45,150,30,"Connect to server");
        quitButton = new UIButton(this, w/2-75,h/4-90,150,30,"Quit");
        comps.add(quitButton);
        comps.add(playButton);
        comps.add(serverButton);
        
//        SoundManager.instance.playBackgroundMusic("/assets/musics/menu.ogg", 1, 0.8f);
//        SoundManager.instance.setLooping("bgmusic", true);
    }
    
    public void componentClicked(UIComponentBase c)
    {
        super.componentClicked(c);
        if(c == quitButton)
        {
            BlockyMain.shutdown();
        }
        else if(c == serverButton)
        {
            UI.displayMenu(new UIConnectToServer(this));
        }
        else if(c == playButton)
        {
            UI.displayMenu(new UIWorldList(this, new File(BlockyMain.getFolder(), "saves")));
        }
    }
    
    public void renderBackground(int mx, int my, boolean[] buttons)
    {
        background.render();
        super.renderBackground(mx,my,buttons);
    }
    
    public void update(int mx, int my, boolean[] buttons)
    {
        background.tick();
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

package org.jglrxavpok.blocky.gui;

import org.jglrxavpok.blocky.entity.EntityPlayerSP;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.ui.UIButton;
import org.jglrxavpok.blocky.ui.UIComponentBase;
import org.jglrxavpok.blocky.ui.UIMenu;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.opengl.FontRenderer;
import org.jglrxavpok.opengl.Tessellator.OpenGlHelper;

public class UIGameOverMenu extends UIMenu
{

    private UIButton respawnButton;
    private World world;

    public UIGameOverMenu(World w)
    {
        this.world = w;
    }
    
    public void initMenu()
    {
        respawnButton = new UIButton(this,w/2-75,h/2-70,150,30,"Respawn");
        comps.add(respawnButton);
    }
    
    public void componentClicked(UIComponentBase c)
    {
        if(c == respawnButton)
        {
            EntityPlayerSP playerSp = new EntityPlayerSP();
            playerSp.move(world.spawnPoint.x, world.spawnPoint.y);
            world.addEntity(playerSp);
            world.centerOfTheWorld = playerSp;
            UI.displayMenu(null);
        }
    }
    
    public void render(int mx, int my, boolean[] buttons)
    {
        super.render(mx, my, buttons);
        OpenGlHelper.startNegativeMode();
        FontRenderer.setScale(2);
        FontRenderer.drawString("GAME OVER", mx, my, 0xFFFFFF);
        FontRenderer.setScale(1);
        OpenGlHelper.endNegativeMode();
    }
    
    public void update(int mx, int my, boolean[] buttons)
    {
        super.update(mx, my, buttons);
        world.tick();
    }
}

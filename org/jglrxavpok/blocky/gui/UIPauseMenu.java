package org.jglrxavpok.blocky.gui;

import java.io.IOException;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.ui.UIButton;
import org.jglrxavpok.blocky.ui.UIComponentBase;
import org.jglrxavpok.blocky.world.WorldGenerator.WorldType;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.opengl.Textures;
import org.lwjgl.opengl.GL11;

public class UIPauseMenu extends UIBlockyMenu
{

    private UIButton resumeButton;
    private UIButton saveButton;

    public void initMenu()
    {
        resumeButton = new UIButton(this, w/2-75,h/2+20,150,30,"Resume");
        saveButton = new UIButton(this, w/2-75,h/2-35,150,30,"Save and quit");
        if(BlockyMain.instance.getLevel().worldType == WorldType.CLIENT)
        {
            saveButton.displayString="Quit";
        }
        comps.add(saveButton);
        comps.add(resumeButton);
    }
    
    public void componentClicked(UIComponentBase c)
    {
        super.componentClicked(c);
        if(resumeButton == c)
            UI.displayMenu(null);
        else if(saveButton == c)
        {
            try
            {
                BlockyMain.instance.saveLevel();
                BlockyMain.instance.loadLevel(null);
                UI.displayMenu(new UIMainMenu());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
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
    
    public void renderBackground(int mx, int my, boolean[] buttons)
    {
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setColorRGBA_F(0, 0, 0, 0.5f);
        t.addVertex(0, 0, 0);
        t.addVertex(BlockyMain.width, 0, 0);
        t.addVertex(BlockyMain.width, BlockyMain.height, 0);
        t.addVertex(0, BlockyMain.height, 0);
        t.flush();
        t.setColorRGBA_F(1,1,1, 1);
        super.renderBackground(mx, my, buttons);
    }
}

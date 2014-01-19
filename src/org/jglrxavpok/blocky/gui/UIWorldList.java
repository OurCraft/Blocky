package org.jglrxavpok.blocky.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.entity.EntityPlayerSP;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.ui.UIButton;
import org.jglrxavpok.blocky.ui.UIComponentBase;
import org.jglrxavpok.blocky.ui.UIList;
import org.jglrxavpok.blocky.ui.UIMenu;
import org.jglrxavpok.blocky.ui.UISlot;
import org.jglrxavpok.blocky.utils.IO;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.opengl.Textures;
import org.lwjgl.opengl.GL11;

public class UIWorldList extends UIBlockyMenu
{

    private File folder;
    private UIList worldList;
    private UIMenu parentScreen;
    private UIButton playButton;
    private UIButton newGame;
    private UIButton backButton;
    private UIButton delete;

    public UIWorldList(UIMenu parent, File saveFolder)
    {
        folder = saveFolder;
        this.parentScreen = parent;
    }
    
    public void initMenu()
    {
        worldList = new UIList(200,150,w-400,h-350);
        comps.add(worldList);
        ArrayList<UIWorldSlot> slots = new ArrayList<UIWorldSlot>();
        File[] files = folder.listFiles();
        if(files != null)
        for(int i = 0;i<files.length;i++)
        {
            File save = files[i];
            if(save.isDirectory())
            {
                File levelData = new File(save,"level.data");
                if(levelData.exists())
                {
                    UIWorldSlot slot = new UIWorldSlot(this, save.getName(), save);
                    slots.add(slot);
                }
            }
        }
        UIWorldSlot[] array = slots.toArray(new UIWorldSlot[0]);
        Arrays.sort(array, new Comparator<UIWorldSlot>()
                {

                    @Override
                    public int compare(UIWorldSlot arg0, UIWorldSlot arg1)
                    {
                        return Long.compare(arg1.getWorldInfos().lastModified, arg0.getWorldInfos().lastModified);
                    }
            
                });
        worldList.slots = array;
        playButton = new UIButton(this, w/2-170, 90, 150, 30, "Play");
        backButton = new UIButton(this, w/2+20, 50, 150, 30, "Back");
        newGame = new UIButton(this, w/2-170, 50, 150, 30, "Create new world");
        delete = new UIButton(this, w/2+20, 90, 150, 30, "Delete");
        playButton.enabled = false;
        delete.enabled = false;
        comps.add(playButton);
        comps.add(backButton);
        comps.add(newGame);
        comps.add(delete);
    }
    
    public void componentClicked(UIComponentBase c)
    {
        super.componentClicked(c);
        if(c instanceof UIWorldSlot)
        {
            if(worldList.getSelected() == c)
            {
                componentClicked(playButton);
            }
            else
            {
                worldList.setSelected((UIWorldSlot)c);
                playButton.enabled = true;
                delete.enabled = true;
            }
        }
        else if(c == playButton)
        {
            UISlot s = worldList.getSelected();
            if(s != null && s instanceof UIWorldSlot)
            {
                UIWorldSlot slot = (UIWorldSlot)s;
                World level = new World(slot.getWorldInfos());
                Entity p = new EntityPlayerSP().move(250000*Block.BLOCK_WIDTH, 120*Block.BLOCK_HEIGHT);
                level.centerOfTheWorld = p;
                level.addEntity(p);
                BlockyMain.instance.loadLevel(level);
                UI.displayMenu(null);
            }
        }
        else if(c == delete)
        {
            UISlot s = worldList.getSelected();
            if(s != null && s instanceof UIWorldSlot)
            {
                UIWorldSlot slot = (UIWorldSlot)s;
                IO.deleteFolderContents(slot.getWorldInfos().worldFolder);
                slot.getWorldInfos().worldFolder.delete();
                UI.displayMenu(new UIWorldList(parentScreen,folder));
            }
        }
        else if(c == newGame)
        {
            UI.displayMenu(new UIWorldCreationMenu(this));
        }
        else if(c == backButton)
        {
            UI.displayMenu(parentScreen);
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
        super.renderOverlay(mx, my, buttons);
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

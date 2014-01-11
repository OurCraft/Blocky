package org.jglrxavpok.blocky.gui;

import java.io.File;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.achievements.AchievementList;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.entity.EntityPlayerSP;
import org.jglrxavpok.blocky.inventory.Inventory;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.items.Item;
import org.jglrxavpok.blocky.ui.TextChangeListener;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.ui.UIButton;
import org.jglrxavpok.blocky.ui.UIComponentBase;
import org.jglrxavpok.blocky.ui.UIMenu;
import org.jglrxavpok.blocky.ui.UITextField;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.opengl.Textures;
import org.lwjgl.opengl.GL11;

public class UIWorldCreationMenu extends UIBlockyMenu implements TextChangeListener
{

    private UIButton create;
    private UITextField worldName;
    private UIButton back;
    private UIMenu parent;
    public static final String[] ILLEGAL_WORLD_NAMES = new String[] {"CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};
    public static final char[] ILLEGAL_WORLD_NAME_COMPONENTS = new char[] {'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};
    
    public UIWorldCreationMenu(UIMenu menu)
    {
        parent = menu;
    }
    
    public void initMenu()
    {
        create = new UIButton(this, w/2-75,h/2-40-70,70,30,"Create");
        comps.add(create);
        back = new UIButton(this, w/2+5,h/2-40-70,70,30,"Back");
        comps.add(back);
        worldName = new UITextField(w/2-75,h/2-70,150,30);
        worldName.setText("New world");
        comps.add(worldName);
        worldName.setListener(this);
    }
    
    public void componentClicked(UIComponentBase comp)
    {
        if(comp == create)
        {
            World level = new World(worldName.getText());
            if(!new File(BlockyMain.getFolder(), "saves/").exists())
                new File(BlockyMain.getFolder(), "saves/").mkdirs();
            File savesFolder = new File(BlockyMain.getFolder(), "saves");
            level.setChunkFolder(new File(savesFolder,getCorrectWorldFileName(savesFolder,worldName.getText())));
            level.spawnPoint.set(250000*Block.BLOCK_WIDTH, 120*Block.BLOCK_HEIGHT);
            EntityPlayerSP p = (EntityPlayerSP) new EntityPlayerSP().move(250000*Block.BLOCK_WIDTH, 120*Block.BLOCK_HEIGHT);
            level.centerOfTheWorld = p;
            Inventory inv = p.inv;
            inv.tryAdd(new ItemStack(Item.steelPick, 1));
            inv.tryAdd(new ItemStack(Block.rock.getItem(), 20));
            inv.tryAdd(new ItemStack(Block.grass.getItem(), 20));
            inv.tryAdd(new ItemStack(Block.dirt.getItem(), 20));
            inv.tryAdd(new ItemStack(Block.log.getItem(), 20));
            inv.tryAdd(new ItemStack(Block.leaves.getItem(), 20));
            inv.tryAdd(new ItemStack(Block.torch.getItem(), 20));
            inv.tryAdd(new ItemStack(Block.planks.getItem(), 20));
            inv.tryAdd(new ItemStack(Item.door, 20));
            inv.tryAdd(new ItemStack(Block.getBlock("water_2").getItem(), 20));

            level.addEntity(p);
            BlockyMain.instance.loadLevel(level);
            UI.displayMenu(null);
            p.toggleAchievement(AchievementList.beginNewWorld);
        }
        else if(comp == back)
        {
            UI.displayMenu(parent);
        }
    }
    
    private String getCorrectWorldFileName(File folder, String text)
    {
        int i = 1;
        for(int ii = 0;ii<ILLEGAL_WORLD_NAMES.length;ii++)
        {
            if(text.equalsIgnoreCase(ILLEGAL_WORLD_NAMES[ii]))
                text = "_"+text+"_";
        }
        for(int ii =0;ii<ILLEGAL_WORLD_NAME_COMPONENTS.length;ii++)
        {
            text = text.replace(ILLEGAL_WORLD_NAME_COMPONENTS[ii], '_');
        }
        if(new File(folder, text).exists())
        {
            while(new File(folder, text+ " ("+i+")").exists())
            {
                i++;
            }
            return text+" ("+i+")";
        }
        return text;
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

    @Override
    public void textChanged(Object source, String text)
    {
        if(text == null || text.trim().equals(""))
        {
            create.enabled = false;
        }
        else
        {
            create.enabled = true;
        }
    }
}

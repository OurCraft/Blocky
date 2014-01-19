package org.jglrxavpok.blocky.gui;

import java.util.ArrayList;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.crafting.Craft;
import org.jglrxavpok.blocky.crafting.CraftingManager;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.utils.MathHelper;
import org.jglrxavpok.opengl.FontRenderer;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.opengl.Textures;
import org.lwjgl.input.Keyboard;

public class UICraftingTable extends UIContainer
{

    private boolean checkCraft;
    private int posX;
    private int posY;

    public UICraftingTable(EntityPlayer p, int x, int y)
    {
        super(p, 47);
        this.posX = x;
        this.posY = y;
        
        for(int i = 0 ; i < 10 ; i++)
        {
            this.slots[i] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + i * 32 + i + 3, BlockyMain.height / 2 - 32-50 - 3 - 32 - 20);
        }
        
        for(int i = 0 ; i < 10 ; i++)
        {
            this.slots[i + 10] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + i * 32 + i + 3, BlockyMain.height / 2-50 - 32 - 3);
        }
        
        for(int i = 0 ; i < 10 ; i++)
        {
            this.slots[i + 20] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + i * 32 + i + 3, BlockyMain.height / 2-50);
        }
        
        for(int i = 0 ; i < 10 ; i++)
        {
            this.slots[i + 30] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + i * 32 + i + 3, BlockyMain.height / 2-50 + 32 + 3);
        }
        
        for(int i = 0;i<6;i++)
        {
            this.slots[i + 40] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 2 * 32+32*i + i+2 + 3, BlockyMain.height / 2-25+35 + 32 + 3);
            this.slots[i + 40].addListener(this);
        }
        
        slots[46] = new UICraftResultSlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 2 * 32+(int)(32*2.5f) + 3+2 + 3, BlockyMain.height / 2+25+35 + 32 + 3);
        
        slots[46].addListener(this);
        
        for(int i = 0 ; i < p.inv.getInventorySize() ; i++)
        {
            this.slots[i].setStack(p.inv.getStackIn(i));
        }
    }
    
    public void renderBackground(int mx, int my, boolean[] buttonsPressed)
    {
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setColorRGBA_F(0, 0, 0, 0.35f);
        t.addVertex(0, 0, 0);
        t.addVertex(BlockyMain.width, 0, 0);
        t.addVertex(BlockyMain.width, BlockyMain.height, 0);
        t.addVertex(0, BlockyMain.height, 0);
        t.flush();
        t.setColorRGBA_F(1,1,1, 1);
        super.renderBackground(mx, my, buttonsPressed);
    }
    
    public void render(int mx, int my, boolean[] buttonsPressed)
    {
        Tessellator t = Tessellator.instance;
        Textures.bind(0);
        t.startDrawingQuads();
        t.setColorRGBA_F(1,1,1, 1);
        t.addVertex(BlockyMain.width / 2 - (128) - 36, BlockyMain.height / 2+60 + 78, 0);
        t.setColorRGBA_F(0.5f,0.5f,0.5f, 1);
        t.addVertex(BlockyMain.width / 2 - (32 * 4) - 12 + 10 * 32 + 13+2, BlockyMain.height / 2+60 + 78, 0);
        t.addVertex(BlockyMain.width / 2 - (32 * 4) - 12 + 10 * 32 + 13+2, BlockyMain.height / 2+60 + 78-2, 0);
        t.setColorRGBA_F(1,1,1, 1);
        t.addVertex(BlockyMain.width / 2 - (128) - 36, BlockyMain.height / 2+60 + 78-2, 0);

        t.setColorRGBA_F(0.5f,0.5f,0.5f, 1);
        t.addVertex(BlockyMain.width / 2 - (128) - 36, BlockyMain.height / 2 - 75 - 52-12, 0);
        t.addVertex(BlockyMain.width / 2 - (128) - 36+2, BlockyMain.height / 2 - 75 - 52-12, 0);
        t.setColorRGBA_F(1,1,1, 1);
        t.addVertex(BlockyMain.width / 2 - (128) - 36+2, BlockyMain.height / 2+60 + 78, 0);
        t.addVertex(BlockyMain.width / 2 - (128) - 36, BlockyMain.height / 2+60 + 78, 0);
        
        
        t.setColorRGBA_F(0.5f,0.5f,0.5f, 1);
        t.addVertex(BlockyMain.width / 2 - (128) - 36, BlockyMain.height / 2 - 75 - 52-12, 0);
        t.setColorRGBA_F(0.05f,0.05f,0.05f, 1);
        t.addVertex(BlockyMain.width / 2 - (32 * 4) - 12 + 10 * 32 + 13+2, BlockyMain.height / 2 - 75 - 52-12, 0);
        t.addVertex(BlockyMain.width / 2 - (32 * 4) - 12 + 10 * 32 + 13+2, BlockyMain.height / 2 - 75 - 52-12-2, 0);
        t.setColorRGBA_F(0.5f,0.5f,0.5f, 1);
        t.addVertex(BlockyMain.width / 2 - (128) - 36, BlockyMain.height / 2 - 75 - 52-2-12, 0);

        t.setColorRGBA_F(0.05f,0.05f,0.05f, 1);
        t.addVertex(BlockyMain.width / 2 - (32 * 4) - 12 + 10 * 32 + 13, BlockyMain.height / 2 - 75 - 52-12, 0);
        t.addVertex(BlockyMain.width / 2 - (32 * 4) - 12 + 10 * 32 + 13+2, BlockyMain.height / 2 - 75 - 52-12, 0);
        t.setColorRGBA_F(0.5f,0.5f,0.5f, 1);

        t.addVertex(BlockyMain.width / 2 - (32 * 4) - 12 + 10 * 32 + 13+2, BlockyMain.height / 2+60 + 78, 0);
        t.addVertex(BlockyMain.width / 2 - (32 * 4) - 12 + 10 * 32 + 13, BlockyMain.height / 2+60 + 78, 0);
        t.flush();
        t.setColorRGBA_F(1,1,1, 1);
        super.render(mx, my, buttonsPressed);
        
        FontRenderer.drawShadowedString("Inventory", BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 3, BlockyMain.height / 2 + 32-50 + 3+32+3, 0xFFFFFF);
        FontRenderer.drawShadowedString("Hotbar", BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 3, BlockyMain.height / 2 - 32-32+13-50, 0xFFFFFF);
        
        FontRenderer.drawShadowedString("Crafting", BlockyMain.width / 2 - (128) - 36, BlockyMain.height / 2+60 + 78, 0xFFFFFF);
        
    }
    
    public void onKeyEvent(char c, int key, boolean action)
    {
        if(action)
        if(key == Keyboard.KEY_E)
        {
            onMenuClose();
            UI.displayMenu(null);
        }
    }
    
    public void onMenuClose()
    {
        super.onMenuClose();
        for(int i = 40;i<47;i++)
            player.dropStack(slots[i].stack);
    }
    
    public void update(int mx, int my, boolean[] buttonsPressed)
    {
        if(BlockyMain.instance.getLevel() != null)
        {
            BlockyMain.instance.getLevel().tick();
        }
        super.update(mx, my, buttonsPressed);
        if(MathHelper.dist(posX*Block.BLOCK_WIDTH, posY*Block.BLOCK_HEIGHT, player.x, player.y) > Block.BLOCK_WIDTH*6f)
        {
            onMenuClose();
            UI.displayMenu(null);
            return;
        }
        if(checkCraft)
        {
            ArrayList<Craft> crafts = CraftingManager.getInstance().getResults(new ItemStack[]
                    {
                    slots[40].stack == null ? new ItemStack() : slots[40].stack,
                    slots[41].stack == null ? new ItemStack() : slots[41].stack,
                    slots[42].stack == null ? new ItemStack() : slots[42].stack,
                    slots[43].stack == null ? new ItemStack() : slots[43].stack,
                    slots[44].stack == null ? new ItemStack() : slots[44].stack,
                    slots[45].stack == null ? new ItemStack() : slots[45].stack,
                    });
            if(crafts.size() > 0)
            {
                //Temporary
                Craft c = crafts.get(0);
                if(slots[46].stack != null)
                {
                    player.dropStack(slots[46].stack);
                }
                slots[46].stack = c.getOutput();
            }
            else
            {
                slots[46].stack = null;
            }
            checkCraft = false;
        }
    }
    
    @Override
    public void onStackRemoved(UIInventorySlot s, ItemStack previous)
    {
        if(s == slots[46] && previous != null)
        {
            slots[40].stack = null;
            slots[41].stack = null;
            slots[42].stack = null;
            slots[43].stack = null;
            slots[44].stack = null;
            slots[45].stack = null;
        }
        checkCraft = true;
    }

    @Override
    public void onStackChanged(UIInventorySlot slot, ItemStack previous)
    {
        if(slot == slots[40] || slot == slots[41] || slot == slots[42] || slot == slots[43] || slot == slots[44] || slot == slots[45])
        {
            checkCraft = true;
        }
    }


}

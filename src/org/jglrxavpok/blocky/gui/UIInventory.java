package org.jglrxavpok.blocky.gui;

import java.util.ArrayList;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.crafting.Craft;
import org.jglrxavpok.blocky.crafting.CraftingManager;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.opengl.FontRenderer;
import org.jglrxavpok.opengl.Tessellator;
import org.lwjgl.input.Keyboard;

/**
 * <font color="red">WARNING : ☢ Code to redo ☢</font> 
 * @author Epharos & jglrxavpok
 *
 *
 */
public class UIInventory extends UIContainer
{
	private boolean checkCraft;

    public UIInventory(EntityPlayer p)
	{
	    super(p, 44);
		for(int i = 0 ; i < 10 ; i++)
		{
			this.slots[i] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + i * 32 + i + 3, BlockyMain.height / 2 - 32 - 3 - 32 - 20);
		}
		
		for(int i = 0 ; i < 10 ; i++)
		{
			this.slots[i + 10] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + i * 32 + i + 3, BlockyMain.height / 2 - 32 - 3);
		}
		
		
		for(int i = 0 ; i < 10 ; i++)
		{
			this.slots[i + 20] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + i * 32 + i + 3, BlockyMain.height / 2);
		}
		
		for(int i = 0 ; i < 10 ; i++)
		{
			this.slots[i + 30] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + i * 32 + i + 3, BlockyMain.height / 2 + 32 + 3);
		}
		
		for(int i = 0 ; i < p.inv.getInventorySize() ; i++)
		{
			this.slots[i].setStack(p.inv.getStackIn(i));
		}
		
		for(int i = 0;i<3;i++)
		{
		    this.slots[i+40] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 32 + 5+320, BlockyMain.height / 2 - 32 - 3 +(32+3)*i);
		}
		
		this.slots[43] = new UICraftResultSlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 32 + 5+320+32+32, BlockyMain.height / 2 - 32 - 3 +32+3);
		for(int i = 40;i<44;i++)
		    slots[i].addListener(this);
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
		super.render(mx, my, buttonsPressed);
		
		FontRenderer.drawShadowedString("Inventory", BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 3, BlockyMain.height / 2 + 32 + 3+32+3, 0xFFFFFF);
		FontRenderer.drawShadowedString("Hotbar", BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 3, BlockyMain.height / 2 - 32-32+13, 0xFFFFFF);
		
		FontRenderer.drawShadowedString("Crafting", BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 32 + 5+320, BlockyMain.height / 2 + 32 + 3+32+3, 0xFFFFFF);
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
	    for(int i = 40;i<44;i++)
	        player.dropStack(slots[i].stack);
	}
	
	public void update(int mx, int my, boolean[] buttonsPressed)
	{
	    if(BlockyMain.instance.getLevel() != null)
	    {
	        BlockyMain.instance.getLevel().tick();
	    }
		super.update(mx, my, buttonsPressed);
		
		if(checkCraft)
        {
            ArrayList<Craft> crafts = CraftingManager.getInstance().getResults(new ItemStack[]
                    {
                    slots[40].stack == null ? new ItemStack() : slots[40].stack,
                    slots[41].stack == null ? new ItemStack() : slots[41].stack,
                    slots[42].stack == null ? new ItemStack() : slots[42].stack,
                    });
            if(crafts.size() > 0)
            {
                //Temporary
                Craft c = crafts.get(0);
                if(slots[43].stack != null)
                {
                    player.dropStack(slots[43].stack);
                }
                slots[43].stack = c.getOutput().clone();
            }
            else
            {
                slots[43].stack = null;
            }
            checkCraft = false;
        }
	}

    @Override
    public void onStackRemoved(UIInventorySlot s, ItemStack previous)
    {
        if(s == slots[43] && previous != null)
        {
            slots[40].stack = null;
            slots[41].stack = null;
            slots[42].stack = null;
        }
        if(s == slots[40] || s == slots[41] || s == slots[42])
        {
            checkCraft = true;
        }
    }

    @Override
    public void onStackChanged(UIInventorySlot slot, ItemStack previous)
    {
        if(slot == slots[40] || slot == slots[41] || slot == slots[42])
        {
            checkCraft = true;
        }
    }
}

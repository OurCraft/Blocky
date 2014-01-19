package org.jglrxavpok.blocky.gui;

import java.util.ArrayList;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.crafting.Craft;
import org.jglrxavpok.blocky.crafting.CraftingManager;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.ui.UIMenu;
import org.jglrxavpok.opengl.FontRenderer;
import org.jglrxavpok.opengl.Tessellator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * <font color="red">WARNING : ☢ Code to redo ☢</font> 
 * @author Epharos & jglrxavpok
 *
 *
 */
public class UIInventory extends UIMenu implements SlotListener
{
	public UIInventorySlot[] slots = new UIInventorySlot[44];
	public EntityPlayer player;
	public long timer = 0;
	
	public UIInventory(EntityPlayer p)
	{
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
		this.player = p;
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
		
		for(int i = 0 ; i < this.slots.length ; i++)
		{
			this.slots[i].render(mx, my, buttonsPressed);
		}
		
		if(player.selectedStack != null)
		{
			player.selectedStack.item.renderInventory(mx-10, my-10, 20, 20);
			
			if(player.selectedStack.nbr > 1)
        	{
        		FontRenderer.drawString(player.selectedStack.nbr + "", mx-10 - FontRenderer.getWidth("" + player.selectedStack.nbr) + 27, my-5-10, 0xFFFFFF);
        	}
		}
		
		FontRenderer.drawShadowedString("Inventory", BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 3, BlockyMain.height / 2 + 32 + 3+32+3, 0xFFFFFF);
		FontRenderer.drawShadowedString("Hotbar", BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 3, BlockyMain.height / 2 - 32-32+13, 0xFFFFFF);
		
		FontRenderer.drawShadowedString("Crafting", BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 32 + 5+320, BlockyMain.height / 2 + 32 + 3+32+3, 0xFFFFFF);
	}
	
	public void onKeyEvent(char c, int key, boolean action)
	{
	    if(action)
	    if(key == Keyboard.KEY_E)
	    {
	        if(player.selectedStack != null)
	        {
	            ItemStack stack = player.selectedStack;
	            player.dropStack(stack);
                player.selectedStack = null;
	        }
	        
	        for(int i = 40;i<43;i++)
	        {
	            if(slots[i].stack != null)
	            {
	                ItemStack stack = slots[i].stack;
	                player.dropStack(stack);
	            }
	        }
	        UI.displayMenu(null);
	    }
	}
	
	public void update(int mx, int my, boolean[] buttonsPressed)
	{
	    if(BlockyMain.instance.getLevel() != null)
	    {
	        BlockyMain.instance.getLevel().tick();
	    }
		super.update(mx, my, buttonsPressed);
		
		if(Mouse.isButtonDown(0) && this.timer + 200L < System.currentTimeMillis()) //lors du clic gauche
		{
			this.timer = System.currentTimeMillis();
			
			if(this.player.selectedStack == null)
			{
				for(int i = 0 ; i < this.slots.length ; i++)
				{
					if(slots[i].isMouseOver(mx, my))
					{
						if(slots[i].stack != null)
						{
							this.player.selectedStack = slots[i].stack;
							slots[i].stack = (ItemStack) null;
							slots[i].onStackRemoved(player.selectedStack);
							player.inv.putStack(i, slots[i].stack);
						}
					}
				}
			}
			else
			{
			    boolean flag = false;
				for(int i = 0 ; i < this.slots.length ; i++)
				{
				    if(slots[i].canStackBePut(player.selectedStack))
				    {
    					if(slots[i].isMouseOver(mx, my))
    					{
    					    flag = true;
    						if(slots[i].stack == null)
    						{
    							slots[i].stack = this.player.selectedStack;
    							this.player.selectedStack = (ItemStack) null;
    							slots[i].onStackChanged(null);
    							player.inv.putStack(i, slots[i].stack);
    						}
    						else
    						{
    							if(ItemStack.areItemStacksEquals(player.selectedStack, slots[i].stack))
    							{
    								if(slots[i].stack.nbr < slots[i].stack.item.getMaxInStack())
    								{
    									if(slots[i].stack.nbr + player.selectedStack.nbr <= slots[i].stack.item.getMaxInStack())
    									{
    										slots[i].stack.nbr += player.selectedStack.nbr;
    										player.selectedStack = (ItemStack) null;
    										player.inv.putStack(i, slots[i].stack);
    									}
    									else if(slots[i].stack.nbr + player.selectedStack.nbr > slots[i].stack.item.getMaxInStack())
    									{
    										int toRemove = slots[i].stack.item.getMaxInStack() - player.selectedStack.nbr;
    										
    										slots[i].stack.nbr = slots[i].stack.item.getMaxInStack();
    										slots[i].onStackChanged(slots[i].stack);
    										player.selectedStack.nbr -= toRemove;
    										player.inv.putStack(i, slots[i].stack);
    									}
    								}
    							}
    							else
    							{
    								ItemStack temp = slots[i].stack;
    								ItemStack tmp = slots[i].stack;
    	                            slots[i].stack = player.selectedStack;
    	                            slots[i].onStackChanged(tmp);
    								player.selectedStack = temp;
    								player.inv.putStack(i, slots[i].stack);
    							}
    						}
    					}
				    }
				    else if(slots[i].isMouseOver(mx, my))
				        flag = true;
				}
				if(!flag)
				{
				    ItemStack stack = player.selectedStack;
				    player.dropStack(stack);
	                player.selectedStack = null;
				}
			}
		}
		else if(Mouse.isButtonDown(1) && this.timer + 200L < System.currentTimeMillis()) //et lors du clic droit
		{
			this.timer = System.currentTimeMillis();
			
			if(this.player.selectedStack == null)
			{
				for(int i = 0 ; i < this.slots.length ; i++)
				{
					if(slots[i].isMouseOver(mx, my))
					{
						if(slots[i].stack != null)
						{
							ItemStack toPlayer = ItemStack.copyTo(slots[i].stack);
							toPlayer.nbr /= 2;
							ItemStack toSlot = ItemStack.copyTo(slots[i].stack);
							toSlot.nbr -= toPlayer.nbr;
							this.player.selectedStack = toPlayer;
							ItemStack tmp = slots[i].stack;
                            slots[i].stack = toSlot;
                            slots[i].onStackChanged(tmp);
							player.inv.putStack(i, slots[i].stack);
						}
					}
				}
			}
			else
			{
				for(int i = 0 ; i < this.slots.length ; i++)
				{
					if(slots[i].isMouseOver(mx, my))
					{
						if(slots[i].stack == null)
						{
							ItemStack toSlot = ItemStack.copyTo(player.selectedStack);
							toSlot.nbr = 1;
							player.selectedStack.nbr--;
							ItemStack tmp = slots[i].stack;
							slots[i].stack = toSlot;
							slots[i].onStackChanged(tmp);
							player.inv.putStack(i, slots[i].stack);
						}
						else
						{
							if(ItemStack.areItemStacksEquals(player.selectedStack, slots[i].stack))
							{
								if(slots[i].stack.nbr < slots[i].stack.item.getMaxInStack())
								{
								    ItemStack tmp = slots[i].stack;
									slots[i].stack.nbr++;
									slots[i].onStackChanged(tmp);
									player.inv.putStack(i, slots[i].stack);
									player.selectedStack.nbr--;
								}
							}
						}
					}
				}
			}
		}
		
		if(player.selectedStack != null)
		{
			if(player.selectedStack.nbr <= 0)
			{
				player.selectedStack = (ItemStack) null;
			}
		}
		
		for(int i = 0;i<player.inv.getInventorySize();i++)
		{
		    slots[i].stack = player.inv.getStackIn(i);
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
            ArrayList<Craft> crafts = CraftingManager.getInstance().getResults(new ItemStack[]
                    {
                    slots[40].stack == null ? new ItemStack() : slots[40].stack,
                    slots[41].stack == null ? new ItemStack() : slots[41].stack,
                    slots[42].stack == null ? new ItemStack() : slots[42].stack
                    });
            if(crafts.size() > 0)
            {
                //Temporary
                Craft c = crafts.get(0);
                if(slots[43].stack != null)
                {
                    player.dropStack(slots[43].stack);
                }
                slots[43].stack = c.getOutput();
            }
            else
            {
                slots[43].stack = null;
            }
        }
    }

    @Override
    public void onStackChanged(UIInventorySlot slot, ItemStack previous)
    {
        if(slot == slots[40] || slot == slots[41] || slot == slots[42])
        {
            ArrayList<Craft> crafts = CraftingManager.getInstance().getResults(new ItemStack[]
                    {
                    slots[40].stack == null ? new ItemStack() : slots[40].stack,
                    slots[41].stack == null ? new ItemStack() : slots[41].stack,
                    slots[42].stack == null ? new ItemStack() : slots[42].stack
                    });
            if(crafts.size() > 0)
            {
                //Temporary
                Craft c = crafts.get(0);
                if(slots[43].stack != null)
                {
                    player.dropStack(slots[43].stack);
                }
                slots[43].stack = c.getOutput();
            }
            else
            {
                slots[43].stack = null;
            }
        }
    }
}

package org.jglrxavpok.blocky.gui;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.tileentity.TileEntityFurnace;
import org.jglrxavpok.blocky.ui.UIInventorySlot;
import org.jglrxavpok.blocky.ui.UIMenu;
import org.jglrxavpok.opengl.FontRenderer;
import org.lwjgl.input.Mouse;

public class UIFurnace extends UIMenu 
{
	public UIInventorySlot[] slots = new UIInventorySlot[43];
	public EntityPlayer player;
	public long timer = 0;
	
	public UIFurnace(EntityPlayer p, TileEntityFurnace tileEntity)
	{
		for(int i = 0 ; i < 10 ; i++)
		{
			this.slots[i] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + i * 32 + i + 3, BlockyMain.height / 2 - 32 - 3 - 32 - 12);
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
		
		this.slots[40] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 0 * 32 + 0 + 3, BlockyMain.height / 2 + 32 + 3);
		this.slots[41] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 2 * 32 + 2 + 3, BlockyMain.height / 2 + 32 + 3);
		this.slots[42] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 1 * 32 + 1 + 3, BlockyMain.height / 2 + 64 + 6);
		
		this.slots[40].setStack(tileEntity.in);
		this.slots[41].setStack(tileEntity.out);
		this.slots[42].setStack(tileEntity.fire);
		
		this.player = p;
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
			player.selectedStack.item.renderInventory(mx, my, 20, 20);
			
			if(player.selectedStack.nbr > 1)
        	{
        		FontRenderer.drawString(player.selectedStack.nbr + "", mx - FontRenderer.getWidth("" + player.selectedStack.nbr) + 32, my, 0xFFFFFF);
        	}
		}
	}
	
	public void update(int mx, int my, boolean[] buttonsPressed)
	{
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
							slots[i].stack = this.player.selectedStack;
							this.player.selectedStack = (ItemStack) null;
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
									}
									else if(slots[i].stack.nbr + player.selectedStack.nbr > slots[i].stack.item.getMaxInStack())
									{
										int toRemove = slots[i].stack.item.getMaxInStack() - player.selectedStack.nbr;
										
										slots[i].stack.nbr = slots[i].stack.item.getMaxInStack();
										player.selectedStack.nbr -= toRemove;
									}
								}
							}
							else
							{
								ItemStack temp = slots[i].stack;
								slots[i].stack = player.selectedStack;
								player.selectedStack = temp;
							}
						}
					}
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
							ItemStack toPlayer = ItemStack.copy(slots[i].stack);
							toPlayer.nbr /= 2;
							ItemStack toSlot = ItemStack.copy(slots[i].stack);
							toSlot.nbr -= toPlayer.nbr;
							this.player.selectedStack = toPlayer;
							slots[i].stack = toSlot;
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
							ItemStack toSlot = ItemStack.copy(player.selectedStack);
							toSlot.nbr = 1;
							player.selectedStack.nbr--;
							slots[i].stack = toSlot;
						}
						else
						{
							if(ItemStack.areItemStacksEquals(player.selectedStack, slots[i].stack))
							{
								if(slots[i].stack.nbr < slots[i].stack.item.getMaxInStack())
								{
									slots[i].stack.nbr++;
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
		
		for(int i = 0 ; i < player.inv.getInventorySize() ; i++)
		{
			player.inv.putStack(i, slots[i].stack);
		}
	}
}

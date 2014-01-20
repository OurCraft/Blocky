package org.jglrxavpok.blocky.gui;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.crafting.FurnaceManager;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.tileentity.TileEntityFurnace;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.ui.UIMenu;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.opengl.FontRenderer;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.opengl.Textures;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class UIFurnace extends UIMenu 
{
	public UIInventorySlot[] slots = new UIInventorySlot[43];
	public EntityPlayer player;
	public long timer = 0;
	public TileEntityFurnace tile;
	public World theWorld;
	
	public UIFurnace(EntityPlayer p, TileEntityFurnace tileEntity, World world)
	{
		this.tile = tileEntity;
		this.theWorld = world;
		
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
	       
		
		for(int i = 0 ; i < p.inv.getInventorySize() ; i++)
		{
			this.slots[i].setStack(p.inv.getStackIn(i));
		}
		
		this.slots[40] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 4 * 32 + 4 + 3, BlockyMain.height / 2 + 32 + 3);
		this.slots[41] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 6 * 32 + 6 + 3, BlockyMain.height / 2 + 32 + 3);
		this.slots[42] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 3 * 32 + 5 + 3, BlockyMain.height / 2 + 32 + 3);
		
		this.slots[40].setStack((ItemStack) tileEntity.in);
		this.slots[41].setStack((ItemStack) tileEntity.out);
		this.slots[42].setStack((ItemStack) tileEntity.fire);
		
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
	
	public void onKeyEvent(char c, int key, boolean action)
	{
	    if(action)
	    if(key == Keyboard.KEY_E || key == Keyboard.KEY_ESCAPE)
	    {
	        onMenuClose();
	        UI.displayMenu(null);
	    }
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
		
		FontRenderer.drawString("Burn Time : " + this.tile.burnTime + " ; CookedTime : " + this.tile.cookedTime, mx - FontRenderer.getWidth("Burn Time : " + this.tile.burnTime + " ; CookedTime : " + this.tile.cookedTime) + 32, my, 0xFFFFFF);
	
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
        
        FontRenderer.drawShadowedString("Furnace", BlockyMain.width / 2 - (128) - 36, BlockyMain.height / 2 + 32 - 50 + 3 + 32 + 3 + 32 + 3 + 32, 0xFFFFFF);
		
		Textures.bind("/assets/textures/ui/furnaceCookTime.png");
		
		t.startDrawingQuads();
		float minU = 0 / 128f;
        float maxU = 30;
        float minV = 96f / 128f;
        float maxV = 128f / 128f;
        float x1 = BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 5 * 32 + 5 + 3;
        float y1 = BlockyMain.height / 2 + 32 + 3;
        t.addVertexWithUV(x1, y1, 0, minU, minV);
        t.addVertexWithUV(x1 + w, y1, 0, maxU, minV);
        t.addVertexWithUV(x1 + w, y1 + h, 0, maxU, maxV);
        t.addVertexWithUV(x1, y1 + h, 0, minU, maxV);
        
        float coeff = 0;
        if(this.tile.in != null)
        	coeff = this.tile.cookedTime / FurnaceManager.instance().getFurnaceRecipeByIn(this.tile.in).burnTime;
        
        minU = 30 / 128f;
        maxU = 30 + coeff / 128f;
        minV = 96f / 128f;
        maxV = 128f / 128f;
        x1 = BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 5 * 32 + 5 + 3;
        y1 = BlockyMain.height / 2 + 32 + 3;
        t.addVertexWithUV(x1, y1, 0, minU, minV);
        t.addVertexWithUV(x1 + w, y1, 0, maxU, minV);
        t.addVertexWithUV(x1 + w, y1 + h, 0, maxU, maxV);
        t.addVertexWithUV(x1, y1 + h, 0, minU, maxV);
        
        
        t.flush();
	}
	
	public void update(int mx, int my, boolean[] buttonsPressed)
	{
		super.update(mx, my, buttonsPressed);
		
		this.slots[40].setStack((ItemStack) tile.in);
		this.slots[41].setStack((ItemStack) tile.out);
		this.slots[42].setStack((ItemStack) tile.fire);
		
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
							ItemStack toPlayer = ItemStack.copyTo(slots[i].stack);
							toPlayer.nbr /= 2;
							ItemStack toSlot = ItemStack.copyTo(slots[i].stack);
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
							ItemStack toSlot = ItemStack.copyTo(player.selectedStack);
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
		
		this.tile.in = this.slots[40].stack;
		this.tile.out = this.slots[41].stack;
		this.tile.fire = this.slots[42].stack;
	}
	
	public boolean doesPauseGame()
    {
        return false;
    }
}

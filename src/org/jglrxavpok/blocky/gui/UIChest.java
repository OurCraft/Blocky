package org.jglrxavpok.blocky.gui;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.tileentity.TileEntityChest;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.opengl.FontRenderer;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.opengl.Textures;
import org.lwjgl.input.Keyboard;

public class UIChest extends UIContainer 
{
	public TileEntityChest tileEntity;
	
	public UIChest(EntityPlayer p, TileEntityChest tile) 
	{
		super(p, 80);
		
		this.tileEntity = tile;
		
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
        
        //Chest Inventory
        
        for(int i = 0 ; i < 10 ; i++)
        {
            this.slots[i + 40] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + i * 32 + i + 3, BlockyMain.height / 2-50 + 32 + 3 + 32 + 20);
        }
        
        for(int i = 0 ; i < 10 ; i++)
        {
            this.slots[i + 50] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + i * 32 + i + 3, BlockyMain.height / 2-50 + 32 + 3 + 20 + 3 + 32 + 32);
        }
        
        for(int i = 0 ; i < 10 ; i++)
        {
            this.slots[i + 60] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + i * 32 + i + 3, BlockyMain.height / 2-50 + 32 + 3 + 20 + 32 + 3 + 32 + 3 + 32);
        }
        
        for(int i = 0 ; i < 10 ; i++)
        {
            this.slots[i + 70] = new UIInventorySlot(BlockyMain.width / 2 - 16 - (32 * 4) - 12 + i * 32 + i + 3, BlockyMain.height / 2-50 + 32 + 3 + 20 + 32 + 3 + 32 + 3 + 32 + 3 + 32);
        }
        
        for(int i = 0 ; i < p.inv.getInventorySize() ; i++)
        {
            this.slots[i].setStack(p.inv.getStackIn(i));
        }
        
        for(int i = 0 ; i < tile.chestInventory.getInventorySize() ; i++)
        {
        	this.slots[i + 40].setStack(tile.chestInventory.getStackIn(i));
        }
	}
	
	public void onMouseEvent(int mx, int my, int button, boolean action)
    {
        super.onMouseEvent(mx, my, button, action);
        
        for(int i = 0 ; i < this.tileEntity.chestInventory.getInventorySize() ; i++)
        {
        	this.tileEntity.chestInventory.putStack(i, this.slots[i + 40].stack);
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
        t.addVertex(BlockyMain.width / 2 - (128) - 36, BlockyMain.height / 2+60 + 78 + 64, 0);
        t.setColorRGBA_F(0.5f,0.5f,0.5f, 1);
        t.addVertex(BlockyMain.width / 2 - (32 * 4) - 12 + 10 * 32 + 13+2, BlockyMain.height / 2+60 + 78 + 64, 0);
        t.addVertex(BlockyMain.width / 2 - (32 * 4) - 12 + 10 * 32 + 13+2, BlockyMain.height / 2+60 + 78-2 + 64, 0);
        t.setColorRGBA_F(1,1,1, 1);
        t.addVertex(BlockyMain.width / 2 - (128) - 36, BlockyMain.height / 2+60 + 78-2 + 64, 0);

        t.setColorRGBA_F(0.5f,0.5f,0.5f, 1);
        t.addVertex(BlockyMain.width / 2 - (128) - 36, BlockyMain.height / 2 - 75 - 52-12, 0);
        t.addVertex(BlockyMain.width / 2 - (128) - 36+2, BlockyMain.height / 2 - 75 - 52-12, 0);
        t.setColorRGBA_F(1,1,1, 1);
        t.addVertex(BlockyMain.width / 2 - (128) - 36+2, BlockyMain.height / 2+60 + 78 + 64, 0);
        t.addVertex(BlockyMain.width / 2 - (128) - 36, BlockyMain.height / 2+60 + 78 + 64, 0);
        
        
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

        t.addVertex(BlockyMain.width / 2 - (32 * 4) - 12 + 10 * 32 + 13+2, BlockyMain.height / 2+60 + 78 + 64, 0);
        t.addVertex(BlockyMain.width / 2 - (32 * 4) - 12 + 10 * 32 + 13, BlockyMain.height / 2+60 + 78 + 64, 0);
        t.flush();
        t.setColorRGBA_F(1,1,1, 1);
        super.render(mx, my, buttonsPressed);
        
        FontRenderer.drawShadowedString("Inventory", BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 3, BlockyMain.height / 2 + 32-50 + 3+32+3, 0xFFFFFF);
        FontRenderer.drawShadowedString("Hotbar", BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 3, BlockyMain.height / 2 - 32-32+13-50, 0xFFFFFF);
        
        FontRenderer.drawShadowedString("Chest", BlockyMain.width / 2 - 16 - (32 * 4) - 12 + 3, BlockyMain.height / 2+60 + 78 + 39, 0xFFFFFF);
        
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
}

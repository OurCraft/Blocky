package org.jglrxavpok.blocky.tileentity;

import java.util.Random;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.entity.EntityItem;
import org.jglrxavpok.blocky.inventory.BasicInventory;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.storage.TaggedStorageChunk;

public class TileEntityChest extends TileEntity 
{
	public BasicInventory chestInventory = new BasicInventory(40);
	
	public TileEntityChest(World w) 
	{
		super(w);
	}

	public void dropItems()
	{
		Random rand = new Random();
		EntityItem item = null;
		
		BlockyMain.console("TileEntityChest starts droping");
		
		for(int i = 0 ; i < chestInventory.getInventorySize() ; i++)
		{
			ItemStack stack = chestInventory.getStackIn(i);
			
			if(stack != null)
			{
				item = new EntityItem(ItemStack.copyTo(stack));
				item.move(this.posX, this.posY);
				item.x += rand.nextInt(4) - 2;
				item.y += rand.nextInt(2);
				this.theWorld.addEntity(item);
				
				if(this.theWorld.getEntityByID(item.entityID) != null)
					BlockyMain.console("My entity exists");
					
				BlockyMain.console("Drop chest item " + ItemStack.copyTo(stack) + " at [" + item.x + " ; " + item.y + "]");
			}
		}
		
		BlockyMain.console("TileEntityChest stops droping");
	}
	
	public TaggedStorageChunk save(int nbr)
	{
		TaggedStorageChunk chunk = super.save(nbr);
		
		this.chestInventory.write("chestInventory", chunk);
		
		return chunk;
	}
	
	public void load(TaggedStorageChunk chunk)
	{
		this.chestInventory.read("chestInventory", chunk);
		
		super.load(chunk);
	}
}

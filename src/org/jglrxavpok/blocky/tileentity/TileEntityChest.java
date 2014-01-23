package org.jglrxavpok.blocky.tileentity;

import java.util.Random;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.block.Block;
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
		
		for(int i = 0 ; i < chestInventory.getInventorySize() ; i++)
		{
			ItemStack stack = chestInventory.getStackIn(i);
			
			if(stack != null)
			{
				item = new EntityItem(ItemStack.copyTo(stack));
				item.move(posX * Block.BLOCK_WIDTH + Block.BLOCK_WIDTH / 2 - item.w / 2 + rand.nextInt(30) - 15, posY * Block.BLOCK_HEIGHT + Block.BLOCK_HEIGHT / 2 - item.h / 2 + rand.nextInt(10) + 5);
		        item.vy = 1.5f;
		        item.gravityEfficienty = 0.5f;
		        
				this.theWorld.addEntity(item);
				
			}
		}
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

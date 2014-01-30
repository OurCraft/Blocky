package org.jglrxavpok.blocky.block;

import org.jglrxavpok.blocky.entity.EntityItem;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.world.World;

public class BlockGrass extends Block 
{	
	public BlockGrass(String name, boolean isSnow) 
	{
		super(name);
		
		if(!isSnow)
		{
			this.setTextureFromTerrain(12, 0, 6, 6);
		}
		else
		{
			this.setTextureFromTerrain(24, 18, 6, 6);
		}
	}

	public boolean isSolid() 
	{
		return true;
	}

	public boolean canBlockBeReplaced(int x, int y, World lvl, Block block) 
	{
		return false;
	}

	public boolean isOpaqueCube() 
	{
		return true;
	}
	
	public void dropItems(World w, int x, int y)
	{
        EntityItem item = new EntityItem(new ItemStack(Block.dirt.getItem(), 1));
        item.move(x*Block.BLOCK_WIDTH+Block.BLOCK_WIDTH/2-item.w/2, y*Block.BLOCK_HEIGHT+Block.BLOCK_HEIGHT/2-item.h/2);
        item.vy = 1.5f;
        item.gravityEfficienty = 0.5f;
        w.addEntity(item);
	}

	public float setBlockOpacity() 
	{
		return 0.75f;
	}
	
	public void update(int x, int y, World lvl)
	{
	    super.update(x, y, lvl);
	    
	    if(lvl.getBlockAt(x, y + 1).equals("snow"))
	    {
	    	if(!lvl.getBlockAt(x, y).equals("grassSnow"))
	    	{
	    		lvl.setBlock(x, y, Block.grassSnow.getBlockName());
	    	}
	    }
	    else
	    {
	    	if(!lvl.getBlockAt(x, y).equals("grass"))
	    	{
	    		lvl.setBlock(x, y, Block.grass.getBlockName());
	    	}
	    }
	}
	
	
}

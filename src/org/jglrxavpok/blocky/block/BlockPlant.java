package org.jglrxavpok.blocky.block;

import org.jglrxavpok.blocky.world.World;

public class BlockPlant extends Block
{
	public BlockPlant(String name) 
	{
		super(name);
	}
	
	public boolean letLightGoThrough()
    {
        return true;
    }

	public boolean isSolid() 
	{
		return false;
	}

	public boolean canBlockBeReplaced(int x, int y, World lvl, Block block) 
	{
		return true;
	}
	public boolean isOpaqueCube() 
	{
		return false;
	}

	public float setBlockOpacity() 
	{
		return 0;
	}
	
	public void update(int x, int y, World lvl)
	{
		super.update(x, y, lvl);
		
		if(lvl.getBlockAt(x, y - 1).equals("air") || !Block.getBlock(lvl.getBlockAt(x, y - 1)).isSolid()) 
		{			
			lvl.setBlock(x, y, "air");
		}
	}
}

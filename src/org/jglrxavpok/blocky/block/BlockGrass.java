package org.jglrxavpok.blocky.block;

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

	public float setBlockOpacity() 
	{
		return 0.75f;
	}
	
	public void update(int x, int y, World lvl)
	{
	    super.update(x, y, lvl);
	    
	    if(lvl.getBlockAt(x, y + 1).equals("snow"))
	    {
	    	lvl.setBlock(x, y, Block.grassSnow.getBlockName());
	    }
	    else
	    {
	    	lvl.setBlock(x, y, Block.grass.getBlockName());
	    }
	}
	
	
}

package org.jglrxavpok.blocky.block;

import org.jglrxavpok.blocky.world.World;

public class BlockCactus extends Block
{

	public BlockCactus(String name) 
	{
		super(name);
		this.setResistance(0);
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
		return false;
	}

	public float setBlockOpacity() 
	{
		return 0;
	}

	public void update(int x, int y, World lvl)
	{
		super.update(x, y, lvl);
		
		if(lvl.getBlockAt(x, y - 1).equals("cactus") || lvl.getBlockAt(x, y - 1).equals("sand")) {}
		else
		{
			lvl.setBlock(x, y, "air");
			this.dropItems(lvl, x, y);
		}
		
		if(rand.nextInt(16000) == 8000)
		{
			if(!lvl.getBlockAt(x, y - 1).equals("cactus"))
			{
				lvl.setBlock(x, y, "cactus");
			}
		}
	}
	
}

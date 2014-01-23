package org.jglrxavpok.blocky.block;

import org.jglrxavpok.blocky.tileentity.TileEntity;
import org.jglrxavpok.blocky.world.World;

public abstract class BlockContainer extends Block 
{
	public BlockContainer(String name) 
	{
		super(name);
	}
	
	public abstract TileEntity createTileEntity(World w, float x, float y);
}

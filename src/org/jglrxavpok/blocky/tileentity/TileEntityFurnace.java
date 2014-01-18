package org.jglrxavpok.blocky.tileentity;

import org.jglrxavpok.blocky.inventory.ItemStack;

public class TileEntityFurnace extends TileEntity 
{
	public ItemStack in, out, fire;
	public int burnTime = 0;
	
	public int getBurnTimeByStack(ItemStack i)
	{
		switch(i.item.id)
		{
		
		}
		
		return 0;
	}
}

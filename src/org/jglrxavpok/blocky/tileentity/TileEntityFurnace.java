package org.jglrxavpok.blocky.tileentity;

import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.world.World;

public class TileEntityFurnace extends TileEntity 
{
	public TileEntityFurnace(World w)
    {
        super(w);
    }

    public ItemStack in, out, fire;
	public int burnTime = 0;
	
	public int getBurnTimeByStack(ItemStack i)
	{
		if(i.item.id.equals("Something"))
		{
		
		}
		
		return 0;
	}
}

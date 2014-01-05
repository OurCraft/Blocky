package org.jglrxavpok.blocky.inventory;

import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.entity.EntityPlayerSP;
import org.jglrxavpok.blocky.items.Item;
import org.jglrxavpok.blocky.world.World;

public class ItemStack
{

	public Item item;
	public int	nbr;

	public ItemStack(Item item, int nbr)
	{
		this.item = item;
		this.nbr = nbr;
	}

	public ItemStack()
	{
	}
	
	public void use(Entity owner, int x, int y, World lvl)
	{
	    if(item != null)
	    item.onUse(this, owner, x, y, lvl);
	}

    public void update(Entity owner, float x, float y, World lvl)
    {
        if(item != null)
            item.update(owner, x,y,lvl);
    }

    public int getStrengthAgainstBlock(Entity owner, int tx, int ty, World lvl)
    {
        if(item != null)
            return item.getStrengthAgainstBlock(owner,this,tx,ty,lvl);
        return 0;
    }
}

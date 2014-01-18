package org.jglrxavpok.blocky.inventory;

import org.jglrxavpok.blocky.entity.Entity;
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

    public float getStrengthAgainstEntity(Entity owner, Entity e, World world)
    {
        if(item != null)
            return item.getStrengthEntity(owner,this,e,world);
        return 0f;
    }
    
    public static boolean areItemStacksEquals(ItemStack is, ItemStack is1)
    {
    	return is.item.id.equals(is1.item.id);
    }
    
    public String toString()
    {
    	return "ID : " + this.item.id + " ; quantité : " + this.nbr;
    }
    
    public static ItemStack copy(ItemStack is)
    {
    	ItemStack i = new ItemStack();
    	i.item = is.item;
    	i.nbr = is.nbr;
    	return i;
    }
}

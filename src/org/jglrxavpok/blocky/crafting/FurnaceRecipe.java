package org.jglrxavpok.blocky.crafting;

import org.jglrxavpok.blocky.inventory.ItemStack;

public class FurnaceRecipe 
{
	public ItemStack in, out;
	public int burnTime;
	
	public FurnaceRecipe(ItemStack i, ItemStack o, int time)
	{
		this.in = i;
		this.out = o;
		this.burnTime = time;
	}
}

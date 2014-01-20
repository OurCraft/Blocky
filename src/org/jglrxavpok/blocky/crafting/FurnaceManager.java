package org.jglrxavpok.blocky.crafting;

import java.util.ArrayList;
import java.util.List;

import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.items.Item;

public class FurnaceManager 
{
	public final List<FurnaceRecipe> furnaceRecipe = new ArrayList<FurnaceRecipe>();
	private static FurnaceManager instance;
	
	public FurnaceManager()
	{
		this.addFurnaceRecipe(new ItemStack(Block.iron.getItem(), 1), new ItemStack(Item.ironIngot, 1), 700);
		this.addFurnaceRecipe(new ItemStack(Block.log.getItem(), 1), new ItemStack(Item.coal, 1), 500);
	}
	
	public void addFurnaceRecipe(ItemStack in, ItemStack out, int time)
	{
		furnaceRecipe.add(new FurnaceRecipe(in, out, time));
	}
	
	public static FurnaceManager instance()
	{
		if(instance == null)
		{
			instance = new FurnaceManager();
		}
		
		return instance;
	}
	
	public FurnaceRecipe getFurnaceRecipeByIn(ItemStack is)
	{
		for(int i = 0 ; i < furnaceRecipe.size() ; i++)
		{
			if(ItemStack.areItemStacksEquals(furnaceRecipe.get(i).in, is))
			{
				return furnaceRecipe.get(i);
			}
		}
		
		return null;
	}
}

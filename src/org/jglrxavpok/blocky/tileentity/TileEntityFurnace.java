package org.jglrxavpok.blocky.tileentity;

import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.crafting.FurnaceManager;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.items.Item;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.storage.TaggedStorageChunk;

public class TileEntityFurnace extends TileEntity 
{
	public TileEntityFurnace(World w)
    {
        super(w);
    }

    public ItemStack in, out, fire;
	public int burnTime = 0;
	public int cookedTime = 0;
	
	public int getBurnTimeByStack(ItemStack i)
	{
		if(i.item.id.equals(Block.planks.getItem().id))
		{
			return 600;
		}
		else if(i.item.id.equals(Block.log.getItem().id))
		{
			return 800;
		}
		else if(i.item.id.equals(Item.coal.id))
		{
			return 1800;
		}
		else if(i.item.id.equals(Item.door.id))
		{
			return 200;
		}
		
		return 0;
	}
	
	public void onUpdate()
	{
		if(this.burnTime < 0)
		{
			this.burnTime = 0;
		}
		
		if(this.burnTime > 0)
		{
			this.burnTime--;
		}
		
		if(this.burnTime == 0 && (this.fire != null && this.in != null))
		{
			if(this.out != null)
			{
				if((ItemStack.areItemStacksEquals((ItemStack) this.in, (ItemStack) this.out)))
				{
					if(this.getBurnTimeByStack(this.fire) > 0)
					{
						this.burnTime += this.getBurnTimeByStack(this.fire);
						
						this.fire.nbr--;
						
						if(this.fire.nbr <= 0)
						{
							this.fire = (ItemStack) null;
						}
					}
				}
			}
			else
			{
				if(this.getBurnTimeByStack(this.fire) > 0)
				{
					this.burnTime += this.getBurnTimeByStack(this.fire);
					
					this.fire.nbr--;
					
					if(this.fire.nbr <= 0)
					{
						this.fire = (ItemStack) null;
					}
				}
			}
		}
		
		if(this.in == null)
		{
			this.cookedTime = 0;
		}
		else
		{
			if(this.burnTime > 0)
			{
				this.cookedTime++;
			}
			
			if(FurnaceManager.instance().getFurnaceRecipeByIn(this.in) != null)
			{
				if(FurnaceManager.instance().getFurnaceRecipeByIn(this.in).burnTime <= this.cookedTime)
				{
					this.in.nbr--;
					this.cookedTime = 0;
					this.out = FurnaceManager.instance().getFurnaceRecipeByIn(this.in).out;
					
					if(this.in.nbr <= 0)
					{
						this.in = (ItemStack) null;
					}
				}
			}
		}
	}
	
	public TaggedStorageChunk save(int nbr)
	{
		TaggedStorageChunk chunk = super.save(nbr);
		chunk.setInteger("cookedTime", this.cookedTime);
		chunk.setInteger("burnTime", this.burnTime);
		
		chunk.setString("inID", this.in.item.id);
		chunk.setString("outID", this.out.item.id);
		chunk.setString("fireID", this.fire.item.id);
		
		chunk.setInteger("innbr", this.in.nbr);
		chunk.setInteger("outnbr", this.out.nbr);
		chunk.setInteger("firenbr", this.fire.nbr);
		return chunk;
	}
	
	public void load(TaggedStorageChunk chunk)
	{
		this.cookedTime = chunk.getInteger("cookedTime");
		this.burnTime = chunk.getInteger("burnTime");
		
		this.in = new ItemStack(Item.get(chunk.getString("inID")), chunk.getInteger("innbr"));
		this.out = new ItemStack(Item.get(chunk.getString("outID")), chunk.getInteger("outnbr"));
		this.fire = new ItemStack(Item.get(chunk.getString("fireID")), chunk.getInteger("firenbr"));
		
		super.load(chunk);
	}
	
}

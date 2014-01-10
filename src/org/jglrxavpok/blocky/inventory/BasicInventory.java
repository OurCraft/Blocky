package org.jglrxavpok.blocky.inventory;

import org.jglrxavpok.blocky.items.Item;
import org.jglrxavpok.storage.TaggedStorageChunk;

public class BasicInventory implements Inventory
{

	private int	size;
	private ItemStack[]	slots;

	public BasicInventory(int size)
	{
		this.slots = new ItemStack[size];
		this.size = size;
	}
	
	@Override
	public int getInventorySize()
	{
		return size;
	}

	@Override
	public ItemStack getStackIn(int slot)
	{
		if(slot < 0 || slot >= slots.length)
			return null;
		if(slots[slot] != null && slots[slot].nbr <= 0)
		{
		    slots[slot] = null;
		}
		return slots[slot];
	}

	@Override
	public void putStack(int slot, ItemStack stack)
	{
		if(slot < 0 || slot >= slots.length)
			return;
		slots[slot] = stack;
	}
	
	public int getNextCompatibleIndex(Item item, int min)
	{
		for(int i = min;i<slots.length;i++)
		{
			ItemStack s = getStackIn(i);
			if(s != null && s.nbr > 0 && s.item.equals(item) && s.nbr < item.getMaxInStack())
				return i;
		}
		return getNextCompletlyFreeIndex();
	}
	
	public int getNextCompletlyFreeIndex()
	{
		return getNextCompletlyFreeIndex(0);
	}
	
	public int getNextCompletlyFreeIndex(int min)
	{
		for(int i = min;i<slots.length;i++)
		{
			ItemStack s = getStackIn(i);
			if(s == null || s.nbr <= 0)
				return i;
		}
		return -1;
	}

	@Override
	public ItemStack tryAdd(ItemStack stack)
	{
		int free = getNextCompatibleIndex(stack.item, 0);
		if(free == -1)
			return stack;
		else
		{ 
			ItemStack comp = getStackIn(free);
			if(comp == null)
				comp = new ItemStack();
			if(comp.nbr+stack.nbr <= stack.item.getMaxInStack())
			{
				this.putStack(free, new ItemStack(stack.item, stack.nbr+comp.nbr));
				return null;
			}
			else
			{
				boolean flag = true;
				do
				{
					free = getNextCompatibleIndex(stack.item, 0);
					if(free == -1)
						flag = false;
					ItemStack is = getStackIn(free);
					stack.nbr-=stack.item.getMaxInStack()-(is == null ? 0 : is.nbr);
					
					ItemStack added = new ItemStack(stack.item,stack.item.getMaxInStack());
					this.putStack(free, added);
					if(stack.nbr <= 0)
						return null;
					else if(stack.nbr <= stack.item.getMaxInStack())
						return tryAdd(stack);
				}while(flag);
				return stack;
			}
		}
	}

	@Override
	public boolean isStackCompatible(ItemStack stack)
	{
		return true;
	}

    @Override
    public void write(String string, TaggedStorageChunk chunk)
    {
        chunk.setInteger(string+"_Size", size);
        for(int i=0;i<size;i++)
        {
            if(slots[i] != null && slots[i].item != null)
            {
                chunk.setString(string+"_Slot"+i+"_ID", slots[i].item.id);
                chunk.setInteger(string+"_Slot"+i+"_Nbr", slots[i].nbr);
            }
        }
    }
    
    @Override
    public void read(String prefix, TaggedStorageChunk chunk)
    {
        size = chunk.getInteger(prefix+"_Size");
        slots = new ItemStack[size];
        for(int i = 0;i<size;i++)
        {
            if(!chunk.hasTag(prefix+"_Slot"+i+"_ID"))
                continue;
            String id = chunk.getString(prefix+"_Slot"+i+"_ID");
            if(id != null)
            {
                Item item = Item.get(id);
                if(item == null)
                    continue;
                slots[i] = new ItemStack(item, chunk.getInteger(prefix+"_Slot"+i+"_Nbr"));
            }
        }
    }

}

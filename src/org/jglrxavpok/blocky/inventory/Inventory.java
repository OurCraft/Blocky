package org.jglrxavpok.blocky.inventory;

import org.jglrxavpok.storage.TaggedStorageChunk;

public interface Inventory
{

	public int getInventorySize();
	
	public ItemStack getStackIn(int slot);
	
	public void putStack(int slot, ItemStack stack);
	
	public ItemStack tryAdd(ItemStack stack);
	
	public boolean isStackCompatible(ItemStack stack);

    public void write(String string, TaggedStorageChunk chunk);

    public void read(String string, TaggedStorageChunk chunk);
}

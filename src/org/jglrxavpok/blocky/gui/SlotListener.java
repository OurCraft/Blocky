package org.jglrxavpok.blocky.gui;

import org.jglrxavpok.blocky.inventory.ItemStack;

public interface SlotListener
{

    public void onStackRemoved(UIInventorySlot s, ItemStack previous);

    public void onStackChanged(UIInventorySlot slot, ItemStack previous);
}

package org.jglrxavpok.blocky.gui;

import org.jglrxavpok.blocky.inventory.ItemStack;

public class UICraftResultSlot extends UIInventorySlot
{

    public UICraftResultSlot(int x, int y)
    {
        super(x, y);
    }
    
    public boolean canStackBePut(ItemStack s)
    {
        return false;
    }

}

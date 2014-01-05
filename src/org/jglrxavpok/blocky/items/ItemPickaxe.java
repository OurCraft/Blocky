package org.jglrxavpok.blocky.items;

import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.world.World;

public class ItemPickaxe extends Item
{

    public ItemPickaxe(String id)
    {
        super(id);
    }
    
    public int getStrengthAgainstBlock(Entity owner, ItemStack itemStack, int tx, int ty, World w)
    {
        return 10;
    }

    public void renderHand(float posX, float posY, float width, float h, boolean flipX, Entity owner)
    {
        if(!flipX)
        {
            this.renderInventory(posX, posY, width, h);
        }
        else
            this.renderInventory(posX+width, posY, -width, h);
    }
    
    public boolean shouldBeRotatedAtHandRendering()
    {
        return false;
    }
}

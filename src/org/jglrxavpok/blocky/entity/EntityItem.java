package org.jglrxavpok.blocky.entity;

import org.jglrxavpok.blocky.inventory.BasicInventory;
import org.jglrxavpok.blocky.inventory.ItemStack;

public class EntityItem extends Entity
{


    public EntityItem()
    {
        w = 10;
        h = 10;
        inv = new BasicInventory(1);
    }
    
    public EntityItem(ItemStack stack)
    {
        this();
        inv.putStack(0, stack);
    }
    
    public void onCollide(Entity e)
    {
        if(e instanceof EntityPlayer)
        {
            if(e.addToInventory(inv.getStackIn(0)) == null)
                die();
        }
    }
    
    public void render(float posx, float posy, float a)
    {
        inv.getStackIn(0).item.renderInventory(posx, posy, w, h);
    }
    
    public void dropItems(){}
}

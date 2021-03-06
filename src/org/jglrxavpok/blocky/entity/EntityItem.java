package org.jglrxavpok.blocky.entity;

import org.jglrxavpok.blocky.inventory.BasicInventory;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.server.WorldServer;

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
        if(inv.getStackIn(0) == null || inv.getStackIn(0).item == null)
            die();
        if(e instanceof EntityPlayer)
        {
            if(!this.world.isRemote)
            {
                if(e.addToInventory(inv.getStackIn(0)) == null)
                {
                    needUpdate = true;
                    die();
                }
            }
        }
    }
    
    public void tick()
    {
        if(inv.getStackIn(0) == null || inv.getStackIn(0).item == null)
            die();
        super.tick();
    }
    
    public void render(float posx, float posy, float a)
    {
        if(inv !=null && inv.getStackIn(0) != null && inv.getStackIn(0).item != null)
        inv.getStackIn(0).item.renderInventory(posx, posy, w, h);
    }
    
    public void dropItems(){}
}

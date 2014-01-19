package org.jglrxavpok.blocky.items;

import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.block.BlockDoor;
import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.utils.AABB;
import org.jglrxavpok.blocky.world.World;

public class ItemDoor extends Item
{

    public ItemDoor()
    {
        super("door");
    }
    
    public void onUse(ItemStack s, Entity owner, int x, int y, World lvl)
    {
        AABB aabb = new AABB(x*Block.BLOCK_WIDTH,y*Block.BLOCK_HEIGHT,Block.BLOCK_WIDTH, Block.BLOCK_HEIGHT*2);
        if(lvl.getEntitiesInAABB(aabb, null).size() == 0)
        {
            Block above = Block.getBlock(lvl.getBlockAt(x, y+1));
            Block old = Block.getBlock(lvl.getBlockAt(x, y));
            if(above != Block.outBlock && above.canBlockBeReplaced(x, y+1, lvl, BlockDoor.closed) && old != Block.outBlock && old.canBlockBeReplaced(x, y, lvl, BlockDoor.closed));
            {
                lvl.setBlock(x,y,BlockDoor.closed.getBlockName());
                lvl.setBlock(x,y+1,BlockDoor.closed.getBlockName());
                if(owner instanceof EntityPlayer)
                {
                    Block.doorClosed.onBlockAdded(lvl, (EntityPlayer) owner, x, y);
                    s.nbr--;
                }
            }
        }
    }

}

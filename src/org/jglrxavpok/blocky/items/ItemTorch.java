package org.jglrxavpok.blocky.items;

import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.world.World;

public class ItemTorch extends ItemBlock
{

    public ItemTorch()
    {
        super(Block.torch);
    }
    
    public void update(Entity owner, float x, float y, World lvl)
    {
//        if(x >= 0)
//            Block.torch.onWorldUpdate((int)(x/Block.BLOCK_WIDTH), (int)(y/Block.BLOCK_HEIGHT)+1, lvl);
//        else
//            Block.torch.onWorldUpdate((int)(x/Block.BLOCK_WIDTH)-1, (int)(y/Block.BLOCK_HEIGHT)+1, lvl);
    }

}

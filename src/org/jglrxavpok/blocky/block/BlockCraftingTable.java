package org.jglrxavpok.blocky.block;

import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.gui.UICraftingTable;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.world.World;

public class BlockCraftingTable extends Block
{

    public BlockCraftingTable(String name)
    {
        super(name);
        this.setTextureFromTerrain(24, 12, 6, 6);
    }
    
    public boolean onRightClick(World w, EntityPlayer p, int x, int y)
    {
        UI.displayMenu(new UICraftingTable(p,x,y));
        return true;
    }

    @Override
    public boolean isSolid()
    {
        return true;
    }

    @Override
    public boolean canBlockBeReplaced(int x, int y, World lvl, Block block)
    {
        return true;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return true;
    }

    @Override
    public float setBlockOpacity()
    {
        return 0.75f;
    }

}

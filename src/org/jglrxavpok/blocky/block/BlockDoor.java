package org.jglrxavpok.blocky.block;

import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.world.World;

public class BlockDoor extends Block
{

    public static final BlockDoor opened = new BlockDoor(false); 
    public static final BlockDoor closed = new BlockDoor(true); 
        public BlockDoor(boolean closed)
    {
        super("door_"+(closed ? "closed" : "open"));
    }

    public void update(int x, int y, World lvl)
    {
        super.update(x,y,lvl);
        EntityPlayer player =lvl.getNearestPlayer(x*Block.BLOCK_WIDTH, y*Block.BLOCK_HEIGHT, 2f*Block.BLOCK_WIDTH);
        if(player == null)
            player = lvl.getNearestPlayer(x*Block.BLOCK_WIDTH, (y-1)*Block.BLOCK_HEIGHT, 2f*Block.BLOCK_WIDTH);
        String current = getBlockName();
        if(player == null)
        {
            if(this == opened)
            {
                System.out.println("Closed!");
            }
            current = closed.getBlockName();
        }
        else
        {
            if(this == closed)
            {
                System.out.println("Opened!");
            }
            current = opened.getBlockName();
        }
            
        Block under = Block.getBlock(lvl.getBlockAt(x, y-1));
        Block above = Block.getBlock(lvl.getBlockAt(x, y+1));
        if(under != this && under instanceof BlockDoor)
        {
            lvl.setBlock(x,y-1,current);
        }
        if(above != opened && above != this && above instanceof BlockDoor)
        {
            lvl.setBlock(x,y+1,current);
        }
        if(!(under instanceof BlockDoor))
        if(under != Block.outBlock && under.isSolid())
        {
            if(!(under instanceof BlockDoor))
            {
                if(above.canBlockBeReplaced(x,y+1,lvl,this))
                {
                    lvl.setBlock(x, y+1, current);
                }
            }
            if(!(above instanceof BlockDoor))
                current = "air";
        }
        else
        {
            current = "air";
        }
        if(!current.equals(lvl.getBlockAt(x, y)))
            lvl.setBlock(x, y, current);
    }
    
    @Override
    public boolean isSolid()
    {
        return opened != this;
    }
    
    public void render(float posX, float posY, int x, int y, World lvl, boolean selected)
    {
        Block under = Block.getBlock(lvl.getBlockAt(x, y-1));
        Block above = Block.getBlock(lvl.getBlockAt(x, y+1));
        float offset = 0;
        if(under instanceof BlockDoor)
        {
            offset = 1;
        }
        else if(above instanceof BlockDoor)
        {
            offset = 0;
        }
        if(this == closed)
            offset+=2;
        this.setTextureFromTerrain((int) (12+6*offset), 6, 6, 6);
        
        super.render(posX, posY, x, y, lvl, selected);
    }

    @Override
    public boolean canBlockBeReplaced(int x, int y, World lvl, Block block)
    {
        return false;
    }

}

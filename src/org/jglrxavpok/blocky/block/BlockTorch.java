package org.jglrxavpok.blocky.block;

import java.awt.Color;

import org.jglrxavpok.blocky.items.Item;
import org.jglrxavpok.blocky.items.ItemTorch;
import org.jglrxavpok.blocky.world.Particle;
import org.jglrxavpok.blocky.world.World;

public class BlockTorch extends Block
{

    public BlockTorch(String name)
    {
        super(name);
        setTextureFromTerrain(0, 12, 12, 12);
        setResistance(1f);
    }
    
    public Item getItem()
    {
        Item item = Item.get("torch");
        if(item == null)
            item = new ItemTorch();
        return item;
    }
    
    public void update(int x, int y, World w)
    {
        if(!Block.getBlock(w.getBlockAt(x, y-1)).isSolid())
        {
            w.setBlock(x,y,"air");
        }
    }
    
    public void onWorldUpdate(int x, int y, World lvl)
    {
        if(rand.nextInt(100) == 1 && Block.getBlock(lvl.getBlockAt(x, y)) == this)
        {
            Particle p = new Particle();
            p.addKillerBlock(Block.getBlock("water_All"));
            p.setColor(rand.nextBoolean() ? Color.red.getRGB() : Color.orange.getRGB());
            p.setPos(x*Block.BLOCK_WIDTH+Block.BLOCK_WIDTH/2, y*Block.BLOCK_HEIGHT+Block.BLOCK_HEIGHT/2);
            p.setVelocity(rand.nextFloat()*5-2.5f, 4f);
            p.setLife(60);
            lvl.spawnParticle(p);
        }
        lvl.setLightValue(1f,x,y);
        int power = (int)(Math.sin(lvl.ticks/20f)*0.01f+15f);
        Block.litWorld(x,y,power,lvl);
    }

    @Override
    public boolean isSolid()
    {
        return false;
    }
    
    public boolean letLightGoThrough()
    {
        return true;
    }

    @Override
    public boolean canBlockBeReplaced(int x, int y, World lvl, Block block)
    {
        return block instanceof BlockFluid;
    }
    
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    public float setBlockOpacity()
    {
        return 0f;
    }

}

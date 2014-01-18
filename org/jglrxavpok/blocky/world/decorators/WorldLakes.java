package org.jglrxavpok.blocky.world.decorators;

import java.util.Random;

import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.entity.EntityFish;
import org.jglrxavpok.blocky.utils.Fluid;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldChunk;

public class WorldLakes extends WorldDecorator
{

    private Fluid fluid;
    private int minHeight;
    private int widthMax;
    private int widthMin;
    private int maxDepth;
    private int rarity;
    private int maxHeight;
    private String around;
    private boolean fishes;

    public WorldLakes(Fluid fluid, int minHeight, int maxHeight, int widthMin, int widthMax, int maxDepth, int rarity, String surroundingBlock, boolean fishes)
    {
        this.fluid = fluid;
        this.fishes = fishes;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.widthMin = widthMin;
        this.widthMax = widthMax;
        this.maxDepth = maxDepth;
        this.rarity = rarity;
        this.around = surroundingBlock;
    }
    
    @Override
    public void decorateChunk(WorldChunk chunk, World w, Random rand)
    {
        int oddLakeInChunk = rand.nextInt(rarity);
        boolean hasFish = false;
        if(oddLakeInChunk == 0)
        {
            int x = rand.nextInt(8);
            int width = rand.nextInt(widthMax-widthMin)+widthMin;
            int depth = 0;
            int height = rand.nextInt(maxHeight-minHeight)+minHeight;
            for(int xx = 0;xx<width*2;xx++)
            {
                if(xx < width)
                {
                    depth++;
                }
                else if(xx > width)
                {
                    depth--;
                }
                if(depth >= maxDepth)
                    depth = maxDepth;
                int xPos = xx+x+chunk.chunkID*16;
                for(int i = height;i>height-depth;i--)
                {
                    w.setBlock(xPos, i, Block.getBlock(fluid.getName()+"_All").getBlockName());
                    int oddFish = rand.nextInt(5);
                    if(fishes && ((hasFish == false && i==height-depth+1) || oddFish == 0))
                    {
                        EntityFish fish = new EntityFish();
                        fish.move(xPos*Block.BLOCK_WIDTH, i*Block.BLOCK_HEIGHT);
                        w.addEntity(fish);
                    }
                }
                w.setBlock(xPos, height-depth, around);
                w.setBlock(xPos, height-depth-1, around);
            }
            int xPos = x+chunk.chunkID*16;
            w.setBlock(xPos-1,height,around);
            w.setBlock(xPos-1,height-1,around);
            w.setBlock(xPos-2,height,around);
            w.setBlock(xPos+width*2,height,around);
            w.setBlock(xPos+width*2+1,height,around);
            w.setBlock(xPos+width*2,height-1,around);
        }
    }

}

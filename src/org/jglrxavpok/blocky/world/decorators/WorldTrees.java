package org.jglrxavpok.blocky.world.decorators;

import java.util.Random;

import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.entity.EntityPig;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldChunk;

public class WorldTrees extends WorldDecorator
{

    private int rarity;

    public WorldTrees(int rarity)
    {
        this.rarity = rarity;
    }
    
    @Override
    public void decorateChunk(WorldChunk chunk, World w, Random rand)
    {
        xAxis : for(int x = 0;x<16;x++)
        {
            int height = chunk.getMaxHeight(x)+1;
            {
                int oddTree = rand.nextInt(rarity);
                if(oddTree == 1 && Block.getBlock(chunk.getBlock(x, height-1)) == Block.grass && 
                        Block.getBlock(chunk.getBlock(x, height+4)) == Block.air
                     && Block.getBlock(chunk.getBlock(x, height+3)) == Block.air
                     && Block.getBlock(chunk.getBlock(x, height+2)) == Block.air
                     && Block.getBlock(chunk.getBlock(x, height+1)) == Block.air
                     && Block.getBlock(chunk.getBlock(x, height)) == Block.air)
                {
                    w.setBlock(x+chunk.chunkID*16, height, "log");
                    w.setBlock(x+chunk.chunkID*16, height+1, "log");
                    w.setBlock(x+chunk.chunkID*16, height+2, "leaves");
                    w.setBlock(x-1+chunk.chunkID*16, height+2, "leaves");
                    w.setBlock(x+1+chunk.chunkID*16, height+2, "leaves");
                    w.setBlock(x+chunk.chunkID*16, height+3, "leaves");
                    w.setBlock(x+1+chunk.chunkID*16, height+3, "leaves");
                    w.setBlock(x-1+chunk.chunkID*16, height+3, "leaves");
                    w.setBlock(x+chunk.chunkID*16, height+4, "leaves");
                    
                    EntityPig testPig = new EntityPig();
                    testPig.move((x+chunk.chunkID*16)*Block.BLOCK_WIDTH, (height+5)*Block.BLOCK_HEIGHT);
                    w.addEntity(testPig);
                    continue xAxis;
                }
            }
        }
    }

}

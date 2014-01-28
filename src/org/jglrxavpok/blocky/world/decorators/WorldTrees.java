package org.jglrxavpok.blocky.world.decorators;

import java.util.Random;

import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.entity.EntityPig;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldChunk;

public class WorldTrees extends WorldDecorator
{

    private int rarity;
    private boolean isDarkWood = false;

    public WorldTrees(int rarity)
    {
        this.rarity = rarity;
    }
    
    public WorldTrees(int rarity, boolean wood)
    {
    	this(rarity);
    	this.isDarkWood = wood;
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
                	if(!this.isDarkWood)
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
                	}
                	else
                	{
                		int randInt = rand.nextInt(3);
                		
                		if(randInt == 0)
                		{
	                		w.setBlock(x + chunk.chunkID * 16, height, "logDark");
	                		w.setBlock(x + chunk.chunkID * 16, height + 1, "logDark");
	                		
	                		w.setBlock(x + chunk.chunkID * 16 - 2, height + 2, "leavesDark");
	                		w.setBlock(x + chunk.chunkID * 16 - 1, height + 2, "leavesDark");
	                		w.setBlock(x + chunk.chunkID * 16, height + 2, "leavesDark");
	                		w.setBlock(x + chunk.chunkID * 16 + 1, height + 2, "leavesDark");
	                		w.setBlock(x + chunk.chunkID * 16 + 2, height + 2, "leavesDark");
	                		
	                		w.setBlock(x + chunk.chunkID * 16 - 1, height + 3, "leavesDark");
	                		w.setBlock(x + chunk.chunkID * 16, height + 3, "leavesDark");
	                		w.setBlock(x + chunk.chunkID * 16 + 1, height + 3, "leavesDark");
	                		
	                		w.setBlock(x + chunk.chunkID * 16, height + 4, "leavesDark");
                		}
                		else if(randInt == 1)
                		{
                			w.setBlock(x + chunk.chunkID * 16, height, "logDark");
	                		
	                		w.setBlock(x + chunk.chunkID * 16 - 2, height + 1, "leavesDark");
	                		w.setBlock(x + chunk.chunkID * 16 - 1, height + 1, "leavesDark");
	                		w.setBlock(x + chunk.chunkID * 16, height + 1, "leavesDark");
	                		w.setBlock(x + chunk.chunkID * 16 + 1, height + 1, "leavesDark");
	                		w.setBlock(x + chunk.chunkID * 16 + 2, height + 1, "leavesDark");
	                		
	                		w.setBlock(x + chunk.chunkID * 16 - 1, height + 2, "leavesDark");
	                		w.setBlock(x + chunk.chunkID * 16, height + 2, "leavesDark");
	                		w.setBlock(x + chunk.chunkID * 16 + 1, height + 2, "leavesDark");
	                		
	                		w.setBlock(x + chunk.chunkID * 16, height + 3, "leavesDark");
                		}
                		else
                		{
                			w.setBlock(x + chunk.chunkID * 16, height, "logDark");
	                		
	                		w.setBlock(x + chunk.chunkID * 16 - 1, height + 1, "leavesDark");
	                		w.setBlock(x + chunk.chunkID * 16, height + 1, "leavesDark");
	                		w.setBlock(x + chunk.chunkID * 16 + 1, height + 1, "leavesDark");
	                		
	                		w.setBlock(x + chunk.chunkID * 16, height + 2, "leavesDark");
	                		
                		}
                		
                	}
                    
                    EntityPig testPig = new EntityPig();
                    testPig.move((x+chunk.chunkID*16)*Block.BLOCK_WIDTH, (height+5)*Block.BLOCK_HEIGHT);
                    w.addEntity(testPig);
                    continue xAxis;
                }
            }
        }
    }

}

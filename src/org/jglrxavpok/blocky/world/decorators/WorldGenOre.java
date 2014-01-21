package org.jglrxavpok.blocky.world.decorators;

import java.util.Random;

import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldChunk;

public class WorldGenOre extends WorldDecorator 
{
	public String ore;
    private int genLoops;
    private int minHeight;
    private int maxHeight;
    private int size;
	
	public WorldGenOre(String oreBlock, int genLoops, int size, int minHeight, int maxHeight)
	{
		this.ore = oreBlock;
		this.genLoops = genLoops;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		this.size = size;
	}
	
	public void decorateChunk(WorldChunk chunk, World w, Random rand) 
	{
		for(int gen = 0 ; gen < genLoops ; gen++)
        {
            int width = rand.nextInt(16);
            int height = rand.nextInt(maxHeight-minHeight)+minHeight;
            
            for(int i = 0 ; i < rand.nextInt(size) ; i++)
            {               
                int width1 = width + rand.nextInt(6) - 3;
                int height1 = height + rand.nextInt(6) - 3;
                
                if(width1 < 0)
                {
                    width1 = 0;
                }
                
                if(height1 < minHeight)
                {
                    height1 = minHeight;
                }
                
                if(width1 >= 16)
                {
                    width1 = 15;
                }
                
                if(height1 >= maxHeight)
                {
                    height1 = maxHeight;
                }
                
                if(chunk.getBlock(width1, height1).equals("rock"))
                {
                    w.setBlock(chunk.chunkID * 16 + width1, height1, ore);
                }
            }
        }
	}
}

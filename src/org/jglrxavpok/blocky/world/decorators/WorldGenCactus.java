package org.jglrxavpok.blocky.world.decorators;

import java.util.Random;

import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldChunk;

public class WorldGenCactus extends WorldDecorator 
{
	public void decorateChunk(WorldChunk chunk, World w, Random rand) 
	{
		for(int x = 0 ; x < 16 ; x++)
		{
			if(rand.nextInt(16) == 8)
			{
				w.setBlock(x + chunk.chunkID * 16, chunk.getMaxHeight(x) + 1, "cactus");
			}
		}
	}
}

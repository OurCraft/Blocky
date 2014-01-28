package org.jglrxavpok.blocky.world.decorators;

import java.util.Random;

import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldChunk;

public class WorldGenPlant extends WorldDecorator 
{
	public void decorateChunk(WorldChunk chunk, World w, Random rand) 
	{
		int randInt = 0;
		int theTop = 0;
		
		for(int i = 0 ; i < 16 ; i++)
		{
			randInt = rand.nextInt(10);
			theTop = chunk.getMaxHeight(i) + 1;
			
			if(w.getBlockAt(16 * chunk.chunkID + i, theTop).equals("air"))
			{
				if(randInt == 3)
				{
					if(rand.nextBoolean())
					{
						w.setBlock(chunk.chunkID * 16 + i, theTop, "flowerRed");
					}
				}
				else if(randInt == 7)
				{
					if(rand.nextBoolean())
					{
						w.setBlock(chunk.chunkID * 16 + i, theTop, "flowerWhite");
					}
				}
				else
				{
					if(rand.nextBoolean())
					{
						w.setBlock(chunk.chunkID * 16 + i, theTop, "herb");
					}
				}
				
			}
		}
	}
}

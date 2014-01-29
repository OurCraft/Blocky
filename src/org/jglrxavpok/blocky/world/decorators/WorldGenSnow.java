package org.jglrxavpok.blocky.world.decorators;

import java.util.Random;

import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldChunk;

public class WorldGenSnow extends WorldDecorator 
{

	public void decorateChunk(WorldChunk chunk, World w, Random rand) 
	{
		for(int x = 0 ; x < 16 ; x++)
		{
			int height = 150;
			
			do
			{
				height--;
				
				if(!w.getBlockAt(x + chunk.chunkID * 16, height).equals("air") || height <= 0)
				{
					break;
				}
			}
			while(true);
			
			w.setBlock(x + chunk.chunkID * 16, height + 1, "snow");
			
			if(w.getBlockAt(x + chunk.chunkID * 16, chunk.getMaxHeight(x) + 1).equals("air"))
				w.setBlock(x + chunk.chunkID * 16, chunk.getMaxHeight(x) + 1, "snow");
		}
	}

}

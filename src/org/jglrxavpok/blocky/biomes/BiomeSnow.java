package org.jglrxavpok.blocky.biomes;

import org.jglrxavpok.blocky.world.WorldChunk;
import org.jglrxavpok.blocky.world.decorators.WorldGenSnow;
import org.jglrxavpok.blocky.world.decorators.WorldTrees;

public class BiomeSnow extends Biome 
{

	public BiomeSnow() 
	{
		super("snow", "grass", "dirt");
		this.setHasDifferentGeneration();
		this.addToWorldDecorator(new WorldTrees(10, true), new WorldGenSnow());
	}

	public void customGeneration(int lastHeight, int x, int y, WorldChunk chunk)
	{
		for (int y1 = 1; y1 < y; y1++)
        {
            chunk.setBlock(x, y1, "rock");
        }
		
		int bottomHeight = y + 2;
		
        for (int y1 = y - rand.nextInt(2); y1 < bottomHeight; y1++)
        {
            chunk.setBlock(x, y1, this.bottomBlock);
        }
        chunk.setBlock(x, bottomHeight, this.topBlock);
        chunk.setBlock(x, 0, "bedrock");
        
        
	}
}

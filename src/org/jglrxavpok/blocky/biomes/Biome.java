package org.jglrxavpok.blocky.biomes;

import java.util.*;

import org.jglrxavpok.blocky.world.WorldChunk;
import org.jglrxavpok.blocky.world.decorators.WorldDecorator;
import org.jglrxavpok.blocky.world.decorators.WorldGenCactus;
import org.jglrxavpok.blocky.world.decorators.WorldGenPlant;
import org.jglrxavpok.blocky.world.decorators.WorldGenSnow;
import org.jglrxavpok.blocky.world.decorators.WorldTrees;

public class Biome 
{
	public String topBlock, bottomBlock;
	
	public List<WorldDecorator> decorator = new ArrayList<WorldDecorator>();
	
	public boolean hasDifferentGeneration = false;
	
	public String biomeId;
	
	public static List<Biome> biomeList = new ArrayList<Biome>();
	
	public Random rand = new Random();
	
	public static final Biome plain = new Biome("plain", "grass", "dirt").addToWorldDecorator(new WorldGenPlant());
	public static final Biome desert = new Biome("desert", "sand", "sand").addToWorldDecorator(new WorldGenCactus());
	public static final Biome snow = new Biome("snow", "grass", "dirt").addToWorldDecorator(new WorldTrees(10, true), new WorldGenSnow());
	public static final Biome forest = new Biome("forest", "grass", "dirt").addToWorldDecorator(new WorldTrees(7));
	
	public Biome(String bId, String tB, String bB)
	{
		this.biomeId = bId;
		this.topBlock = tB;
		this.bottomBlock = bB;
		biomeList.add(this);
	}
	
	public Biome setHasDifferentGeneration()
	{
		this.hasDifferentGeneration = true;
		return this;
	}
	
	public Biome addToWorldDecorator(WorldDecorator ... world)
	{
		for(int i = 0 ; i < world.length ; i++)
		{
			this.decorator.add(world[i]);
		}
		
		return this;
	}
	
	public final void generate(int lastHeight, int x, int y, WorldChunk chunk)
	{
		if(this.hasDifferentGeneration)
		{
			this.customGeneration(lastHeight, x, y, chunk);
			return;
		}
		else
		{
			this.simpleGeneration(lastHeight, x, y, chunk);
			return;
		}
	}
	
	public void simpleGeneration(int lastHeight, int x, int y, WorldChunk chunk)
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
	
	public void customGeneration(int lastHeight, int x, int y, WorldChunk chunk)
	{
		
	}

    public static Biome getBiomeByID(String biomeID)
    {
        for(Biome b : biomeList)
        {
            if(b.biomeId.equals(biomeID))
                return b;
        }
        return forest;
    }
}

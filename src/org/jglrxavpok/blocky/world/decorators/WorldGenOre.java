package org.jglrxavpok.blocky.world.decorators;

import java.util.Random;

import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldChunk;

public class WorldGenOre extends WorldDecorator 
{
	public String ore;
	
	public WorldGenOre(String s)
	{
		this.ore = s;
	}
	
	public void decorateChunk(WorldChunk chunk, World w, Random rand) 
	{
		if(this.ore.equals("coal"))
		{			
			for(int gen = 0 ; gen < 15 ; gen++)
			{
				int width = rand.nextInt(16);
				int height = rand.nextInt(128);
				
				for(int i = 0 ; i < rand.nextInt(12) ; i++)
				{				
					int width1 = width + rand.nextInt(6) - 3;
					int height1 = height + rand.nextInt(6) - 3;
					
					if(width1 < 0)
					{
						width1 = 0;
					}
					
					if(height1 < 0)
					{
						height1 = 0;
					}
					
					if(width1 >= 16)
					{
						width1 = 15;
					}
					
					if(height1 >= 128)
					{
						height1 = 127;
					}
					
					if(chunk.getBlock(width1, height1).equals("rock"))
					{
						w.setBlock(chunk.chunkID * 16 + width1, height1, "coal");
					}
				}
			}
		}
		
		if(this.ore.equals("iron"))
		{
			for(int gen = 0 ; gen < 9 ; gen++)
			{
				int width = rand.nextInt(16);
				int height = rand.nextInt(128);
				
				for(int i = 0 ; i < rand.nextInt(6) ; i++)
				{				
					int width1 = width + rand.nextInt(6) - 3;
					int height1 = height + rand.nextInt(6) - 3;
					
					if(width1 < 0)
					{
						width1 = 0;
					}
					
					if(height1 < 0)
					{
						height1 = 0;
					}
					
					if(width1 >= 16)
					{
						width1 = 15;
					}
					
					if(height1 >= 128)
					{
						height1 = 127;
					}
					
					if(chunk.getBlock(width1, height1).equals("rock"))
					{
						w.setBlock(chunk.chunkID * 16 + width1, height1, "iron");
					}
				}
			}
		}
		
		if(this.ore.equals("diamond"))
		{
			for(int gen = 0 ; gen < 2 ; gen++)
			{
				int width = rand.nextInt(16);
				int height = rand.nextInt(16);
				
				for(int i = 0 ; i < rand.nextInt(4) ; i++)
				{				
					int width1 = width + rand.nextInt(6) - 3;
					int height1 = height + rand.nextInt(6) - 3;
					
					if(width1 < 0)
					{
						width1 = 0;
					}
					
					if(height1 < 0)
					{
						height1 = 0;
					}
					
					if(width1 >= 16)
					{
						width1 = 15;
					}
					
					if(height1 >= 16)
					{
						height1 = 15;
					}
					
					if(chunk.getBlock(width1, height1).equals("rock"))
					{
						w.setBlock(chunk.chunkID * 16 + width1, height1, "diamond");
					}
				}
			}
		}
	}
}

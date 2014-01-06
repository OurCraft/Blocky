package org.jglrxavpok.blocky.world;

import java.util.ArrayList;
import java.util.Random;

import org.jglrxavpok.blocky.utils.Fluid;
import org.jglrxavpok.blocky.world.decorators.WorldDecorator;
import org.jglrxavpok.blocky.world.decorators.WorldLakes;
import org.jglrxavpok.blocky.world.decorators.WorldTrees;

public class WorldGenerator
{

    public static enum WorldType
    {
        NORMAL(), FLAT(), CLIENT();
    }

    public static final WorldGenerator instance   = new WorldGenerator();
    private ArrayList<WorldDecorator>  decorators = new ArrayList<WorldDecorator>();
    private Random                     rand;

    public WorldGenerator()
    {
        this.rand = new Random();
        decorators.add(new WorldLakes(Fluid.get("water"), 102, 103, 3, 8, 6, 4,
                "sand", true));
        decorators.add(new WorldLakes(Fluid.get("lava"), 6, 102, 3, 6, 2, 1,
                "rock", false));
        decorators.add(new WorldTrees(20));
    }

    public void generateChunk(World lvl, int chunkID, WorldChunk chunk,
            WorldType type)
    {
        lvl.handlingChanges = true;
        if(type == WorldType.FLAT)
        {
            for (int x = 0; x < 16; x++)
            {
                chunk.setBlock(x, 0, "bedrock");
                for (int y = 1; y < 10; y++)
                {
                    chunk.setBlock(x, y, "rock");
                }
                for (int y = 10; y < 12; y++)
                {
                    chunk.setBlock(x, y, "dirt");
                }
                chunk.setBlock(x, 12, "grass");
            }
        }
        else if(type == WorldType.NORMAL)
        {
            for (int x = 0; x < 16; x++)
            {
                int height = (int) ((Math.sin(x * (rand.nextInt(12) + 3))) * (rand
                        .nextFloat() * 2f)) + 100;
                for (int y = 1; y < height; y++)
                {
                    chunk.setBlock(x, y, "rock");
                }
                for (int y = height; y < height + 2; y++)
                {
                    chunk.setBlock(x, y, "dirt");
                }
                chunk.setBlock(x, height + 2, "grass");
                chunk.setBlock(x, 0, "bedrock");
            }
            for (WorldDecorator d : decorators)
                d.decorateChunk(chunk, lvl, rand);
        }
        lvl.handlingChanges = false;
    }
}

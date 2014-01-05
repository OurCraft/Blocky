package org.jglrxavpok.blocky.world.decorators;

import java.util.Random;

import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldChunk;

public abstract class WorldDecorator
{

    public WorldDecorator()
    {
        
    }
    
    public abstract void decorateChunk(WorldChunk chunk, World w, Random rand);
}

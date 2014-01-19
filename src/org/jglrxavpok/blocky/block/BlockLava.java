package org.jglrxavpok.blocky.block;

import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.entity.EntityLiving;
import org.jglrxavpok.blocky.utils.DamageType;
import org.jglrxavpok.blocky.utils.Fluid;
import org.jglrxavpok.blocky.world.World;

public class BlockLava extends BlockFluid
{

    public BlockLava(Fluid fluid, int vol)
    {
        super(fluid, vol);
    }
    
    public void onEntityCollide(World world, int gridX, int gridY, Entity entity, boolean b)
    {
        if(entity.canBeHurt(DamageType.lava))
        {
            if(entity instanceof EntityLiving)
            {
                ((EntityLiving) entity).attackEntity(DamageType.lava, 5f);
            }
            else
            {
                entity.die();
            }
        }
    }

}

package org.jglrxavpok.blocky.entity;

import org.jglrxavpok.blocky.utils.DamageType;
import org.jglrxavpok.storage.TaggedStorageChunk;

public abstract class EntityLiving extends Entity
{

    public float life = getMaxLife();
    public float lastLifeAmount;

    public abstract float getMaxLife();
    
    public void attackEntity(DamageType type, float amount)
    {
        if(onAttack(type, amount))
        {
            life-=amount*(1f/this.getArmorMultiplier());
            if(life <= 0.f && onDeath(type, amount))
            {
                alive = false;
                this.die();
            }
            if(life > getMaxLife())
                life = getMaxLife();
        }
    }
    
    public TaggedStorageChunk writeTaggedStorageChunk(int nbr)
    {
        TaggedStorageChunk chunk = super.writeTaggedStorageChunk(nbr);
        chunk.setFloat("life", life);
        return chunk;
    }
    
    public void readFromChunk(TaggedStorageChunk chunk)
    {
        super.readFromChunk(chunk);
        life = chunk.getFloat("life");
    }
    
    public void tick()
    {
        lastLifeAmount = life;
        super.tick();
    }
    
    public boolean canBeHurt(DamageType d)
    {
        return true;
    }

    public boolean onDeath(DamageType type, float amount)
    {
        return true;
    }

    public float getArmorMultiplier()
    {
        return 1;
    }

    public boolean onAttack(DamageType type, float amount)
    {
        return true;
    }
}

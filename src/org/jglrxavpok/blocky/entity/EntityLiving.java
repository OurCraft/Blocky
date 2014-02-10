package org.jglrxavpok.blocky.entity;

import org.jglrxavpok.blocky.utils.DamageType;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.storage.TaggedStorageChunk;

public abstract class EntityLiving extends Entity
{

    public float life = getMaxLife();
    public float lastLifeAmount;
    private int hurtTime;

    public abstract float getMaxLife();
    
    public void attackEntity(DamageType type, float amount)
    {
        if(onAttack(type, amount) && hurtTime <= 0)
        {
            life-=amount*(1f/this.getArmorMultiplier());
            hurtTime = 20;
            if(life <= 0.f && onDeath(type, amount))
            {
                alive = false;
                this.die();
            }
            if(life > getMaxLife())
                life = getMaxLife();
        }
    }
    
    public void attackFrom(DamageType type, float amount)
    {
        if(type.getHasOwner())
        {
            Entity owner = type.getOwner();
            this.knockback((owner.x-x < 0 ? -1f : 1)*5f, 0.5f);
        }

        this.attackEntity(type, amount);
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
        if(chunk.hasTag("life"))
        life = chunk.getFloat("life");
    }
    
    public void tick()
    {
        lastLifeAmount = life;
        if(hurtTime > 0)
            hurtTime--;
        super.tick();
    }
    
    public void render(float posx, float posy, float a)
    {
        if(hurtTime > 0)
            Tessellator.instance.setColorRGBA_F(1, 0, 0, 1);
        else
            Tessellator.instance.setColorRGBA_F(1, 1, 1, 1);
        super.render(posx, posy, a);
        Tessellator.instance.setColorRGBA_F(1, 1, 1, 1);
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

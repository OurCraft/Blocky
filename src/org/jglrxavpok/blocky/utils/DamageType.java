package org.jglrxavpok.blocky.utils;

import org.jglrxavpok.blocky.entity.Entity;

public class DamageType
{

    public static final DamageType lava = new DamageType("lava").setHasOwner(false);
    
    private String id;
    private boolean ownership;

    private Entity owner;

    public DamageType(String id)
    {
        this.id = id;
    }
    
    public String getID()
    {
        return id;
    }
    
    public DamageType clone()
    {
        return new DamageType(id).setHasOwner(ownership);
    }
    
    public DamageType setOwner(Entity owner)
    {
        this.owner = owner;
        return this;
    }
    
    public Entity getOwner()
    {
        return owner;
    }
    
    public boolean getHasOwner()
    {
        return ownership;
    }
    
    public DamageType setHasOwner(boolean hasOwner)
    {
        ownership = hasOwner;
        return this;
    }
}

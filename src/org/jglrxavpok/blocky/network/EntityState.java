package org.jglrxavpok.blocky.network;

import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.storage.TaggedStorageChunk;

public class EntityState
{

    private TaggedStorageChunk entityInfos;

    public EntityState()
    {
        
    }
    
    public EntityState(TaggedStorageChunk chunk)
    {
        this.entityInfos = chunk;
    }

    public void applyToEntity(Entity e)
    {
        e.readFromChunk(entityInfos);
    }
    
    public static EntityState createFromEntity(Entity e)
    {
        if(e == null)
        {
            return null;
        }
        EntityState state = new EntityState(e.writeTaggedStorageChunk(e.entityID));
        return state;
    }

    public TaggedStorageChunk getEntityState()
    {
        return entityInfos;
    }

    public void setEntityState(TaggedStorageChunk chunk)
    {
        entityInfos = chunk;
    }
}

package org.jglrxavpok.blocky.tileentity;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.storage.TaggedStorageChunk;

public class TileEntity 
{
	public float posX, posY;
	public World theWorld;
	public int id;
	
	public TileEntity()
	{
	    theWorld = BlockyMain.instance.getLevel();
		this.setID();
	}
	
	public TileEntity setPos(float x, float y)
	{
		this.posX = x;
		this.posY = y;
		return this;
	}
	
	public void setID()
	{
		int theFinalID = 0;
		
		while(this.isIDAlreadyUsed(theFinalID))
		{
			theFinalID++;
		}
		
		this.id = theFinalID;
	}
	
	public boolean isIDAlreadyUsed(int ID)
	{
		if(!this.theWorld.tileEntities.isEmpty())
			for(int i = 0 ; i < this.theWorld.tileEntities.size() ; i++)
				if(this.theWorld.tileEntities.get(i).id == ID)
					return true;
		
		return false;
	}
	
	public void onUpdate()
	{
		
	}
	
	public TaggedStorageChunk save(int nbr)
	{
		TaggedStorageChunk chunk = new TaggedStorageChunk("TileEntity_" + this.getClass().getCanonicalName() + "_" + nbr);
		chunk.setFloat("posX", this.posX);
		chunk.setFloat("posY", this.posY);
		chunk.setInteger("id", this.id);
		return chunk;
	}
	
	public void load(TaggedStorageChunk chunk)
	{
		this.id = chunk.getInteger("id");
		this.setPos(chunk.getFloat("posX"), chunk.getFloat("posY"));
	}
}

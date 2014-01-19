package org.jglrxavpok.blocky.tileentity;

import org.jglrxavpok.blocky.world.World;

public class TileEntity 
{
	public float posX, posY;
	public World theWorld;
	public int id;
	
	public TileEntity(World w)
	{
	    theWorld = w;
		this.setID();
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
	
	//ce commentaire ne sert à rien :)
}

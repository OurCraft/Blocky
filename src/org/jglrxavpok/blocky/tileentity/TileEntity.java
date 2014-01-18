package org.jglrxavpok.blocky.tileentity;

import org.jglrxavpok.blocky.world.World;

public class TileEntity 
{
	public float posX, posY;
	public World theWorld;
	public int id;
	
	public TileEntity()
	{
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
		if(!this.theWorld.tileEntitys.isEmpty())
			for(int i = 0 ; i < this.theWorld.tileEntitys.size() ; i++)
				if(this.theWorld.tileEntitys.get(i).id == ID)
					return true;
		
		return false;
	}
	
	//ce commentaire ne sert à rien :)
}

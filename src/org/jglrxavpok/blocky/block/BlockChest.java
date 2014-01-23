package org.jglrxavpok.blocky.block;

import java.awt.Color;
import java.util.ArrayList;

import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.gui.UIChest;
import org.jglrxavpok.blocky.gui.UIFurnace;
import org.jglrxavpok.blocky.tileentity.TileEntity;
import org.jglrxavpok.blocky.tileentity.TileEntityChest;
import org.jglrxavpok.blocky.tileentity.TileEntityFurnace;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.world.Particle;
import org.jglrxavpok.blocky.world.World;

public class BlockChest extends BlockContainer 
{
	public BlockChest(String name) 
	{
		super(name);
		this.setResistance(0.7f);
		this.setTextureFromTerrain(30, 12, 6, 6);
	}

	public TileEntity createTileEntity(World w, float x, float y) 
	{
		return new TileEntityChest(w).setPos(x, y); //Ne pas oublier de créer le TileEntity du coffre, ça serait con ...
	}
	
	public boolean onRightClick(World world, EntityPlayer player, int x, int y)
    {
		UI.displayMenu(new UIChest(player, (TileEntityChest) world.getTileEntityAt(x, y)));
		return true;
    }
	
	public boolean onBlockDestroyedByPlayer(String lastAttackPlayerName, int rx, int y, World lvl)
    {
       super.onBlockDestroyedByPlayer(lastAttackPlayerName, rx, y, lvl);
       
       if(lvl.getTileEntityAt(rx, y) != null)
       {
    	   ((TileEntityChest) lvl.getTileEntityAt(rx, y)).dropItems();
    	   lvl.tileEntities.remove(lvl.getTileEntityAt(rx, y));
       }
       
       return true;
    }
	
	public void onBlockAdded(World world, EntityPlayer player, int x, int y)
    {
		if(world.getTileEntityAt(x, y) == null)
			world.tileEntities.add(this.createTileEntity(world, (float) x, (float) y));
    }

	public boolean isSolid()
	{
		return true;
	}

	public boolean canBlockBeReplaced(int x, int y, World lvl, Block block) 
	{
		return false;
	}
	
	public boolean isOpaqueCube() 
	{
		return false;
	}

	public float setBlockOpacity() 
	{
		return 0f;
	}
}

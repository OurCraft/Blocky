package org.jglrxavpok.blocky.block;

import java.awt.Color;
import java.util.ArrayList;

import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.gui.UIFurnace;
import org.jglrxavpok.blocky.tileentity.TileEntity;
import org.jglrxavpok.blocky.tileentity.TileEntityFurnace;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.world.Particle;
import org.jglrxavpok.blocky.world.World;

public class BlockFurnace extends Block
{
	public BlockFurnace(String name, boolean isIdled) 
	{
		super(name);
		this.setResistance(1.5f);
		this.idle = isIdled;
		this.setTextureFromTerrain(!isIdled ? 18 : 24, 30, 6, 6);
	}

	public boolean idle = false;

	@Override
	public boolean isSolid() 
	{
		return true;
	}

	@Override
	public boolean canBlockBeReplaced(int x, int y, World lvl, Block block) 
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube() 
	{
		return true;
	}

	@Override
	public float setBlockOpacity() 
	{
		return 0.7f;
	}
	
	public void onWorldUpdate(int x, int y, World lvl)
    {
		if(this.idle)
		{
	        if(rand.nextInt(5) == 1 && Block.getBlock(lvl.getBlockAt(x, y)) == this)
	        {
	            Particle p = new Particle();
	            p.addKillerBlock(Block.getBlock("water_All"));
	            p.setColor(rand.nextBoolean() ? Color.red.getRGB() : Color.orange.getRGB());
	            p.setPos(x * Block.BLOCK_WIDTH + Block.BLOCK_WIDTH / 2, y * Block.BLOCK_HEIGHT + Block.BLOCK_HEIGHT / 2);
	            p.setVelocity(rand.nextFloat() * 5 - 2.5f, 4f);
	            p.setLife(60);
	            lvl.spawnParticle(p);
	        }
	        
	        lvl.setLightValue(1f,x,y);
	        int power = (int) (Math.sin(lvl.ticks / 20f) * 0.01f + 15f);
	        Block.litWorld(x, y, power, lvl);
		}
		
		if(lvl.getTileEntityAt(x, y) != null)
			if(((TileEntityFurnace) lvl.getTileEntityAt(x, y)).burnTime > 0)
			{
				lvl.setBlock(x, y, Block.furnaceIdle.getBlockName());
			}
			else
			{
				lvl.setBlock(x, y, Block.furnace.getBlockName());
			}
    }
	
	public boolean onRightClick(World world, EntityPlayer player, int x, int y)
    {
		UI.displayMenu(new UIFurnace(player, (TileEntityFurnace) world.getTileEntityAt(x, y), world));
		return true;
    }
	
	public boolean onBlockDestroyedByPlayer(String lastAttackPlayerName, int rx, int y, World lvl)
    {
       super.onBlockDestroyedByPlayer(lastAttackPlayerName, rx, y, lvl);
       
       if(lvl.getTileEntityAt(rx, y) != null)
    	   lvl.tileEntities.remove(lvl.getTileEntityAt(rx, y));
       
       return true;
    }
	
	public void onBlockAdded(World world, EntityPlayer player, int x, int y)
    {
		if(world.getTileEntityAt(x, y) == null)
			world.tileEntities.add(this.createTileEntity(world, (float) x, (float) y));
    }

	public TileEntity createTileEntity(World w, float x, float y)
	{
	    TileEntity e = new TileEntityFurnace(w).setPos(x, y);
		return e;
	}
}

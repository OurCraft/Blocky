package org.jglrxavpok.blocky.items;

import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.utils.AABB;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.opengl.Textures;
import org.lwjgl.opengl.GL11;

public class ItemBlock extends Item
{

	private Block	block;

	public ItemBlock(Block b)
	{
		super("itemBlock."+b.getBlockName());
		this.block = b;
	}
	
	public void renderInventory(float posX, float posY, float width, float h)
	{
		GL11.glPushMatrix();
		Tessellator t = Tessellator.instance;
		Textures.bind("/assets/textures/terrain.png");
		t.startDrawingQuads();
		t.setColorEnabled(true);
		float fX = width/Block.BLOCK_WIDTH;
		float fY = h/Block.BLOCK_HEIGHT;
		GL11.glScalef(fX, fY, 0);
		block.render(posX/fX, posY/fY, 0, 0, World.zeroBlocks);
		t.flush();
		GL11.glPopMatrix();
	}
	
	public Block getBlock()
	{
		return block;
	}
	
	public void onUse(ItemStack s, Entity owner, int x, int y, World lvl)
    {
	    AABB aabb = new AABB(x*Block.BLOCK_WIDTH,y*Block.BLOCK_HEIGHT,Block.BLOCK_WIDTH, Block.BLOCK_HEIGHT);
	    if((lvl.getEntitiesInAABB(aabb, null).size() == 0 || !block.isSolid()) && Block.getBlock(lvl.getBlockAt(x, y)) != block)
	    {
	        lvl.setBlock(x,y,block.getBlockName());
	        if(owner instanceof EntityPlayer)
	        {
	            block.onBlockAdded(lvl, (EntityPlayer) owner, x, y);
	            s.nbr--;
	        }
	    }
    }

	public boolean equals(Object o)
	{
		if(o instanceof ItemBlock)
		{
			ItemBlock i = (ItemBlock)o;
			if(i.block.equals(block))
				return true;
		}
		return false;
	}
}

package org.jglrxavpok.blocky.items;

import java.util.HashMap;

import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.opengl.Textures;
import org.lwjgl.opengl.GL11;

public class Item
{

	public static final int ITEMS_IMG_WIDTH = 100;
	public static final int ITEMS_IMG_HEIGHT = 100;
    private static HashMap<String, Item> items = new HashMap<String, Item>();
    public static Item door = new ItemDoor().setTextureFromSheet(0, 0, 10, 10);
    public static ItemPickaxe steelPick = (ItemPickaxe) new ItemPickaxe("steelPickaxe").setTextureFromSheet(10,0,10,10);
    public static Item coal = new Item("coal").setTextureFromSheet(20, 0, 10, 10);
    public static Item ironIngot = new Item("ironIngot").setTextureFromSheet(40, 0, 10, 10);
    public static Item diamond = new Item("diamond").setTextureFromSheet(30, 0, 10, 10);
	public String	id;
    public float minU;
    public float minV;
    public float maxU;
    public float maxV;
	
	public Item(String id)
	{
		this.id = id;
		items.put(id, this);
	}
	
	public Item setTextureFromSheet(int xPos, int yPos, int width, int height)
    {
        int texW = (int) ITEMS_IMG_WIDTH;
        int texH = (int) ITEMS_IMG_HEIGHT;
        yPos = (int)texH - (yPos+height);
        minU = (float)(xPos)/(float)texW;
        minV = (float)(yPos)/(float)texH;
        maxU = (float)(xPos+width)/(float)texW;
        maxV = (float)(yPos+height)/(float)texH;
        return this;
    }
    
    public void setTextureUV(float minU, float minV, float maxU, float maxV)
    {
        this.minU = minU;
        this.minV = minV;
        this.maxU = maxU;
        this.maxV = maxV;
    }
	
	public void renderInventory(float posX, float posY, float width, float h)
	{
	    GL11.glPushMatrix();
        Tessellator t = Tessellator.instance;
        Textures.bind("/assets/textures/items.png");
        t.startDrawingQuads();
        t.setColorEnabled(true);
        t.addVertexWithUV(posX, posY, 0, minU, minV);
        t.addVertexWithUV(posX+width, posY, 0, maxU, minV);
        t.addVertexWithUV(posX+width, posY+h, 0, maxU, maxV);
        t.addVertexWithUV(posX, posY+h, 0, minU, maxV);
        t.flush();
        GL11.glPopMatrix();
	}
	
	public void renderHand(float posX, float posY, float width, float h, boolean flipX, Entity owner)
	{
	    this.renderInventory(posX, posY, width, h);
	}
	
	public void onUse(ItemStack s, Entity owner, int x, int y, World lvl)
	{
	    
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof Item)
		{
			Item i = (Item)o;
			if(i.id.equals(id))
				return true;
		}
		return false;
	}
	
	public static Item get(String id)
	{
	    Item i = items.get(id);
	    if(i != null)
		return i;
	    else
	        return null;
	}

	public float getDestroyPower()
	{
		return 100f;
	}

	public int getMaxInStack()
	{
		return 50;
	}

    public void update(Entity owner, float x, float y, World lvl)
    {
        
    }

    public int getStrengthAgainstBlock(Entity owner, ItemStack itemStack, int tx, int ty, World lvl)
    {
        return 1;
    }

    public boolean shouldBeRotatedAtHandRendering()
    {
        return true;
    }

    public float getStrengthEntity(Entity owner, ItemStack itemStack, Entity e, World world)
    {
        return 1;
    }
}

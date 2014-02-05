package org.jglrxavpok.blocky.block;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.jglrxavpok.blocky.GameObject;
import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.entity.EntityItem;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.items.Item;
import org.jglrxavpok.blocky.items.ItemBlock;
import org.jglrxavpok.blocky.utils.AABB;
import org.jglrxavpok.blocky.utils.ImageUtils;
import org.jglrxavpok.blocky.utils.MathHelper;
import org.jglrxavpok.blocky.utils.Points;
import org.jglrxavpok.blocky.world.Particle;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.opengl.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public abstract class Block implements GameObject
{

	public static final float BLOCK_WIDTH = 30;
	public static final float BLOCK_HEIGHT = 30;
	public static final float TERRAIN_IMG_WIDTH = 36;
	public static final float TERRAIN_IMG_HEIGHT = 72;
	private String	blockName = "null";
	public static final HashMap<String, Block> blocks = new HashMap<String, Block>();
	public float minU = 0;
	public float minV = 0;
	public float maxU = 1f;
	public float maxV = 1f;
	public float	resistance = 1f;
    private AABB aabb = new AABB(0,0,0,0);
    private int averageColor;
    private ArrayList<Integer> particleColors = new ArrayList<Integer>();
	/**
	 * Placeholder to prevent bugs
	 */
	public static Block nullBlock = new Block("null")
	{
		public boolean isPlaceholder()
		{
			return true;
		}

		@Override
		public boolean isSolid()
		{
			return false;
		}
		
		@Override
        public boolean letLightGoThrough()
        {
            return true;
        }

        @Override
        public boolean canBlockBeReplaced(int x, int y, World lvl, Block block)
        {
            return true;
        }
        
        @Override
        public boolean isOpaqueCube() 
        {
            return false;
        }

        @Override
        public float setBlockOpacity() 
        {
            return 1f;
        }
	};
	public static Block outBlock = new Block("out")
	{
		public boolean isPlaceholder()
		{
			return true;
		}

		@Override
		public boolean isSolid()
		{
			return true;
		}
		
		@Override
        public boolean letLightGoThrough()
        {
            return true;
        }

        @Override
        public boolean canBlockBeReplaced(int x, int y, World lvl, Block block)
        {
            return true;
        }
        
        @Override
        public boolean isOpaqueCube() 
        {
            return false;
        }

        @Override
        public float setBlockOpacity() 
        {
            return 1f;
        }
	};
	public static Block air = new Block("air")
	{
		public boolean isSolid()
		{
			return false;
		}
		
		public boolean onBlockDestroyedByPlayer(String lastAttackPlayerName, int rx, int y, World w)
		{
		    return false;
		}
		
		public Item getItem()
		{
		    return null;
		}

		public void render(float posX, float posY, int x, int y, World lvl, boolean selected)
		{
		}
		
		public void update(int x, int y, World w)
		{
		    super.update(x,y,w);
		}
		
		@Override
		public boolean letLightGoThrough()
		{
		    return true;
		}

        @Override
        public boolean canBlockBeReplaced(int x, int y, World lvl, Block block)
        {
            return true;
        }
        
        @Override
        public boolean isOpaqueCube() 
        {
            return false;
        }

        @Override
        public float setBlockOpacity() 
        {
            return 0f;
        }

	};
	public static Block rock = generateBlock("rock", true, 2.5f).setTextureFromTerrain(0, 0, 6, 6).setAverageColor(0x838383);
	public static Block dirt = generateBlock("dirt", true, 1f).setTextureFromTerrain(6, 0, 6, 6).setAverageColor(0x7E3E3E);
	public static Block grass = new BlockGrass("grass", false).setResistance(1.1f).setAverageColor(0x5C8506);
	public static Block grassSnow = new BlockGrass("grassSnow", true).setResistance(1.1f).setAverageColor(0x5C8506);
	public static Block bricks = generateBlock("bricks", true, 2.5f).setTextureFromTerrain(18, 0, 6, 6).setAverageColor(0x7D0000);
	public static Block bedrock = generateBlock("bedrock", true, Float.POSITIVE_INFINITY).setTextureFromTerrain(24, 0, 6, 6).setAverageColor(0x222222);
	public static Block log = generateBlock("log", true, 2f).setTextureFromTerrain(30, 0, 6, 6).setAverageColor(0x451C02);
    public static Block leaves = generateBlock("leaves", true, 0.05f, true, 0.1f).setTextureFromTerrain(0, 6, 6, 6).setAverageColor(0x85A92A);
    public static Block torch = new BlockTorch("torch").setResistance(0);
    public static Block planks = generateBlock("planks", true, 1.5f).setTextureFromTerrain(6, 6, 6, 6).setAverageColor(0xFFD800);
    public static Block sand = generateBlock("sand", true, 1.f).setTextureFromTerrain(12, 12, 6, 6).setAverageColor(0xFAE47A);
    public static final BlockDoor doorOpened = BlockDoor.opened; 
    public static final BlockDoor doorClosed = BlockDoor.closed; 
    public static Block coal = generateBlock("coal", true, 2.5f).setTextureFromTerrain(0, 30, 6, 6).setAverageColor(0x838383);
    public static Block iron = generateBlock("iron", true, 2.5f).setTextureFromTerrain(6, 30, 6, 6).setAverageColor(0x838383);
    public static Block diamond = generateBlock("diamond", true, 2.5f).setTextureFromTerrain(12, 30, 6, 6).setAverageColor(0x838383);
    
    public static Block furnace = new BlockFurnace("furnace", false);
    public static Block furnaceIdle = new BlockFurnace("furnaceIdle", true);
    public static Block craftingTable = new BlockCraftingTable("craftingTable");
    public static Block chest = new BlockChest("chest");
    
    public static Block glass = generateBlock("glass", true, 0.1f, true, 0f).setTextureFromTerrain(12, 18, 6, 6).setAverageColor(0x000000);
    public static Block snow = new BlockSnow("snow").setTextureFromTerrain(18, 18, 6, 6).setAverageColor(0xFFFFFF);
    public static Block cactus = new BlockCactus("cactus").setTextureFromTerrain(6, 36, 6, 6).setAverageColor(0x5C8506);
    
    public static Block logDark = generateBlock("logDark", true, 2f).setTextureFromTerrain(30, 18, 6, 6).setAverageColor(0x451C02);
    public static Block leavesDark = generateBlock("leavesDark", true, 0.05f, true, 0.1f).setTextureFromTerrain(30, 30, 6, 6);
    
    public static Block herb = new BlockPlant("herb").setTextureFromTerrain(0, 36, 6, 6);
    public static Block flowerRed = new BlockPlant("flowerRed").setTextureFromTerrain(12, 36, 6, 6);
    public static Block flowerWhite = new BlockPlant("flowerWhite").setTextureFromTerrain(18, 36, 6, 6);
    
	public Block(String name)
	{
		this.blockName = name;
		blocks.put(blockName, this);
		this.particleColors = new ArrayList<Integer>();
	}
	
	public Block(String name, float resistance)
    {
        this.blockName = name;
        blocks.put(blockName, this);
        this.particleColors = new ArrayList<Integer>();
        this.setResistance(resistance);
    }
	
	public Item getItem()
	{
	    Item item = Item.get("itemBlock."+getBlockName());
	    if(item == null)
	        item = new ItemBlock(this);
	    return item;
	}
	
    public void render(float posX, float posY, int x, int y, World lvl)
	{
		render(posX,posY,x,y,lvl,false);
	}
	
	public void render(float posX, float posY, int x, int y, World lvl, boolean selected)
	{
		Tessellator t = Tessellator.instance;
		float val = lvl.getLightValue(x, y);
		if(val < 0.015f)
            val = 0.015f;
		t.setColorRGBA_F(val,val,val,1f);
		t.addVertexWithUV(posX, posY, 0, minU, minV);
		t.addVertexWithUV(posX+BLOCK_WIDTH, posY, 0, maxU, minV);
		t.addVertexWithUV(posX+BLOCK_WIDTH, posY+BLOCK_HEIGHT, 0, maxU, maxV);
		t.addVertexWithUV(posX, posY+BLOCK_HEIGHT, 0, minU, maxV);
		t.setColorRGBA_F(1,1,1,1);
	}
	
	public static void drawSelectBox(float posX,float posY, int x,int y, World lvl)
	{
		Tessellator t = Tessellator.instance;
		EntityPlayer p = lvl.getNearestPlayer(x*BLOCK_WIDTH,y*BLOCK_HEIGHT);
		t.startDrawing(GL11.GL_LINES);
		if(p != null && p.canReachBlock(x,y))
		{
			t.setColorRGBA_F(0, 0, 0, 0.75f);
		}
		else
		{
			t.setColorRGBA_F(1, 1f, 1f, 0.75f);
		}
		t.addVertex(posX, posY+1, 0);
		t.addVertex(posX+BLOCK_WIDTH, posY+1, 0);
		t.addVertex(posX+BLOCK_WIDTH, posY+BLOCK_HEIGHT, 0);
		t.addVertex(posX, posY+BLOCK_HEIGHT, 0);
		
		t.addVertex(posX, posY+1, 0);
		t.addVertex(posX, posY+BLOCK_HEIGHT, 0);
		t.addVertex(posX+BLOCK_WIDTH, posY+1, 0);
		t.addVertex(posX+BLOCK_WIDTH, posY+BLOCK_HEIGHT, 0);
		t.setColorRGBA_F(1,1,1, 1);
		t.flush();
	}
	
	public void update(int x, int y, World lvl)
	{
	    float val = 0;
        float time = lvl.time;
        if(time < 0 )
            time = 0;
        float timeValue = (float)time/(18000f);
        if(timeValue > 1.0f)
        {
            timeValue = 1f-(timeValue-1f);
            if(timeValue < 0.0f)
                timeValue = 0;
        }
        if(lvl.canBlockSeeTheSky(x,y))
        {
            val = 1f;
        }
        else if(lvl.canBlockSeeTheSky(x,y+1))
        {
            val = 0.666f;
        }
        else if(lvl.canBlockSeeTheSky(x,y+1))
        {
            val = 0.333f;
        }
        val*=timeValue;
        if(val > 1f)
            val = 1f;
        lvl.setLightValue(val,x,y);
        
        if(lvl.isRaining && lvl.getBiomeAt(x, y).biomeId.equals("snow") && rand.nextInt(400) == 200)
	    {
	    	if(lvl.getBlockAt(x, y + 1).equals("air"))
	    	{
	    		lvl.setBlock(x, y + 1, "snow");
	    	}
	    }
	}
	
	public void onWorldUpdate(int x, int y, World lvl){}
	
	public abstract boolean isSolid();

	/**
	 * Returns the block with the given id
	 * Should never return "null", a placeholder is sent else.
	 * @param id
	 * @return
	 */
	public static Block getBlock(String id)
	{
		Block t = blocks.get(id);
		return t == null ? nullBlock : t;
	}
	
	public static Block generateBlock(String textName, final boolean solid, float resistance, final boolean letLight)
    {
	    if(getBlock(textName) == nullBlock)
        {
            Block result = new Block(textName)
            {
                @Override
                public boolean letLightGoThrough()
                {
                    return letLight;
                }
                
                @Override
                public boolean isSolid()
                {
                    return solid;
                }

                @Override
                public boolean canBlockBeReplaced(int x, int y, World lvl,
                        Block block)
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
                    return 1f;
                }
            };
            return result;
        }
        else
            return getBlock(textName);
    }
	
	public static Block generateBlock(String textName, final boolean solid, float resistance, final boolean letLight, final float lightOpacity)
    {
        if(getBlock(textName) == nullBlock)
        {
            Block result = new Block(textName)
            {
                @Override
                public boolean letLightGoThrough()
                {
                    return letLight;
                }
                
                @Override
                public boolean isSolid()
                {
                    return solid;
                }

                @Override
                public boolean canBlockBeReplaced(int x, int y, World lvl,
                        Block block)
                {
                    return false;
                }
                
                @Override
                public boolean isOpaqueCube() 
                {
                    return false;
                }

                @Override
                public float setBlockOpacity() 
                {
                    return lightOpacity;
                }
            };
            return result;
        }
        else
            return getBlock(textName);
    }
	
	public static Block generateBlock(String textName, final boolean solid, float resistance)
	{
		if(getBlock(textName) == nullBlock)
		{
			Block result = new Block(textName)
			{

			    @Override
                public boolean letLightGoThrough()
                {
                    return false;
                }
			    
				@Override
				public boolean isSolid()
				{
					return solid;
				}

                @Override
                public boolean canBlockBeReplaced(int x, int y, World lvl,
                        Block block)
                {
                    return false;
                }

                @Override
                public boolean isOpaqueCube()
                {
                    return false;
                }

                @Override
                public float setBlockOpacity()
                {
                    return 1;
                }
			};
			return result.setResistance(resistance);
		}
		else
			return getBlock(textName);
	}
	
	public String getBlockName()
	{
		return blockName;
	}

	public static String[] getNameList()
	{
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> it = blocks.keySet().iterator();
		while(it.hasNext())
		{
			String nextOne = it.next();
			if(nextOne != null && !getBlock(nextOne).isPlaceholder())
			{
				result.add(nextOne);
			}
		}
		Collections.sort(result);
		return result.toArray(new String[1]);
	}

	public boolean isPlaceholder()
	{
		return false;
	}
	
	public Block setTextureFromTerrain(int xPos, int yPos, int width, int height)
	{
		int texW = (int) TERRAIN_IMG_WIDTH;
		int texH = (int) TERRAIN_IMG_HEIGHT;
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

	public void spread(int x, int y, World lvl, ArrayList<Vector2f> blocks, String toSpread)
	{
		blocks.add(Points.getPoint(x,y));
		String top = lvl.getBlockAt(x,y+1);
		String left = lvl.getBlockAt(x-1,y);
		String right = lvl.getBlockAt(x+1,y);
		String bottom = lvl.getBlockAt(x,y-1);
		if(top.equals(blockName) && !blocks.contains(Points.getPoint(x,y+1)))
		{
			spread(x, y+1, lvl, blocks, toSpread);
		}
		if(left.equals(blockName) && !blocks.contains(Points.getPoint(x-1,y)))
		{
			spread(x-1, y, lvl, blocks, toSpread);
		}
		if(right.equals(blockName) && !blocks.contains(Points.getPoint(x+1,y)))
		{
			spread(x+1, y, lvl, blocks, toSpread);
		}
		if(bottom.equals(blockName) && !blocks.contains(Points.getPoint(x,y-1)))
		{
			spread(x, y-1, lvl, blocks, toSpread);
		}
		lvl.setBlock(x, y, toSpread);
	}
	
	public Block setResistance(float r)
	{
		resistance = r;
		return this;
	}
	
	public boolean canBeDestroyedWith(ItemStack stack, Entity owner)
	{
		if(resistance < 0)
			return false;
		if(stack == null)
		{
			return (resistance <= 10f);
		}
		return stack.item.getDestroyPower() >= this.resistance;
	}

    public boolean letLightGoThrough()
    {
        return false;
    }

    public abstract boolean canBlockBeReplaced(int x, int y, World lvl, Block block);


    public boolean onBlockDestroyedByPlayer(String lastAttackPlayerName, int rx, int y, World lvl)
    {
        for(int i = 0;i<50;i++)
        {
            Particle p = new Particle();
            ArrayList<Integer> list = Block.getBlock(lvl.getBlockAt(rx,y)).getParticleColors();
            if(list.size() <= 0)
                p.setColor(0xFFFFFF);
            else
                p.setColor(new Color(list.get(rand.nextInt(list.size()))).getRGB()/*0xFFFFFF*/);
            p.setPos((rx)*Block.BLOCK_WIDTH+rand.nextInt((int) Block.BLOCK_WIDTH), y*Block.BLOCK_HEIGHT+rand.nextInt((int) Block.BLOCK_HEIGHT));
            p.setVelocity(0, 0f);
            p.setLife(50);
            lvl.spawnParticle(p);
        }
        dropItems(lvl, rx, y);
        return true;
    }
    
    public void dropItems(World w, int x, int y)
    {
        EntityItem item = new EntityItem(new ItemStack(this.getItem(), 1));
        item.move(x*Block.BLOCK_WIDTH+Block.BLOCK_WIDTH/2-item.w/2, y*Block.BLOCK_HEIGHT+Block.BLOCK_HEIGHT/2-item.h/2);
        item.vy = 1.5f;
        item.gravityEfficienty = 0.5f;
        w.addEntity(item);
    }
    
    public AABB getCollisionBox(int x, int y)
    {
        return aabb.set(x*Block.BLOCK_WIDTH, y*Block.BLOCK_HEIGHT, Block.BLOCK_WIDTH, Block.BLOCK_HEIGHT);
    }

    public boolean equals(Object o)
    {
        if(o instanceof Block)
        {
            Block b = (Block)o;
            return b.getBlockName().equals(this.getBlockName());
        }
        return false;
    }

    public static void litWorld(int x, int y, int dist, World w)
    {
        float val = 1f;
        double dist1 = 0d;
        
        for(int xx = -dist / 2 ; xx <= dist / 2 ; xx++)
        {
            for(int yy = -dist / 2 ; yy <= dist / 2 ; yy++)
            {
                dist1 = (MathHelper.dist(x, y, xx + x, yy + y));
                
                if(Block.getBlock(w.getBlockAt(xx+x+1,yy+y)).setBlockOpacity() == 1f
                && Block.getBlock(w.getBlockAt(xx+x-1,yy+y)).setBlockOpacity() == 1f
                && Block.getBlock(w.getBlockAt(xx+x,yy+y-1)).setBlockOpacity() == 1f
                && Block.getBlock(w.getBlockAt(xx+x,yy+y+1)).setBlockOpacity() == 1f)
                {
                	
                }
                else
                if((int) dist1 <= dist / 2)
                {
                    val = (float) ((float) 1f - (dist1 / (dist / 2f))) - (w.isRaining ? 0.25f : 0f);
                    
                    if(val >= w.getLightValue(xx + x, yy + y))
                        w.setLightValue(val, xx + x, yy + y);
                }
            }   
        }
        
        w.setLightValue(1f - (w.isRaining ? 0.25f : 0f), x, y);
    }
    
    public static void loadAll()
    {
        for(String b : Block.getNameList())
        {
            Block.getBlock(b).getItem();
            Block.getBlock(b).generateParticuleColors();
        }
        
    }

    private void generateParticuleColors()
    {
        BufferedImage img = ImageUtils.getFromClasspath("/assets/textures/terrain.png");
        int h = (int)(((this.maxV-minV))*img.getHeight());
        if(h == 0)
            return;
        BufferedImage i = ImageUtils.toBufferedImage(img.getSubimage((int)(this.minU*img.getWidth()), (int)((1f-this.maxV)*img.getHeight()), (int)((this.maxU-minU)*img.getWidth()), h));
        int pixels[] = i.getRGB(0, 0, i.getWidth(), i.getHeight(), null, 0, i.getWidth());
        for(int index = 0;index<pixels.length;index++)
        {
            if(!particleColors.contains(pixels[index]))
                particleColors.add(pixels[index]);
        }
    }


    public Block setAverageColor(int color)
    {
        averageColor = color;
        return this;
    }

    public int getAverageColor()
    {
        return averageColor;
    }

    public void onEntityCollide(World world, int gridX, int gridY, Entity entity, boolean isEntityFullyInBlock){}

    public ArrayList<Integer> getParticleColors()
    {
        return particleColors;
    }
    
    public abstract boolean isOpaqueCube();
    
    public abstract float setBlockOpacity();
    
    private float getBlockOpacity()
    {
        return this.isOpaqueCube() ? 0.7f : this.setBlockOpacity();
    }
    
    public boolean onRightClick(World world, EntityPlayer player, int x, int y)
    {
        return false;
    }
    
    public void onBlockAdded(World world, EntityPlayer player, int x, int y)
    {
        
    }
    
    public boolean canBlockBePlaced(World world, int x, int y)
    {
    	return true;
    }
}

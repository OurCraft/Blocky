package org.jglrxavpok.blocky.entity;

import java.util.ArrayList;

import org.jglrxavpok.blocky.GameObject;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.block.BlockFluid;
import org.jglrxavpok.blocky.inventory.BasicInventory;
import org.jglrxavpok.blocky.inventory.Inventory;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.utils.AABB;
import org.jglrxavpok.blocky.utils.DamageType;
import org.jglrxavpok.blocky.utils.Fluid;
import org.jglrxavpok.blocky.world.Particle;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.opengl.Textures;
import org.jglrxavpok.storage.TaggedStorageChunk;
import org.lwjgl.util.Point;

public abstract class Entity implements GameObject
{

	public boolean visible = true, noclip = false, isInAir = true, wasInAir = true, gravityEfficient = true,
					alive = true, computeCollisions = true;
	public World world;
	public float x,y,vx,vy;
	protected int direction;
	public float gravityEfficienty = 1f;
	/**
	 * 10 m/s
	 */
	public float maxPositiveVX = 6;
	/**
	 * 10 m/s
	 */
	public float maxNegativeVX = -6;
	/**
	 * 20 m/s
	 */
	public float maxPositiveVY = 16;
	/**
	 * 20 m/s
	 */
	public float maxNegativeVY = -16f;
	public float decceleration = 0.1f;
	public int w = 16,h = 16;
	public AABB aabb = new AABB(x,y,w,h);
	public int ticksAlive = 0;
	
	public float offsetX = 0;
	public float offsetY = 0;
	
	public ArrayList<Entity> alreadyCollisionsChecked = new ArrayList<Entity>();
	public ArrayList<Point> blocksDone = new ArrayList<Point>();
	public Inventory inv;
    protected Fluid fluidIn;
    public int entityID;
	
	public Entity()
	{
		
	}
	
	public void tick()
	{
		stepTimers();
		stepMovements();
		if(this.computeCollisions)
			checkCollisions();
		
		if(vx > 0)
		    direction = 1;
		else if(vx < 0)
		    direction = 0;
	}
	
	public void checkCollisions()
	{
		for(int i = 0;i<world.entities.size();i++)
		{
			Entity e = world.entities.get(i);
			if(e != this)
			if(e != null && !alreadyCollisionsChecked.contains(e))
			{
				if(e.clipAABB().collide(clipAABB()))
				{
					e.onCollide(this);
					this.onCollide(e);
				}
				e.alreadyCollisionsChecked.add(this);
			}
		}
		doBlockCollisions();
		alreadyCollisionsChecked.clear();
	}
	
	/**
	 * Check for blocks AROUND
	 */
	private void doBlockCollisions()
    {
	    for(int x1 = -1;x1<=w;x1++)
        {
            for(int y1 = -1;y1<=h;y1++)
            {
                int gridX = (int) ((x1+x)/Block.BLOCK_WIDTH);
                if(gridX < -1)
                    gridX -= 1;
                int gridY = (int) ((y1+y)/Block.BLOCK_HEIGHT);
                if(!blocksDone.contains(new Point(gridX,gridY)))
                {
                    Block t = Block.getBlock(world.getBlockAt(gridX, gridY));
                    t.onEntityCollide(world, gridX, gridY, this, x1 > -1 && y1 > -1 && x1 != w && y1!= h);
                    blocksDone.add(new Point(gridX, gridY));
                }
            }
        }
	    blocksDone.clear();
    }

    public void render(float posX,float posY, float alpha)
	{
		Textures.render(Textures.getFromClasspath("/assets/textures/missing.png"), posX, posY, this.w, h);
	}
	
	public void onCollide(Entity e)
	{
		
	}
	
	public void stepMovements()
	{
		isInAir = true;
		if(vx > this.maxPositiveVX)
		{
			vx = this.maxPositiveVX;
		}
		if(vx < this.maxNegativeVX)
		{
			vx = this.maxNegativeVX;
		}
		if(vy > this.maxPositiveVY)
		{
			vy = this.maxPositiveVY;
		}
		if(vy < this.maxNegativeVY)
		{
			vy = this.maxNegativeVY;
		}
		if(gravityEfficient)
		{
			vx+=world.gravity.x*2*this.gravityEfficienty;
			vy+=world.gravity.y*2*this.gravityEfficienty;
		}
		boolean wasInFluid = fluidIn != null;
		fluidIn = null;
		if(canSwim())
        {
            handleFluidsMovement();
        }
		if(!wasInFluid && fluidIn != null)
		{
		    int nbr = rand.nextInt(50)+50;
		    for(int i = 0;i<nbr;i++)
		    {
    		    Particle p = new Particle();
    		    p.addKillerBlock(Block.getBlock(fluidIn.getName()+"_All"));
    		    p.setColor(fluidIn.getColor());
    		    p.setPos(x+rand.nextFloat()*w/2-w/4+w/2, y-vy+6);
    		    p.setLife(150);
    		    float pvy = (-vy*2f)*(rand.nextFloat()+0.8f);
    		    if(pvy < 0)
    		        pvy = -pvy;
    		    p.setVelocity(rand.nextFloat()*vy*2-vy, pvy);
    		    world.spawnParticle(p);
		    }
		}
		if(canGoto(x+vx*2,y))
		{
			x+=vx*2;
		}
		else
			vx = 0;
		if(canGoto(x,y+vy*2))
		{
			y+=vy*2;
		}
		else if(vy*2 < 0.f)
		{
			isInAir = false;
			vy = 0;
		}
		else
			vy = 0;
		if(vx > decceleration*2)
		{
			vx -= decceleration*2;
		}
		else if(vx < -decceleration*2)
			vx+= decceleration*2;
		else
			vx = 0;
		wasInAir = isInAir;
		
		if(y+h < 0)
		{
			die();
		}
		
	}
	
	public void postWorldRender(float posX, float posY){}
	
	private void handleFluidsMovement()
    {
	    int gridX = (int) ((x)/Block.BLOCK_WIDTH);
        if(gridX < -1)
            gridX -= 1;
        int gridY = (int) ((y)/Block.BLOCK_HEIGHT);
        Block t = Block.getBlock(world.getBlockAt(gridX, gridY));
        if(t instanceof BlockFluid)
        {
            BlockFluid fluidBlock = (BlockFluid)t;
            float h = ((float)(fluidBlock.volume)/(float)fluidBlock.getFluid().getMaxVolume())*Block.BLOCK_HEIGHT;
            if(h > y-gridY*Block.BLOCK_HEIGHT)
            {
                vx/=fluidBlock.getFluid().getDensity();
                vy/=fluidBlock.getFluid().getDensity();
                if(vy > 1f/(0.5f*fluidBlock.getFluid().getDensity()))
                    vy = 1f/(0.5f*fluidBlock.getFluid().getDensity());
                isInAir = false;
                fluidIn = fluidBlock.getFluid();
            }
        }
    }

    public void die()
	{
		if(onDeath())
		{
			world.removeEntity(this);
			alive = false;
		}
	}
	
	public boolean onDeath()
	{
		return true;
	}

	public void stepTimers()
	{
		ticksAlive++;
	}
	
	public boolean canSwim()
	{
	    return true;
	}
	
	public boolean canGoto(float posX, float posY)
	{
		if(!noclip)
		for(int x1 = 0;x1<w;x1++)
		{
			for(int y1 = 0;y1<h;y1++)
			{
				int gridX = (int) ((x1+posX)/Block.BLOCK_WIDTH);
				if(gridX < -1)
				    gridX -= 1;
				int gridY = (int) ((y1+posY)/Block.BLOCK_HEIGHT);
				Block t = Block.getBlock(world.getBlockAt(gridX, gridY));
				if(t.isSolid())
					return false;
			}
		}
		return true;
	}
	
	/**
	 * <font color="red"><b>No collisions checking</b></font>
	 * @return This for chaining
	 */
	public Entity move(float x, float y)
	{
		this.x = x;
		this.y = y;
		return this;
	}
	
	public AABB clipAABB()
	{
		return aabb.set(x,y,w,h);
	}
	
	public void readFromChunk(TaggedStorageChunk chunk)
	{
	    x = chunk.getFloat("x");
	    y = chunk.getFloat("y");
	    vx = chunk.getFloat("vx");
	    vy = chunk.getFloat("vy");
	    w = chunk.getInteger("width");
	    h = chunk.getInteger("height");
	    maxPositiveVX = chunk.getFloat("max+VX");
	    maxPositiveVY = chunk.getFloat("max+VY");
	    maxNegativeVX = chunk.getFloat("max-VX");
	    maxNegativeVY = chunk.getFloat("max-VY");
	    noclip = chunk.getBoolean("noclip");
	    gravityEfficient = chunk.getBoolean("gravityEfficient");
	    visible = chunk.getBoolean("visible");
	    decceleration = chunk.getFloat("decceleration");
	    ticksAlive = chunk.getInteger("ticksAlive");
	    if(chunk.hasTag("invEntity_Size"))
	    {
	        inv = new BasicInventory(chunk.getInteger("invEntity_Size"));
	        inv.read("invEntity", chunk);
	    }
	}

    public TaggedStorageChunk writeTaggedStorageChunk(int nbr)
    {
        TaggedStorageChunk chunk = new TaggedStorageChunk("Entity_"+this.getClass().getCanonicalName()+"_"+nbr);
        chunk.setFloat("x",x);
        chunk.setFloat("y",y);
        chunk.setFloat("vx",vx);
        chunk.setFloat("vy",vy);
        chunk.setInteger("width", w);
        chunk.setInteger("height", h);
        chunk.setBoolean("noclip", noclip);
        chunk.setBoolean("visible", visible);
        chunk.setBoolean("gravityEfficient", gravityEfficient);
        chunk.setFloat("max+VX", maxPositiveVX);
        chunk.setFloat("max+VY", maxPositiveVY);
        chunk.setFloat("max-VX", maxNegativeVX);
        chunk.setFloat("max-VY", maxNegativeVY);
        chunk.setFloat("decceleration", decceleration);
        chunk.setInteger("ticksAlive", ticksAlive);
        chunk.setString("class", this.getClass().getCanonicalName());
        if(inv != null)
        {
            inv.write("invEntity",chunk);
        }
        return chunk;
    }

    public boolean canBeHurt(DamageType lava)
    {
        return false;
    }

    public boolean shouldSendUpdate()
    {
        return true;
    }

    public void attackFrom(DamageType type, float amount)
    {
        if(type.getHasOwner())
        {
            Entity owner = type.getOwner();
            this.knockback((owner.x-x > 0 ? -1f : 1)*1f, 10f);
        }
        if(amount > 2)
            die();
    }
    
    public void knockback(float xAxis, float yAxis)
    {
        vx=xAxis;
        vy=yAxis;
    }

    public ItemStack addToInventory(ItemStack stack)
    {
        if(inv == null)
        return stack;
        return inv.tryAdd(stack);
    }
}

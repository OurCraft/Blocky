package org.jglrxavpok.blocky.world;

import java.util.ArrayList;

import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.utils.AABB;
import org.lwjgl.util.vector.Vector2f;

public class Particle
{

    private AABB aabb = new AABB(0,0,0,0);
    private ArrayList<Block> killers = new ArrayList<Block>();
    private Vector2f pos;
    private Vector2f vel;
    private int color;
    private int life;
    public float decceleration = 0.02f;
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
    
    boolean killOnCollide = false;
    
    public Particle()
    {
        pos = new Vector2f();
        vel = new Vector2f();
        life = 10;
    }
    
    public Particle(boolean killOnCollide)
    {
        this();
        this.killOnCollide = killOnCollide;
    }
    
    public void tick(World w)
    {
        if(life <= 0)
            return;
        vel.x += w.gravity.x*2;
        vel.y += w.gravity.y*2;
        if(vel.x > this.maxPositiveVX)
        {
            vel.x = this.maxPositiveVX;
        }
        if(vel.x < this.maxNegativeVX)
        {
            vel.x = this.maxNegativeVX;
        }
        if(vel.y > this.maxPositiveVY)
        {
            vel.y = this.maxPositiveVY;
        }
        if(vel.y < this.maxNegativeVY)
        {
            vel.y = this.maxNegativeVY;
        }
        if(canGo(pos.x+vel.x, pos.y, w))
        {
            pos.x+=vel.x;
        }
        else
        {
            vel.x = vel.x*-.5f;
        }
        
        if(canGo(pos.x, pos.y+vel.y, w))
        {
            pos.y+=vel.y;
        }
        else
        {
            vel.y = 0;
        }
        if(vel.x > decceleration*2)
        {
            vel.x -= decceleration*2;
        }
        else if(vel.x < -decceleration*2)
            vel.x+= decceleration*2;
        else
            vel.x = 0;
        
        life--;
        
        
        if(this.killOnCollide)
        {
        	float x = pos.x;
            float y = pos.y;
            
            int gridX = (int) ((x)/Block.BLOCK_WIDTH);
            if(gridX < -1)
                gridX -= 1;
            int gridY = (int) ((y)/Block.BLOCK_HEIGHT);
            Block t = Block.getBlock(w.getBlockAt(gridX, gridY));
            
            this.life = Integer.MAX_VALUE;
            
            if(t.getCollisionBox(gridX, gridY).collide(this.aabb.set(x, y, 1, 1)))
            {
            	this.life = -1;
            }
        }
    }
    
    public void addKillerBlock(Block b)
    {
        killers.add(b);
    }
    
    private boolean canGo(float x, float y, World w)
    {
        int gridX = (int) ((x)/Block.BLOCK_WIDTH);
        if(gridX < -1)
            gridX -= 1;
        int gridY = (int) ((y)/Block.BLOCK_HEIGHT);
        Block t = Block.getBlock(w.getBlockAt(gridX, gridY));
        if(t.isSolid())
            return false;
        else
        {
            for(int i = 0;i<killers.size();i++)
            {
                if(killers.get(i).equals(t))
                {
                    if(t.getCollisionBox(gridX, gridY).collide(aabb.set(x,y,1,1)))
                    {
                        life = -1;
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void setLife(int l)
    {
        life = l;
    }
    
    public int getLife()
    {
        return life;
    }
    
    public void setColor(int color)
    {
        this.color = color;
    }
    
    public int getColor()
    {
        return color;
    }
    
    public void setVelocity(float vx, float vy)
    {
        vel.x = vx;
        vel.y = vy;
    }
    
    public void setPos(float x, float y)
    {
        this.pos.x = x;
        this.pos.y = y;
    }
    
    public Vector2f getVelocity()
    {
        return vel;
    }
    
    public Vector2f getPos()
    {
        return pos;
    }
}

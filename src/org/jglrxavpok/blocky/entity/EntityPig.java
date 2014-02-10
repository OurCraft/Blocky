package org.jglrxavpok.blocky.entity;

import java.awt.Color;

import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.opengl.Textures;
import org.lwjgl.opengl.GL11;

public class EntityPig extends EntityLiving
{
	int lastTickPosX;
	int directionTimer = 0, maxDirectionTimes = 0;
	boolean hasToMove = false;
	int timerBlocked = 0;
	
    public EntityPig()
    {
        w = 40;
        h = 20;
        this.lastTickPosX = (int) this.x;
    }
    
    public void tick()
    {        
        if(this.directionTimer >= this.maxDirectionTimes)
        {
        	this.hasToMove = !this.hasToMove;
        	this.directionTimer = 0;
        	this.maxDirectionTimes = 0;
        }
        
        if(this.hasToMove)
        {
        	if(this.maxDirectionTimes == 0)
        	{
        		this.maxDirectionTimes = rand.nextInt(300);
        		
        		if(rand.nextBoolean())
        		{
        			vx = -vx;
        			needUpdate = true;
        		}
        	}
        	
        	this.directionTimer++;
        	
        	if(direction == 0)
            {
                vx = -1;
            }
            else if(direction == 1)
            {
                vx = 1;
            }
        	
        	if(!canGoto(x + vx, y))
            {
            	this.jump();
            	
            	if(this.lastTickPosX == this.x && this.timerBlocked >= 20)
            	{
            		vx = -vx;
            	}
            }
        	
        }
        else if(!this.hasToMove)
        {
        	if(this.maxDirectionTimes == 0)
        	{
        		this.maxDirectionTimes = rand.nextInt(300);
        		needUpdate = true;
        	}
        	
        	this.directionTimer++;
        }
        
        super.tick();
        
        if(this.lastTickPosX == x)
        {
        	this.timerBlocked++;
        }
        else
        {
        	this.timerBlocked = 0;
        }
        
        this.lastTickPosX = (int) this.x;
    }
    
    private void jump() 
    {
    	if(!this.isInAir)
    		this.vy = 4;
	}

	public void render(float posX, float posY, float a)
    {
        Tessellator t = Tessellator.instance;
        Textures.bind("/assets/textures/entities/pig.png");
        GL11.glPushMatrix();
        t.startDrawingQuads();
        float val = world.getLightValue((int)((x+w/2)/Block.BLOCK_WIDTH), (int)((y+h/2)/Block.BLOCK_HEIGHT));
        if(val < 0.05f)
            val = 0.05f;
        Color c = new Color(t.getColor());
        t.setColorRGBA_F(val*((float)c.getRed()/255f),val*((float)c.getGreen()/255f),val*((float)c.getBlue()/255f),1f);

        if(direction == 0)
        {
            t.addVertexWithUV(posX, posY, 0, 0, 0);
            t.addVertexWithUV(posX + w, posY, 0, 1, 0);
            t.addVertexWithUV(posX + w, posY + h, 0, 1, 1);
            t.addVertexWithUV(posX, posY + h, 0, 0, 1);
        }
        else if(direction == 1)
        {
            t.addVertexWithUV(posX, posY, 0, 1, 0);
            t.addVertexWithUV(posX + w, posY, 0, 0, 0);
            t.addVertexWithUV(posX + w, posY + h, 0, 0, 1);
            t.addVertexWithUV(posX, posY+h, 0, 1, 1);
        }
        t.flush();
        GL11.glPopMatrix();
    }

    @Override
    public float getMaxLife()
    {
        return 10f;
    }
}

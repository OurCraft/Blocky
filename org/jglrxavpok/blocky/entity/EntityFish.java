package org.jglrxavpok.blocky.entity;

import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.block.BlockFluid;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.opengl.Textures;
import org.lwjgl.opengl.GL11;

public class EntityFish extends EntityLiving
{
    private int direction;
    private int type;
    private double angle;
    private boolean wasInFluid;

    public EntityFish()
    {
        do
        {
            vx = rand.nextFloat()-0.5f;
        }while(vx == 0.0f);
        do
        {
            vy = rand.nextFloat()-0.5f;
        }while(vy == 0.f);
        w = 10;
        h = 10;
        type = rand.nextInt(2);
    }
    
    public void tick()
    {
        this.ticksAlive++;
        boolean canGoX = true;
        boolean canGoY = true;
        
        Block in = Block.getBlock(world.getBlockAt((int)((x)/Block.BLOCK_WIDTH), (int)((y)/Block.BLOCK_HEIGHT)));
        if(in instanceof BlockFluid)
        {
            EntityPlayer p = world.getNearestPlayer(x+w/2, y+h/2, 4f*Block.BLOCK_WIDTH);
            if(p != null && ticksAlive % 1 == 0 && type == 1)
            {
                angle = Math.toDegrees(Math.atan2((x-p.x), (y-p.y)))+rand.nextFloat()*20f-10f;
                vx = (float)(1*Math.sin(Math.toRadians(angle)));
                vy = (float)(1*Math.cos(Math.toRadians(angle)));
            }
            else
            {
                angle = (int)Math.toDegrees(Math.atan2(vx, vy));
            }

            if(!wasInFluid)
            {
                do
                {
                    vx = rand.nextFloat()-0.5f;
                }while(vx == 0.0f);
                do
                {
                    vy = rand.nextFloat()-0.5f;
                }while(vy == 0.f);
            }
            while(vx == 0.0f)
            {
                vx = rand.nextFloat()-0.5f;
            }
            while(vy == 0.0f)
            {
                vy = rand.nextFloat()-0.5f;
            }
            wasInFluid = true;
            if(!this.canGoSwim(x, y+vy))
            {
                if(p == null)
                vy = -vy;
                else
                    vy = 0;
            }
            if(!this.canGoSwim(x+vx, y))
            {
                if(p == null)
                vx = -vx;
                else
                    vx = 0;
            }
            if(canGoX)
                x+=vx;
            if(canGoY)
                y+=vy;
        }
        else
        {
            super.tick();
            wasInFluid = false;
        }
        if(vx > 0.f)
            direction = 1;
        else if(vx < 0.f)
            direction = 0;
    }
    
    public boolean canGoSwim(float posX, float posY)
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
                if(!(t instanceof BlockFluid))
                    return false;
                else
                {
                    BlockFluid f = (BlockFluid)t;
                    gridY = (int) ((y1+posY)/Block.BLOCK_HEIGHT);
                    if(Block.getBlock(world.getBlockAt(gridX, gridY+1)) instanceof BlockFluid == false)
                        return posY+y1-((int)((posY+y1)/Block.BLOCK_HEIGHT)*Block.BLOCK_HEIGHT)+h < ((float)(f.volume)/(float)f.getFluid().getMaxVolume())*Block.BLOCK_HEIGHT && Block.getBlock(world.getBlockAt(gridX, gridY)) instanceof BlockFluid && canGoto(posX,posY);
                }
            }
        }
        return true;
    }
    
    public void render(float posX, float posY, float a)
    {
        Tessellator t = Tessellator.instance;
        Textures.bind("/assets/textures/entities/fish"+type+".png");
        GL11.glPushMatrix();
        t.startDrawingQuads();
        float val = world.getLightValue((int)((x+w/2)/Block.BLOCK_WIDTH), (int)((y+h/2)/Block.BLOCK_HEIGHT));
        if(val < 0.05f)
            val = 0.05f;
        t.setColorRGBA_F(val,val,val,1f);

        if(direction == 0)
        {
            t.addVertexWithUV(posX, posY, 0, 0, 0);
            t.addVertexWithUV(posX+w, posY, 0, 1, 0);
            t.addVertexWithUV(posX+w, posY+h, 0, 1,1);
            t.addVertexWithUV(posX, posY+h, 0, 0, 1);
        }
        else if(direction == 1)
        {
            t.addVertexWithUV(posX, posY, 0, 1, 0);
            t.addVertexWithUV(posX+w, posY, 0, 0, 0);
            t.addVertexWithUV(posX+w, posY+h, 0, 0,1);
            t.addVertexWithUV(posX, posY+h, 0, 1, 1);
        }
        GL11.glTranslated(posX+w/2, posY+h/2, 0);
        if(direction == 0)
        {
            GL11.glRotatef((float) (180-angle-90+180), 0, 0, 1);
        }
        else
            GL11.glRotatef((float) (180-angle-90), 0, 0, 1);
        GL11.glTranslated(-(posX+w/2), -(posY+h/2), 0);
        t.flush();
        GL11.glPopMatrix();
    }

    @Override
    public float getMaxLife()
    {
        return 1f;
    }
}

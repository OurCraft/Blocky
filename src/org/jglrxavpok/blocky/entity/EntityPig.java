package org.jglrxavpok.blocky.entity;

import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.opengl.Textures;
import org.lwjgl.opengl.GL11;

public class EntityPig extends EntityLiving
{

    public EntityPig()
    {
        w = 40;
        h = 20;
    }
    
    public void tick()
    {
        if(direction == 0)
        {
            vx = -1;
        }
        else if(direction == 1)
        {
            vx = 1;
        }
        if(!canGoto(x+vx,y))
        {
            vx = -vx;
        }
        super.tick();
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
        t.flush();
        GL11.glPopMatrix();
    }

    @Override
    public float getMaxLife()
    {
        return 10f;
    }
}

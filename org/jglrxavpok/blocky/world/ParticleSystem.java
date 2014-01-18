package org.jglrxavpok.blocky.world;

import org.jglrxavpok.opengl.Tessellator;
import org.lwjgl.opengl.GL11;

public class ParticleSystem
{

    private Particle[] particles;

    public ParticleSystem(int maxParticules)
    {
        this.particles = new Particle[maxParticules];
    }
    
    public void spawnParticle(Particle p)
    {
        int index = getFreeIndex();
        if(index != -1)
        {
            particles[index] = p;
        }
        else
        {
            for(int i = 0;i<particles.length-1;i++)
            {
                particles[i] = particles[i+1];
            }
            particles[particles.length-1] = p;
        }
    }
    
    public int getFreeIndex()
    {
        for(int i = 0;i<particles.length;i++)
            if(particles[i] == null)
            {
                return i;
            }
            else if(particles[i].getLife() <= 0)
            {
                particles[i] = null;
                return i;
            }
        return -1;
    }
    
    public void tickAll(World w)
    {
        if(w == World.zeroBlocks)
            return;
        for(int i = 0;i<particles.length;i++)
            if(particles[i] != null)
            {
                particles[i].tick(w);
                if(particles[i].getLife() <= 0)
                    particles[i] = null;
            }
    }
    
    public void renderAll(World w)
    {
        if(w == World.zeroBlocks)
            return;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        for(int i = 0;i<particles.length;i++)
            if(particles[i] != null)
            {
                Particle p = particles[i];
                t.setColorOpaque_I(p.getColor());
                t.addVertex(w.lvlox+p.getPos().x, w.lvloy+p.getPos().y, 0);
                t.addVertex(w.lvlox+p.getPos().x+1, w.lvloy+p.getPos().y, 0);
                t.addVertex(w.lvlox+p.getPos().x+1, w.lvloy+p.getPos().y+1, 0);
                t.addVertex(w.lvlox+p.getPos().x, w.lvloy+p.getPos().y+1, 0);
                
                if(particles[i].getLife() <= 0)
                    particles[i] = null;
            }
        t.flush();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}

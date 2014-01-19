package org.jglrxavpok.blocky.block;

import java.awt.Color;

import org.jglrxavpok.blocky.utils.AABB;
import org.jglrxavpok.blocky.utils.Fluid;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.opengl.Tessellator;

public class BlockFluid extends Block
{

    public int volume;
    private String fluidName; 
    private Fluid fluid;
    
    public BlockFluid(Fluid fluid, int vol)
    {
        super(fluid.getName()+"_"+(vol == Integer.MAX_VALUE ? "All" : vol));
        this.fluid = fluid;
        this.setTextureFromTerrain(18, 12, 6, 6);
        this.fluidName = fluid.getName();
        this.volume = vol;
        resistance = 0;
    }
    
    public int getAverageColor()
    {
        return fluid.getColor();
    }
    
    public boolean equals(Object o)
    {
        if(o instanceof BlockFluid)
        {
            BlockFluid f = (BlockFluid)o;
            if(f.fluid == fluid)
            {
                if(f.getBlockName().equals(fluidName+"_All") || getBlockName().equals(fluidName+"_All"))
                {
                    return true;
                }
                else
                    return f.volume == volume;
            }
        }
        return super.equals(o);
    }
    
    public Fluid getFluid()
    {
        return fluid;
    }
    
    public void onWorldUpdate(int x, int y, World w)
    {
        if(fluid.getLightingPower() > 0)
        {
            Block.litWorld(x, y, fluid.getLightingPower(), w);
        }
    }
    
    public AABB getCollisionBox(int x, int y)
    {
        return super.getCollisionBox(x, y).setHeight(((float)(volume)/(float)this.fluid.getMaxVolume())*Block.BLOCK_HEIGHT);
    }
    
    public boolean onBlockDestroyedByPlayer(String lastAttackPlayerName, int rx, int y, World lvl)
    {
        return false;
    }
    
    public void update(int x, int y, World lvl)
    {
//        lvl.setBlock(x,y,"air");
//        if(true)
//            return;
        super.update(x,y,lvl);
        if(getBlockName().equals(fluid.getName()+"_All"))
        {
            lvl.setBlock(x, y, fluidName+"_"+fluid.getMaxVolume());
            return;
        }
        if(volume > fluid.getMaxVolume())
            volume = fluid.getMaxVolume();
        int t = lvl.getLastBlockChange(x, y);
        int min = 1;
        if(fluid != null)
            min = fluid.getMinVolume();
        
        if(volume < min)
        {
            lvl.setBlock(x, y, "air");
            return;
        }
        Block bottomBlock = Block.getBlock(lvl.getBlockAt(x, y-1));
        if(bottomBlock instanceof BlockFluid)
        {
            BlockFluid bottomFluid = (BlockFluid)bottomBlock;
            if(bottomFluid.fluid == fluid)
            {
                int bVolume = bottomFluid.volume;
                if(bVolume == fluid.getMaxVolume())
                {
                    ;
                }
                else if(bVolume+this.volume >= fluid.getMaxVolume())
                {
                    lvl.setBlock(x, y-1, fluid.getName()+"_"+fluid.getMaxVolume());
                    int volume1 = fluid.getMaxVolume()-volume;
                    if(volume1 > min && volume1 <= volume)
                        lvl.setBlock(x, y, fluid.getName()+"_"+volume1);
                    return;

                }
                else
                {
                    lvl.setBlock(x, y-1, fluid.getName()+"_"+(bVolume+volume));
                    lvl.setBlock(x, y, "air");
                    return;
                }
            }
        }
        else if(bottomBlock.canBlockBeReplaced(x, y-1, lvl, this) || Block.air == bottomBlock)
        {
            lvl.setBlock(x, y, "air");
            lvl.setBlock(x,y-1,fluidName+"_"+volume);
            return;
        }

        if((int)lvl.ticks-t >= fluid.getSpreadSpeed())
        {
            {
                Block leftBlock = Block.getBlock(lvl.getBlockAt(x-1, y));
                Block rightBlock = Block.getBlock(lvl.getBlockAt(x+1, y));
                {
                    int currentVolume = volume;
                    if(currentVolume > min)
                    {
                        if(leftBlock instanceof BlockFluid)
                        {
                            BlockFluid fluidLeft = (BlockFluid)leftBlock;
                            if(fluidLeft.fluid == fluid)
                            {
                                if(fluidLeft.volume < currentVolume)
                                {
                                    lvl.setBlock(x-1,y, fluidName+"_"+(--currentVolume));
                                }
                            }
                        }
                        if(leftBlock.canBlockBeReplaced(x-1, y, lvl, this))
                        {
                            lvl.setBlock(x-1,y, fluidName+"_"+(--currentVolume));
                        }
                        
                        if(rightBlock instanceof BlockFluid)
                        {
                            BlockFluid fluidRight = (BlockFluid)rightBlock;
                            if(fluidRight.fluid == fluid)
                            {
                                if(fluidRight.volume < currentVolume)
                                {
                                    lvl.setBlock(x+1,y, fluidName+"_"+(--currentVolume));
                                }
                            }
                        }
                        if(rightBlock.canBlockBeReplaced(x+1, y, lvl, this))
                        {
                            lvl.setBlock(x+1,y, fluidName+"_"+(currentVolume--));
                        }
                        if(currentVolume < min)
                        {
                            lvl.setBlock(x,y, "air");
                        }
                        else
                        {
                            lvl.setBlock(x,y, fluidName+"_"+(currentVolume));
                        }
                    }
                }
            }
        }
        if(x == 250151 && y == 101)
            System.err.println(volume);
    }
    
    public void render(float posX, float posY, int x, int y, World w, boolean selected)
    {
        if(volume > fluid.getMaxVolume())
            volume = fluid.getMaxVolume();
        if(getBlockName().equals(fluid.getName()+"_All") || volume == Integer.MAX_VALUE)
        {
            w.setBlock(x, y, fluidName+"_"+fluid.getMaxVolume());
        }
        Tessellator t = Tessellator.instance;
        Color c = new Color(fluid.getColor());
        float val = w.getLightValue(x, y);
        if(val < 0.01f)
            val = 0.01f;
        if(fluid.getLightingPower() == 0)
            t.setColorRGBA((int)(val*((float)c.getRed())),(int)(val*((float)c.getGreen())),(int)(val*((float)c.getBlue())), (int)(fluid.getOpacity()*255f));
        else
            t.setColorRGBA((int)(((float)c.getRed())),(int)(((float)c.getGreen())),(int)(((float)c.getBlue())), (int)(fluid.getOpacity()*255f));
        float h = ((float)(volume)/(float)this.fluid.getMaxVolume())*Block.BLOCK_HEIGHT;
        Block atLeft =  Block.getBlock(w.getBlockAt(x-1, y));
        
        float leftH = h;
        float rightH = h;
        if(atLeft instanceof BlockFluid)
        {
            if((((BlockFluid) atLeft).volume) > volume)
            leftH = ((float)(((BlockFluid) atLeft).volume)/(float)((BlockFluid) atLeft).fluid.getMaxVolume())*Block.BLOCK_HEIGHT;
        }
        Block atRight =  Block.getBlock(w.getBlockAt(x+1, y));
        if(atRight instanceof BlockFluid)
        {
            if((((BlockFluid) atRight).volume) > volume)
            rightH = ((float)(((BlockFluid) atRight).volume)/(float)((BlockFluid) atRight).fluid.getMaxVolume())*Block.BLOCK_HEIGHT;
        }
        t.addVertexWithUV(posX, posY, 0,minU,minV);
        t.addVertexWithUV(posX+Block.BLOCK_WIDTH/2, posY, 0,maxU,minV);
        t.addVertexWithUV(posX+Block.BLOCK_WIDTH/2, posY+h, 0,maxU,maxV);
        t.addVertexWithUV(posX, posY+leftH, 0,minU,maxV);
        
        t.addVertexWithUV(posX+Block.BLOCK_WIDTH/2, posY, 0,minU,minV);
        t.addVertexWithUV(posX+Block.BLOCK_WIDTH, posY, 0,maxU,minV);
        t.addVertexWithUV(posX+Block.BLOCK_WIDTH, posY+rightH, 0,maxU,maxV);
        t.addVertexWithUV(posX+Block.BLOCK_WIDTH/2, posY+h, 0,minU,maxV);
        t.setColorRGBA_F(1,1,1,1);
    }

    @Override
    public boolean isSolid()
    {
        return false;
    }

    @Override
    public boolean canBlockBeReplaced(int x, int y, World lvl, Block block)
    {
        if(block instanceof BlockFluid)
        {
            return false;
        }
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
        return 0.5f;
    }
}

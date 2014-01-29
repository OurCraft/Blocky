package org.jglrxavpok.blocky.entity;

import org.jglrxavpok.blocky.achievements.Achievement;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.opengl.FontRenderer;
import org.jglrxavpok.opengl.Textures;


public class EntityDemoPlayer extends EntityPlayer
{

    private boolean helpMode;
    private int helpCounter;

    public EntityDemoPlayer()
    {
        super();
        inv.tryAdd(new ItemStack(Block.torch.getItem(), 1));
    }
    
    public void toggleAchievement(final Achievement achievement){}
    
    public void tick()
    {
        vx+=1;
        if(Block.getBlock(world.getBlockAt((int)(this.x/Block.BLOCK_WIDTH)+1, (int)(this.y/Block.BLOCK_HEIGHT)+2)).isSolid())
        {
            world.setBlock((int)(this.x/Block.BLOCK_WIDTH)+1, (int)(this.y/Block.BLOCK_HEIGHT)+1, "air");
            world.setBlock((int)(this.x/Block.BLOCK_WIDTH)+1, (int)(this.y/Block.BLOCK_HEIGHT), "air");
        }
        else if(Block.getBlock(world.getBlockAt((int)(this.x/Block.BLOCK_WIDTH)+2, (int)(this.y/Block.BLOCK_HEIGHT)+1)).isSolid())
        {
            jump();
        }
        else if(Block.getBlock(world.getBlockAt((int)(this.x/Block.BLOCK_WIDTH)+2, (int)(this.y/Block.BLOCK_HEIGHT))).isSolid())
        {
            jump();
        }
        else if(Block.getBlock(world.getBlockAt((int)(this.x/Block.BLOCK_WIDTH)+1, (int)(this.y/Block.BLOCK_HEIGHT)+1)).isSolid())
        {
            jump();
        }
        else if(Block.getBlock(world.getBlockAt((int)(this.x/Block.BLOCK_WIDTH)+1, (int)(this.y/Block.BLOCK_HEIGHT))).isSolid())
        {
            jump();
        }
        else if(!Block.getBlock(world.getBlockAt((int)(this.x/Block.BLOCK_WIDTH)+1, (int)(this.y/Block.BLOCK_HEIGHT)-1)).isSolid())
        {
            jump();
        }
        else if(!Block.getBlock(world.getBlockAt((int)(this.x/Block.BLOCK_WIDTH)+2, (int)(this.y/Block.BLOCK_HEIGHT)-1)).isSolid())
        {
            jump();
        }
        if(Block.getBlock(world.getBlockAt((int)(this.x/Block.BLOCK_WIDTH)+1, (int)(this.y/Block.BLOCK_HEIGHT))).isSolid()
        && Block.getBlock(world.getBlockAt((int)(this.x/Block.BLOCK_WIDTH), (int)(this.y/Block.BLOCK_HEIGHT)+2)).isSolid())
        {
            world.setBlock((int)(this.x/Block.BLOCK_WIDTH)+1, (int)(this.y/Block.BLOCK_HEIGHT), "air");
        }
        super.tick();
        if(vx*2 < 0.01f)
        {
            helpCounter++;
        }
        else
            helpCounter = 0;
        if(helpCounter >= 60)
            helpMode = true;
    }
    
    public void postWorldRender(float posX, float posY)
    {}
    
    public void render(float posX, float posY, float alpha)
    {
        super.render(posX, posY, alpha);
        if(helpMode)
        {
            Textures.render(Textures.getFromClasspath("/assets/textures/entities/fakePlayerHelp.png"), posX+w, posY+h, 55, 24);
            FontRenderer.drawString("Help!!", posX+w+2+55/2f-FontRenderer.getWidth("Help!!")/2f, posY+h+24f/2f-7, 0x0);
        }
    }
}

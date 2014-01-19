package org.jglrxavpok.blocky.entity;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.gui.UIGameOverMenu;
import org.jglrxavpok.blocky.input.PlayerInputHandler;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.utils.DamageType;
import org.jglrxavpok.blocky.utils.HUDComponent;
import org.jglrxavpok.opengl.FontRenderer;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.opengl.Textures;
import org.lwjgl.input.Keyboard;

public class EntityPlayerSP extends EntityPlayer
{

	private boolean tPressed;

    public EntityPlayerSP()
	{
	    super();
	    if(BlockyMain.username != null)
	    {
	        username = BlockyMain.username;
	    }
		BlockyMain.instance.addHUDComponent(new PlayerHUDComponent(this));
		BlockyMain.instance.addInputProcessor(new PlayerInputHandler(this));
	}
	
    
    public boolean onDeath(DamageType type, float amount)
    {
        if(super.onDeath(type, amount))
        {
            UI.displayMenu(new UIGameOverMenu(world));
            return true;
        }
        return false;
    }
    
	public void tick()
	{
		super.tick();
		if(Keyboard.isKeyDown(Keyboard.KEY_T) && !tPressed)
		{
		    tPressed = true;
		    if(onDeath(DamageType.generic, 100))
		    {
		        die();
		    }
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_T) && tPressed)
        {
            tPressed = false;
        }
		
	}
	
	public void incrementSelectedHotbar(float f)
    {
	    invIndex+=f;
        if(invIndex < 0)
        {
            invIndex = 9;
        }
        else if(invIndex >= 10)
        {
            invIndex = 0;
        }
    }
	
	public static class PlayerHUDComponent implements HUDComponent
	{

		public EntityPlayerSP player;
		
		public PlayerHUDComponent(EntityPlayerSP player)
		{
			this.player = player;
		}
		
		@Override
		public void render()
		{
		    if(BlockyMain.instance.getLevel() == null)
            {
		        BlockyMain.instance.removeHUDComponent(this);
		        return;
            }
			Tessellator t = Tessellator.instance;
			float x = 0;
			float y = BlockyMain.height-35;
			float w = 32;
			float h = 32;
			
            for(int i = 0;i<10;i++)
            {
                ItemStack stack = player.inv.getStackIn(i);
                if(stack != null)
                {
                    stack.item.renderInventory(x+6, y+6, 20, 20);
                }
                x+=w+5;
            }
            x = 0;
            Textures.bind("/assets/textures/ui/inventory.png");
            t.startDrawingQuads();
            for(int i = 0;i<10;i++)
            {
                boolean selected = i == (int)player.invIndex;
                float minU = (selected ? 32f : 0) /128f;
                float maxU = (selected ? 64f : 32f) /128f;
                float minV = 96f/128f;
                float maxV = 128f/128f;
                t.addVertexWithUV(x, y, 0, minU, minV);
                t.addVertexWithUV(x+w, y, 0, maxU, minV);
                t.addVertexWithUV(x+w, y+h, 0, maxU, maxV);
                t.addVertexWithUV(x, y+h, 0, minU, maxV);
                x+=w+5;
            }
            t.flush();
            x = 30f;
            for(int i = 0;i<10;i++)
            {
                ItemStack stack = player.inv.getStackIn(i);
                if(stack != null)
                {
                    if(stack.nbr > 1)
                    FontRenderer.drawString(stack.nbr+"", x-FontRenderer.getWidth(""+stack.nbr), y, 0xFFFFFF);
                }
                x+=w+5;
            }
            
			y-=h-10;
			x = 0;
			w=16;
			h=16;
			Textures.bind("/assets/textures/ui/compsTemplate.png");
            t.startDrawingQuads();
            t.setColorRGBA_F(1,1,1,1);
            float heartBordersMinU = 100f/UI.TEMPLATE_WIDTH;
            float heartBordersMaxU = 116f/UI.TEMPLATE_WIDTH;
            float heartBordersMinV = (UI.TEMPLATE_HEIGHT-16f)/UI.TEMPLATE_HEIGHT;
            float heartBordersMaxV = 1f;
            
            float heartContentMinU = 116f/UI.TEMPLATE_WIDTH;
            float heartContentMaxU = 132f/UI.TEMPLATE_WIDTH;
            float heartContentMinV = (UI.TEMPLATE_HEIGHT-16f)/UI.TEMPLATE_HEIGHT;
            float heartContentMaxV = 1f;
            for(int i = 0;i<20;i++)
            {
                t.setColorRGBA_F(1,1,1,1);
                if(player.lastLifeAmount != player.life)
                {
                    if(player.lastLifeAmount < player.life)
                        t.setColorRGBA_F(1, 1, 1, 1);
                    else
                    {
                        if(player.world.time % 10 != 0)
                            t.setColorRGBA_F(1, 0, 0, 1);
                        else
                            t.setColorRGBA_F(1, 1, 1, 1);
                    }
                }
                else
                    t.setColorRGBA_F(0, 0, 0, 1);
                t.addVertexWithUV(x, y, 0,heartBordersMinU,heartBordersMinV);
                t.addVertexWithUV(x+w, y, 0,heartBordersMaxU,heartBordersMinV);
                t.addVertexWithUV(x+h, y+h, 0,heartBordersMaxU,heartBordersMaxV);
                t.addVertexWithUV(x, y+h, 0,heartBordersMinU,heartBordersMaxV);
                t.setColorRGBA_F(1, 1, 1, 1);                
                float part = player.getMaxLife()/20f;
                part*=(float)i;

                if(player.life >= part && (player.life >= player.getMaxLife()/20f*(float)(i+1)))
                {
                    t.addVertexWithUV(x, y, 0,heartContentMinU,heartContentMinV);
                    t.addVertexWithUV(x+w, y, 0,heartContentMaxU,heartContentMinV);
                    t.addVertexWithUV(x+w, y+h, 0,heartContentMaxU,heartContentMaxV);
                    t.addVertexWithUV(x, y+h, 0,heartContentMinU,heartContentMaxV);
                }
                else
                {
                    if(player.life >= part)
                    {
                        float factor = (player.life-part)/(player.getMaxLife()/20f);
                        heartContentMaxV = heartContentMinV+((factor*16f)/UI.TEMPLATE_HEIGHT);
                        h*=factor;
                        t.addVertexWithUV(x, y, 0,heartContentMinU,heartContentMinV);
                        t.addVertexWithUV(x+w, y, 0,heartContentMaxU,heartContentMinV);
                        t.addVertexWithUV(x+w, y+h, 0,heartContentMaxU,heartContentMaxV);
                        t.addVertexWithUV(x, y+h, 0,heartContentMinU,heartContentMaxV);
                        
                        h=16f;
                        heartContentMaxV = 1f;
                    }
                }
                x+=w+1f;
            }
            t.flush();
            w=32;
            h=32;
            
		}

		@Override
		public void update()
		{
		    if(Keyboard.isKeyDown(Keyboard.KEY_H))
		        player.attackEntity(null, -1f);
		    if(player.alive == false)
		        BlockyMain.instance.removeHUDComponent(this);
		}
	}
	
	public void setSelectedHotBat(int i)
	{
	    this.invIndex = (float) i;
	}
}

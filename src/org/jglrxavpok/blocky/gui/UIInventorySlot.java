package org.jglrxavpok.blocky.gui;

import java.util.ArrayList;

import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.ui.UIComponentBase;
import org.jglrxavpok.opengl.FontRenderer;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.opengl.Textures;

public class UIInventorySlot extends UIComponentBase
{
	public ItemStack stack;
    private ArrayList<SlotListener> listeners = new ArrayList<SlotListener>();
	
	public UIInventorySlot(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.w = 32;
		this.h = 32;
	}
	
	public void setStack(ItemStack s)
	{
		this.stack = s;
	}
	
	public void update(int mx, int my, boolean[] buttonsPressed)
	{
		
	}
	
	public void renderBackground(int mx, int my, boolean[] buttonsPressed)
    {
        
    }
	
	public void renderOverlay(int mx, int my, boolean[] buttons)
	{
        if(this.isMouseOver(mx, my))
        {
            if(stack != null && stack.item != null)
            FontRenderer.drawString(stack.item.id + "", mx - FontRenderer.getWidth("" + stack.item.id) / 2, my + 10, 0xFFFFFF);
        }
	}
	
	public void render(int mx, int my, boolean[] buttonsPressed)
	{
		Tessellator t = Tessellator.instance;
		
		Textures.bind("/assets/textures/ui/inventory.png");
		
		t.startDrawingQuads();
		float minU = (this.isMouseOver(mx, my) ? 32f : 0) / 128f;
        float maxU = (this.isMouseOver(mx, my) ? 64f : 32f) / 128f;
        float minV = 96f / 128f;
        float maxV = 128f / 128f;
        t.addVertexWithUV(x, y, 0, minU, minV);
        t.addVertexWithUV(x + w, y, 0, maxU, minV);
        t.addVertexWithUV(x + w, y + h, 0, maxU, maxV);
        t.addVertexWithUV(x, y + h, 0, minU, maxV);
        t.flush();
        
        if(stack != null)
        {
        	stack.item.renderInventory(x + 6, y + 6, 20, 20);
        	
        	if(stack.nbr > 1)
        	{
        		FontRenderer.drawString(stack.nbr + "", x - FontRenderer.getWidth("" + stack.nbr) + 32, y, 0xFFFFFF);
        	}
        }
	}
	
	public void onKeyEvent(char c, int key, boolean releasedOrPressed)
	{
		
	}
	
	public void addListener(SlotListener s)
	{
	    listeners.add(s);
	}

    public boolean canStackBePut(ItemStack selectedStack)
    {
        return true;
    }

    public void onStackRemoved(ItemStack previous)
    {
        for(SlotListener s : listeners )
            s.onStackRemoved(this, previous);
    }

    public void onStackChanged(ItemStack previous)
    {
        for(SlotListener s : listeners )
            s.onStackChanged(this, previous);
    }
}

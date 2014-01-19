package org.jglrxavpok.blocky.gui;

import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.ui.UIMenu;
import org.jglrxavpok.opengl.FontRenderer;

public class UIContainer extends UIMenu implements SlotListener
{

    public UIInventorySlot[] slots;
    
    public EntityPlayer player;
    public long timer = 0;

    public UIContainer(EntityPlayer p, int size)
    {
        player = p;
        slots = new UIInventorySlot[size];
    }

    public void onMenuClose()
    {
        if(player.selectedStack != null)
        {
            ItemStack stack = player.selectedStack;
            player.dropStack(stack);
            player.selectedStack = null;
        }
    }
    
    public void onMouseEvent(int mx, int my, int button, boolean action)
    {
        if(button == 0 && this.timer + 200L < System.currentTimeMillis()) //lors du clic gauche
        {
            this.timer = System.currentTimeMillis();
            
            if(this.player.selectedStack == null)
            {
                for(int i = 0 ; i < this.slots.length ; i++)
                {
                    if(slots[i] != null)
                    if(slots[i].isMouseOver(mx, my))
                    {
                        if(slots[i].stack != null)
                        {
                            this.player.selectedStack = slots[i].stack;
                            slots[i].stack = (ItemStack) null;
                            slots[i].onStackRemoved(player.selectedStack);
                            player.inv.putStack(i, slots[i].stack);
                        }
                    }
                }
            }
            else
            {
                boolean flag = false;
                for(int i = 0 ; i < this.slots.length ; i++)
                {
                    if(slots[i] != null)
                    if(slots[i].canStackBePut(player.selectedStack))
                    {
                        if(slots[i].isMouseOver(mx, my))
                        {
                            flag = true;
                            if(slots[i].stack == null)
                            {
                                slots[i].stack = this.player.selectedStack;
                                this.player.selectedStack = (ItemStack) null;
                                slots[i].onStackChanged(null);
                                player.inv.putStack(i, slots[i].stack);
                            }
                            else
                            {
                                if(ItemStack.areItemStacksEquals(player.selectedStack, slots[i].stack))
                                {
                                    if(slots[i].stack.nbr < slots[i].stack.item.getMaxInStack())
                                    {
                                        if(slots[i].stack.nbr + player.selectedStack.nbr <= slots[i].stack.item.getMaxInStack())
                                        {
                                            slots[i].stack.nbr += player.selectedStack.nbr;
                                            player.selectedStack = (ItemStack) null;
                                            player.inv.putStack(i, slots[i].stack);
                                        }
                                        else if(slots[i].stack.nbr + player.selectedStack.nbr > slots[i].stack.item.getMaxInStack())
                                        {
                                            int toRemove = slots[i].stack.item.getMaxInStack() - player.selectedStack.nbr;
                                            
                                            slots[i].stack.nbr = slots[i].stack.item.getMaxInStack();
                                            slots[i].onStackChanged(slots[i].stack);
                                            player.selectedStack.nbr -= toRemove;
                                            player.inv.putStack(i, slots[i].stack);
                                        }
                                    }
                                }
                                else
                                {
                                    ItemStack temp = slots[i].stack;
                                    ItemStack tmp = slots[i].stack;
                                    slots[i].stack = player.selectedStack;
                                    slots[i].onStackChanged(tmp);
                                    player.selectedStack = temp;
                                    player.inv.putStack(i, slots[i].stack);
                                }
                            }
                        }
                    }
                    else if(slots[i].isMouseOver(mx, my))
                        flag = true;
                }
                if(!flag)
                {
                    ItemStack stack = player.selectedStack;
                    player.dropStack(stack);
                    player.selectedStack = null;
                }
            }
        }
        else if(button == 1 && this.timer + 200L < System.currentTimeMillis()) //et lors du clic droit
        {
            this.timer = System.currentTimeMillis();
            
            if(this.player.selectedStack == null)
            {
                for(int i = 0 ; i < this.slots.length ; i++)
                {
                    if(slots[i] != null)
                    if(slots[i].isMouseOver(mx, my))
                    {
                        if(slots[i].stack != null)
                        {
                            ItemStack toPlayer = ItemStack.copyTo(slots[i].stack);
                            toPlayer.nbr /= 2;
                            ItemStack toSlot = ItemStack.copyTo(slots[i].stack);
                            toSlot.nbr -= toPlayer.nbr;
                            this.player.selectedStack = toPlayer;
                            ItemStack tmp = slots[i].stack;
                            slots[i].stack = toSlot;
                            slots[i].onStackChanged(tmp);
                            player.inv.putStack(i, slots[i].stack);
                        }
                    }
                }
            }
            else
            {
                for(int i = 0 ; i < this.slots.length ; i++)
                {
                    if(slots[i] != null)
                    if(slots[i].isMouseOver(mx, my))
                    {
                        if(slots[i].stack == null)
                        {
                            ItemStack toSlot = ItemStack.copyTo(player.selectedStack);
                            toSlot.nbr = 1;
                            player.selectedStack.nbr--;
                            ItemStack tmp = slots[i].stack;
                            slots[i].stack = toSlot;
                            slots[i].onStackChanged(tmp);
                            player.inv.putStack(i, slots[i].stack);
                        }
                        else
                        {
                            if(ItemStack.areItemStacksEquals(player.selectedStack, slots[i].stack))
                            {
                                if(slots[i].stack.nbr < slots[i].stack.item.getMaxInStack())
                                {
                                    ItemStack tmp = slots[i].stack;
                                    slots[i].stack.nbr++;
                                    player.inv.putStack(i, slots[i].stack);
                                    slots[i].onStackChanged(tmp);
                                    player.selectedStack.nbr--;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if(player.selectedStack != null)
        {
            if(player.selectedStack.nbr <= 0)
            {
                player.selectedStack = (ItemStack) null;
            }
        }
        
        for(int i = 0;i<player.inv.getInventorySize();i++)
        {
            if(slots[i] != null)
            slots[i].stack = player.inv.getStackIn(i);
        }
    }

    public void update(int mx, int my, boolean[] buttonsPressed)
    {

    }
    
    public void render(int mx, int my, boolean buttons[])
    {
        super.render(mx, my, buttons);
        for(int i = 0 ; i < this.slots.length ; i++)
        {
            if(slots[i] != null)
            this.slots[i].render(mx, my, buttons);
        }
        
        
        if(player.selectedStack != null)
        {
            player.selectedStack.item.renderInventory(mx-10, my-10, 20, 20);
            
            if(player.selectedStack.nbr > 1)
            {
                FontRenderer.drawString(player.selectedStack.nbr + "", mx-10 - FontRenderer.getWidth("" + player.selectedStack.nbr) + 27, my-5-10, 0xFFFFFF);
            }
        }
    }

    @Override
    public void onStackRemoved(UIInventorySlot s, ItemStack previous)
    {}

    @Override
    public void onStackChanged(UIInventorySlot slot, ItemStack previous)
    {}
}

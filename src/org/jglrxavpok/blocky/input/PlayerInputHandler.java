package org.jglrxavpok.blocky.input;

import java.util.ArrayList;

import net.java.games.input.Component;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.entity.EntityPlayerSP;
import org.jglrxavpok.blocky.gui.UIInventory;
import org.jglrxavpok.blocky.gui.UIPauseMenu;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.utils.AABB;
import org.jglrxavpok.blocky.utils.DamageType;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class PlayerInputHandler implements InputProcessor
{

    private EntityPlayerSP player;
    private boolean menuButton;

    public PlayerInputHandler(EntityPlayerSP player)
    {
        this.player = player;
    }
    
    @Override
    public void onKeyEvent(char c, int key, boolean released)
    {
        if(BlockyMain.instance.hasControllerPlugged())
        {
            return;
        }
        if(key == Keyboard.KEY_Q)
        {
            player.vx-=1f;
        }
        if(key == Keyboard.KEY_D)
        {
            player.vx+=1f;
        }
        if(key == Keyboard.KEY_SPACE)
        {
            player.jump();
        }
        
        if(key == Keyboard.KEY_Q)
        {
            player.vx-=1f;
        }
        if(key == Keyboard.KEY_D)
        {
            player.vx+=1f;
        }
        if(key == Keyboard.KEY_SPACE)
        {
            player.jump();
        }
        
        if(this.getIntForKey(key) <= 9 && this.getIntForKey(key) >= 0)
        {
            player.setSelectedHotbar(this.getIntForKey(key));
        }
        
        if(released && UI.isMenuNull())
        if(key == Keyboard.KEY_E)
        {
            UI.displayMenu(new UIInventory(player));
        }
    }
    
    public int getIntForKey(int key)
    {
        switch(key)
        {
        case Keyboard.KEY_0 :
            return 9;
            
        case Keyboard.KEY_1 :
            return 0;
            
        case Keyboard.KEY_2 :
            return 1;
            
        case Keyboard.KEY_3 :
            return 2;
            
        case Keyboard.KEY_4 :
            return 3;
            
        case Keyboard.KEY_5 :
            return 4;
            
        case Keyboard.KEY_6 :
            return 5;
            
        case Keyboard.KEY_7 :
            return 6;
            
        case Keyboard.KEY_8 :
            return 7;
            
        case Keyboard.KEY_9 :
            return 8;
            
        default :
            return -1;
        }
    }

    @Override
    public void onMouseEvent(int mx, int my, int button, boolean released)
    {
        if(!BlockyMain.instance.hasControllerPlugged() || UI.doesMenuPauseGame())
        {
            int scroll = Mouse.getDWheel();
            if(scroll != 0)
            {
                this.player.incrementSelectedHotbar(-scroll/120f);
            }
        }
    }

    @Override
    public void onAxisUpdate(int controllerID, float value, Component component)
    {
        if(component.getIdentifier() == Component.Identifier.Axis.X)
        {
            if(Math.abs(value) > 0.2f)
                player.vx+=value*2f;
        }
    }

    @Override
    public void onButtonUpdate(int controllerID, boolean buttonPressed, Component component)
    {
        if(buttonPressed)
        {
            String buttonIndex;
            buttonIndex = component.getIdentifier().toString();
            if(buttonIndex.equals("2"))
                player.jump();
            int mx = BlockyMain.instance.getCursorX();
            int my = BlockyMain.instance.getCursorY();
            int tx = (int)(Math.floor((float)(mx-player.world.lvlox)/Block.BLOCK_WIDTH));
            int ty = (int)((float)(my-player.world.lvloy)/Block.BLOCK_HEIGHT);
            if(buttonIndex.equals("0"))
            {
                if(player.canReachBlock(tx, ty) && Block.getBlock(player.world.getBlockAt(tx, ty)).canBeDestroyedWith(player.getHeldItem(), player) && UI.isMenuNull())
                {
                    AABB selection = new AABB(tx*Block.BLOCK_WIDTH,ty*Block.BLOCK_HEIGHT,Block.BLOCK_WIDTH-1,Block.BLOCK_HEIGHT-1);
                    ArrayList<Entity> list = player.world.getEntitiesInAABB(selection, player);
                    if(list.size() > 0)
                    {
                        ItemStack held = player.getHeldItem();
                        float multiplier = 1;
                        for(Entity e : list)
                        {
                            if(held != null)
                            {
                                multiplier = held.getStrengthAgainstEntity(player, e,player.world);
                            }
                            if(e.canBeHurt(DamageType.generic))
                            {
                                e.attackFrom(DamageType.getWithOwner(DamageType.generic, player), multiplier);
                            }
                        }
                    }
                    else
                    {
                        ItemStack held = player.getHeldItem();
                        int multiplier = 1;
                        if(held != null)
                        {
                            multiplier = held.getStrengthAgainstBlock(player, tx,ty,player.world);
                        }
                        player.world.setAttackValue(tx, ty, player.world.getAttackValue(tx,ty)+multiplier, player.username);
                    }
                }
            }
            if(buttonIndex.equals("1"))
            {
                if(player.canReachBlock(tx, ty) && !Block.getBlock(this.player.world.getBlockAt(tx, ty)).isSolid() && Block.getBlock(this.player.world.getBlockAt(tx, ty)).canBlockBePlaced(this.player.world, tx, ty))
                {
                    if(player.getHeldItem() != null)
                        player.getHeldItem().use(player, tx, ty, player.world);
                    if(player.world.getBlockAt(tx, ty) != null)
                    {
                        Block.getBlock(player.world.getBlockAt(tx, ty)).onRightClick(player.world, player, tx, ty);
                    }
                }
            }
            
            if(UI.isMenuNull())
            {
                if(buttonIndex.equals("8") && !menuButton)
                {
                    UI.displayMenu(new UIPauseMenu());
                    menuButton = true;
                }
                else if(buttonIndex.equals("9") && !menuButton)
                {
                    UI.displayMenu(new UIInventory(player));
                    menuButton = true;
                }
            }
            else
            {
                if(buttonIndex.equals("8") || buttonIndex.equals("9") && !menuButton)
                {
                    menuButton = true;
                    UI.displayMenu(null);
                }
            }
        }
        else
            menuButton = false;
    }

    @Override
    public void onPovUpdate(int controllerID, float hatSwitchPosition, Component component)
    {
        if(Float.compare(hatSwitchPosition, Component.POV.UP) == 0)
        {
            player.incrementSelectedHotbar(-0.075f*2f);
        }
        else if(Float.compare(hatSwitchPosition, Component.POV.DOWN) == 0)
        {
            player.incrementSelectedHotbar(0.075f*2f);
        }
        else if(Float.compare(hatSwitchPosition, Component.POV.LEFT) == 0)
        {
            player.incrementSelectedHotbar(-0.075f*2f);
        }
        else if(Float.compare(hatSwitchPosition, Component.POV.RIGHT) == 0)
        {
            player.incrementSelectedHotbar(0.075f*2f);
        }
        else if(Float.compare(hatSwitchPosition, Component.POV.UP_LEFT) == 0)
        {
        }
        else if(Float.compare(hatSwitchPosition, Component.POV.UP_RIGHT) == 0)
        {
        }
        else if(Float.compare(hatSwitchPosition, Component.POV.DOWN_LEFT) == 0)
        {
        }
        else if(Float.compare(hatSwitchPosition, Component.POV.DOWN_RIGHT) == 0)
        {
        }
    }

    @Override
    public void onCustomComponentUpdate(int controllerID, Component component)
    {
        if(component.getIdentifier() == Component.Identifier.Axis.RZ)
        {
            BlockyMain.instance.setCursorPos(BlockyMain.instance.getCursorX(), BlockyMain.instance.getCursorY()-(int)(component.getPollData()*3f));
        }
        else if(component.getIdentifier() == Component.Identifier.Axis.Z)
        {
            BlockyMain.instance.setCursorPos(BlockyMain.instance.getCursorX()+(int)(component.getPollData()*3f), BlockyMain.instance.getCursorY());
        }
    }
    
    public void onUpdate()
    {
        if(player.alive == false || BlockyMain.instance.getLevel() != player.world)
            BlockyMain.instance.removeInputProcessor(this);
        if(!BlockyMain.instance.hasControllerPlugged())
        {
            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            {
                player.jump();
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_Q))
            {
                player.vx-=1f*2;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_D))
            {
                player.vx+=1f*2;
            }
            int mx = BlockyMain.instance.getCursorX();
            int my = BlockyMain.instance.getCursorY();
            int tx = (int)(Math.floor((float)(mx-player.world.lvlox)/Block.BLOCK_WIDTH));
            int ty = (int)((float)(my-player.world.lvloy)/Block.BLOCK_HEIGHT);
            if(Mouse.isButtonDown(0))
            {
                
                if(player.canReachBlock(tx, ty) && Block.getBlock(player.world.getBlockAt(tx, ty)).canBeDestroyedWith(player.getHeldItem(), player) && UI.isMenuNull())
                {
                    AABB selection = new AABB(tx*Block.BLOCK_WIDTH,ty*Block.BLOCK_HEIGHT,Block.BLOCK_WIDTH,Block.BLOCK_HEIGHT);
                    ArrayList<Entity> list = player.world.getEntitiesInAABB(selection, player);
                    if(list.size() > 0)
                    {
                        ItemStack held = player.getHeldItem();
                        float multiplier = 1;
                        for(Entity e : list)
                        {
                            if(held != null)
                            {
                                multiplier = held.getStrengthAgainstEntity(player, e,player.world);
                            }
                            if(e.canBeHurt(DamageType.generic))
                            {
                                e.attackFrom(DamageType.getWithOwner(DamageType.generic, player), multiplier);
                            }
                        }
                    }
                    else
                    {
                        ItemStack held = player.getHeldItem();
                        int multiplier = 1;
                        if(held != null)
                        {
                            multiplier = held.getStrengthAgainstBlock(player, tx,ty,player.world);
                        }
                        player.world.setAttackValue(tx, ty, player.world.getAttackValue(tx,ty)+multiplier, player.username);
                    }
                }
            }
            else if(Mouse.isButtonDown(1))
            {
                if(player.canReachBlock(tx, ty))
                {
                    if(player.getHeldItem() != null)
                        player.getHeldItem().use(player, tx, ty, player.world);
                    if(player.world.getBlockAt(tx, ty) != null)
                    {
                        Block.getBlock(player.world.getBlockAt(tx, ty)).onRightClick(player.world, player, tx, ty);
                    }
                }
                
            }
        }
    }

}

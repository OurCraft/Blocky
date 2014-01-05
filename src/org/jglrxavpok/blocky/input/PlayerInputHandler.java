package org.jglrxavpok.blocky.input;

import net.java.games.input.Component;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.entity.EntityPlayerSP;
import org.jglrxavpok.blocky.gui.UIPauseMenu;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.items.ItemBlock;
import org.jglrxavpok.blocky.ui.UI;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class PlayerInputHandler implements InputProcessor
{

    private EntityPlayerSP player;

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
    }

    @Override
    public void onMouseEvent(int mx, int my, int button, boolean released)
    {
        if(!BlockyMain.instance.hasControllerPlugged() || UI.doesMenuPauseGame())
        {
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
            int tx = (int)((float)(mx-player.world.lvlox)/Block.BLOCK_WIDTH);
            int ty = (int)((float)(my-player.world.lvloy)/Block.BLOCK_HEIGHT);
            if(buttonIndex.equals("0"))
            {
                if(!UI.isMenuNull())
                {
                    UI.onMouseEvent(BlockyMain.instance.getCursorX(), BlockyMain.instance.getCursorY(), 0, buttonPressed);
                }
                if(player.canReachBlock(tx, ty) && Block.getBlock(player.world.getBlockAt(tx, ty)).canBeDestroyedWith(player.getHeldItem(), player))
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
            if(buttonIndex.equals("1"))
            {
                if(player.canReachBlock(tx, ty) && !Block.getBlock(this.player.world.getBlockAt(tx, ty)).isSolid())
                {
                    if(player.getHeldItem() != null)
                        player.getHeldItem().use(player, tx, ty, player.world);
                }
            }
            
            if(buttonIndex.equals("8"))
                UI.displayMenu(new UIPauseMenu());
        }
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
            int tx = (int)((float)(mx-player.world.lvlox)/Block.BLOCK_WIDTH);
            int ty = (int)((float)(my-player.world.lvloy)/Block.BLOCK_HEIGHT);
            if(Mouse.isButtonDown(0))
            {
                
                if(player.canReachBlock(tx, ty) && Block.getBlock(player.world.getBlockAt(tx, ty)).canBeDestroyedWith(player.getHeldItem(), player))
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
            else if(Mouse.isButtonDown(1))
            {
                if(player.canReachBlock(tx, ty) && !Block.getBlock(this.player.world.getBlockAt(tx, ty)).isSolid())
                {
                    if(player.getHeldItem() != null)
                        player.getHeldItem().use(player, tx, ty, player.world);
                }
            }
        }
    }

}

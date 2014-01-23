package org.jglrxavpok.blocky.client;

import java.io.IOException;
import java.util.ArrayList;

import net.java.games.input.Component;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.entity.EntityPlayerSP;
import org.jglrxavpok.blocky.gui.UIPauseMenu;
import org.jglrxavpok.blocky.input.InputProcessor;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.netty.NettyClientHandler;
import org.jglrxavpok.blocky.netty.NettyCommons;
import org.jglrxavpok.blocky.server.PacketPlayer;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.utils.AABB;
import org.jglrxavpok.blocky.utils.DamageType;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class ClientPlayerInputHandler implements InputProcessor
{

    private EntityPlayerSP player;

    public ClientPlayerInputHandler(EntityPlayerSP player)
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
            BlockyMain.instance.setCursorPos((int)(mx*1d/BlockyMain.ratioW),(int)(my*1d/BlockyMain.ratioH));
            int scroll = Mouse.getDWheel();
            if(scroll != 0)
            {
                player.incrementSelectedHotbar(-scroll/120f);
            }
        }
    }

    @Override
    public void onAxisUpdate(int controllerID, float value, Component component)
    {
        if(component.getIdentifier() == Component.Identifier.Axis.X)
        {
            if(Math.abs(value) > 0.2f)
                player.vx+=value;
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
                else if(player.canReachBlock(tx, ty) && Block.getBlock(player.world.getBlockAt(tx, ty)).canBeDestroyedWith(player.getHeldItem(), player) && UI.isMenuNull())
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
            
            if(buttonIndex.equals("8"))
                UI.displayMenu(new UIPauseMenu());
        }
    }

    @Override
    public void onPovUpdate(int controllerID, float hatSwitchPosition, Component component)
    {
        if(Float.compare(hatSwitchPosition, Component.POV.UP) == 0)
        {
            player.incrementSelectedHotbar(-0.075f);
        }
        else if(Float.compare(hatSwitchPosition, Component.POV.DOWN) == 0)
        {
            player.incrementSelectedHotbar(0.075f);
        }
        else if(Float.compare(hatSwitchPosition, Component.POV.LEFT) == 0)
        {
            player.incrementSelectedHotbar(-0.075f);
        }
        else if(Float.compare(hatSwitchPosition, Component.POV.RIGHT) == 0)
        {
            player.incrementSelectedHotbar(0.075f);
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
    }
    
    public void onUpdate()
    {
        if(player.alive == false)
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
        }
        if(NettyClientHandler.current != null)
            try
            {
                NettyCommons.sendPacket(new PacketPlayer(player), NettyClientHandler.current.serverChannel);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
    }
    
}
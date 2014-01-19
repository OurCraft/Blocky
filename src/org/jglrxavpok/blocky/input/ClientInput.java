package org.jglrxavpok.blocky.input;

import net.java.games.input.Component;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.ui.UI;

public class ClientInput implements InputProcessor
{

    @Override
    public void onKeyEvent(char c, int key, boolean released)
    {
        
    }

    @Override
    public void onMouseEvent(int mx, int my, int button, boolean released)
    {
        if(!BlockyMain.instance.hasControllerPlugged() || UI.doesMenuPauseGame())
        {
            BlockyMain.instance.setCursorPos((int)(mx*1d/BlockyMain.ratioW),(int)(my*1d/BlockyMain.ratioH));
        }
    }

    @Override
    public void onAxisUpdate(int controllerID, float value, Component component)
    {
        
    }

    @Override
    public void onButtonUpdate(int controllerID, boolean buttonPressed,
            Component component)
    {
        if(buttonPressed)
        {
            String buttonIndex;
            buttonIndex = component.getIdentifier().toString();
            if(buttonIndex.equals("0"))
            {
                if(!UI.isMenuNull())
                {
                    UI.onMouseEvent(BlockyMain.instance.getCursorX(), BlockyMain.instance.getCursorY(), 0, buttonPressed);
                }
            }
            if(buttonIndex.equals("1"))
            {
                if(!UI.isMenuNull())
                {
                    UI.onMouseEvent(BlockyMain.instance.getCursorX(), BlockyMain.instance.getCursorY(), 1, buttonPressed);
                }
            }
            
        }
    }

    @Override
    public void onPovUpdate(int controllerID, float value, Component component)
    {
        
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

    @Override
    public void onUpdate()
    {
        
    }

}

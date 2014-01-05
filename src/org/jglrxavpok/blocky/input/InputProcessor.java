package org.jglrxavpok.blocky.input;

import net.java.games.input.Component;

public interface InputProcessor
{

    public void onKeyEvent(char c, int key, boolean released);
    
    public void onMouseEvent(int mx, int my, int button, boolean released);
    
    public void onAxisUpdate(int controllerID, float value, Component component);
    
    public void onButtonUpdate(int controllerID, boolean buttonPressed, Component component);
    
    public void onPovUpdate(int controllerID, float value, Component component);
    
    /**
     * @param controllerID
     * @param component
     */
    public void onCustomComponentUpdate(int controllerID, Component component);

    public void onUpdate();
}

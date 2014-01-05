package org.jglrxavpok.blocky.gui;

import org.jglrxavpok.blocky.ui.UIMenu;
import org.jglrxavpok.opengl.FontRenderer;
import org.jglrxavpok.opengl.Tessellator.OpenGlHelper;

public class UIGameOverMenu extends UIMenu
{

    public void render(int mx, int my, boolean[] buttons)
    {
        super.render(mx, my, buttons);
        OpenGlHelper.startNegativeMode();
        FontRenderer.setScale(2);
        FontRenderer.drawString("GAME OVER", mx, my, 0xFFFFFF);
        FontRenderer.setScale(1);
        OpenGlHelper.endNegativeMode();
    }
}

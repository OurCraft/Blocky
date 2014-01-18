package org.jglrxavpok.blocky.gui;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import javax.imageio.ImageIO;

import org.jglrxavpok.blocky.ui.ActionListener;
import org.jglrxavpok.blocky.ui.UISlot;
import org.jglrxavpok.blocky.utils.LWJGLHandler;
import org.jglrxavpok.blocky.world.WorldInfos;
import org.jglrxavpok.opengl.FontRenderer;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.opengl.Textures;

public class UIWorldSlot extends UISlot
{

    private WorldInfos infos;
    private int saveTexID = -1;
    private int saveTexW;
    private int saveTexH;

    public UIWorldSlot(ActionListener listener, String name, File folder)
    {
        super(listener, 500, 100);
        try
        {
            infos = new WorldInfos();
            File levelData = new File(folder, "level.data");
            DataInputStream input = new DataInputStream(new FileInputStream(levelData));
            infos.worldName = input.readUTF();
            infos.worldType = input.readInt();
            infos.worldTime = input.readLong();
            infos.lastModified = input.readLong();
            infos.worldFolder = folder;
            input.close();
            File saveImg = new File(folder, "save.png");
            if(saveImg.exists())
            {
                try
                {
                    BufferedImage img = ImageIO.read(saveImg);
                    saveTexW = img.getWidth();
                    saveTexH = img.getHeight();
                    saveTexID = LWJGLHandler.loadTexture(img);
                }
                catch(Exception e)
                {
                    System.err.println("Couldn't load save image");
                }
            }
        }
        catch(Exception e)
        {
            throw new IllegalArgumentException("level.data file is corrumpted", e);
        }
    }

    @SuppressWarnings("deprecation")
    public void renderSlot(int id, float x, float y, int mx, int my, boolean[] buttons, boolean selected)
    {
        super.renderSlot(id, x, y, mx, my, buttons, selected);
        String n = infos.worldName;
        FontRenderer.drawString(n, x, y+h-16, !isMouseOver(x,y,w,h,mx, my) ? 0xFFFFFF: 0xFFFF00);
        FontRenderer.drawString(infos.worldFolder.getName(), x, y+h-32, 0xC0C0C0);
        FontRenderer.drawString(new Date(infos.lastModified).toLocaleString(), x, y+h-32-16, 0xC0C0C0);
        if(saveTexID != -1)
        {
            float texH = this.h;
            float texW = saveTexW*(this.h/this.saveTexH);
            Textures.bind(saveTexID);
            Tessellator t = Tessellator.instance;
            t.startDrawingQuads();
            t.setColorRGBA_F(1, 1,1,0.50f);
            t.addVertexWithUV(x+this.w-texW, y, 0,0,0);
            t.addVertexWithUV(x+this.w, y, 0,1,0);
            t.addVertexWithUV(x+this.w, y+texH, 0,1,1);
            t.addVertexWithUV(x+this.w-texW, y+texH, 0,0,1);
            t.flush();
        }
    }
    
    public WorldInfos getWorldInfos()
    {
        return infos;
    }
}

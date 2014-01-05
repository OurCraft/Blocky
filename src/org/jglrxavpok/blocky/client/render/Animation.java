package org.jglrxavpok.blocky.client.render;

import java.util.ArrayList;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.opengl.Tessellator;

public class Animation
{

    private int id;
    private int texW;
    private int texH;
    private int frameW;
    private int frameH;
    private float frameDuration;
    private float frame;
    private long lastChecked;

    /**
     * 
     * @param texID
     * @param texW
     * @param texH
     * @param frameW
     * @param frameH
     * @param frameDuration : 1f = 1 second
     */
    public Animation(int texID, int texW, int texH, int frameW, int frameH, float frameDuration)
    {
        this.frameDuration = frameDuration;
        this.texW = texW;
        this.texH = texH;
        this.frameW = frameW;
        this.frameH = frameH;
        this.id = texID;
    }
    
    public void render(float posX, float posY, float w, float h)
    {
        frame+=(lastChecked-BlockyMain.instance.timeRunning)*(frameDuration/60f);
        lastChecked = BlockyMain.instance.timeRunning;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        int frameNbr = (int)frame;
        float minU = (frameNbr*frameW)/texW;
        float maxU = ((frameNbr+1)*frameW)/texW;
        float minV = (frameNbr*frameH)/texH;
        float maxV = ((frameNbr+1)*frameH)/texH;
        t.addVertexWithUV(posX, posY, 0, minU, minV);
        t.addVertexWithUV(posX+w, posY, 0, maxU, minV);
        t.addVertexWithUV(posX+w, posY+h, 0, maxU, maxV);
        t.addVertexWithUV(posX, posY+h, 0, minU, maxV);
        t.flush();
    }
}

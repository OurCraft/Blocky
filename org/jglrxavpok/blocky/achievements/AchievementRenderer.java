package org.jglrxavpok.blocky.achievements;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.utils.HUDComponent;
import org.jglrxavpok.opengl.FontRenderer;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.opengl.Textures;

public class AchievementRenderer implements HUDComponent
{

    private LinkedList<Achievement> queue;
    private Achievement currentAchievement;
    private long lastTime;
    private float x=0,y=0;
    private HashMap<Achievement, Point> map = new HashMap<Achievement, Point>();
    public static final float ACHIEVEMENT_ICONS_WIDTH = 200f;
    public static final float ACHIEVEMENT_ICONS_HEIGHT = 200f;

    public AchievementRenderer()
    {
        queue = new LinkedList<Achievement>();
        this.setAchievementIconPos(AchievementList.beginNewWorld, 1, 0);
        this.setAchievementIconPos(AchievementList.touchWater, 2, 0);
    }
    
    public AchievementRenderer setAchievementIconPos(Achievement a, int xIndex, int yIndex)
    {
        map.put(a,new Point(xIndex, yIndex));
        
        return this; // For chaining
    }
    
    public void drawAchievementIcon(float x, float y, float w, float h, Achievement a)
    {
        Point p = map.get(a);
        if(p == null)
        {
            this.setAchievementIconPos(a, 0, 0);
            p = map.get(a);
        }
        Textures.bind("/assets/textures/achievementIcons.png");
        float minU = (p.x*20f)/ACHIEVEMENT_ICONS_WIDTH;
        float maxU = (p.x*20f+20f)/ACHIEVEMENT_ICONS_WIDTH;
        float maxV = 1f-(p.y*20f)/ACHIEVEMENT_ICONS_HEIGHT;
        float minV = 1f-(p.y*20f+20)/ACHIEVEMENT_ICONS_HEIGHT;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(x, y, 0, minU, minV);
        t.addVertexWithUV(x+w, y, 0, maxU, minV);
        t.addVertexWithUV(x+w, y+h, 0, maxU, maxV);
        t.addVertexWithUV(x, y+h, 0, minU, maxV);
        t.flush();
    }
    
    @Override
    public void render()
    {
        if(currentAchievement != null)
        {
            Textures.bind("/assets/textures/ui/compsTemplate.png");
            Tessellator t = Tessellator.instance;
            
            int type = 0;
            float w = 300;
            float h = 80;
            x = BlockyMain.width/2-w/2;
            float minU = 0f;
            float maxU = 10f/UI.TEMPLATE_WIDTH;
            float minV = (UI.TEMPLATE_HEIGHT-type*20f+10f-10f)/UI.TEMPLATE_HEIGHT;
            float maxV = (UI.TEMPLATE_HEIGHT-type*20f-10f)/UI.TEMPLATE_HEIGHT;
            t.startDrawingQuads();
            t.setColorRGBA_F(1, 1,1,0.75f);
            /**
             * Fill center
             */
            {
                float widthToFill = (x+w-10f)-(x+10f);
                float heightToFill = (y+h-10f)-(y+10f);
                float xNbr = widthToFill/10f;
                float yNbr = heightToFill/10f;
                minU = 90f/UI.TEMPLATE_WIDTH;
                maxU = 100f/UI.TEMPLATE_WIDTH;
                minV = (UI.TEMPLATE_HEIGHT-type*20f+10f-10f)/UI.TEMPLATE_HEIGHT;
                maxV = (UI.TEMPLATE_HEIGHT-type*20f-10f)/UI.TEMPLATE_HEIGHT;
                for(int i = 0;i<xNbr;i++)
                {
                    for(int ii = 0;ii<yNbr;ii++)
                    {
                        t.addVertexWithUV(x+10f+i*10f, y+10f+ii*10f, 0, minU, minV);
                        t.addVertexWithUV(x+20f+i*10f, y+10f+ii*10f, 0, maxU, minV);
                        t.addVertexWithUV(x+20f+i*10f, y+20f+ii*10f, 0, maxU, maxV);
                        t.addVertexWithUV(x+10f+i*10f, y+20f+ii*10f, 0, minU, maxV);
                    }   
                }
            }
            /**
             *  Fill borders
             */
            {
                float widthToFill = (x+w-10f)-(x+10f);
                if(widthToFill > 0)
                {
                    float xNbr = widthToFill/10f;
                    for(int i = 0;i<(int)xNbr;i++)
                    {
                        minU = 70f/UI.TEMPLATE_WIDTH;
                        maxU = 80f/UI.TEMPLATE_WIDTH;
                        minV = (UI.TEMPLATE_HEIGHT-type*20f+10f-20f)/UI.TEMPLATE_HEIGHT;
                        maxV = (UI.TEMPLATE_HEIGHT-type*20f-20f)/UI.TEMPLATE_HEIGHT;
                        t.addVertexWithUV(x+10f+i*10f, y+h-10f, 0, minU, minV);
                        t.addVertexWithUV(x+20f+i*10f, y+h-10f, 0, maxU, minV);
                        t.addVertexWithUV(x+20f+i*10f, y+h, 0, maxU, maxV);
                        t.addVertexWithUV(x+10f+i*10f, y+h, 0, minU, maxV);

                        
                        minV = (UI.TEMPLATE_HEIGHT-type*20f+10f-10f)/UI.TEMPLATE_HEIGHT;
                        maxV = (UI.TEMPLATE_HEIGHT-type*20f-10f)/UI.TEMPLATE_HEIGHT;
                        t.addVertexWithUV(x+10f+i*10f, y, 0, minU, minV);
                        t.addVertexWithUV(x+20f+i*10f, y, 0, maxU, minV);
                        t.addVertexWithUV(x+20f+i*10f, y+10f, 0, maxU, maxV);
                        t.addVertexWithUV(x+10f+i*10f, y+10f, 0, minU, maxV);

                    }
                }
                
                float heightToFill = (y+h-10f)-(y+10f);
                if(heightToFill > 0)
                {
                    float yNbr = heightToFill/10f;
                    for(int i = 0;i<(int)yNbr;i++)
                    {
                        minV = (UI.TEMPLATE_HEIGHT-type*20f-20f+20f)/UI.TEMPLATE_HEIGHT;
                        maxV = (UI.TEMPLATE_HEIGHT-type*20f-30f+20f)/UI.TEMPLATE_HEIGHT;
                        minU = 80f/UI.TEMPLATE_WIDTH;
                        maxU = 90f/UI.TEMPLATE_WIDTH;
                        t.addVertexWithUV(x, y+10f+i*10f, 0, minU, minV);
                        t.addVertexWithUV(x+10f, y+10f+i*10f, 0, maxU, minV);
                        t.addVertexWithUV(x+10f, y+20f+i*10f, 0, maxU, maxV);
                        t.addVertexWithUV(x, y+20f+i*10f, 0, minU, maxV);

                        minV = (UI.TEMPLATE_HEIGHT-type*20f+10f-20f)/UI.TEMPLATE_HEIGHT;
                        maxV = (UI.TEMPLATE_HEIGHT-type*20f-20f)/UI.TEMPLATE_HEIGHT;
                        t.addVertexWithUV(x+w-10f, y+10f+i*10f, 0, minU, minV);
                        t.addVertexWithUV(x+w, y+10f+i*10f, 0, maxU, minV);
                        t.addVertexWithUV(x+w, y+20f+i*10f, 0, maxU, maxV);
                        t.addVertexWithUV(x+w-10f, y+20f+i*10f, 0, minU, maxV);
                    }
                }
            }
            
            /**
             * Corners
             */
            {
                minU = 50f/UI.TEMPLATE_WIDTH;
                maxU = 60f/UI.TEMPLATE_WIDTH;
                minV = (UI.TEMPLATE_HEIGHT-type*20f+10f-10f)/UI.TEMPLATE_HEIGHT;
                maxV = (UI.TEMPLATE_HEIGHT-type*20f-10f)/UI.TEMPLATE_HEIGHT;
                t.addVertexWithUV(x, y, 0, minU, minV);
                t.addVertexWithUV(x+10f, y, 0, maxU, minV);
                t.addVertexWithUV(x+10f, y+10f, 0, maxU, maxV);
                t.addVertexWithUV(x, y+10f, 0, minU, maxV);
                
                minU = (10f+50f)/UI.TEMPLATE_WIDTH;
                maxU = (20f+50f)/UI.TEMPLATE_WIDTH;
                
                t.addVertexWithUV(x+w-10f, y, 0, minU, minV);
                t.addVertexWithUV(x+w, y, 0, maxU, minV);
                t.addVertexWithUV(x+w, y+10f, 0, maxU, maxV);
                t.addVertexWithUV(x+w-10f, y+10f, 0, minU, maxV);
                
                minV = (UI.TEMPLATE_HEIGHT-type*20f+10f-20f)/UI.TEMPLATE_HEIGHT;
                maxV = (UI.TEMPLATE_HEIGHT-type*20f-20f)/UI.TEMPLATE_HEIGHT;
                t.addVertexWithUV(x+w-10f, y+h-10f, 0, minU, minV);
                t.addVertexWithUV(x+w, y+h-10f, 0, maxU, minV);
                t.addVertexWithUV(x+w, y+h, 0, maxU, maxV);
                t.addVertexWithUV(x+w-10f, y+h, 0, minU, maxV);
                
                minU = 50f/UI.TEMPLATE_WIDTH;
                maxU = (10f+50f)/UI.TEMPLATE_WIDTH;
                t.addVertexWithUV(x, y+h-10f, 0, minU, minV);
                t.addVertexWithUV(x+10f, y+h-10f, 0, maxU, minV);
                t.addVertexWithUV(x+10f, y+h, 0, maxU, maxV);
                t.addVertexWithUV(x, y+h, 0, minU, maxV);
            }
            
            t.flush();

            FontRenderer.drawShadowedString("Achievement get!", x+10, y+h-30, 0xFFFF00);
            FontRenderer.drawShadowedString(currentAchievement.getName(), x+75, y+h-60, 0xFFFFFF);
            this.drawAchievementIcon(x+(75+10)/4, y+10, 40, 40, currentAchievement);
        }
    }

    @Override
    public void update()
    {
        try
        {
            if(currentAchievement == null)
            {
                currentAchievement = queue.removeFirst();
                lastTime = System.currentTimeMillis();
                x = BlockyMain.width/2-100;
                y=-80;
            }
            else
            {
                if(System.currentTimeMillis()-lastTime < 4000)
                    y+=2;
                else
                    y-=1;
                
                if(y > 50)
                    y = 50;
                if(y < -80)
                    currentAchievement = null;
            }
        }
        catch(NoSuchElementException e)
        {
        }
    }

    public void newAchievement(Achievement achievement)
    {
        queue.add(achievement);
    }

}

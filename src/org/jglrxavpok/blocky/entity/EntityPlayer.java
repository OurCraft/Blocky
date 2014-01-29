package org.jglrxavpok.blocky.entity;

import java.awt.Color;
import java.util.ArrayList;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.achievements.Achievement;
import org.jglrxavpok.blocky.achievements.AchievementDataBase;
import org.jglrxavpok.blocky.achievements.AchievementList;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.inventory.BasicInventory;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.items.Item;
import org.jglrxavpok.blocky.lexa.LexaManager;
import org.jglrxavpok.blocky.lexa.SkinObject;
import org.jglrxavpok.blocky.utils.MathHelper;
import org.jglrxavpok.opengl.FontRenderer;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.opengl.Textures;
import org.jglrxavpok.storage.TaggedStorageChunk;
import org.lwjgl.opengl.GL11;

public class EntityPlayer extends EntityLiving
{

    protected float frame = 0;
    public float   invIndex = 0;
    public String username = "Player_"+rand.nextInt(200000);
    private ArrayList<Achievement> achievementGet = new ArrayList<Achievement>();
    private SkinObject skin;
    
    public ItemStack selectedStack = null;

    public EntityPlayer()
    {
        inv = new BasicInventory(40);
        maxPositiveVX = 3;
        maxNegativeVX = -3;
        decceleration= 0.25f;
        w = 24;
        h = 24*2;
    }
    
    public TaggedStorageChunk writeTaggedStorageChunk(int nbr)
    {
        TaggedStorageChunk chunk = super.writeTaggedStorageChunk(nbr);
        chunk.setString("username", username);
        return chunk;
    }
    
    public void readFromChunk(TaggedStorageChunk chunk)
    {
        super.readFromChunk(chunk);
        username = chunk.getString("username");
    }
    
    public void tick()
    {
        super.tick();
        if(this.fluidIn != null && this.fluidIn.getName().equals("water") && !hasAchievement(AchievementList.touchWater))
        {
            this.toggleAchievement(AchievementList.touchWater);
        }
        if(getHeldItem() != null)
        {
            getHeldItem().update(this, x, y, world);
        }
        
        if(this.inv.containsItem(Block.torch.getItem().id) && this.fluidIn == null)
        {
            if(x >= 0)
                Block.torch.onWorldUpdate((int) (x / Block.BLOCK_WIDTH), (int) (y / Block.BLOCK_HEIGHT) + 1, world);
            else
                Block.torch.onWorldUpdate((int) (x / Block.BLOCK_WIDTH) - 1, (int) (y / Block.BLOCK_HEIGHT) + 1, world);
        }
    }
    
    private boolean hasAchievement(Achievement a)
    {
        return achievementGet.contains(a);
    }

    public boolean shouldSendUpdate()
    {
        return false;
    }
    
    public void render(float posX,float posY, float alpha)
    {
        if(skin == null)
        {
            skin = new SkinObject();
            if(this instanceof EntityDemoPlayer)
            {
            }
            else
            {
                new Thread()
                {
                    public void run()
                    {
                        skin = LexaManager.getSkinForPlayer(username);
                    }
                }.start();
            }
        }
        if(vx < 0.f)
            direction = 0;
        else if(vx > 0.f)
            direction  = 1;
        if(vx == 0f)
            frame+=0.025f*2;
        else
            frame+=0.075f*2;
        if(frame >= 4f)
        {
            frame = 0f;
        }
        String textureSuffix = "";
        if(vx != 0f)
        {
            textureSuffix = "Walking";
        }
        String parts[] = new String[]
                {
                "head","body","hands","legs","feet"
                };
        Color c = null;
        float val = world.getLightValue((int)((x+w/2)/Block.BLOCK_WIDTH), (int)((y+h/2)/Block.BLOCK_HEIGHT));
        if(val < 0.05f)
            val = 0.05f;
        Tessellator t = Tessellator.instance;
        for(String part : parts)
        {
            c = new Color(skin.getColor(part));
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, Textures.getFromClasspath("/assets/textures/entities/player/"+part+textureSuffix+".png"));
            float minU = 0;
            float maxU = 0;
            if(direction == 1)
            {
                minU = ((int)frame*24)/96f;
                maxU = ((int)(frame+1)*24)/96f;
            }
            else
            {
                maxU = ((int)frame*24)/96f;
                minU = ((int)(frame+1)*24)/96f;
            }
            t.startDrawingQuads();
            t.setColorRGBA_F(val*(c.getRed()/255f),val*(c.getGreen()/255f),val*(c.getBlue()/255f),1f);
            t.addVertexWithUV(posX, posY, 0, minU, 0);
            t.addVertexWithUV(posX+w, posY, 0, maxU, 0);
            t.addVertexWithUV(posX+w, posY+h, 0, maxU, 1);
            t.addVertexWithUV(posX, posY+h, 0, minU, 1);
            t.setColorRGBA_F(1,1,1,1f);
            t.flush();
        }
        
        ItemStack s = this.getHeldItem();
        if(s != null)
        {
            Item i = s.item;
            GL11.glPushMatrix();
            float ix = posX;
            float iy = posY+h/2f;
            float iangle = -15f;
            float translationX = ix;
            float translationY = iy+6f;
            if(direction == 1)
            {
                ix+=w-2.5f;
                translationX = ix;
                translationY = iy-2;
            }
            else
            {
                iangle = -iangle;
                ix-=10;
                translationX = ix+0.5f;
                translationY = iy-6;
            }
                
            GL11.glTranslatef(translationX, translationY, 0);
            if(i.shouldBeRotatedAtHandRendering())
            GL11.glRotatef(iangle, 0, 0, 1);
            
            Textures.bind("/assets/textures/items.png");
            t.setColorRGBA_F(val,val,val,1f);
            i.renderHand(0, 0, 12, 12, direction == 1, this);
            GL11.glPopMatrix();
        }
    }
    
    public void postWorldRender(float posX, float posY)
    {
        FontRenderer.drawShadowedString(username, posX+w/2-FontRenderer.getWidth(username)/2, posY+h, 0xFFFFFF);
    }
    
    public void jump()
    {
        if(!isInAir)
        {
            vy = 5;
        }
    }

    public boolean canReachBlock(int x, int y)
    {
        double dist = MathHelper.dist(x, y, 0, this.x/Block.BLOCK_WIDTH, this.y/Block.BLOCK_HEIGHT, 0);
        return dist < 4f;
    }
    
    public ItemStack getHeldItem()
    {
        return inv.getStackIn((int)invIndex);
    }
    
    public void toggleAchievement(final Achievement achievement)
    {
        new Thread()
        {
            public void run()
            {
                if(username.equals(BlockyMain.username) && !achievementGet.contains(achievement))
                {
                    AchievementDataBase base = AchievementDataBase.getInstance();
                    if(base.activateAchievement(username, achievement))
                    {
                        BlockyMain.instance.achievementRenderer.newAchievement(achievement);
                    }
                    achievementGet.add(achievement);
                }
                else
                {
                }
            }
        }.start();
    }

    @Override
    public float getMaxLife()
    {
        return 100f;
    }
}

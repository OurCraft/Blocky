package org.jglrxavpok.blocky.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import org.jglrxavpok.blocky.block.BlockFluid;
import org.jglrxavpok.blocky.block.BlockLava;

public class Fluid
{

    private static boolean loaded;
    private String name;
    private static HashMap<String, Fluid> fluids = new HashMap<String, Fluid>();
    private int color = 0;
    protected int maxVolume = 15;
    protected int minVolume = 1;
    private int spreadSpeed;
    protected ArrayList<BlockFluid> volumes = new ArrayList<BlockFluid>();
    private float density;
    private float opacity;
    private int light;
    
    public static void load()
    {
        if(loaded)
            return;
        Fluid lava = new Fluid("lava")
        {
            public void register()
            {
                this.volumes.add(new BlockLava(this, Integer.MAX_VALUE));
                for(int i = this.minVolume;i<=this.maxVolume;i++)
                {
                    volumes.add(new BlockLava(this, i));
                }
                fluids.put(this.getName(), this);
            }
        };
        lava.setSpreadSpeed(20);
        lava.setDensity(3.0f);
        lava.setColor(Color.red.getRGB());
        lava.setMaxVolume(4);
        lava.setOpacity(0.75f);
        lava.setLightingPower(15);
        lava.register();

        Fluid water = new Fluid("water");
        water.setSpreadSpeed(5);
        water.setDensity(2.0f);
        water.setColor(Color.blue.getRGB());
        water.setMaxVolume(7);
        water.setOpacity(0.60f);
        water.register();
        loaded = true;
    }

    public void setDensity(float i)
    {
        density = i;
    }
    
    public float getDensity()
    {
        return density;
    }

    public Fluid(String fluidName)
    {
        this.name = fluidName;
    }

    public void setMinVolume(int minVolume)
    {
        this.minVolume = minVolume;
    }
    
    public void setMaxVolume(int maxVolume)
    {
        this.maxVolume = maxVolume;
    }
    
    public int getMinVolume()
    {
        return minVolume;
    }
    
    public int getMaxVolume()
    {
        return maxVolume;
    }
    
    public void setColor(int color)
    {
        this.color = color;  
    }
    
    public int getColor()
    {
        return color;
    }
    
    public void register()
    {
        volumes.add(new BlockFluid(this, Integer.MAX_VALUE));
        for(int i = minVolume;i<=maxVolume;i++)
        {
            volumes.add(new BlockFluid(this, i));
        }
        fluids.put(name, this);
        
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setSpreadSpeed(int speed)
    {
        spreadSpeed = speed;
    }

    public int getSpreadSpeed()
    {
        return spreadSpeed;
    }

    public float getOpacity()
    {
        return opacity;
    }
    
    public void setOpacity(float opacity)
    {
        this.opacity = opacity;
    }

    public int getLightingPower()
    {
        return light;
    }
    
    public void setLightingPower(int l)
    {
        light = l;
    }

    public static Fluid get(String string)
    {
        return fluids.get(string);
    }
}

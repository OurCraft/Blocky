package org.jglrxavpok.blocky.client;

import java.util.ArrayList;

import org.jglrxavpok.blocky.utils.HUDComponent;
import org.jglrxavpok.opengl.FontRenderer;

public class ChatHUDComponent implements HUDComponent
{

    private ArrayList<String> list;
    private ArrayList<Long> chatTime;

    public ChatHUDComponent(ArrayList<String> list, ArrayList<Long> chatTime)
    {
        this.list = list;
        this.chatTime = chatTime;
        // Test
    }
    
    @Override
    public void render()
    {
//        if(BlockyMain.instance.getLevel() != null || BlockyMain.instance.clientHandler != null)
        {
            int max = list.size();
            for(int index = max;10 > max-index && index >= 0;index--)
            {
                if(index < 0 || index >= list.size() || index >= chatTime.size())
                    continue;
                if(System.currentTimeMillis()-chatTime.get(index) <= 3500)
                {
                    FontRenderer.drawString(list.get(index), 100, 50f+(max-index)*12f, 0xFFFFFF);
                }
            }
        }
    }

    @Override
    public void update()
    {

    }

}

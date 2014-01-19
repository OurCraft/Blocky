package org.jglrxavpok.blocky.crafting;

import java.util.ArrayList;

import org.jglrxavpok.blocky.inventory.ItemStack;

public class Craft
{

    private ItemStack[] input;
    private ItemStack output;

    public Craft(ItemStack[] input, ItemStack output)
    {
        this.input = input;
        this.output = output;
    }
    
    public String toString()
    {
        String s = "";
        for(int i = 0;i<input.length;i++)
        {
            ItemStack stack = input[i];
            if(i != 0)
                s+=" + ";
            s+=stack.toString()+"";
        }
        s+=" = "+output.toString();
        return s;
    }
    
    public boolean matches(ItemStack[] input)
    {
        if(input.length < this.input.length)
            return false;
        boolean f = false;
        for(int i = 0;i<input.length;i++)
        {
            if(input[i] != null && input[i].item != null)
            {
                f = true;
                break;
            }
        }
        if(!f)
            return false;
        ArrayList<ItemStack> stacksAvailable = new ArrayList<ItemStack>();
        for(ItemStack s : this.input)
        {
            stacksAvailable.add(s);
        }
        for(int i = 0;i<input.length;i++)
        {
            boolean flag = false;
            for(ItemStack stack : stacksAvailable)
            {
                if(input[i] == null || stack == null)
                {
                }
                else if(ItemStack.areItemStacksEquals(stack, input[i]) && stack.nbr == input[i].nbr)
                {
                    flag = true;
                    stacksAvailable.remove(stack);
                    break;
                }
            }
            if(!flag && input[i].item != null && input[i] != null)
            {
                return false;
            }
        }
        
        return stacksAvailable.size() == 0;
    }
    
    public ItemStack getOutput()
    {
        return output;
    }
    
    public ItemStack[] getInput()
    {
        return input;
    }
}

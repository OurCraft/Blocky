package org.jglrxavpok.blocky.crafting;

import java.util.ArrayList;

import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.inventory.ItemStack;
import org.jglrxavpok.blocky.items.Item;

public class CraftingManager
{

    private static CraftingManager instance;
    private final ArrayList<Craft> craftsList = new ArrayList<Craft>();

    public CraftingManager()
    {
        addCraft(new ItemStack[]
                {
                    new ItemStack(Block.planks.getItem(),2),
                    new ItemStack(Block.rock.getItem(),1)
                },
                new ItemStack(Item.steelPick,1));
        addCraft(new ItemStack[]
                {
                    new ItemStack(Block.rock.getItem(),8)
                },
                new ItemStack(Block.furnace.getItem(),1));
        addCraft(new ItemStack[]
                {
                    new ItemStack(Block.log.getItem(),1)
                },
                new ItemStack(Block.planks.getItem(),1));
        addCraft(new ItemStack[]
                {
                    new ItemStack(Block.planks.getItem(),4)
                },
                new ItemStack(Block.craftingTable.getItem(),1));
    }
    
    public void addCraft(ItemStack[] input, ItemStack output)
    {
        craftsList.add(new Craft(input, output));
    }
    
    public ArrayList<Craft> getResults(ItemStack[] input)
    {
        ArrayList<Craft> possibles = new ArrayList<Craft>();
        for(int i = 0;i<craftsList.size();i++)
        {
            if(craftsList.get(i).matches(input))
                possibles.add(craftsList.get(i));
        }
        return possibles;
    }
    
    public static CraftingManager getInstance()
    {
        if(instance == null)
            instance = new CraftingManager();
        return instance;
    }
    
    /**
     * Testing
     */
    public static void main(String[] args)
    {
        CraftingManager cm = CraftingManager.getInstance();
        ItemStack[] input = new ItemStack[]
                {
                new ItemStack(Block.planks.getItem(), 2), new ItemStack(Block.rock.getItem(), 1)
                };
        ArrayList<Craft> crafts = cm.getResults(input);
        System.out.println("Crafts possibles");
        System.out.println("====================");
        for(Craft c : crafts)
        {
            System.out.println(c.toString());
        }
        System.out.println("====================");
    }
}

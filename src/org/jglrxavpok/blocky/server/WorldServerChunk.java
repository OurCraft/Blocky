package org.jglrxavpok.blocky.server;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.block.BlockInfo;
import org.jglrxavpok.blocky.network.NetworkCommons;
import org.jglrxavpok.blocky.network.packets.PacketBlockUpdate;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldChunk;

public class WorldServerChunk extends WorldChunk
{

    public boolean stopUpdate;

    public WorldServerChunk(World lvl, int chunkID)
    {
        super(lvl, chunkID);
    }
    
    public void setBlock(int x, int y, String block)
    {
        super.setBlock(x,y,block);
        if(chunkID < 0)
            x = 15-x;
        int rx = (chunkID < 0 ? x : x)+(16*chunkID);
        if(!stopUpdate)
        {
            NetworkCommons.sendPacketTo(new PacketBlockUpdate(rx,y,BlockInfo.ID, block),  false, BlockyMainServer.instance.getNetwork().getIngameConnectionsFromClients());
        }
    }
    
    public void tick()
    {
//        if(!this.lvl.isRemote)
        if(blocks != null)
        {
            boolean flag = false;
            if(chunkID < 0)
            {
                flag = true;
            }
            for(int x = 0;x<16;x++)
            {
                for(int y = 0;y<256;y++)
                {
                    float posX = 0;
                    if(flag)
                    {
                        posX = (((15-x)*Block.BLOCK_WIDTH+16f*chunkID*Block.BLOCK_WIDTH))+lvl.lvlox;
                    }
                    else
                    {
                        posX = ((x*Block.BLOCK_WIDTH+16f*chunkID*Block.BLOCK_WIDTH))+lvl.lvlox;
                    }
                    if(posX+Block.BLOCK_WIDTH < 0 || posX > BlockyMain.width)
                        continue;
                    float posY = y*Block.BLOCK_HEIGHT+lvl.lvloy;
                    if(posY+Block.BLOCK_HEIGHT < 0 || posY > BlockyMain.height)
                        continue;
                    String id = getBlock(x,y);
                    Block block = Block.getBlock(id);
                    if(block.resistance == Float.POSITIVE_INFINITY)
                        continue;
                    if(block != null && block != Block.outBlock)
                    {
                        int rx = (chunkID < 0 ? 15-x : x)+(16*chunkID);
                        block.update(rx, y, lvl);
                        if(this.attackValue[x][y] > block.resistance*50f)
                        {
                            if(block.onBlockDestroyedByPlayer(lastAttackPlayerName[x][y], rx,y,lvl))
                            {
                                setBlock(x,y,"air");
                                setAttackValueToBlock(x, y, 0, null);
                            }
                        }
                        else
                        {
                            if(lvl.ticks % 60 == 0)
                                this.attackValue[x][y]-=5;
                            if(attackValue[x][y] < 0)
                                attackValue[x][y] = 0;
                        }
                    }
                }
            }
            for(int x = 0;x<16;x++)
            {
                for(int y = 0;y<256;y++)
                {
                    String id = getBlock(x,y);
                    int rx = (chunkID < 0 ? 15-x : x)+(16*chunkID);
                    Block.getBlock(id).onWorldUpdate(rx, y, lvl);
                }
            }
        }
    }
    
    public void setAttackValueToBlock(int x, int y, int v, String p)
    {
        if(v > Block.getBlock(getBlock((chunkID < 0 ? 15-x : x),y)).resistance*50f)
        {
            if(Block.getBlock(getBlock((chunkID < 0 ? 15-x : x),y)).onBlockDestroyedByPlayer(p, 16*chunkID+(chunkID < 0 ? 15-x : x), y, lvl))
            {
                setBlock(x,y,"air");
            }
            super.setAttackValueToBlock(x, y, 0, p);
        }
        else
            super.setAttackValueToBlock(x, y, v, p);
    }
}

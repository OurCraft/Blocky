package org.jglrxavpok.blocky.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.block.BlockInfo;
import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.network.EntityState;
import org.jglrxavpok.blocky.network.NetworkCommons;
import org.jglrxavpok.blocky.network.packets.PacketBlockUpdate;
import org.jglrxavpok.blocky.network.packets.PacketEntityState;
import org.jglrxavpok.blocky.tileentity.TileEntity;
import org.jglrxavpok.blocky.utils.IO;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldChunk;
import org.jglrxavpok.blocky.world.WorldGenerator;
import org.jglrxavpok.blocky.world.WorldInfos;
import org.jglrxavpok.blocky.world.WorldGenerator.WorldType;
import org.jglrxavpok.storage.TaggedStorageChunk;

public class WorldServer extends World
{

    public WorldServer(String name)
    {
        super(name);
        isRemote = false;
    }
    
    public WorldServer(WorldInfos infos)
    {
        super(infos);
        isRemote = false;
    }
    
    public WorldChunk getChunkAt(int x, int y, boolean b)
    {
        int chunkID =(int) Math.floor(((float)x/16f));
        WorldChunk chunk = this.chunksList.get(chunkID);
        if(chunk == null && b)
        {
            chunk = new WorldServerChunk(this, chunkID);
            ((WorldServerChunk)chunk).stopUpdate = true;
            chunksList.put(chunkID, chunk);
            boolean flag = false;
            if(chunkFolder != null)
            {
                File f = new File(chunkFolder, chunkID+".chunk");
                if(f.exists())
                {
                    try
                    {
                        InputStream in = new BufferedInputStream(new FileInputStream(f));
                        byte[] bytes = IO.read(in);
                        in.close();
                        TaggedStorageChunk storageChunk = BlockyMain.saveSystem.readChunk(bytes);
                        chunk.readStorageChunk(storageChunk);
                        if(BlockyMain.isDevMode)
                            BlockyMain.console("Getting chunk infos from files");
                        flag = true;
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    if(BlockyMain.isDevMode)
                        BlockyMain.console("File \""+f.getName()+"\"doesn't exist");
                }
            }
            if(!flag)
                WorldGenerator.instance.generateChunk(this, chunk.chunkID, chunk, worldType);
            ((WorldServerChunk)chunk).stopUpdate = false;
        }
        return chunk;
    }
    
    public void setBlock(int x, int y, String block)
    {
        super.setBlock(x, y, block);
    }
    
    protected void tickEntities()
    {
        for(int i = 0;i<entities.size();i++)
        {
            Entity e = entities.get(i);
            if(e != null)
            {
                if(!(e instanceof EntityPlayer))
                {
                    e.world = this;
                    e.tick();
                }
            }
        }
        if(!this.tileEntities.isEmpty())
        {
            for(int i = 0 ; i < this.tileEntities.size() ; i++)
            {
                TileEntity e = this.tileEntities.get(i);
                if(e != null)
                {
                    e.onUpdate();
                }
            }
        }
    }    
    public void addEntityWithID(int entityID, Entity e)
    {
        super.addEntityWithID(entityID, e);
        sendSpawnInfos(e);
    }
    
    public void sendSpawnInfos(Entity e)
    {
        NetworkCommons.sendPacketTo(new PacketEntityState(e.entityID, EntityState.createFromEntity(e)), false, BlockyMainServer.instance.getNetwork().getIngameConnectionsFromClients());
    }
    
    protected void tickChunks()
    {
        for(int i = 0;i<chunksList.values().size();i++)
        {
            WorldChunk chunk = chunksList.get(i);
            if(chunk != null)
                chunk.tick();
        }
    }

    public void addEntity(Entity e)
    {
        super.addEntity(e);
        sendSpawnInfos(e);
    }

}

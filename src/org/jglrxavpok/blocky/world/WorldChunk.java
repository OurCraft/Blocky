package org.jglrxavpok.blocky.world;

import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.biomes.Biome;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.storage.TaggedStorageChunk;
import org.jglrxavpok.storage.TaggedStorageTag;
import org.lwjgl.input.Mouse;

public class WorldChunk
{

	public String[][] blocks;
	public int[][] lightChanges;
	public float[][] lightValues;
//	private String[][][] data;
	protected World	lvl;
	public int	chunkID;
	protected int[] heightMap = new int[16];
	protected int[][] attackValue;
    protected String[][] lastAttackPlayerName;
    protected int[][] blockChanges;
//    private Random rand = new Random();
    public String biomeID;
 
	public WorldChunk(World lvl, int chunkID)
	{
		this.lvl = lvl;
		this.chunkID = chunkID;
		blocks = new String[16][256];
		lightChanges = new int[16][256];
		blockChanges = new int[16][256];
		lightValues = new float[16][256];
		attackValue = new int[16][256];
		lastAttackPlayerName = new String[16][256];
//		data = new String[16][256][15];
	}
	
	public void tick()
	{
	    if(!this.lvl.isRemote)
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
                                this.setAttackValueToBlock(x, y, 0, null);
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
	
	public void renderAttackValues(float lvlox, float lvloy)
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
                    posX = (((15-x)*Block.BLOCK_WIDTH+16f*(float)chunkID*Block.BLOCK_WIDTH))+lvlox;
                }
                else
                {
                    posX = ((x*Block.BLOCK_WIDTH+16f*(float)chunkID*Block.BLOCK_WIDTH))+lvlox;
                }
                if(posX+Block.BLOCK_WIDTH < 0 || posX > (float)BlockyMain.width)
                    continue;
                float posY = y*Block.BLOCK_HEIGHT+lvloy;
                if(posY+Block.BLOCK_HEIGHT < 0 || posY > (float)BlockyMain.height)
                    continue;
                int rx1 = (chunkID < 0 ? 15-x : x);
                if(Block.getBlock(blocks[rx1][y]) == Block.air)
                    continue;
                if(Block.getBlock(blocks[rx1][y]).resistance*50f == 0.0f)
                    continue;
                float value = (((float)this.attackValue[x][y]/(Block.getBlock(blocks[rx1][y]).resistance*50f)));
                if(value <= 0f)
                {
                    continue;
                }
                value -= 6f/6f/6f; 
                int texW = (int) Block.TERRAIN_IMG_WIDTH;
                int texH = (int) Block.TERRAIN_IMG_HEIGHT;
                float yPos = 4*6;
                yPos = (int)texH - (yPos+6);
                if((float)((int)(value*6f*6f)/6*6+6)/(float)texW >= 1f)
                    continue;
                float maxU = (float)((int)(value*6f*6f)/6*6)/(float)texW;
                float minV = (float)(yPos)/(float)texH;
                float minU = (float)((int)(value*6f*6f)/6*6+6)/(float)texW;
                float maxV = (float)(yPos+6)/(float)texH;
                Tessellator t = Tessellator.instance;
//                float val = lvl.getLightValue(x, y);
//                if(val < 0.01f)
//                    val = 0.01f;
                t.setColorRGBA_F(1,1,1,1f);
                t.addVertexWithUV(posX, posY, 0, minU, minV);
                t.addVertexWithUV(posX+Block.BLOCK_WIDTH, posY, 0, maxU, minV);
                t.addVertexWithUV(posX+Block.BLOCK_WIDTH, posY+Block.BLOCK_HEIGHT, 0, maxU, maxV);
                t.addVertexWithUV(posX, posY+Block.BLOCK_HEIGHT, 0, minU, maxV);
            }
        }
	}
	
	public void render(float lvlox, float lvloy)
	{
		if(blocks != null)
		{
			int mx = Mouse.getX();
			int my = Mouse.getY();
			int tx = (int)((mx-lvlox)/Block.BLOCK_WIDTH);
			int ty = (int)((my-lvloy)/Block.BLOCK_HEIGHT);
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
				        posX = (((float)(15-x)*Block.BLOCK_WIDTH+16f*(float)chunkID*Block.BLOCK_WIDTH))+lvlox;
				    }
				    else
				    {
				        posX = ((x*Block.BLOCK_WIDTH+16f*(float)chunkID*Block.BLOCK_WIDTH))+lvlox;
				    }
				    if(posX+Block.BLOCK_WIDTH < 0 || posX > BlockyMain.width)
				        continue;
				    float posY = y*Block.BLOCK_HEIGHT+lvloy;
				    if(posY+Block.BLOCK_HEIGHT < 0 || posY > BlockyMain.height)
                        continue;
					String id = getBlock(x,y);
					if(flag)
						id = getBlock(x,y);
					Block block = Block.getBlock(id);
					if(block != null && block != Block.outBlock)
					{
						boolean selected = tx == x && ty == y;
						int rx = (chunkID < 0 ? 15-x : x)+(16*chunkID);
						block.render(posX, posY, rx, y, lvl, selected);
					}
				}
			}
		}
	}

	public void setBlock(int x, int y, String block)
	{
		if(block == null || x < 0 || x >= blocks.length || y < 0 || y >= blocks[0].length)
			return;
		if(chunkID < 0)
			x = 15-x;
		blocks[x][y] = block;
		this.lastAttackPlayerName[x][y] = null;
		if(y >= heightMap[x] && Block.getBlock(block).letLightGoThrough())
		{
		    int hy = y;
            int rx = (chunkID < 0 ? x : x)+(16*chunkID);
		    while(hy > 0)
		    {
		        hy--;
		        if(!Block.getBlock(lvl.getBlockAt(rx, hy)).letLightGoThrough())
		        {
		            heightMap[x] = hy;
		            break;
		        }
		    }
		    heightMap[x] = hy;
		}
		if(y >= heightMap[x] && !Block.getBlock(block).letLightGoThrough())
		    heightMap[x] = y;
		
		this.blockChanges[x][y] = (int)lvl.ticks;
	}
	
	public String getBlock(int x, int y)
	{
		if(chunkID < 0)
		{
			x = 15-x;
		}
		if(x < 0 || x >= blocks.length || y < 0 || y >= blocks[0].length)
			return "out";
		String b = blocks[x][y];
		if(b == null)
		{
			blocks[x][y] = "air";
			return "air";
		}
		return b;
	}

    public TaggedStorageChunk createStorageChunk()
    {
        TaggedStorageChunk chunk = new TaggedStorageChunk("Chunk Storage ID: "+chunkID);
        for(int x = 0;x<16;x++)
        {
            String column = "";
            for(int y = 0;y<256;y++)
            {
                if(y != 0)
                    column+=";";
                int rx = (chunkID < 0 ? 15-x : x);
                column+=getBlock(x,y)+":"+this.getAttackValue(x, y)+":"+this.lastAttackPlayerName[rx][y];
            }
            chunk.setString("column "+x, column);
        }
        chunk.setString("biome", biomeID);
        chunk.setInteger("id", chunkID);
        return chunk;
    }

    public void readStorageChunk(TaggedStorageChunk chunk)
    {
        lvl.handlingChanges = true;
        TaggedStorageTag tags[] = chunk.getTags();
        for(TaggedStorageTag tag : tags)
        {
            if(tag != null )
            if(tag.getTagName().startsWith("column "))
            {
                int x = Integer.parseInt(tag.getTagName().replace("column ", ""));
                String[] yValues = tag.getValue().split(";");
                for(int y = 0;y<yValues.length;y++)
                {
                    if(yValues[y].contains(":"))
                    {
                        String[] p = yValues[y].split(":");
                        setBlock(x,y,p[0]);
                        this.setAttackValueToBlock(x, y, Integer.parseInt(p[1]), p[2]);
                    }
                    else
                        setBlock(x,y,yValues[y]);
                }
            }
        }
        if(chunk.hasTag("id"))
            this.chunkID = chunk.getInteger("id");
        biomeID = chunk.getString("biome");
        lvl.handlingChanges = false;
    }

    public void setLightValue(int x, int y, float v)
    {
        if(v<0f || x < 0 || x >= blocks.length || y < 0 || y >= blocks[0].length)
            return;
        if(chunkID < 0)
            x = 15-x;
        lightValues[x][y] = v;
        this.lightChanges[x][y] = (int)lvl.ticks;
    }
    
    public float getLightValue(int x, int y)
    {
        if(chunkID < 0)
        {
            x = 15-x;
        }
        if(x < 0 || x >= blocks.length || y < 0 || y >= blocks[0].length)
            return 0;
        return lightValues[x][y];
    }
    
    public boolean canBlockSeeTheSky(int x, int y)
    {
        if(chunkID < 0)
        {
            x = 15-x;
        }
        if(x < 0 || x >= blocks.length || y < 0 || y >= blocks[0].length)
            return false;
        return y >= heightMap[x];
    }

    public int getLastBlockChange(int x, int y)
    {
        if(chunkID < 0)
        {
            x = 15-x;
        }
        if(x < 0 || x >= blocks.length || y < 0 || y >= blocks[0].length)
            return 0;
        return blockChanges[x][y];
    }
    
    public int getLastLightChange(int x, int y)
    {
        if(chunkID < 0)
        {
            x = 15-x;
        }
        if(x < 0 || x >= blocks.length || y < 0 || y >= blocks[0].length)
            return 0;
        return lightChanges[x][y];
    }
    
    public int getAttackValue(int x, int y)
    {
//        if(chunkID < 0)
//        {
//            x = 15-x;
//        }
        if(x < 0 || x >= blocks.length || y < 0 || y >= blocks[0].length)
            return 0;
        return attackValue[x][y];
    }
    
    public void setAttackValueToBlock(int x, int y, int attackValue, String player)
    {
//        if(chunkID < 0)
//            x = 15-x;
        if(attackValue < 0 || x < 0 || x >= blocks.length || y < 0 || y >= blocks[0].length)
            return;
        
        this.attackValue[x][y] = attackValue;
        lastAttackPlayerName[x][y] = player;
    }

    public int getMaxHeight(int x)
    {
        return this.heightMap[x];
    }
}

package org.jglrxavpok.blocky.world;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.jglrxavpok.blocky.BlockyMain;
import org.jglrxavpok.blocky.biomes.Biome;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.block.BlockInfo;
import org.jglrxavpok.blocky.client.ClientNetworkListener;
import org.jglrxavpok.blocky.entity.Entity;
import org.jglrxavpok.blocky.entity.EntityPlayer;
import org.jglrxavpok.blocky.network.NetworkCommons;
import org.jglrxavpok.blocky.network.packets.Packet;
import org.jglrxavpok.blocky.network.packets.PacketRequestChunk;
import org.jglrxavpok.blocky.server.OldPacket;
import org.jglrxavpok.blocky.server.PacketBlockInfos;
import org.jglrxavpok.blocky.server.PacketEntityUpdate;
import org.jglrxavpok.blocky.server.PacketTime;
import org.jglrxavpok.blocky.tileentity.TileEntity;
import org.jglrxavpok.blocky.utils.AABB;
import org.jglrxavpok.blocky.utils.IO;
import org.jglrxavpok.blocky.utils.MathHelper;
import org.jglrxavpok.blocky.world.WorldGenerator.WorldType;
import org.jglrxavpok.opengl.FontRenderer;
import org.jglrxavpok.opengl.Tessellator;
import org.jglrxavpok.opengl.Textures;
import org.jglrxavpok.storage.TaggedStorageChunk;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class World
{

	private String	lvlName;
	public double ticks = 0;
	public double tickSpeed = 1f;
	public float lvlox = 0;
	public float lvloy = 0;
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public Vector2f gravity = new Vector2f(0, -(9.81f/60f));
	public Entity	centerOfTheWorld;
	public boolean showOutside;
	public float	animTime;
	private ArrayList<LevelListener>	listeners = new ArrayList<LevelListener>();
	public ArrayList<EntityPlayer>	players = new ArrayList<EntityPlayer>();
	protected HashMap<Integer, WorldChunk>	chunksList;
	public WorldType	worldType;
    protected File chunkFolder;
    protected ArrayList<Integer> chunkAsked = new ArrayList<Integer>();
    public boolean handlingChanges;
    public long time;
    private ParticleSystem particles = new ParticleSystem(2000);
    private ArrayList<EntityPlayer> loadedPlayers = new ArrayList<EntityPlayer>();
    public int entityID;
    public Vector2f spawnPoint = new Vector2f(0f, 0f);
    public List<TileEntity> tileEntities = new ArrayList<TileEntity>();
    public Biome lastBiome = null;
    public boolean isRaining = false;
    public boolean isRemote = false;
	
	public static final World zeroBlocks = new World("zeroBlocks")
	{
		public String getBlockAt(int x, int y)
		{
			return "air";
		}
		
		public float getLightValue(int x,int y)
		{
		    return 1f;
		}
		
		public void setLightValue(float val, int x,int y)
		{
		    
		}
		
		public void tick()
		{
			
		}
		
		public void render()
		{
			
		}
		
		public void setBlock(int x, int y, String block)
		{
		}
		
		public void setData(int x, int y, int index, String data)
		{
		}
		
		public String getData(int x, int y, int index)
		{
			return "";
		}
	};
	
	public World(WorldInfos infos)
    {
	    this.lvlName = infos.worldName;
	    this.worldType = WorldType.values()[infos.worldType];
	    this.time = infos.worldTime;
	    if(infos.worldFolder != null)
	        this.setChunkFolder(infos.worldFolder);
	    if(infos.spawnPoint != null)
	        this.spawnPoint = infos.spawnPoint;
	    chunksList = new HashMap<Integer, WorldChunk>();
    }
	
	public World(String name)
	{
		this.lvlName = name;
		chunksList = new HashMap<Integer, WorldChunk>();
		this.worldType = WorldType.NORMAL;
	}
	
	public Biome getBiomeAt(int x, int y)
	{
	    WorldChunk chunk = this.getChunkAt(x, y, false);
	    if(chunk != null)
	    {
	        return Biome.getBiomeByID(chunk.biomeID);
	    }
	    return Biome.getBiomeByID(null);
	}
	
    public void setChunkFolder(File folder)
	{
	    chunkFolder = folder;
	    File entitiesFile = new File(chunkFolder, "entities.data");
	    if(entitiesFile.exists())
	    {
	        try
	        {
	            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(entitiesFile)));
	            int size = in.readInt();
	            for(int i = 0;i<size;i++)
	            {
	                try
	                {
    	                String entityClass = in.readUTF();
    	                InputStream in1 = new ByteArrayInputStream(Base64.decodeBase64(in.readUTF()));
                        byte[] bytes = IO.read(in1);
                        in1.close();
                        TaggedStorageChunk chunk = BlockyMain.saveSystem.readChunk(bytes);
                        Entity e = (Entity) Class.forName(entityClass).newInstance();
                        e.readFromChunk(chunk);
                        addEntity(e);
	                }
	                catch(Exception e)
	                {
	                    if(e instanceof EOFException)
	                    {
	                        
	                    }
	                    else
	                        e.printStackTrace();
	                }
	            }
	            in.close();

	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	    }
	    File playerFolder = new File(chunkFolder, "players");
	    if(playerFolder.exists())
	    {
	        File files[] = playerFolder.listFiles();
	        if(files != null)
	        {
	            for(int i =0;i<files.length;i++)
	            {
	                File f = files[i];
	                if(f.getName().endsWith(".data"))
	                {
	                    try
	                    {
    	                    EntityPlayer playerEntity = new EntityPlayer();
    	                    InputStream in = new BufferedInputStream(new FileInputStream(f));
                            byte[] bytes = IO.read(in);
                            in.close();
                            TaggedStorageChunk chunk = BlockyMain.saveSystem.readChunk(bytes);
                            playerEntity.readFromChunk(chunk);
    	                    loadedPlayers.add(playerEntity);
	                    }
	                    catch(Exception e)
	                    {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        }
	    }
	    
	    File tileFile = new File(chunkFolder, "tileEntities.data");
	    if(tileFile.exists())
	    {
	        try
	        {
	            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(tileFile)));
	            int size = in.readInt();
	            for(int i = 0;i<size;i++)
	            {
	                try
	                {
    	                String tileEntityClass = in.readUTF();
    	                InputStream in1 = new ByteArrayInputStream(Base64.decodeBase64(in.readUTF()));
                        byte[] bytes = IO.read(in1);
                        in1.close();
                        TaggedStorageChunk chunk = BlockyMain.saveSystem.readChunk(bytes);
                        Class<?> c = Class.forName(tileEntityClass);
                        Constructor<?> construct = c.getDeclaredConstructor(World.class);
                        TileEntity e = null;
                        e = (TileEntity) construct.newInstance(this);
                        e.load(chunk);
                        this.tileEntities.add(e);
	                }
	                catch(Exception e)
	                {
	                    if(e instanceof EOFException)
	                    {
	                        
	                    }
	                    else
	                        e.printStackTrace();
	                }
	            }
	            in.close();

	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	    }
	}
	
	public String getBlockAt(int x, int y)
	{
		WorldChunk chunk = getChunkAt(x,y,false);
		if(chunk == null)
			return "out";
        if(x != 0)
        {
            x = x-chunk.chunkID*16;
            if(chunk.chunkID < 0)
            {
                x = 15-x;
            }
        }
		return chunk.getBlock(x,y);
	}
	
	public void setLightValue(float value, int x, int y)
	{
	    WorldChunk chunk = getChunkAt(x,y, true);
	    if(chunk == null)
	        return;
        if(x != 0)
        {
            x = x-chunk.chunkID*16;
            if(chunk.chunkID < 0)
            {
                x = 15-x;
            }
        }
        if(value != chunk.getLightValue(x,y))
        for(int i = 0;i<listeners.size();i++)
        {
            listeners.get(i).onLightChanged(chunk.getLightValue(x,y), x, y, value);
        }
        chunk.setLightValue(x, y, value);
	}
	
	public float getLightValue(int x,int y)
	{
	    WorldChunk chunk = getChunkAt(x,y,false);
        if(chunk == null)
            return 0;
        if(x != 0)
        {
            x = x-chunk.chunkID*16;
            if(chunk.chunkID < 0)
            {
                x = 15-x;
            }
        }
        return chunk.getLightValue(x,y) - (this.isRaining ? 0.25f : 0f);
	}
	
	/**
	 * 
	 * @param x : Block x
	 * @param y : Block y
	 * @param b : Generate chunk if doesn't exists
	 * @return
	 */
	public WorldChunk getChunkAt(int x, int y, boolean b)
	{
		int chunkID =(int) Math.floor(((float)x/16f));
		WorldChunk chunk = this.chunksList.get(chunkID);
		if(chunk == null && b)
		{
		    if(this.worldType == WorldType.CLIENT)
		    {
		        if(!chunkAsked.contains(chunkID))
		        {
		            askForChunk(chunkID);
		            chunkAsked.add(chunkID);
		        }
		    }
		    else
		    {
    		    chunk = new WorldChunk(this, chunkID);
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
		    }
		}
		return chunk;
	}

	public void askForChunk(int chunkID)
    {
//	    if(NettyClientHandler.current != null)
//            try
//            {
//                NettyCommons.sendPacket(new OldPacket("Request ChunkContent "+chunkID, null), NettyClientHandler.current.serverChannel);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
	    if(BlockyMain.instance.getClientNetwork() != null)
	    {
	        ClientNetworkListener list = BlockyMain.instance.getClientNetwork();
	        Packet packet = new PacketRequestChunk(chunkID);
	        
	        NetworkCommons.sendPacketTo(packet, false, list.getClientConnection());
	    }
    }

    public String getName()
	{
		return lvlName;
	}
	
	public void tick()
	{
	    if(time > 18000*4f)
        {
            time = 0;
        }
	    if(worldType != WorldType.CLIENT)
        time+=1;
		
	    time = 14000;
	    
		ticks+=tickSpeed;
		this.particles.tickAll(this);
        
		tickChunks();
        
        tickEntities();
        
        if(this.isRaining)
        {
        	Collection<WorldChunk> chunkList = this.chunksList.values();
        	
        	for(WorldChunk chunk : chunkList)
        	{
        		if(!chunk.biomeID.equals("snow"))
        		{
        			Random rand = new Random();
        			if(rand.nextBoolean())
        			{
        				Particle p = new Particle();
        				p.setLife(200);
        				
        				p.setColor(Color.blue.getRGB());
        				p.setVelocity(rand.nextFloat() * 5 - 2.5f, 4f);
        				p.setPos(chunk.chunkID * 16 * Block.BLOCK_WIDTH + Block.BLOCK_WIDTH / 2 + rand.nextInt(16) * Block.BLOCK_WIDTH, 150 * Block.BLOCK_HEIGHT);
        				
        				this.spawnParticle(p);
        			}
        		}
        		else
        		{
        			Random rand = new Random();
        			if(rand.nextBoolean())
        			{
        				Particle p = new Particle().setGravity(3.5f);
        				p.setLife(500);
        				
        				p.setColor(Color.white.getRGB());
        				p.setVelocity(rand.nextFloat() * 5 - 2.5f, 16f);
        				p.setPos(chunk.chunkID * 16 * Block.BLOCK_WIDTH + Block.BLOCK_WIDTH / 2 + rand.nextInt(16) * Block.BLOCK_WIDTH, 150 * Block.BLOCK_HEIGHT);
        				
        				this.spawnParticle(p);
        			}
        		}
        	}
        }
	}
	
	protected void tickChunks()
    {
	    int startID = (int)((-(lvlox))/Block.BLOCK_WIDTH/16f)-1;
        int endID = (int)(((-(lvlox))+BlockyMain.width)/Block.BLOCK_WIDTH/16f)+1;
        int index = startID;
        
        for(;index<endID;index++)
        {
            WorldChunk chunk = getChunkByID(index, true);
            if(chunk != null)
                chunk.tick();
        }
    }

    protected void tickEntities()
    {
        for(int i = 0;i<entities.size();i++)
        {
            Entity e = entities.get(i);
            if(e != null)
            {
                if(e.world != this)
                {
                    entities.remove(e);
                }
                else
                e.tick();
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

    public void render()
	{
	    
	    float voidFactor = 1;
	    if(this.centerOfTheWorld != null)
	    {
	        if(centerOfTheWorld.y < 100f*Block.BLOCK_HEIGHT)
	        {
	            voidFactor = ((this.centerOfTheWorld.y)/(100f*Block.BLOCK_HEIGHT));
	            if(voidFactor < 0f)
	                voidFactor = 0f;
	        }
	    }
		if(!this.isRaining)
		{
	        float b = (float)time/(18000f);
	        if(b > 1.0f)
	            b = 1f-(b-1f);
	        if(b >0.75f)
	            b = 0.75f;
            float r = (float)time/(18000f);
            if(r > 1.0f)
                r = 1f-(r-1f);
            if(r > 0.75f)
                r = 0.15f;
            float g = (float)time/(18000f);
            if(g > 1.0f)
                g = 1f-(g-1f);
            if(g >0.75f)
                g = 0.15f;
            r*=0.15f;
            g*=0.15f;
            
            r*=voidFactor;
            g*=voidFactor;
            b*=voidFactor;
		    BlockyMain.instance.setBackgroundColor(r, g, b);
		    float sunAngle = (float)time/(18000f)*90f;
	        float size = 128*2;
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        GL11.glPushMatrix();
	        GL11.glColor3f(1f, 0.95f, 0f);
	        GL11.glTranslatef(BlockyMain.width/2-size/2, lvloy+Block.BLOCK_HEIGHT*90f, 0);
	        GL11.glRotatef(sunAngle,0,0,1);
	        GL11.glTranslatef(600, 0, 0);
	        GL11.glRotatef(-sunAngle,0,0,1);
	        Textures.render(Textures.getFromClasspath("/assets/textures/world/sun.png"), 0, 0, size, size);

	        GL11.glColor3f(1f, 1f, 1f);
	        GL11.glRotatef(sunAngle,0,0,1);
	        GL11.glTranslatef(-1200, 0, 0);
	        GL11.glRotatef(-sunAngle,0,0,1);

	        Textures.render(Textures.getFromClasspath("/assets/textures/world/sun.png"), 0, 0, size, size);

	        GL11.glColor3f(1,1,1);
	        
	        GL11.glPopMatrix();
	        GL11.glBlendFunc(GL11.GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		}
		else
		{
			BlockyMain.instance.setBackgroundColor(0.35f, 0.35f, 0.35f);
		}
        
		if(centerOfTheWorld != null)
		{
			lvlox = -((centerOfTheWorld.x+(float)centerOfTheWorld.w/2f)-(float)BlockyMain.width/2f);
			lvloy = -((centerOfTheWorld.y+(float)centerOfTheWorld.h/2f)-(float)BlockyMain.height/2f);
		}
		for(int i = 0;i<entities.size();i++)
		{
			Entity e = entities.get(i);
			if(e != null && e.visible
			&& e.x+e.w+this.lvlox >= 0 && e.x+this.lvlox < BlockyMain.width
			&& e.y+e.h+this.lvloy >= 0 && e.y+this.lvloy < BlockyMain.height)
				e.render(lvlox+e.x+e.offsetX, lvloy+e.y+e.offsetY, 1);
		}
		int startID = (int)((-(lvlox))/Block.BLOCK_WIDTH/16f)-1;
		int endID = (int)(((-(lvlox))+BlockyMain.width)/Block.BLOCK_WIDTH/16f)+1;
		int index = startID;
		Textures.bind("/assets/textures/terrain.png");
		Tessellator.instance.startDrawingQuads();
		for(;index<endID;index++)
		{
			WorldChunk chunk = getChunkByID(index, true);
			if(chunk != null)
			{
			    chunk.render(Math.round(lvlox), Math.round(lvloy));
			    chunk.renderAttackValues(Math.round(lvlox), Math.round(lvloy));
			}
		}
		Tessellator.instance.flush();
		
		this.particles.renderAll(this);
		for(int i = 0;i<entities.size();i++)
        {
            Entity e = entities.get(i);
            if(e != null && e.visible
            && e.x+e.w+this.lvlox+e.w >= 0 && e.x+e.w+e.w+this.lvlox-e.w < BlockyMain.width
            && e.y+e.h+this.lvloy+e.h >= 0 && e.y+e.h+e.h+this.lvloy-e.h < BlockyMain.height)
                e.postWorldRender(lvlox+e.x+e.offsetX, lvloy+e.y+e.offsetY);
        }
		
        int mx = BlockyMain.instance.getCursorX();
        int my = BlockyMain.instance.getCursorY();
		int tx = (int)(Math.floor((float)(mx-lvlox)/Block.BLOCK_WIDTH));
		int ty = (int)((float)(my-lvloy)/Block.BLOCK_HEIGHT);
//
//		float posX = tx*Block.BLOCK_WIDTH+lvlox;
//		float posY = ty*Block.BLOCK_HEIGHT+lvloy;
//		Block.drawSelectBox(posX,posY,tx,ty,this);
		if(BlockyMain.isDevMode)
		{
    		FontRenderer.drawString(tx+";"+ty+" : "+getBlockAt(tx,ty)+" ; biome: "+getBiomeAt(tx,ty).biomeId, 0, 0, 0xFFFFFF);
    		FontRenderer.drawString(""+time, 0, 20, 0xFFFFFF);
		}
	}
	
	public WorldChunk getChunkByID(int index, boolean generate)
	{
		return this.getChunkAt(index*16, 0, generate);
	}

	public void addEntityWithID(int entityID, Entity e)
	{
	    if(entities.contains(e))
            entities.remove(e);
        if(e instanceof EntityPlayer && !players.contains(e))
        {
            for(int i = 0;i<loadedPlayers.size();i++)
            {
                if(((EntityPlayer) e).username.equals(loadedPlayers.get(i).username))
                {
                    e.readFromChunk(loadedPlayers.get(i).writeTaggedStorageChunk(0));
                    loadedPlayers.remove(i);
                    break;
                }
            }
            players.add((EntityPlayer) e);
        }
        entities.add(e);
        for(Entity e1 : entities)
        {
            if(e1.entityID == entityID && !isRemote)
            {
                int old = entityID;
                entityID = getCorrectID();
                System.out.println("ID mismatch, "+old+" replaced by "+entityID);
                break;
            }
        }
        e.entityID = entityID;
        e.world = this;
	}
	
	public void addEntity(Entity e)
	{
		if(entities.contains(e))
			entities.remove(e);
		if(e instanceof EntityPlayer && !players.contains(e))
		{
			for(int i = 0;i<loadedPlayers.size();i++)
			{
			    if(((EntityPlayer) e).username.equals(loadedPlayers.get(i).username))
			    {
			        e.readFromChunk(loadedPlayers.get(i).writeTaggedStorageChunk(0));
			        loadedPlayers.remove(i);
			        break;
			    }
			}
			players.add((EntityPlayer) e);
		}
		e.entityID = getCorrectID();
		entities.add(e);
		e.world = this;
	}
	
	public int getCorrectID()
    {
	    boolean flag = true;
	    while(flag)
	    {
	        flag = false;
	        for(Entity e : entities)
	        {
	            if(e.entityID == entityID)
	            {
	                flag = true;
	                break;
	            }
	        }
	        entityID++;
	    }
	    return entityID;
    }

    public Entity getEntityByID(int id)
	{
	    for(Entity e : entities)
	    {
	        if(e.entityID == id)
	            return e;
	    }
	    return null;
	}
	
	public ArrayList<Entity> getEntitiesInAABB(AABB aabb, Entity exclude)
	{
	    ArrayList<Entity> result = new ArrayList<Entity>();
	    for(int i = 0;i<entities.size();i++)
	    {
	        Entity e = entities.get(i);
	        if(e.clipAABB().collide(aabb))
	        {
	            if(e != exclude)
	                result.add(e);
	        }
	    }
	    return result;
	}
	
	public void removeEntity(Entity e)
	{
		entities.remove(e);
	}
	
	public void setBlock(int x, int y, String block)
	{
		if(block == null)
			return;
		
		WorldChunk chunk = getChunkAt(x,y, true);
		if(chunk == null)
		    return;
		if(this.worldType == WorldType.CLIENT && !handlingChanges)
        {
		    PacketBlockInfos p =new PacketBlockInfos(new BlockInfo(x,y,BlockInfo.ID, block));
//            if(NettyClientHandler.current != null)
//                try
//                {
//                    NettyCommons.sendPacket(p, NettyClientHandler.current.serverChannel);
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
        }
		if(x != 0)
		{
			x = x-chunk.chunkID*16;
			if(chunk.chunkID < 0)
			{
				x = 15-x;
			}
		}
		if(!block.equals(chunk.getBlock(x,y)))
		{
			for(int i = 0;i<listeners.size();i++)
			{
				listeners.get(i).onBlockChanged(chunk.getBlock(x,y), x, y, block);
			}
		}
		chunk.setBlock(x, y, block);
	}
	
    public void setData(int x, int y, int index, String data)
	{
        // TODO
	}
	
	public String getData(int x, int y, int index)
	{
	    // TODO
		return "out";
	}
	
	public void addLevelListener(LevelListener l)
	{
		listeners.add(l);
	}

	public EntityPlayer getNearestPlayer(float x, float y)
	{
		return getNearestPlayer(x,y,Float.POSITIVE_INFINITY);
	}
	
	public EntityPlayer getNearestPlayer(float x, float y, float dist)
	{
		EntityPlayer result = null;
		float minDist = dist;
		for(int i = 0;i<players.size();i++)
		{
			if(players.get(i) != null)
			{
				EntityPlayer p = players.get(i);
				if(MathHelper.dist(x, y, 0, p.x, p.y, 0) <= minDist)
				{
					result = p;
				}
			}
		}
		return result;
	}
	
	public void save(File folder) throws IOException
	{
	    if(!folder.exists())
	        folder.mkdirs();
	    Iterator<Integer> it = chunksList.keySet().iterator();
	    while(it.hasNext())
	    {
	        int id = it.next();
	        File chunkFile = new File(folder, ""+id+".chunk");
	        chunkFile.createNewFile();
	        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(chunkFile));
	        out.write(BlockyMain.saveSystem.writeChunk(chunksList.get(id).createStorageChunk()));
	        out.close();
	    }
	    File levelFile = new File(folder, "level.data");
	    levelFile.createNewFile();
	    DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(levelFile)));
	    output.writeUTF(lvlName);
	    output.writeInt(worldType.ordinal());
	    output.writeLong(time);
	    output.writeLong(System.currentTimeMillis());
	    output.writeFloat(spawnPoint.x);
	    output.writeFloat(spawnPoint.y);
	    output.close();
	    
        int imgW = (int) (BlockyMain.width/Block.BLOCK_WIDTH);
        int imgH = (int) (BlockyMain.height/Block.BLOCK_HEIGHT);
        BufferedImage img = new BufferedImage(imgW,imgH,BufferedImage.TYPE_INT_RGB);
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        for(int x = 0;x<imgW;x++)
        {
            for(int y = 0;y<imgH;y++)
            {
                String id = this.getBlockAt(x-(int)(lvlox/Block.BLOCK_WIDTH), y-(int)(lvloy/Block.BLOCK_HEIGHT));
                Block block = Block.getBlock(id);
                int color = block.getAverageColor();
                if(block == Block.air)
                {
                    float b = (float)time/(18000f);
                    if(b > 1.0f)
                        b = 1f-(b-1f);
                    if(b >0.75f)
                        b = 0.75f;
                    if(b < 0.f)
                        b = 0f;
                    if(b > 1.f)
                        b = 1.f;
                    color = new Color(0,0,b).getRGB();
                }
                pixels[(x+(imgH-y-1)*imgW)] = color;
            }   
        }
        ImageIO.write(img, "png",new File(folder,"save.png"));
	    if(entities.size() > 0)
	    {
    	    File entitiesFile = new File(folder, "entities.data");
            entitiesFile.createNewFile();
            output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(entitiesFile)));
            output.writeInt(entities.size());
            for(int i = 0;i<entities.size();i++)
            {
                if(entities.get(i) instanceof EntityPlayer)
                {
                    EntityPlayer p = (EntityPlayer)entities.get(i);
                    File playerFile = new File(folder, "players/"+p.username+".data");
                    if(!playerFile.getParentFile().exists())
                    {
                        playerFile.getParentFile().mkdirs();
                    }
                    playerFile.createNewFile();
                    OutputStream output1 = new BufferedOutputStream(new FileOutputStream(playerFile));
                    output1.write(BlockyMain.saveSystem.writeChunk(p.writeTaggedStorageChunk(i)));
                    output1.flush();
                    output1.close();
                }
                else
                {
                    Entity e = entities.get(i);
                    output.writeUTF(e.getClass().getCanonicalName());
                    output.writeUTF(new String(Base64.encodeBase64(BlockyMain.saveSystem.writeChunk(e.writeTaggedStorageChunk(i)))));
                }
            }
            output.flush();
            output.close();
	    }
	    
	    if(!tileEntities.isEmpty())
	    {
	    	File tileEntitiesFile = new File(folder, "tileEntities.data");
            tileEntitiesFile.createNewFile();
            output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(tileEntitiesFile)));
            output.writeInt(tileEntities.size());
            
            for(int i = 0 ; i < tileEntities.size() ; i++)
            {
            	TileEntity e = this.tileEntities.get(i);
            	
            	output.writeUTF(e.getClass().getCanonicalName());
            	output.writeUTF(new String(Base64.encodeBase64(BlockyMain.saveSystem.writeChunk(e.save(i)))));
            	
//            	BlockyMain.console("Coucou je viens de save une TileEntity " + e.getClass().getCanonicalName() + " - " + e.id + "(" + e.posX + ";" + e.posY + ")");
            }
            
            output.flush();
            output.close();
	    }
	    	
	}

    public boolean canBlockSeeTheSky(int x, int y)
    {
        WorldChunk chunk = getChunkAt(x,y, false);
        if(chunk == null)
            return true;
        if(x != 0)
        {
            x = x-chunk.chunkID*16;
            if(chunk.chunkID < 0)
            {
                x = 15-x;
            }
        }
        return chunk.canBlockSeeTheSky(x, y);
    }

    public int getAttackValue(int x, int y)
    {
        WorldChunk chunk = getChunkAt(x,y, false);
        if(chunk == null)
            return 0;
        if(x != 0)
        {
            x = x-chunk.chunkID*16;
            if(chunk.chunkID < 0)
            {
                x = 15-x;
            }
        }
        return chunk.getAttackValue(x, y);
    }
    
    public void setAttackValue(int x, int y, int value, String player)
    {
        WorldChunk chunk = getChunkAt(x,y, false);
        if(chunk == null)
            return;
       
        if(this.worldType == WorldType.CLIENT && !handlingChanges)
        {
//            if(NettyClientHandler.current != null)
//                try
//                {
//                    NettyCommons.sendPacket(new PacketBlockInfos(new BlockInfo(x,y,BlockInfo.ATTACK_VALUE, value+"\001"+player)), NettyClientHandler.current.serverChannel);
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
        }
        if(x != 0)
        {
            x = x-chunk.chunkID*16;
            if(chunk.chunkID < 0)
            {
                x = 15-x;
            }
        }
        chunk.setAttackValueToBlock(x, y, value, player);
    }
    
    public int getLastBlockChange(int x, int y)
    {
        WorldChunk chunk = getChunkAt(x,y, false);
        if(chunk == null)
            return (int)ticks;
        if(x != 0)
        {
            x = x-chunk.chunkID*16;
            if(chunk.chunkID < 0)
            {
                x = 15-x;
            }
        }
        return chunk.getLastBlockChange(x,y);
    }
    
    public int getLastLightChange(int x, int y)
    {
        WorldChunk chunk = getChunkAt(x,y, false);
        if(chunk == null)
            return (int)ticks;
        if(x != 0)
        {
            x = x-chunk.chunkID*16;
            if(chunk.chunkID < 0)
            {
                x = 15-x;
            }
        }
        return chunk.getLastLightChange(x,y);
    }

    public void putChunk(WorldChunk chunk)
    {
        this.chunksList.put(chunk.chunkID, chunk);
    }

    public EntityPlayer getPlayerByName(String n)
    {
        for(int i = 0;i<players.size();i++)
        {
            if(players.get(i).username.equals(n))
                return players.get(i);
        }
        return null;
    }

    public void removePlayer(EntityPlayer p)
    {
        players.remove(p);
    }

    public File getChunkFolder()
    {
        return chunkFolder;
    }

    public void spawnParticle(Particle p)
    {
        particles.spawnParticle(p);
    }

    public ArrayList<OldPacket> getUpdatePackets()
    {
        ArrayList<OldPacket> list = new ArrayList<OldPacket>();
        for(int i =0;i< entities.size();i++)
        {
            Entity e = entities.get(i);
            if(e != null)
            if(e.shouldSendUpdate())
            {
                list.add(new PacketEntityUpdate(e, e.entityID));
            }
        }
        list.add(new PacketTime(time));
        return list;
    }
    
    public TileEntity getTileEntityByID(int ID)
    {
        if(!this.tileEntities.isEmpty())
        {
            for(int i = 0 ; i < this.tileEntities.size() ; i++)
            {
                if(ID == this.tileEntities.get(i).id)
                {
                    return this.tileEntities.get(i);
                }
            }
        }
        
        return null;
    }
    
    public TileEntity getTileEntityAt(float x, float y)
    {
        if(!this.tileEntities.isEmpty())
        {
            for(int i = 0 ; i < this.tileEntities.size() ; i++)
            {
                if(x == this.tileEntities.get(i).posX && y == this.tileEntities.get(i).posY)
                {
                    return this.tileEntities.get(i);
                }
            }
        }
        return null;
    }

    public WorldChunk getRandomChunk()
    {
        Random rand = new Random();
        WorldChunk[] chunks = this.chunksList.values().toArray(new WorldChunk[0]);
        if(chunks.length > 0)
            return chunks[rand.nextInt(chunks.length)];
        else 
            return null;
    }

    public void handleBlockUpdate(BlockInfo info)
    {
        if(info.type == BlockInfo.ID)
        {
            this.setBlock(info.x, info.y, info.data);
        }
        else if(info.type == BlockInfo.ATTACK_VALUE)
        {
            String[] parts = info.data.split(":");
            this.setAttackValue(info.x, info.y, this.getAttackValue(info.x, info.y)+Integer.parseInt(parts[0]), parts[1]);
        }
    }
}

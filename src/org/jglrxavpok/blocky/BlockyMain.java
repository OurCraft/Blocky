package org.jglrxavpok.blocky;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import org.jglrxavpok.blocky.achievements.AchievementRenderer;
import org.jglrxavpok.blocky.block.Block;
import org.jglrxavpok.blocky.client.ChatHUDComponent;
import org.jglrxavpok.blocky.client.ClientNetworkListener;
import org.jglrxavpok.blocky.gui.UIMainMenu;
import org.jglrxavpok.blocky.gui.UIPauseMenu;
import org.jglrxavpok.blocky.input.ClientInput;
import org.jglrxavpok.blocky.input.InputProcessor;
import org.jglrxavpok.blocky.network.NetworkCommons;
import org.jglrxavpok.blocky.network.packets.PacketDisconnect;
import org.jglrxavpok.blocky.ui.UI;
import org.jglrxavpok.blocky.utils.Fluid;
import org.jglrxavpok.blocky.utils.HUDComponent;
import org.jglrxavpok.blocky.utils.ImageUtils;
import org.jglrxavpok.blocky.utils.LWJGLHandler;
import org.jglrxavpok.blocky.utils.SoundManager;
import org.jglrxavpok.blocky.world.World;
import org.jglrxavpok.blocky.world.WorldGenerator.WorldType;
import org.jglrxavpok.opengl.FontRenderer;
import org.jglrxavpok.opengl.TextFormatting;
import org.jglrxavpok.opengl.Textures;
import org.jglrxavpok.storage.TaggedStorageSystem;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class BlockyMain implements Runnable
{

	private static JFrame	mainFrame;
	private static File	folder;
    public static String username;

    public static final boolean isDevMode = true;

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if(args.length > 0 && args[0] != null)
		{
		    username = TextFormatting.escapeString(args[0]);
		}
		if(args.length > 1 && args[1] != null)
        {
            folder = new File(args[1]);
        }
		setupMainFrame();
		setupLWJGL();
		Sys.initialize();
		new Thread(new BlockyMain()).start();
	}
	
	private static void setupLWJGL()
	{
		try
		{
			LWJGLHandler.load(getFolder().getPath()+File.separator+"/natives");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static File getFolder()
	{
		if(folder == null)
		{
			String appdata = System.getenv("APPDATA");
			if(appdata != null)
			{
				folder = new File(appdata, ".blocky");
			}
			else
			{
				folder = new File(System.getProperty("user.home"), ".blocky");
			}
			if(!folder.exists())folder.mkdirs();
		}
		return folder;
	}

	private static void setupMainFrame()
	{
		mainFrame = new JFrame();
		mainFrame.addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent event)
		    {
		        run = false;
		    }
		});
		mainFrame.setIconImage(ImageUtils.getFromClasspath("/assets/textures/icon32.png"));
	}
	
	public static void console(String msg)
	{
		System.out.println("[Blocky "+version()+"] "+msg);
	}
	
	public static void shutdown()
	{
		run = false;
	}

	public static String version()
	{
		return "Indev 1.2.1"; // Parce que fuck la police
	}

	public static BlockyMain	instance;
	public static boolean run = false;
	private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();
	private World	level;
	private long	last;
	private int	frameNbr;
	private int	fps;
	private ArrayList<HUDComponent>	hudComps = new ArrayList<HUDComponent>();
    private ArrayList<InputProcessor> inputProcessors = new ArrayList<InputProcessor>();
    private boolean screenshotKey;
    private ArrayList<String> chatContent = new ArrayList<String>();
    private ArrayList<Long> chatTime = new ArrayList<Long>();
    
    private ArrayList<Controller> foundControllers;
    private int cursorX;
    private int cursorY;
    private int polledControllers;
    private float backgroundRed;
    private float backgroundGreen;
    private float backgroundBlue;
    public long timeRunning;
    public AchievementRenderer achievementRenderer;
    private boolean f11Pressed;
    private ClientNetworkListener listener;
    
    public static double ratioW;
    public static double ratioH;

	public static int width = 1920/2;
	public static int height = 1080/2;
	public static TaggedStorageSystem saveSystem = new TaggedStorageSystem();

	public BlockyMain(){}
	
	@Override
	public void run()
	{
		instance = this;
		final Canvas canvas = new Canvas();
		mainFrame.add(canvas);
		canvas.addComponentListener(new ComponentAdapter()
		{
	        @Override
	        public void componentResized(ComponentEvent e)
	        { newCanvasSize.set(canvas.getSize()); }
	     });
	     
	    mainFrame.addWindowFocusListener(new WindowAdapter()
	    {
	       @Override
	       public void windowGainedFocus(WindowEvent e)
	       { canvas.requestFocusInWindow(); }
	    });
	     
	    canvas.setPreferredSize(new Dimension(width,height));
	    mainFrame.pack();
	    mainFrame.setLocationRelativeTo(null);
	    mainFrame.setTitle("Blocky");
	    mainFrame.setVisible(true);
		run = true;
		UI.setWidth(width);
		UI.setHeight(height);
		foundControllers = new ArrayList<Controller>();
		searchForControllers();
		this.addInputProcessor(new ClientInput());
		this.addHUDComponent(new ChatHUDComponent(chatContent,chatTime));
		this.addHUDComponent((achievementRenderer = new AchievementRenderer()));
		try
		{
			Display.setParent(canvas);
			Display.create();
			GL11.glOrtho(0.0, width,  0.0,height,  -1000.0, 10000.0);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL_PROJECTION);
			GL11.glLoadIdentity();
			GLU.gluOrtho2D(0, width, 0, height);
			Mouse.create();
	    	Keyboard.create();
			Fluid.load();
			Block.loadAll();
	    	Textures.get(ImageUtils.toBufferedImage(FileSystemView.getFileSystemView().getSystemIcon(new File(System.getProperty("user.home")))));
	    	SoundManager.instance.equals(SoundManager.instance); // Instantiation of SoundManager
	    	UI.displayMenu(new UIMainMenu());
	    	
	    	while(run && !Display.isCloseRequested())
	    	{
	    		updateLoop();
	    		renderLoop();
	    	}
	    	mainFrame.setVisible(false);
	    	if(Display.isFullscreen())
	    	    Display.setFullscreen(false);
	    	Display.destroy();
	    	mainFrame.dispose();
	    	System.exit(0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
    public void addHUDComponent(HUDComponent comp)
	{
		hudComps.add(comp);
	}

	private void renderLoop()
	{
	    if(this.hasControllerPlugged())
	        Mouse.setCursorPosition(cursorX, cursorY);
		Dimension newDim = newCanvasSize.getAndSet(null);
        if(Display.isFullscreen() && !Display.isActive())
            try
            {
                Display.setFullscreen(false);
            }
            catch (LWJGLException e)
            {
                e.printStackTrace();
            }
        if (newDim != null && !Display.isFullscreen())
        {
           GL11.glViewport(0, 0, newDim.width, newDim.height);
           ratioW = (double)newDim.width/(double)width;
           ratioH = (double)newDim.height/(double)height;
        }
        else
        {
        	GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
        	ratioW = (double)Display.getWidth()/(double)width;
            ratioH = (double)Display.getHeight()/(double)height;
        }
        
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();


		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_2D);

		// Transparent Textures
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.01f);
		
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL_ONE_MINUS_SRC_ALPHA);


		GL11.glClearColor(this.backgroundRed, this.backgroundGreen, this.backgroundBlue, 1);

		GL11.glPushMatrix();
		
        if(level != null)
		{
			level.render();
		}
		for(int i = 0;i<hudComps.size();i++)
        {
            hudComps.get(i).render();
        }
		
		GL11.glPopMatrix();
		UI.lazyDrawAll();

		if(this.hasControllerPlugged())
		    Textures.render(Textures.getFromClasspath("/assets/textures/ui/hasController.png"), width-95f/2f, height-44f/2f, 95f/2f, 44f/2f);
		FontRenderer.drawString("FPS: "+fps, 0, 10, 0xFFFFFF);
		frameNbr++;
		if(System.currentTimeMillis()-last >= 1000)
		{
			last = System.currentTimeMillis();
			fps = frameNbr;
			frameNbr = 0;
		}
		
		String str = "Blocky "+BlockyMain.version();
		FontRenderer.drawString(str, width-FontRenderer.getWidth(str), 0, 0xFFFFFF);
		if(mainFrame.isAlwaysOnTop())
		{
		    str = "Front locked";
		    FontRenderer.drawString(str, width-FontRenderer.getWidth(str), 10, 0xFFFFFF);
		}
		Display.update();
		Display.sync(60);
	}
	

    /**
     * Search (and save) for controllers of type Controller.Type.STICK,
     * Controller.Type.GAMEPAD, Controller.Type.WHEEL and Controller.Type.FINGERSTICK.
     */
    private void searchForControllers()
    {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        for(int i = 0; i < controllers.length; i++)
        {
            Controller controller = controllers[i];
            if (controller.getType() == Controller.Type.STICK || 
                controller.getType() == Controller.Type.GAMEPAD || 
                controller.getType() == Controller.Type.WHEEL ||
                controller.getType() == Controller.Type.FINGERSTICK)
            {
                // Add new controller to the list of all controllers.
                if(!foundControllers.contains(controller))
                foundControllers.add(controller);
            }
        }
    }
	
	private void pollEvents()
    {
        while(Keyboard.next())
        {
            char c = Keyboard.getEventCharacter();
            int k = Keyboard.getEventKey();
            boolean b = Keyboard.getEventKeyState();
            if(k == Keyboard.KEY_F10 && b)
                mainFrame.setAlwaysOnTop(!mainFrame.isAlwaysOnTop());
            UI.onKeyEvent(c, k, b);
            for(int index1 = 0;index1<inputProcessors.size();index1++)
            {
                InputProcessor proc = inputProcessors.get(index1);
                proc.onKeyEvent(c, k, b);
            }
        }
        while(Mouse.next())
        {
            int mx = Mouse.getX();
            int my = Mouse.getY();
            int button = Mouse.getEventButton();
            boolean b = Mouse.getEventButtonState();
            UI.onMouseEvent(cursorX,cursorY,button,b);
            for(int index1 = 0;index1<inputProcessors.size();index1++)
            {
                InputProcessor proc = inputProcessors.get(index1);
                proc.onMouseEvent(mx, my, button, b);
            }
        }
        for(int index1 = 0;index1<inputProcessors.size();index1++)
        {
            InputProcessor proc = inputProcessors.get(index1);
            proc.onUpdate();
        }
        pollControllerData();
    }
	
	private void pollControllerData()
	{
	    polledControllers = 0;
	    for(int index = 0;index<foundControllers.size();index++)
	    {
            // Currently selected controller.
            int selectedControllerIndex = index;
            Controller controller = foundControllers.get(selectedControllerIndex);

            // Pull controller for current data, and break while loop if controller is disconnected.
            if(!controller.poll())
            {
                foundControllers.remove(index);
                continue;
            }
            
            // Go trough all components of the controller.
            Component[] components = controller.getComponents();
            for(int i=0; i < components.length; i++)
            {
                Component component = components[i];
                Identifier componentIdentifier = component.getIdentifier();
                
                // Buttons
                if(component.getIdentifier() instanceof Component.Identifier.Button)
                {
                    // Is button pressed?
                    boolean isItPressed = true;
                    if(component.getPollData() == 0.0f)
                        isItPressed = false;
                    
                    for(int index1 = 0;index1<inputProcessors.size();index1++)
                    {
                        InputProcessor proc = inputProcessors.get(index1);
                        proc.onButtonUpdate(index, isItPressed, component);
                    }
                }
                
                // Hat switch
                if(componentIdentifier == Component.Identifier.Axis.POV)
                {
                    float hatSwitchPosition = component.getPollData();
                    for(int index1 = 0;index1<inputProcessors.size();index1++)
                    {
                        InputProcessor proc = inputProcessors.get(index1);
                        proc.onPovUpdate(index, hatSwitchPosition, component);
                    }
                }
                
                // Axes
                if(component.isAnalog())
                {
                    float axisValue = component.getPollData();
                    for(int index1 = 0;index1<inputProcessors.size();index1++)
                    {
                        InputProcessor proc = inputProcessors.get(index1);
                        proc.onAxisUpdate(index, axisValue, component);
                    }
                }
                
                for(int index1 = 0;index1<inputProcessors.size();index1++)
                {
                    InputProcessor proc = inputProcessors.get(index1);
                    proc.onCustomComponentUpdate(index, component);
                }
            }
        }
    }
	
	public int getControllersPolled()
	{
	    return polledControllers;
	}
	
	public boolean hasControllerPlugged()
	{
	    return foundControllers.size() > 0;
	}

	private void updateLoop()
	{
	    pollEvents();
	    if(!UI.doesMenuPauseGame())
	    {
	        if(level != null)
	        {
    			level.tick();
    		}
	    }
	    for(int i = 0;i<hudComps.size();i++)
        {
            hudComps.get(i).update();
        }
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && level != null)
		    UI.displayMenu(new UIPauseMenu());
		if(Keyboard.isKeyDown(Keyboard.KEY_F11) && !f11Pressed)
            try
            {
                Display.setFullscreen(!Display.isFullscreen());
                Display.setVSyncEnabled(true);
                f11Pressed = true;
            }
            catch (LWJGLException e)
            {
                e.printStackTrace();
            }
		else if(!Keyboard.isKeyDown(Keyboard.KEY_F11) && f11Pressed)
		{
		    f11Pressed = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_F2) && !screenshotKey)
		{
		    screenshotKey = true;
		    BufferedImage img = LWJGLHandler.takeScreenshot();
		    File screenFolder = new File(getFolder(), "screenshots/");
		    if(!screenFolder.exists())screenFolder.mkdirs();
		    String name = computeScreenshotName();
		    File screenFile = new File(screenFolder, name+".png");
		    try
            {
                ImageIO.write(img, "png", screenFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
		}
		else if(!Keyboard.isKeyDown(Keyboard.KEY_F2) && screenshotKey)
		    screenshotKey = false;
		UI.update();
		timeRunning++;
	}
	
	private String computeScreenshotName()
    {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int month = Calendar.getInstance().get(Calendar.MONTH)+1;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int min = Calendar.getInstance().get(Calendar.MINUTE);
        int second = Calendar.getInstance().get(Calendar.SECOND);
        return day+"_"+(month < 10 ? "0"+month : month)+"_"+year+" "+hour+"."+min+"."+second;
    }

    public void saveLevel() throws IOException
    {
        if(level != null)
        {
            if(level.worldType != WorldType.CLIENT)
            {
                File f = level.getChunkFolder();
                if(f == null)
                {
                    f = new File(new File(getFolder(), "saves"), level.getName());
                    f.mkdirs();
                }
                else if(!f.exists())
                {
                    f.mkdirs();
                }
                level.save(f);
            }
            else
            {
                if(this.getClientNetwork() != null)
                {
                    NetworkCommons.sendPacketTo(new PacketDisconnect(), false, getClientNetwork().getClientConnection());
                    this.getClientNetwork().getClientConnection().close();
                }
            }
        }
    }

    public void loadLevel(World level)
    {
        this.level = level;
    }

    public World getLevel()
    {
        return level;
    }

    public void removeHUDComponent(HUDComponent component)
    {
        hudComps.remove(component);
    }

    public void addInputProcessor(InputProcessor proc)
    {
        this.inputProcessors.add(proc);
    }
    
    public int getCursorX()
    {
        return (int) (cursorX);
    }

    public int getCursorY()
    {
        return (int) (cursorY);
    }
    
    public void setCursorPos(int cx, int cy)
    {
        if(cx < 0)
            cx = 0;
        if(cy < 0)
            cy = 0;
        if(cx > (int)UI.getWidth())
            cx = (int)UI.getWidth();
        if(cy > (int)UI.getHeight())
            cy = (int)UI.getHeight();
        this.cursorX = cx;
        this.cursorY = cy;
        UI.setCursorPos(cx,cy);
    }

    public void setBackgroundColor(float r, float g, float b)
    {
        backgroundRed = r;
        backgroundGreen = g;
        backgroundBlue = b;
    }
    
    public void addToChat(String txt)
    {
        this.chatContent.add(txt);
        this.chatTime.add(System.currentTimeMillis());
    }

    public void removeInputProcessor(InputProcessor proc)
    {
        this.inputProcessors.remove(proc);
    }

    public ClientNetworkListener getClientNetwork()
    {
        return listener;
    }
    
    public void setClientNetwork(ClientNetworkListener l)
    {
        listener = l;
    }

}

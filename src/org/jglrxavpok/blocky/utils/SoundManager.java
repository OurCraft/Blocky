package org.jglrxavpok.blocky.utils;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;

public class SoundManager
{

	public static final SoundManager instance = new SoundManager();
	private SoundSystem	sndSystem;
	
	private SoundManager()
	{
		loadCodecs();
	}
	
	public void playSoundFX(String sound, float volume, float pitch)
	{
		sndSystem.newStreamingSource(false, sound, SoundManager.class.getResource(sound), sound.substring(sound.lastIndexOf(".")+1), false, 0, 0, 0, 0, 0);
		sndSystem.setVolume(sound, volume);
		sndSystem.setPitch(sound, pitch);
		sndSystem.play(sound);
	}
	
	private void loadCodecs()
	{
		try
		{
			
			SoundSystemConfig.addLibrary(paulscode.sound.libraries.LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", paulscode.sound.codecs.CodecJOrbis.class);
			SoundSystemConfig.setCodec("wav", paulscode.sound.codecs.CodecWav.class);
			SoundSystemConfig.setDefaultFadeDistance(100);
			try
			{
				Class<?> class1 = Class.forName("paulscode.sound.codecs.CodecIBXM");
				SoundSystemConfig.setCodec("xm", class1);
				SoundSystemConfig.setCodec("s3m", class1);
				SoundSystemConfig.setCodec("mod", class1);
			}
			catch (ClassNotFoundException classnotfoundexception) { }

			sndSystem = new SoundSystem();
		}
		catch (Throwable throwable)
		{
			System.err.println("error linking with the LibraryOpenAL plug-in");
			throwable.printStackTrace();
		}
	}

    public void playBackgroundMusic(String music, float volume, float pitch)
    {
        sndSystem.newStreamingSource(true, "bgmusic", SoundManager.class.getResource(music), music.substring(music.lastIndexOf(".")+1), false, 0, 0, 0, 0, 0);
        sndSystem.setVolume("bgmusic", volume);
        sndSystem.setPitch("bgmusic", pitch);
        sndSystem.play("bgmusic");
    }
    
    public void setLooping(String source, boolean toLoop)
    {
        sndSystem.setLooping(source, toLoop);
    }
}

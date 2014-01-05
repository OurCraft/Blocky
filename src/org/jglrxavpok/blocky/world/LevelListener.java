package org.jglrxavpok.blocky.world;

public interface LevelListener
{
	public void onBlockChanged(String oldBlock, int posX, int posY, String newBlock);
	
	public void onDataChanged(String oldData, int posX, int posY, String newData, int index);

    public void onLightChanged(float oldValue, int x, int y, float value);
}

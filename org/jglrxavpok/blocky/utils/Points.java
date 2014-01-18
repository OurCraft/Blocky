package org.jglrxavpok.blocky.utils;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

public class Points
{

	private static final ArrayList<Vector2f> vecsMap = new ArrayList<Vector2f>();
	private static Vector2f tmp = new Vector2f();
	
	public static Vector2f getPoint(float x, float y)
	{
		tmp.set(x, y);
		if(vecsMap.contains(tmp))
		{
			return vecsMap.get(vecsMap.indexOf(tmp));
		}
		vecsMap.add(new Vector2f(x,y));
		return vecsMap.get(vecsMap.indexOf(tmp));
	}
}

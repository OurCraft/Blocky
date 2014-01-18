package org.jglrxavpok.blocky.utils;

public class MathHelper
{

	public static double dist(double startX, double startY, double startZ, double x, double y, double z)
	{
		return Math.sqrt(Math.pow(Math.max(startX, x)-Math.min(startX, x), 2)+Math.pow(Math.max(startY, y)-Math.min(startY, y), 2)+Math.pow(Math.max(startZ, z)-Math.min(startZ, z), 2));
	}
	
	public static double dist(double startX, double startY, double x, double y)
	{
		return Math.sqrt(Math.pow(Math.max(startX, x)-Math.min(startX, x), 2)+Math.pow(Math.max(startY, y)-Math.min(startY, y), 2));
	}
}

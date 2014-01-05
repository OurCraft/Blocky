package org.jglrxavpok.blocky.utils;

public class AABB
{

	private float	x;
	private float	y;
	private float	w;
	private float	h;

	public AABB(float x, float y, float w, float h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public AABB setY(float y)
    {
        this.y = y;
        return this;
    }
	public AABB setWidth(float width)
    {
        w = width;
        return this;
    }
	
	public AABB setX(float x)
    {
        this.x = x;
        return this;
    }
	
	public AABB setHeight(float height)
	{
	    h = height;
	    return this;
	}
	
	public boolean collide(AABB aabb)
	{
		if(aabb.x+aabb.w < x
		|| aabb.x > x+w
		|| aabb.y+aabb.h < y
		|| aabb.y > y+h)
			return false;
		return true;
	}
	
	public boolean overrides(AABB aabb)
	{
		if(x < aabb.x
		&& x+w > aabb.x+aabb.w
		&& y < aabb.y
		&& y+h > aabb.y+aabb.h)
			return true;
		return false;
	}
	
	public boolean isOverridden(AABB aabb)
	{
		return aabb.overrides(this);
	}
	
	public boolean hasOverride(AABB aabb)
	{
		return overrides(aabb) || aabb.overrides(this);
	}

	public AABB set(float posX, float posY, float width, float height)
	{
		x=posX;
		y=posY;
		w=width;
		h=height;
		return this;
	}
	
	public String toString()
	{
	    return "{x:"+x+";y:"+y+";w:"+w+";h:"+h+"}";
	}
}

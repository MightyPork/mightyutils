package mightypork.utils.math.constraints.rect.proxy;


import mightypork.utils.math.constraints.rect.PluggableRectBound;
import mightypork.utils.math.constraints.rect.Rect;
import mightypork.utils.math.constraints.rect.RectBound;


/**
 * Pluggable rect proxy
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class RectProxy extends RectAdapter implements PluggableRectBound {
	
	private RectBound backing = null;
	
	
	public RectProxy()
	{
	}
	
	
	public RectProxy(RectBound proxied)
	{
		backing = proxied;
	}
	
	
	@Override
	public void setRect(RectBound proxied)
	{
		this.backing = proxied;
	}
	
	
	@Override
	public Rect getSource()
	{
		return backing.getRect();
	}
	
}

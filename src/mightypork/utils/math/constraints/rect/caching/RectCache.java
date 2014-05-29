package mightypork.utils.math.constraints.rect.caching;


import mightypork.utils.math.constraints.rect.Rect;


/**
 * Rect cache implementation
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class RectCache extends AbstractRectCache {
	
	private final Rect source;
	
	
	public RectCache(Rect source)
	{
		this.source = source;
	}
	
	
	@Override
	public final Rect getCacheSource()
	{
		return source;
	}
	
	
	@Override
	public void onConstraintChanged()
	{
	}
	
}

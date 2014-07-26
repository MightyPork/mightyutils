package mightypork.utils.math.constraints.num.caching;


import mightypork.utils.math.constraints.CachedConstraint;
import mightypork.utils.math.constraints.num.Num;
import mightypork.utils.math.constraints.num.proxy.NumAdapter;
import mightypork.utils.math.constraints.num.var.NumVar;


/**
 * <p>
 * A Num cache.
 * </p>
 * <p>
 * Values are held in a caching VectVar, and digest caching is enabled by
 * default.
 * </p>
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class AbstractNumCache extends NumAdapter implements CachedConstraint<Num> {
	
	private final NumVar cache = Num.makeVar();
	private boolean inited = false;
	private boolean cachingEnabled = true;
	
	
	public AbstractNumCache() {
		enableDigestCaching(true); // it changes only on poll
	}
	
	
	@Override
	protected final Num getSource()
	{
		if (!inited) markDigestDirty();
		
		return (cachingEnabled ? cache : getCacheSource());
	}
	
	
	@Override
	public final void poll()
	{
		inited = true;
		
		// poll source
		final Num source = getCacheSource();
		source.markDigestDirty(); // poll cached
		
		// store source value
		cache.setTo(source);
		
		// mark my digest dirty
		markDigestDirty();
		
		onConstraintChanged();
	}
	
	
	@Override
	public abstract void onConstraintChanged();
	
	
	@Override
	public abstract Num getCacheSource();
	
	
	@Override
	public final void enableCaching(boolean yes)
	{
		cachingEnabled = yes;
		enableDigestCaching(yes);
	}
	
	
	@Override
	public final boolean isCachingEnabled()
	{
		return cachingEnabled;
	}
	
}

package mightypork.utils.math.constraints.rect.caching;


import mightypork.utils.math.constraints.CachedConstraint;
import mightypork.utils.math.constraints.rect.Rect;
import mightypork.utils.math.constraints.rect.proxy.RectAdapter;
import mightypork.utils.math.constraints.rect.var.RectVar;


/**
 * <p>
 * A rect cache.
 * </p>
 * <p>
 * Values are held in a caching VectVar, and digest caching is enabled by
 * default.
 * </p>
 *
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class AbstractRectCache extends RectAdapter implements CachedConstraint<Rect> {

	private final RectVar cache = Rect.makeVar();
	private boolean inited = false;
	private boolean cachingEnabled = true;


	public AbstractRectCache()
	{
		enableDigestCaching(true); // it changes only on poll
	}


	@Override
	protected final Rect getSource()
	{
		if (!inited) poll();

		return (cachingEnabled ? cache : getCacheSource());
	}


	@Override
	public final void poll()
	{
		inited = true;

		// poll source
		final Rect source = getCacheSource();
		source.markDigestDirty(); // poll cached

		// store source value
		cache.setTo(source);

		markDigestDirty();

		onConstraintChanged();
	}


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

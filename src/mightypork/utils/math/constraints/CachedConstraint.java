package mightypork.utils.math.constraints;


import mightypork.utils.interfaces.Pollable;


/**
 * Constraint that is cached
 *
 * @author Ondřej Hruška (MightyPork)
 * @param <C> constraint type
 */
public interface CachedConstraint<C> extends Pollable {

	/**
	 * Called after the cache has changed value (and digest).
	 */
	void onConstraintChanged();


	/**
	 * @return the cached value
	 */
	C getCacheSource();


	/**
	 * Enable caching & digest caching
	 *
	 * @param yes enable caching
	 */
	void enableCaching(boolean yes);


	/**
	 * @return true if caching is on
	 */
	boolean isCachingEnabled();


	/**
	 * Update cached value and cached digest (if digest caching is enabled).<br>
	 * source constraint is polled beforehand.
	 */
	@Override
	void poll();
}

package mightypork.utils.math.constraints;


/**
 * <p>
 * Interface for constraints that support digests. Digest is a small data object
 * with final fields, typically primitive, used for processing (such as
 * rendering or other very frequent operations).
 * </p>
 * <p>
 * Taking a digest is expensive, so if it needs to be done often and the value
 * changes are deterministic (such as, triggered by timing event or screen
 * resize), it's useful to cache the last digest and reuse it until such an
 * event occurs again.
 * </p>
 * <p>
 * Implementing class typically needs a field to store the last digest, a flag
 * that digest caching is enabled, and a flag that a digest is dirty.
 * </p>
 *
 * @author Ondřej Hruška (MightyPork)
 * @param <D> digest class
 */
public interface CachedDigestable<D> extends Digestable<D> {

	/**
	 * <p>
	 * Toggle digest caching.
	 * </p>
	 * <p>
	 * To trigger update of the cache, call the <code>poll()</code> method.
	 * </p>
	 *
	 * @param yes
	 */
	void enableDigestCaching(boolean yes);


	/**
	 * @return true if digest caching is enabled.
	 */
	boolean isDigestCachingEnabled();


	/**
	 * If digest caching is enabled, mark current cached value as "dirty". Dirty
	 * digest should be re-created next time a value is requested.<br>
	 */
	void markDigestDirty();

}

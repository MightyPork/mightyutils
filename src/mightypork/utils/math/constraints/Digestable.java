package mightypork.utils.math.constraints;


/**
 * COnstraint that can be converted to a digest, representing current state
 *
 * @author Ondřej Hruška (MightyPork)
 * @param <D>
 */
public interface Digestable<D> {
	
	/**
	 * Take a digest. If digest caching is enabled and the cached digest is
	 * marked as dirty, a new one should be made.
	 *
	 * @return digest
	 */
	D digest();
	
}

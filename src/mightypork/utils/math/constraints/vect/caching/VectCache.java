package mightypork.utils.math.constraints.vect.caching;


import mightypork.utils.math.constraints.vect.Vect;


/**
 * Vect cache implementation
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class VectCache extends AbstractVectCache {

	private final Vect source;


	public VectCache(Vect source)
	{
		this.source = source;
		enableDigestCaching(true);
	}


	@Override
	public Vect getCacheSource()
	{
		return source;
	}


	@Override
	public void onConstraintChanged()
	{
	}
}

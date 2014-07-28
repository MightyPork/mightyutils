package mightypork.utils.ion;


/**
 * External ionizer using a data bundle - can be used if the data type cannot be
 * modified to implement the proper interface
 *
 * @author Ondřej Hruška (MightyPork)
 * @param <T>
 */
public abstract class IonizerBundled<T> {

	@SuppressWarnings("unchecked")
	final void _save(Object object, IonDataBundle out)
	{
		save((T) object, out);
	}


	/**
	 * Save an object to data bundle
	 *
	 * @param object object to save
	 * @param out bundle to save to
	 */
	public abstract void save(T object, IonDataBundle out);


	/**
	 * Load an object from a bundle
	 *
	 * @param in bundle to load from
	 * @return the loaded object
	 */
	public abstract T load(IonDataBundle in);

}

package mightypork.utils.ion;


import java.io.IOException;


/**
 * External ionizer using a IonOutput / IonInput - can be used if the data type
 * cannot be modified to implement the proper interface
 *
 * @author Ondřej Hruška (MightyPork)
 * @param <T>
 */
public abstract class IonizerBinary<T> {
	
	@SuppressWarnings("unchecked")
	final void _save(Object object, IonOutput out) throws IOException
	{
		save((T) object, out);
	}
	
	
	/**
	 * Save an object to ion output
	 *
	 * @param object object to save
	 * @param out ion output
	 * @throws IOException
	 */
	public abstract void save(T object, IonOutput out) throws IOException;
	
	
	/**
	 * Load an object from ion input
	 *
	 * @param in ion input
	 * @return the loaded object
	 */
	public abstract T load(IonInput in) throws IOException;
	
}

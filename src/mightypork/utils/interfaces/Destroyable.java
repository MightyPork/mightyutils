package mightypork.utils.interfaces;


/**
 * Object that can be destroyed (free resources etc)
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public interface Destroyable {
	
	/**
	 * Destroy this object
	 */
	public void destroy();
}

package mightypork.utils.interfaces;


/**
 * Can be asked to update it's state
 *
 * @author Ondřej Hruška (MightyPork)
 */
public interface Pollable {

	/**
	 * Update internal state
	 */
	void poll();
}

package mightypork.utils.interfaces;


/**
 * Can be enabled or disabled.<br>
 * Implementations should take appropriate action (ie. stop listening to events,
 * updating etc.)
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public interface Enableable {
	
	/**
	 * Change enabled state
	 * 
	 * @param yes enabled
	 */
	public void setEnabled(boolean yes);
	
	
	/**
	 * @return true if enabled
	 */
	public boolean isEnabled();
}

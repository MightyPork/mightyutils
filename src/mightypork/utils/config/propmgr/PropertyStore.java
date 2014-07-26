package mightypork.utils.config.propmgr;


import java.io.IOException;
import java.util.Collection;


/**
 * Interface for a property store (used by {@link PropertyManager}).<br>
 * Due to this abstraction, different kind of property storage can be used, not
 * only a file.
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public interface PropertyStore {
	
	/**
	 * Set a header comment
	 * 
	 * @param comment the comment text (can be multi-line)
	 */
	void setComment(String comment);
	
	
	/**
	 * Load properties from the file / store. If the file does not exist or is
	 * inaccessible, nothing is loaded.
	 */
	void load();
	
	
	/**
	 * Save properties to the file / store.
	 * 
	 * @throws IOException if the file cannot be created or written.
	 */
	void save() throws IOException;
	
	
	/**
	 * Get a property value
	 * 
	 * @param key property key
	 * @return value retrieved from the file, or null if none found.
	 */
	String getProperty(String key);
	
	
	/**
	 * Set a property value
	 * 
	 * @param key property key
	 * @param value property value to set
	 * @param comment property comment. Can be null.
	 */
	void setProperty(String key, String value, String comment);
	
	
	/**
	 * Remove a property from the list.
	 * 
	 * @param key property key to remove
	 */
	void removeProperty(String key);
	
	
	/**
	 * Clear the property list
	 */
	void clear();
	
	
	/**
	 * Get keys collection (can be used for iterating)
	 * 
	 * @return keys collection
	 */
	public Collection<String> keys();
}

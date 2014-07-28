package mightypork.utils.config.propmgr;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import mightypork.utils.Convert;
import mightypork.utils.config.propmgr.properties.BooleanProperty;
import mightypork.utils.config.propmgr.properties.DoubleProperty;
import mightypork.utils.config.propmgr.properties.IntegerProperty;
import mightypork.utils.config.propmgr.properties.StringProperty;
import mightypork.utils.config.propmgr.store.PropertyFile;
import mightypork.utils.logging.Log;


/**
 * Property manager with advanced formatting and value checking.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class PropertyManager {

	private final TreeMap<String, Property<?>> entries = new TreeMap<>();
	private final TreeMap<String, String> renameTable = new TreeMap<>();
	private final PropertyStore props;


	/**
	 * Create property manager from file path and a header comment.<br>
	 * This is the same as using a {@link PropertyFile} store.
	 *
	 * @param file property file
	 * @param comment header comment.
	 */
	public PropertyManager(File file, String comment)
	{
		this(new PropertyFile(file, comment));
	}


	/**
	 * Create property manager based on provided {@link PropertyStore}
	 *
	 * @param props a property store implementation backing this property
	 *            manager
	 */
	public PropertyManager(PropertyStore props)
	{
		this.props = props;
	}


	/**
	 * Load from file
	 */
	public void load()
	{
		props.load();

		// rename keys (useful if keys change but value is to be kept)
		for (final Entry<String, String> entry : renameTable.entrySet()) {

			final String value = props.getProperty(entry.getKey());

			if (value == null) continue;

			final String oldKey = entry.getKey();
			final String newKey = entry.getValue();

			props.removeProperty(oldKey);
			props.setProperty(newKey, value, entries.get(newKey).getComment());
		}

		for (final Property<?> entry : entries.values()) {
			entry.fromString(props.getProperty(entry.getKey()));
		}
	}


	public void save()
	{
		try {
			final ArrayList<String> keyList = new ArrayList<>();

			// validate entries one by one, replace with default when needed
			for (final Property<?> entry : entries.values()) {
				keyList.add(entry.getKey());

				props.setProperty(entry.getKey(), entry.toString(), entry.getComment());
			}

			// removed unused props
			for (final String key : props.keys()) {
				if (!keyList.contains(key)) {
					props.removeProperty(key);
				}
			}

			props.save();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}


	/**
	 * Get a property entry (rarely used)
	 *
	 * @param k key
	 * @return the entry
	 */
	public Property<?> getProperty(String k)
	{
		try {
			return entries.get(k);
		} catch (final Exception e) {
			Log.w(e);
			return null;
		}
	}


	/**
	 * Get boolean property
	 *
	 * @param k key
	 * @return the boolean found, or false
	 */
	public Boolean getBoolean(String k)
	{
		return Convert.toBoolean(getProperty(k).getValue());
	}


	/**
	 * Get numeric property
	 *
	 * @param k key
	 * @return the int found, or null
	 */
	public Integer getInteger(String k)
	{
		return Convert.toInteger(getProperty(k).getValue());
	}


	/**
	 * Get numeric property as double
	 *
	 * @param k key
	 * @return the double found, or null
	 */
	public Double getDouble(String k)
	{
		return Convert.toDouble(getProperty(k).getValue());
	}


	/**
	 * Get string property
	 *
	 * @param k key
	 * @return the string found, or null
	 */
	public String getString(String k)
	{
		return Convert.toString(getProperty(k).getValue());
	}


	/**
	 * Get arbitrary property. Make sure it's of the right type!
	 *
	 * @param k key
	 * @return the prioperty found
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(String k)
	{
		try {
			return ((Property<T>) getProperty(k)).getValue();
		} catch (final ClassCastException e) {
			return null;
		}
	}


	/**
	 * Add a boolean property
	 *
	 * @param k key
	 * @param d default value
	 * @param comment the in-file comment
	 */
	public void addBoolean(String k, boolean d, String comment)
	{
		addProperty(new BooleanProperty(k, d, comment));
	}


	/**
	 * Add a numeric property (double)
	 *
	 * @param k key
	 * @param d default value
	 * @param comment the in-file comment
	 */
	public void addDouble(String k, double d, String comment)
	{
		addProperty(new DoubleProperty(k, d, comment));
	}


	/**
	 * Add a numeric property
	 *
	 * @param k key
	 * @param d default value
	 * @param comment the in-file comment
	 */
	public void addInteger(String k, int d, String comment)
	{
		addProperty(new IntegerProperty(k, d, comment));
	}


	/**
	 * Add a string property
	 *
	 * @param k key
	 * @param d default value
	 * @param comment the in-file comment
	 */
	public void addString(String k, String d, String comment)
	{
		addProperty(new StringProperty(k, d, comment));
	}


	/**
	 * Add a generic property (can be used with custom property types)
	 *
	 * @param prop property to add
	 */
	public <T> void addProperty(Property<T> prop)
	{
		entries.put(prop.getKey(), prop);
	}


	/**
	 * Rename key before loading; value is preserved
	 *
	 * @param oldKey old key
	 * @param newKey new key
	 */
	public void renameKey(String oldKey, String newKey)
	{
		renameTable.put(oldKey, newKey);
		return;
	}


	/**
	 * Set value saved to certain key.
	 *
	 * @param key key
	 * @param value the saved value
	 */
	public void setValue(String key, Object value)
	{
		getProperty(key).setValue(value);
	}


	/**
	 * Set heading comment of the property store.
	 *
	 * @param fileComment comment text (can be multi-line)
	 */
	public void setFileComment(String fileComment)
	{
		props.setComment(fileComment);
	}

}

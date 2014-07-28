package mightypork.utils.config.propmgr;


import mightypork.utils.Convert;
import mightypork.utils.annotations.Stub;


/**
 * Property entry for the {@link PropertyManager}.<br>
 * Extending this class can be used to add custom property types that are not
 * supported by default.
 *
 * @author Ondřej Hruška (MightyPork)
 * @param <T> property type
 */
public abstract class Property<T> {
	
	protected final String comment;
	protected final String key;
	
	protected T value;
	protected final T defaultValue;
	
	
	/**
	 * Create a property without comment
	 *
	 * @param key key in the config file
	 * @param defaultValue defualt property value (used as fallback when
	 *            parsing)
	 */
	public Property(String key, T defaultValue)
	{
		this(key, defaultValue, null);
	}
	
	
	/**
	 * Create a property with a comment
	 *
	 * @param key key in the config file
	 * @param defaultValue default property value, used as fallback when
	 *            parsing. Initially the value is assigned to defaultValue.
	 * @param comment optional property comment included above the property in
	 *            the config file. Can be null.
	 */
	public Property(String key, T defaultValue, String comment)
	{
		this.comment = comment;
		this.key = key;
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}
	
	
	/**
	 * Parse a string representation of the value into this property. If the
	 * value cannot be decoded, use the default value instead.
	 *
	 * @param string property value as string
	 */
	public abstract void fromString(String string);
	
	
	/**
	 * Get property value as string (compatible with `fromString())
	 *
	 * @return property value as string
	 */
	@Override
	@Stub
	public String toString()
	{
		return Convert.toString(value, Convert.toString(defaultValue));
	}
	
	
	/**
	 * Get the current property value
	 *
	 * @return the value
	 */
	public T getValue()
	{
		return value;
	}
	
	
	/**
	 * Set property value.<br>
	 * Uses Object to allow setValue(Object) method in {@link PropertyManager}
	 *
	 * @param value value to set.
	 * @throws ClassCastException in case of incompatible type.
	 */
	@SuppressWarnings("unchecked")
	public void setValue(Object value)
	{
		this.value = (T) value;
	}
	
	
	/**
	 * Get property comment.
	 *
	 * @return the comment text (can be null if no comment is defined)
	 */
	public String getComment()
	{
		return comment;
	}
	
	
	/**
	 * Get property key
	 *
	 * @return property key
	 */
	public String getKey()
	{
		return key;
	}
}

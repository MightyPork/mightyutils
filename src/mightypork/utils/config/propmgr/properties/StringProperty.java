package mightypork.utils.config.propmgr.properties;


import mightypork.utils.Convert;
import mightypork.utils.config.propmgr.Property;


/**
 * String property
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class StringProperty extends Property<String> {

	public StringProperty(String key, String defaultValue)
	{
		super(key, defaultValue);
	}


	public StringProperty(String key, String defaultValue, String comment)
	{
		super(key, defaultValue, comment);
	}


	@Override
	public void fromString(String string)
	{
		setValue(Convert.toString(string, defaultValue));
	}
}

package mightypork.utils.config.propmgr.properties;


import mightypork.utils.Convert;
import mightypork.utils.config.propmgr.Property;


/**
 * Integer property
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class IntegerProperty extends Property<Integer> {
	
	public IntegerProperty(String key, Integer defaultValue)
	{
		super(key, defaultValue);
	}
	
	
	public IntegerProperty(String key, Integer defaultValue, String comment)
	{
		super(key, defaultValue, comment);
	}
	
	
	@Override
	public void fromString(String string)
	{
		setValue(Convert.toInteger(string, defaultValue));
	}
}

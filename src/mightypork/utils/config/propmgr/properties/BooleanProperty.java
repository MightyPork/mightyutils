package mightypork.utils.config.propmgr.properties;


import mightypork.utils.Convert;
import mightypork.utils.config.propmgr.Property;


/**
 * Boolean property
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class BooleanProperty extends Property<Boolean> {
	
	public BooleanProperty(String key, Boolean defaultValue)
	{
		super(key, defaultValue);
	}
	
	
	public BooleanProperty(String key, Boolean defaultValue, String comment)
	{
		super(key, defaultValue, comment);
	}
	
	
	@Override
	public void fromString(String string)
	{
		setValue(Convert.toBoolean(string, defaultValue));
	}
}

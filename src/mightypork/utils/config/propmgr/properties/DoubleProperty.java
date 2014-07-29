package mightypork.utils.config.propmgr.properties;


import mightypork.utils.Convert;
import mightypork.utils.config.propmgr.Property;


/**
 * Double property
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class DoubleProperty extends Property<Double> {
	
	public DoubleProperty(String key, Double defaultValue)
	{
		super(key, defaultValue);
	}
	
	
	public DoubleProperty(String key, Double defaultValue, String comment)
	{
		super(key, defaultValue, comment);
	}
	
	
	@Override
	public void fromString(String string)
	{
		setValue(Convert.toDouble(string, defaultValue));
	}
}

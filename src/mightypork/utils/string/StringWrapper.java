package mightypork.utils.string;


/**
 * String provider with constant string
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class StringWrapper implements StringProvider {
	
	private final String value;
	
	
	public StringWrapper(String value)
	{
		this.value = value;
	}
	
	
	@Override
	public String getString()
	{
		return value;
	}
	
}

package mightypork.utils.string;


/**
 * String provider that holds a string.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class StringWrapper implements StringProvider {

	private String value;


	/**
	 * Create a string wrapper
	 *
	 * @param value original value
	 */
	public StringWrapper(String value)
	{
		this.value = value;
	}
	
	
	/**
	 * Set the string
	 *
	 * @param value string to set
	 */
	public void setString(String value)
	{
		this.value = value;
	}


	@Override
	public String getString()
	{
		return value;
	}

}

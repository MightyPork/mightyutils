package mightypork.utils.files.config;


import mightypork.utils.Convert;


public abstract class Property<T extends Object> {
	
	private final String comment;
	private final String key;
	
	private T value;
	private final T defaultValue;
	
	
	public Property(String key, T defaultValue, String comment)
	{
		super();
		this.comment = comment;
		this.key = key;
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}
	
	
	public final void parse(String string)
	{
		setValue(decode(string, defaultValue));
	}
	
	
	public abstract T decode(String string, T defval);
	
	
	public String encode(T value)
	{
		return Convert.toString(value, Convert.toString(defaultValue));
	}
	
	
	@Override
	public final String toString()
	{
		return encode(value);
	}
	
	
	public T getValue()
	{
		return value;
	}
	
	
	@SuppressWarnings("unchecked")
	public void setValue(Object value)
	{
		this.value = (T) value;
	}
	
	
	public String getComment()
	{
		return comment;
	}
	
	
	public String getKey()
	{
		return key;
	}
}

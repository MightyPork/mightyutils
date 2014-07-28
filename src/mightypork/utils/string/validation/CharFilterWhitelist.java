package mightypork.utils.string.validation;


public class CharFilterWhitelist implements CharFilter {

	private final String whitelist;


	public CharFilterWhitelist(String allowed)
	{
		this.whitelist = allowed;
	}


	@Override
	public boolean isValid(char c)
	{
		return whitelist.contains(Character.toString(c));
	}

}

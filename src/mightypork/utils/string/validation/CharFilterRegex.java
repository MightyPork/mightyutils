package mightypork.utils.string.validation;


public class CharFilterRegex implements CharFilter {
	
	private final String formula;
	
	
	public CharFilterRegex(String regex) {
		this.formula = regex;
	}
	
	
	@Override
	public boolean isValid(char c)
	{
		return Character.toString(c).matches(formula);
	}
	
}

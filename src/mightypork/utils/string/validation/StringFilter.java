package mightypork.utils.string.validation;


/**
 * Utility interface for string filters (accepting filepaths and similar)
 *
 * @author Ondřej Hruška (MightyPork)
 */
public interface StringFilter {
	
	public boolean isValid(String entry);
}

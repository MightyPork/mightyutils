package mightypork.utils.exceptions;


/**
 * Thrown by a map-like class when the key specified is already taken.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class KeyAlreadyExistsException extends RuntimeException {

	public KeyAlreadyExistsException()
	{
		super();
	}


	public KeyAlreadyExistsException(String message, Throwable cause)
	{
		super(message, cause);
	}


	public KeyAlreadyExistsException(String message)
	{
		super(message);
	}


	public KeyAlreadyExistsException(Throwable cause)
	{
		super(cause);
	}

}

package mightypork.utils.exceptions;


import java.io.IOException;


/**
 * Thrown when data could not be read successfully.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class CorruptDataException extends IOException {

	public CorruptDataException()
	{
		super();
	}


	public CorruptDataException(String message, Throwable cause)
	{
		super(message, cause);
	}


	public CorruptDataException(String message)
	{
		super(message);
	}


	public CorruptDataException(Throwable cause)
	{
		super(cause);
	}

}

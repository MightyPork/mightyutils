package mightypork.utils.eventbus.clients;


/**
 * Client that can toggle receiving messages.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public interface ToggleableClient {

	/**
	 * @return true if the client wants to receive messages
	 */
	public boolean isListening();

}

package mightypork.utils.eventbus;


import java.util.Collection;
import java.util.HashSet;

import mightypork.utils.Reflect;
import mightypork.utils.Str;
import mightypork.utils.eventbus.clients.DelegatingClient;
import mightypork.utils.eventbus.clients.ToggleableClient;
import mightypork.utils.eventbus.events.flags.NonRejectableEvent;
import mightypork.utils.logging.Log;


/**
 * Event delivery channel, module of {@link EventBus}
 *
 * @author Ondřej Hruška (MightyPork)
 * @param <EVENT> event type
 * @param <CLIENT> client (subscriber) type
 */
class EventChannel<EVENT extends BusEvent<CLIENT>, CLIENT> {
	
	private final Class<CLIENT> clientClass;
	private final Class<EVENT> eventClass;
	
	
	/**
	 * Create a channel
	 *
	 * @param eventClass event class
	 * @param clientClass client class
	 */
	public EventChannel(Class<EVENT> eventClass, Class<CLIENT> clientClass)
	{
		
		if (eventClass == null || clientClass == null) {
			throw new NullPointerException("Null Event or Client class.");
		}
		
		this.clientClass = clientClass;
		this.eventClass = eventClass;
	}
	
	
	/**
	 * Try to broadcast a event.<br>
	 * If event is of wrong type, <code>false</code> is returned.
	 *
	 * @param event a event to be sent
	 * @param clients collection of clients
	 */
	public void broadcast(BusEvent<?> event, Collection<?> clients)
	{
		if (!canBroadcast(event)) return;
		
		doBroadcast(eventClass.cast(event), clients, new HashSet<>());
	}
	
	
	/**
	 * Send the event
	 *
	 * @param event sent event
	 * @param clients subscribing clients
	 * @param processed clients already processed
	 */
	private void doBroadcast(final EVENT event, final Collection<?> clients, final Collection<Object> processed)
	{
		for (final Object client : clients) {
			
			// exclude obvious non-clients
			if (!isClientValid(client)) {
				continue;
			}
			
			// avoid executing more times
			if (processed.contains(client)) {
				Log.w(EventBus.logMark + "Client already served: " + Str.val(client));
				continue;
			}
			processed.add(client);
			
			final boolean must_deliver = Reflect.hasAnnotation(event, NonRejectableEvent.class);
			
			// opt-out
			if (client instanceof ToggleableClient) {
				if (!must_deliver && !((ToggleableClient) client).isListening()) continue;
			}
			
			sendTo(client, event);
			
			if (event.isConsumed()) return;
			
			// pass on to delegated clients
			if (client instanceof DelegatingClient) {
				if (must_deliver || ((DelegatingClient) client).doesDelegate()) {
					
					final Collection<?> children = ((DelegatingClient) client).getChildClients();
					
					if (children != null && children.size() > 0) {
						doBroadcast(event, children, processed);
					}
					
				}
			}
		}
	}
	
	
	/**
	 * Send an event to a client.
	 *
	 * @param client target client
	 * @param event event to send
	 */
	@SuppressWarnings("unchecked")
	private void sendTo(Object client, EVENT event)
	{
		if (isClientOfChannelType(client)) {
			((BusEvent<CLIENT>) event).deliverTo((CLIENT) client);
		}
	}
	
	
	/**
	 * Check if the given event can be broadcasted by this channel
	 *
	 * @param event event object
	 * @return can be broadcasted
	 */
	public boolean canBroadcast(BusEvent<?> event)
	{
		return event != null && eventClass.isInstance(event);
	}
	
	
	/**
	 * Create an instance for given types
	 *
	 * @param eventClass event class
	 * @param clientClass client class
	 * @return the broadcaster
	 */
	public static <F_EVENT extends BusEvent<F_CLIENT>, F_CLIENT> EventChannel<F_EVENT, F_CLIENT> create(Class<F_EVENT> eventClass, Class<F_CLIENT> clientClass)
	{
		return new EventChannel<>(eventClass, clientClass);
	}
	
	
	/**
	 * Check if client is of channel type
	 *
	 * @param client client
	 * @return is of type
	 */
	private boolean isClientOfChannelType(Object client)
	{
		return clientClass.isInstance(client);
	}
	
	
	/**
	 * Check if the channel is compatible with given
	 *
	 * @param client client
	 * @return is supported
	 */
	public boolean isClientValid(Object client)
	{
		return isClientOfChannelType(client) || (client instanceof DelegatingClient);
	}
	
	
	@Override
	public int hashCode()
	{
		final int prime = 13;
		int result = 1;
		result = prime * result + ((clientClass == null) ? 0 : clientClass.hashCode());
		result = prime * result + ((eventClass == null) ? 0 : eventClass.hashCode());
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof EventChannel)) return false;
		final EventChannel<?, ?> other = (EventChannel<?, ?>) obj;
		if (clientClass == null) {
			if (other.clientClass != null) return false;
		} else if (!clientClass.equals(other.clientClass)) return false;
		if (eventClass == null) {
			if (other.eventClass != null) return false;
		} else if (!eventClass.equals(other.eventClass)) return false;
		return true;
	}
	
	
	@Override
	public String toString()
	{
		return "{ " + Str.val(eventClass) + " => " + Str.val(clientClass) + " }";
	}
}

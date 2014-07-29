package mightypork.utils.eventbus.clients;


import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import mightypork.utils.eventbus.EventBus;


/**
 * Client that can be attached to the {@link EventBus}, or added as a child
 * client to another {@link DelegatingClient}
 *
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class BusNode implements ClientHub {
	
	private final Set<Object> clients = new LinkedHashSet<>();
	private boolean listening = true;
	private boolean delegating = true;
	
	
	@Override
	public Collection<Object> getChildClients()
	{
		return clients;
	}
	
	
	@Override
	public boolean doesDelegate()
	{
		return delegating;
	}
	
	
	@Override
	public boolean isListening()
	{
		return listening;
	}
	
	
	/**
	 * Add a child subscriber to the {@link EventBus}.<br>
	 *
	 * @param client
	 */
	@Override
	public void addChildClient(Object client)
	{
		clients.add(client);
	}
	
	
	/**
	 * Remove a child subscriber
	 *
	 * @param client subscriber to remove
	 */
	@Override
	public void removeChildClient(Object client)
	{
		if (client != null) {
			clients.remove(client);
		}
	}
	
	
	/**
	 * Set whether events should be received.
	 *
	 * @param listening receive events
	 */
	public void setListening(boolean listening)
	{
		this.listening = listening;
	}
	
	
	/**
	 * Set whether events should be passed on to child nodes
	 *
	 * @param delegating
	 */
	public void setDelegating(boolean delegating)
	{
		this.delegating = delegating;
	}
}

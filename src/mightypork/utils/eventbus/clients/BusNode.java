package mightypork.utils.eventbus.clients;


import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import mightypork.utils.annotations.Stub;
import mightypork.utils.eventbus.BusAccess;
import mightypork.utils.eventbus.EventBus;


/**
 * Client that can be attached to the {@link EventBus}, or added as a child
 * client to another {@link DelegatingClient}
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class BusNode implements BusAccess, ClientHub {
	
	private BusAccess busAccess;
	
	private final Set<Object> clients = new LinkedHashSet<>();
	private boolean listening = true;
	private boolean delegating = true;
	
	
	/**
	 * @param busAccess access to bus
	 */
	public BusNode(BusAccess busAccess) {
		setBusAccess(busAccess);
	}
	
	
	/**
	 * Create without a bus access. Bus access should be set using the
	 * setBusAccess() method.
	 */
	public BusNode() {
	}
	
	
	/**
	 * Handler called when bus access first provides a valid bus instance.<br>
	 * The node may now eg. subscribe to the bus.
	 */
	@Stub
	public void onBusReady()
	{
		// do stuff
	}
	
	
	/**
	 * Assign a bus access. If the buss access provides a buss, the onBusReady()
	 * method will be called.
	 * 
	 * @param busAccess the assigned bus access
	 */
	public final void setBusAccess(BusAccess busAccess)
	{
		this.busAccess = busAccess;
		
		if (getEventBus() != null) {
			onBusReady();
		}
	}
	
	
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
		if (client instanceof RootBusNode) {
			throw new IllegalArgumentException("Cannot nest RootBusNode.");
		}
		
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
	
	
	@Override
	public EventBus getEventBus()
	{
		if(busAccess==null) return null;
		return busAccess.getEventBus();
	}
	
}

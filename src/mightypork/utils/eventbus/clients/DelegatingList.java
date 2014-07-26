package mightypork.utils.eventbus.clients;


import java.util.Collection;

import mightypork.utils.interfaces.Enableable;


/**
 * List of clients, that can be used as a delegating client.
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class DelegatingList extends ClientList implements DelegatingClient, Enableable, ToggleableClient {
	
	private boolean enabled = true;
	
	
	/**
	 * Delegating list with initial clients
	 * 
	 * @param clients initial list members (clients)
	 */
	public DelegatingList(Object... clients) {
		super(clients);
	}
	
	
	/**
	 * Empty delegating list.
	 */
	public DelegatingList() {
	}
	
	
	@Override
	public Collection<?> getChildClients()
	{
		return this;
	}
	
	
	@Override
	public boolean doesDelegate()
	{
		return isEnabled();
	}
	
	
	@Override
	public boolean isListening()
	{
		return isEnabled();
	}
	
	
	@Override
	public void setEnabled(boolean yes)
	{
		enabled = yes;
	}
	
	
	@Override
	public boolean isEnabled()
	{
		return enabled;
	}
}

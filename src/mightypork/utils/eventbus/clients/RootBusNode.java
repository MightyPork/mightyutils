package mightypork.utils.eventbus.clients;


import mightypork.utils.annotations.DefaultImpl;
import mightypork.utils.eventbus.BusAccess;
import mightypork.utils.interfaces.Destroyable;


/**
 * Bus node that should be directly attached to the bus.
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class RootBusNode extends BusNode implements Destroyable {
	
	/**
	 * @param busAccess access to bus
	 */
	public RootBusNode(BusAccess busAccess)
	{
		super(busAccess);
		
		getEventBus().subscribe(this);
	}
	
	
	@Override
	public final void destroy()
	{
		deinit();
		
		getEventBus().unsubscribe(this);
	}
	
	
	/**
	 * Deinitialize the subsystem<br>
	 * (called during destruction)
	 */
	@DefaultImpl
	protected void deinit()
	{
	}
	
}

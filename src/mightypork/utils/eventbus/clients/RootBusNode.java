package mightypork.utils.eventbus.clients;


import mightypork.utils.annotations.Stub;
import mightypork.utils.eventbus.BusAccess;
import mightypork.utils.interfaces.Destroyable;


/**
 * Bus node that should be directly attached to the bus.
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class RootBusNode extends BusNode implements Destroyable {
	
	/**
	 * Create with a bus access.
	 * 
	 * @param busAccess access to bus
	 */
	public RootBusNode(BusAccess busAccess) {
		super(busAccess);
	}
	
	
	/**
	 * Create without a bus access. It should be assigned later.
	 */
	public RootBusNode() {
	}
	
	
	@Override
	public void onBusReady()
	{
		getEventBus().subscribe(this);
	}
	
	
	@Override
	public final void destroy()
	{
		deinit();
		
		if (getEventBus() != null) {
			getEventBus().unsubscribe(this);
		}
	}
	
	
	/**
	 * Deinitialize the node (subsystem)<br>
	 * (called during destruction)
	 */
	@Stub
	protected void deinit()
	{
	}
	
}

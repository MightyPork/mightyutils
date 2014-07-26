package mightypork.utils.eventbus.events;


import mightypork.utils.eventbus.BusEvent;
import mightypork.utils.eventbus.events.flags.DirectEvent;
import mightypork.utils.eventbus.events.flags.NonConsumableEvent;
import mightypork.utils.eventbus.events.flags.NotLoggedEvent;
import mightypork.utils.interfaces.Updateable;


/**
 * Delta timing update event. Not logged.
 * 
 * @author Ondřej Hruška (MightyPork)
 */
@NotLoggedEvent
@DirectEvent
@NonConsumableEvent
public class UpdateEvent extends BusEvent<Updateable> {
	
	private final double deltaTime;
	
	
	/**
	 * @param deltaTime time since last update (sec)
	 */
	public UpdateEvent(double deltaTime) {
		this.deltaTime = deltaTime;
	}
	
	
	@Override
	public void handleBy(Updateable handler)
	{
		handler.update(deltaTime);
	}
}

package mightypork.utils.eventbus.events;


import mightypork.utils.eventbus.BusEvent;
import mightypork.utils.eventbus.events.flags.DirectEvent;
import mightypork.utils.eventbus.events.flags.NonConsumableEvent;
import mightypork.utils.eventbus.events.flags.NonRejectableEvent;
import mightypork.utils.interfaces.Destroyable;


/**
 * Invoke destroy() method of all subscribers. Used to deinit a system.
 *
 * @author Ondřej Hruška (MightyPork)
 */
@DirectEvent
@NonConsumableEvent
@NonRejectableEvent
public class DestroyEvent extends BusEvent<Destroyable> {

	@Override
	public void handleBy(Destroyable handler)
	{
		handler.destroy();
	}

}

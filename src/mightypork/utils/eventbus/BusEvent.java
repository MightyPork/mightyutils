package mightypork.utils.eventbus;


import mightypork.utils.eventbus.events.flags.DelayedEvent;
import mightypork.utils.eventbus.events.flags.DirectEvent;
import mightypork.utils.eventbus.events.flags.NonConsumableEvent;
import mightypork.utils.eventbus.events.flags.NotLoggedEvent;
import mightypork.utils.eventbus.events.flags.SingleReceiverEvent;


/**
 * <p>
 * Event that can be handled by HANDLER, subscribing to the event bus.
 * </p>
 * <p>
 * Can be annotated as {@link SingleReceiverEvent} to be delivered once only,
 * and {@link DelayedEvent} or {@link DirectEvent} to specify default sending
 * mode. When marked as {@link NotLoggedEvent}, it will not appear in detailed
 * bus logging (useful for very frequent events, such as UpdateEvent).
 * </p>
 * <p>
 * Events annotated as {@link NonConsumableEvent} will throw an exception upon
 * an attempt to consume them.
 * </p>
 * <p>
 * Default sending mode (if not changed by annotations) is <i>queued</i> with
 * zero delay.
 * </p>
 * 
 * @author Ondřej Hruška (MightyPork)
 * @param <HANDLER> handler type
 */
public abstract class BusEvent<HANDLER> {
	
	private boolean consumed;
	private boolean served;
	
	
	/**
	 * Ask handler to handle this message.
	 * 
	 * @param handler handler instance
	 */
	protected abstract void handleBy(HANDLER handler);
	
	
	/**
	 * Consume the event, so no other clients will receive it.
	 * 
	 * @throws UnsupportedOperationException if the {@link NonConsumableEvent}
	 *             annotation is present.
	 */
	public final void consume()
	{
		if (consumed) throw new IllegalStateException("Already consumed.");
		
		if (getClass().isAnnotationPresent(NonConsumableEvent.class)) {
			throw new UnsupportedOperationException("Not consumable.");
		}
		
		consumed = true;
	}
	
	
	/**
	 * Deliver to a handler using the handleBy method.
	 * 
	 * @param handler handler instance
	 */
	final void deliverTo(HANDLER handler)
	{
		handleBy(handler);
		
		if (!served) {
			if (getClass().isAnnotationPresent(SingleReceiverEvent.class)) {
				consumed = true;
			}
			
			served = true;
		}
	}
	
	
	/**
	 * Check if the event is consumed. When an event is consumed, no other
	 * clients will receive it.
	 * 
	 * @return true if consumed
	 */
	public final boolean isConsumed()
	{
		return consumed;
	}
	
	
	/**
	 * @return true if the event was served to at least 1 client
	 */
	public final boolean wasServed()
	{
		return served;
	}
	
	
	/**
	 * Clear "served" and "consumed" flags before dispatching.
	 */
	final void clearFlags()
	{
		served = false;
		consumed = false;
	}
	
	
	/**
	 * Called after all clients have received the event.
	 * 
	 * @param bus event bus instance
	 */
	public void onDispatchComplete(EventBus bus)
	{
	}
}

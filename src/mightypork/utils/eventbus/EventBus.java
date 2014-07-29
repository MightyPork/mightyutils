package mightypork.utils.eventbus;


import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import mightypork.utils.Reflect;
import mightypork.utils.Str;
import mightypork.utils.eventbus.clients.DelegatingClient;
import mightypork.utils.eventbus.events.flags.DelayedEvent;
import mightypork.utils.eventbus.events.flags.DirectEvent;
import mightypork.utils.eventbus.events.flags.NotLoggedEvent;
import mightypork.utils.interfaces.Destroyable;
import mightypork.utils.logging.Log;


/**
 * An event bus, accommodating multiple EventChannels.<br>
 * Channel will be created when an event of type is first encountered.
 *
 * @author Ondřej Hruška (MightyPork)
 */
final public class EventBus implements Destroyable {
	
	/**
	 * Queued event holder
	 */
	private class DelayQueueEntry implements Delayed {
		
		private final long due;
		private final BusEvent<?> evt;
		
		
		public DelayQueueEntry(double seconds, BusEvent<?> event)
		{
			super();
			this.due = System.currentTimeMillis() + (long) (seconds * 1000);
			this.evt = event;
		}
		
		
		@Override
		public int compareTo(Delayed o)
		{
			return Long.valueOf(getDelay(TimeUnit.MILLISECONDS)).compareTo(o.getDelay(TimeUnit.MILLISECONDS));
		}
		
		
		@Override
		public long getDelay(TimeUnit unit)
		{
			return unit.convert(due - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		}
		
		
		public BusEvent<?> getEvent()
		{
			return evt;
		}
		
	}
	
	/**
	 * Thread handling queued events
	 */
	private class QueuePollingThread extends Thread {
		
		public volatile boolean stopped = false;
		
		
		public QueuePollingThread()
		{
			super("Queue Polling Thread");
		}
		
		
		@Override
		public void run()
		{
			DelayQueueEntry evt;
			
			while (!stopped) {
				evt = null;
				
				try {
					evt = sendQueue.take();
				} catch (final InterruptedException ignored) {
					//
				}
				
				if (evt != null) {
					try {
						dispatch(evt.getEvent());
					} catch (final Throwable t) {
						Log.w(logMark + "Error while dispatching event: ", t);
					}
				}
			}
		}
		
	}
	
	static final String logMark = "(bus) ";
	
	
	private static Class<?> getEventListenerClass(BusEvent<?> event)
	{
		return Reflect.getGenericParameters(event.getClass())[0];
	}
	
	/** Log detailed messages (debug) */
	public boolean detailedLogging = false;
	
	/** Queue polling thread */
	private final QueuePollingThread busThread;
	
	/** Registered clients */
	private final Set<Object> clients = Collections.newSetFromMap(new ConcurrentHashMap<Object, Boolean>());
	
	/** Whether the bus was destroyed */
	private boolean dead = false;
	
	/** Message channels */
	private final Set<EventChannel<?, ?>> channels = Collections.newSetFromMap(new ConcurrentHashMap<EventChannel<?, ?>, Boolean>());
	
	/** Messages queued for delivery */
	private final DelayQueue<DelayQueueEntry> sendQueue = new DelayQueue<>();
	
	
	/**
	 * Make a new bus and start it's queue thread.
	 */
	public EventBus()
	{
		busThread = new QueuePollingThread();
		busThread.setDaemon(true);
		busThread.start();
	}
	
	
	/**
	 * Halt bus thread and reject any future events.
	 */
	@Override
	public void destroy()
	{
		assertLive();
		
		busThread.stopped = true;
		dead = true;
	}
	
	
	/**
	 * Send based on annotation
	 *
	 * @param event event
	 */
	public void send(BusEvent<?> event)
	{
		assertLive();
		
		final DelayedEvent adelay = Reflect.getAnnotation(event, DelayedEvent.class);
		if (adelay != null) {
			sendDelayed(event, adelay.delay());
			return;
		}
		
		if (Reflect.hasAnnotation(event, DirectEvent.class)) {
			sendDirect(event);
			return;
		}
		
		sendQueued(event);
	}
	
	
	/**
	 * Add event to a queue
	 *
	 * @param event event
	 */
	public void sendQueued(BusEvent<?> event)
	{
		assertLive();
		
		sendDelayed(event, 0);
	}
	
	
	/**
	 * Add event to a queue, scheduled for given time.
	 *
	 * @param event event
	 * @param delay delay before event is dispatched
	 */
	public void sendDelayed(BusEvent<?> event, double delay)
	{
		assertLive();
		
		final DelayQueueEntry dm = new DelayQueueEntry(delay, event);
		
		if (shallLog(event)) {
			Log.f3(logMark + "Qu [" + Str.val(event) + "]" + (delay == 0 ? "" : (", delay: " + delay + "s")));
		}
		
		sendQueue.add(dm);
	}
	
	
	/**
	 * Send immediately.<br>
	 * Should be used for real-time events that require immediate response, such
	 * as timing events.
	 *
	 * @param event event
	 */
	public void sendDirect(BusEvent<?> event)
	{
		assertLive();
		
		if (shallLog(event)) Log.f3(logMark + "Di [" + Str.val(event) + "]");
		
		dispatch(event);
	}
	
	
	public void sendDirectToChildren(DelegatingClient delegatingClient, BusEvent<?> event)
	{
		assertLive();
		
		if (shallLog(event)) Log.f3(logMark + "Di->sub [" + Str.val(event) + "]");
		
		doDispatch(delegatingClient.getChildClients(), event);
	}
	
	
	/**
	 * Connect a client to the bus. The client will be connected to all current
	 * and future channels, until removed from the bus.
	 *
	 * @param client the client
	 */
	public void subscribe(Object client)
	{
		assertLive();
		
		if (client == null) return;
		
		clients.add(client);
		
		if (detailedLogging) Log.f3(logMark + "Client joined: " + Str.val(client));
	}
	
	
	/**
	 * Disconnect a client from the bus.
	 *
	 * @param client the client
	 */
	public void unsubscribe(Object client)
	{
		assertLive();
		
		clients.remove(client);
		
		if (detailedLogging) Log.f3(logMark + "Client left: " + Str.val(client));
	}
	
	
	@SuppressWarnings("unchecked")
	private boolean addChannelForEvent(BusEvent<?> event)
	{
		try {
			if (detailedLogging) {
				Log.f3(logMark + "Setting up channel for new event type: " + Str.val(event.getClass()));
			}
			
			final Class<?> listener = getEventListenerClass(event);
			final EventChannel<?, ?> ch = EventChannel.create(event.getClass(), listener);
			
			if (ch.canBroadcast(event)) {
				
				channels.add(ch);
				//channels.flush();
				
				if (detailedLogging) {
					Log.f3(logMark + "Created new channel: " + Str.val(event.getClass()) + " -> " + Str.val(listener));
				}
				
				return true;
				
			} else {
				Log.w(logMark + "Could not create channel for event " + Str.val(event.getClass()));
			}
			
		} catch (final Throwable t) {
			Log.w(logMark + "Error while trying to add channel for event.", t);
		}
		
		return false;
	}
	
	
	/**
	 * Make sure the bus is not destroyed.
	 *
	 * @throws IllegalStateException if the bus is dead.
	 */
	private void assertLive() throws IllegalStateException
	{
		if (dead) throw new IllegalStateException("EventBus is dead.");
	}
	
	
	/**
	 * Send immediately.<br>
	 * Should be used for real-time events that require immediate response, such
	 * as timing events.
	 *
	 * @param event event
	 */
	private synchronized void dispatch(BusEvent<?> event)
	{
		assertLive();
		
		doDispatch(clients, event);
		event.onDispatchComplete(this);
	}
	
	
	/**
	 * Send to a set of clients
	 *
	 * @param clients clients
	 * @param event event
	 */
	private synchronized void doDispatch(Collection<?> clients, BusEvent<?> event)
	{
		boolean accepted = false;
		
		event.clearFlags();
		
		for (int i = 0; i < 2; i++) { // two tries.
		
			for (final EventChannel<?, ?> b : channels) {
				if (b.canBroadcast(event)) {
					accepted = true;
					b.broadcast(event, clients);
				}
				
				if (event.isConsumed()) break;
			}
			
			if (!accepted) if (addChannelForEvent(event)) continue;
			
			break;
		}
		
		if (!accepted) Log.e(logMark + "Not accepted by any channel: " + Str.val(event));
		if (!event.wasServed() && shallLog(event)) Log.w(logMark + "Not delivered: " + Str.val(event));
	}
	
	
	private boolean shallLog(BusEvent<?> event)
	{
		if (!detailedLogging) return false;
		if (Reflect.hasAnnotation(event, NotLoggedEvent.class)) return false;
		
		return true;
	}
	
}

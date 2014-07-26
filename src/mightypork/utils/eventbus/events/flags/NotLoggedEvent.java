package mightypork.utils.eventbus.events.flags;


import java.lang.annotation.*;


/**
 * Event that's not worth logging, unless there was an error with it.<br>
 * Useful for common events that would otherwise clutter the log.
 * 
 * @author Ondřej Hruška (MightyPork)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface NotLoggedEvent {}

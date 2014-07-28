package mightypork.utils.eventbus.events.flags;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Event that should be queued with given delay (default: 0);
 *
 * @author Ondřej Hruška (MightyPork)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface DelayedEvent {

	/**
	 * @return event dispatch delay [seconds]
	 */
	double delay() default 0;
}

package mightypork.utils.annotations;


import java.lang.annotation.*;


/**
 * Specify pretty name to be used when logging / converting class name to
 * string.
 * 
 * @author Ondřej Hruška (MightyPork)
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Target(ElementType.TYPE)
public @interface Alias {
	
	String name();
}

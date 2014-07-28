package mightypork.utils.annotations;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Marks a static factory method. This is a description annotation and has no
 * other function.
 *
 * @author Ondřej Hruška (MightyPork)
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Documented
public @interface FactoryMethod {

}

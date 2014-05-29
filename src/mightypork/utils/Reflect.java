package mightypork.utils;


import java.lang.annotation.Annotation;


/**
 * Miscelanous reflection-related utilities
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class Reflect {
	
	/**
	 * Get annotation of given type from an object
	 * 
	 * @param tested the examined object
	 * @param annotation annotation we want
	 * @return the anotation on that object, or null
	 */
	public static <T extends Annotation> T getAnnotation(Object tested, Class<T> annotation)
	{
		return tested.getClass().getAnnotation(annotation);
	}
	
	
	/**
	 * Check if an object has an annotation of given trype
	 * 
	 * @param tested the examined object
	 * @param annotation annotation we want
	 * @return true if the annotation is present on the object
	 */
	public static boolean hasAnnotation(Object tested, Class<? extends Annotation> annotation)
	{
		return tested.getClass().isAnnotationPresent(annotation);
	}
	
}

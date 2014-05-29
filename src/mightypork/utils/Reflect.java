package mightypork.utils;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


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
	
	
	/**
	 * Get generic parameters of a class
	 * 
	 * @param clazz the examined class
	 * @return parameter types
	 */
	public static Class<?>[] getGenericParameters(Class<?> clazz)
	{
		// BEHOLD, MAGIC!
		
		final Type evtc = clazz.getGenericSuperclass();
		
		if (evtc instanceof ParameterizedType) {
			final Type[] types = ((ParameterizedType) evtc).getActualTypeArguments();
			
			final Class<?>[] classes = new Class<?>[types.length];
			
			for (int i = 0; i < types.length; i++) {
				classes[i] = (Class<?>) types[i];
			}
			
			return classes;
		}
		
		throw new RuntimeException(Support.str(clazz) + " is not generic.");
	}
	
	
	public static Object getConstantFieldValue(Class<?> objClass, String fieldName) throws ReflectiveOperationException
	{
		final Field fld = objClass.getDeclaredField(fieldName);
		
		final int modif = fld.getModifiers();
		
		if (!Modifier.isFinal(modif) || !Modifier.isStatic(modif)) {
			throw new RuntimeException("The " + fieldName + " field of " + Support.str(objClass) + " field must be static and final!");
		}
		
		fld.setAccessible(true);
		return fld.get(null);
	}
	
}

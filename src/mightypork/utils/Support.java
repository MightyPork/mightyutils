package mightypork.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import mightypork.utils.annotations.Alias;


/**
 * Miscelanous utilities
 *
 * @author Ondřej Hruška (MightyPork)
 */
public final class Support {
	
	/**
	 * Create a new thread of the runnable, and start it.
	 *
	 * @param r runnable
	 * @return the thread started
	 */
	public static Thread runAsThread(Runnable r)
	{
		final Thread t = new Thread(r);
		t.start();
		return t;
	}
	
	
	/**
	 * Pick first non-null option
	 *
	 * @param options options
	 * @return the selected option
	 */
	public static Object fallback(Object... options)
	{
		for (final Object o : options) {
			if (o != null) return o;
		}
		
		return null; // all null
	}
	
	
	/**
	 * Get current time/date for given format.
	 *
	 * @param format format, according to {@link DateFormat}.
	 * @return the formatted time/date
	 */
	public static String getTime(String format)
	{
		return (new SimpleDateFormat(format)).format(new Date());
	}
	
	
	/**
	 * Parse array of vararg key, value pairs to a LinkedHashMap.<br>
	 * Example:
	 *
	 * <pre>
	 * Object[] array = {
	 *     &quot;one&quot;, 1,
	 *     &quot;two&quot;, 4,
	 *     &quot;three&quot;, 9,
	 *     &quot;four&quot;, 16
	 * };
	 *
	 * Map&lt;String, Integer&gt; args = parseVarArgs(array);
	 * </pre>
	 *
	 * @param args varargs
	 * @return LinkedHashMap
	 * @throws ClassCastException in case of incompatible type in the array
	 * @throws IllegalArgumentException in case of invalid array length (odd)
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> parseVarArgs(Object... args) throws ClassCastException, IllegalArgumentException
	{
		final LinkedHashMap<K, V> attrs = new LinkedHashMap<>();
		
		if (args.length % 2 != 0) {
			throw new IllegalArgumentException("Odd number of elements in varargs map!");
		}
		
		K key = null;
		for (final Object o : args) {
			if (key == null) {
				if (o == null) throw new RuntimeException("Key cannot be NULL in varargs map.");
				key = (K) o;
			} else {
				attrs.put(key, (V) o);
				key = null;
			}
		}
		
		return attrs;
	}
	
	
	/**
	 * Get if an Object is in array (using equals)
	 *
	 * @param needle checked Object
	 * @param haystack array of Objects
	 * @return is in array
	 */
	public static boolean isInArray(Object needle, Object... haystack)
	{
		for (final Object s : haystack) {
			if (needle.equals(s)) return true;
		}
		return false;
	}
	
	
	/**
	 * Get if string is in array
	 *
	 * @param needle checked string
	 * @param case_sensitive case sensitive comparision
	 * @param haystack array of possible values
	 * @return is in array
	 */
	public static boolean isInArray(String needle, boolean case_sensitive, String... haystack)
	{
		if (case_sensitive) {
			return isInArray(needle, (Object[]) haystack);
		} else {
			for (final String s : haystack) {
				if (needle.equalsIgnoreCase(s)) return true;
			}
			return false;
		}
	}
	
	
	/**
	 * Make enumeration iterable
	 *
	 * @param enumeration enumeration
	 * @return iterable wrapper
	 */
	public static <T> Iterable<T> enumIter(Enumeration<? extends T> enumeration)
	{
		return new IterableEnumerationWrapper<>(enumeration);
	}
	
	/**
	 * Helper class for iterationg over an {@link Enumeration}
	 *
	 * @author Ondřej Hruška (MightyPork)
	 * @param <T> target element type (will be cast)
	 */
	private static class IterableEnumerationWrapper<T> implements Iterable<T> {
		
		private final Enumeration<? extends T> enumeration;
		
		
		/**
		 * @param enumeration the iterated enumeration
		 */
		public IterableEnumerationWrapper(Enumeration<? extends T> enumeration)
		{
			this.enumeration = enumeration;
		}
		
		
		@Override
		public Iterator<T> iterator()
		{
			return new Iterator<T>() {
				
				@Override
				public boolean hasNext()
				{
					return enumeration.hasMoreElements();
				}
				
				
				@Override
				public T next()
				{
					return enumeration.nextElement();
				}
				
				
				@Override
				public void remove()
				{
					throw new UnsupportedOperationException("Operation not supported.");
				}
			};
		}
		
	}
	
	
	/**
	 * Convert a class to string, preserving name and outer class, but excluding
	 * path.
	 *
	 * @param cls the class
	 * @return class name
	 */
	public static String str(Class<?> cls)
	{
		final Alias ln = cls.getAnnotation(Alias.class);
		if (ln != null) {
			return ln.name();
		}
		
		String name = cls.getName();
		
		String sep = "";
		
		if (name.contains("$")) {
			name = name.substring(name.lastIndexOf("$") + 1);
			sep = "$";
		} else {
			name = name.substring(name.lastIndexOf(".") + 1);
			sep = ".";
		}
		
		final Class<?> enclosing = cls.getEnclosingClass();
		
		return (enclosing == null ? "" : Support.str(enclosing) + sep) + name;
	}
	
	
	/**
	 * Convert double to string, remove the mess at the end.
	 *
	 * @param d double
	 * @return string
	 */
	public static String str(Double d)
	{
		String s = d.toString();
		s = s.replace(',', '.');
		s = s.replaceAll("([0-9]+\\.[0-9]+)00+[0-9]+", "$1");
		s = s.replaceAll("0+$", "");
		s = s.replaceAll("\\.$", "");
		return s;
	}
	
	
	/**
	 * Convert float to string, remove the mess at the end.
	 *
	 * @param f float
	 * @return string
	 */
	public static String str(Float f)
	{
		String s = f.toString();
		s = s.replaceAll("([0-9]+\\.[0-9]+)00+[0-9]+", "$1");
		s = s.replaceAll("0+$", "");
		s = s.replaceAll("\\.$", "");
		return s;
	}
	
	
	/**
	 * Convert object to string. If the object overrides toString(), it is
	 * caled. Otherwise it's class name is converted to string.
	 *
	 * @param o object
	 * @return string representation
	 */
	public static String str(Object o)
	{
		if (o == null) return "<null>";
		
		boolean hasToString = false;
		
		try {
			hasToString = (o.getClass().getMethod("toString").getDeclaringClass() != Object.class);
		} catch (final Throwable t) {
			// oh well..
		}
		
		if (hasToString) {
			return o.toString();
		} else {
			
			return str(o.getClass());
		}
	}
}

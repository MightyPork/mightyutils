package mightypork.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


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
}

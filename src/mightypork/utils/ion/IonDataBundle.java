package mightypork.utils.ion;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Ion data bundle - simplified Map with facilities for storing maps and
 * sequences.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class IonDataBundle implements IonBinary {
	
	private final Map<String, Object> backingMap = new HashMap<>();
	
	
	/**
	 * Clear & fill a provided bundle with elements from a bundle value
	 *
	 * @param key key
	 * @param filled bundle to fill
	 */
	public void loadBundle(String key, IonDataBundle filled)
	{
		if (!containsKey(key)) return;
		
		final IonDataBundle ib = get(key, new IonDataBundle());
		
		filled.clear();
		filled.putAll(ib);
	}
	
	
	/**
	 * Check if a key is used in the bundle
	 *
	 * @param key key to check
	 * @return true if this key is used in the bundle
	 */
	public boolean containsKey(Object key)
	{
		return backingMap.containsKey(key);
	}
	
	
	/**
	 * Check if a value is contained in the bundle
	 *
	 * @param value value to check
	 * @return true if this value is contained in the bundle
	 */
	public boolean containsValue(Object value)
	{
		return backingMap.containsValue(value);
	}
	
	
	/**
	 * Get a map value
	 *
	 * @param key key
	 * @return a new Map with elements from that value
	 */
	public <K, V> Map<K, V> getMap(String key)
	{
		return loadMap(key, new LinkedHashMap<K, V>());
	}
	
	
	/**
	 * Clear & fill the provided map with elements from a map value
	 *
	 * @param key key
	 * @param filled Map to fill
	 */
	public <K, V> Map<K, V> loadMap(String key, Map<K, V> filled)
	{
		final IonMapWrapper imw = get(key, null);
		if (imw == null) throw new RuntimeException("No such key: " + key);
		filled.clear();
		imw.fill(filled);
		return filled;
	}
	
	
	/**
	 * Get a sequence value
	 *
	 * @param key key
	 * @return a new Collection with elements from that value
	 */
	public <E> Collection<E> getSequence(String key)
	{
		return loadSequence(key, new ArrayList<E>());
	}
	
	
	/**
	 * Clear & fill the provided Collection with elements from a sequence value
	 *
	 * @param key key
	 * @param filled Collection to fill
	 * @return the filled collection
	 */
	public <E> Collection<E> loadSequence(String key, Collection<E> filled)
	{
		final IonSequenceWrapper isw = get(key, null);
		if (isw == null) throw new RuntimeException("No such key: " + key);
		filled.clear();
		isw.fill(filled);
		
		return filled;
	}
	
	
	/**
	 * Load a bundled object from a bundle value.<br>
	 * The object does not have to be registered.
	 *
	 * @param key key
	 * @param loaded loaded object
	 * @return the loaded object
	 */
	public <T extends IonBundled> T loadBundled(String key, T loaded)
	{
		final IonDataBundle bu = get(key, null);
		if (bu == null) throw new RuntimeException("No such key: " + key);
		
		loaded.load(bu);
		
		return loaded;
	}
	
	
	/**
	 * Save a bundled object to a bundle value.<br>
	 * The object does not have to be registered.
	 *
	 * @param key key
	 * @param saved saved object
	 */
	public void putBundled(String key, IonBundled saved)
	{
		final IonDataBundle bu = new IonDataBundle();
		saved.save(bu);
		put(key, bu);
	}
	
	
	/**
	 * Get value, or fallback (if none found of with bad type).
	 *
	 * @param key
	 * @param fallback value
	 * @return value
	 */
	public <T> T get(String key, T fallback)
	{
		try {
			final T itm = (T) backingMap.get(key);
			if (itm == null) return fallback;
			return itm;
		} catch (final ClassCastException e) {
			return fallback;
		}
	}
	
	
	/**
	 * Get value, or null (if none found of with bad type).
	 *
	 * @param key
	 * @return value
	 */
	public <T> T get(String key)
	{
		return get(key, (T) null);
	}
	
	
	public void put(String key, Object value)
	{
		if (key == null || value == null) return;
		if (!Ion.isRegistered(value)) throw new IllegalArgumentException("Cannot add to bundle, not registered: " + value);
		backingMap.put(key, value);
	}
	
	
	public void put(String key, boolean value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, byte value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, char value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, short value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, int value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, long value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, double value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, float value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, String value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, boolean[] value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, char[] value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, short[] value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, int[] value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, long[] value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, double[] value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, float[] value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, String[] value)
	{
		backingMap.put(key, value);
	}
	
	
	public void put(String key, Object[] value)
	{
		backingMap.put(key, value);
	}
	
	
	/**
	 * Put a sequence to the bundle.
	 *
	 * @param key key
	 * @param c value (Collection)
	 */
	@SuppressWarnings("rawtypes")
	public void putSequence(String key, Collection c)
	{
		backingMap.put(key, new IonSequenceWrapper(c));
	}
	
	
	/**
	 * Put a map to the bundle.
	 *
	 * @param key key
	 * @param m value (Map)
	 */
	@SuppressWarnings("rawtypes")
	public void putMap(String key, Map m)
	{
		backingMap.put(key, new IonMapWrapper(m));
	}
	
	
	@Override
	public void load(IonInput in) throws IOException
	{
		in.readMap(backingMap);
	}
	
	
	@Override
	public void save(IonOutput out) throws IOException
	{
		out.writeMap(backingMap);
	}
	
	
	/**
	 * Get number of elements in the bundle
	 *
	 * @return size
	 */
	public int size()
	{
		return backingMap.size();
	}
	
	
	/**
	 * Check whether the bundle is empty
	 *
	 * @return true if empty
	 */
	public boolean isEmpty()
	{
		return backingMap.isEmpty();
	}
	
	
	/**
	 * Remove all elements
	 */
	public void clear()
	{
		backingMap.clear();
	}
	
	
	/**
	 * Remove a value by key
	 *
	 * @param key key to remove
	 * @return the removed object
	 */
	public Object remove(Object key)
	{
		return backingMap.remove(key);
	}
	
	
	/**
	 * Put all from another bundle
	 *
	 * @param anotherBundle another bundle
	 */
	public void putAll(IonDataBundle anotherBundle)
	{
		backingMap.putAll(anotherBundle.backingMap);
	}
	
	
	@Override
	public String toString()
	{
		return backingMap.toString();
	}
	
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((backingMap == null) ? 0 : backingMap.hashCode());
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof IonDataBundle)) return false;
		final IonDataBundle other = (IonDataBundle) obj;
		if (backingMap == null) {
			if (other.backingMap != null) return false;
		} else if (!backingMap.equals(other.backingMap)) return false;
		return true;
	}
}

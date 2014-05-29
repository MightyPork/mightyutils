package mightypork.utils;


import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Map sorting utils
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class MapSort {
	
	/**
	 * Sort a map by keys, maintaining key-value pairs.
	 * 
	 * @param map map to be sorted
	 * @param comparator a comparator, or null for natural ordering
	 * @return linked hash map with sorted entries
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <K extends Comparable, V extends Comparable> Map<K, V> sortByKeys(Map<K, V> map, final Comparator<K> comparator)
	{
		final List<K> keys = new LinkedList<>(map.keySet());
		
		if (comparator == null) {
			Collections.sort(keys);
		} else {
			Collections.sort(keys, comparator);
		}
		
		// LinkedHashMap will keep the keys in the order they are inserted
		// which is currently sorted on natural ordering
		final Map<K, V> sortedMap = new LinkedHashMap<>();
		for (final K key : keys) {
			sortedMap.put(key, map.get(key));
		}
		
		return sortedMap;
	}
	
	
	/**
	 * Sort a map by values, maintaining key-value pairs.
	 * 
	 * @param map map to be sorted
	 * @param comparator a comparator, or null for natural ordering
	 * @return linked hash map with sorted entries
	 */
	@SuppressWarnings("rawtypes")
	public static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(Map<K, V> map, final Comparator<V> comparator)
	{
		final List<Map.Entry<K, V>> entries = new LinkedList<>(map.entrySet());
		
		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
			
			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2)
			{
				if (comparator == null) return o1.getValue().compareTo(o2.getValue());
				return comparator.compare(o1.getValue(), o2.getValue());
			}
		});
		
		// LinkedHashMap will keep the keys in the order they are inserted
		// which is currently sorted on natural ordering
		final Map<K, V> sortedMap = new LinkedHashMap<>();
		
		for (final Map.Entry<K, V> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		
		return sortedMap;
	}
}

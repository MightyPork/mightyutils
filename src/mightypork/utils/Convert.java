package mightypork.utils;


/**
 * Utility for converting Object to data types; Can also convert strings to data
 * types.
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class Convert {
	
	/**
	 * Get INTEGER
	 * 
	 * @param o object
	 * @param def default value
	 * @return integer
	 */
	public static int toInteger(Object o, Integer def)
	{
		try {
			if (o == null) return def;
			if (o instanceof Number) return ((Number) o).intValue();
			if (o instanceof String) return (int) Math.round(Double.parseDouble((String) o));
			if (o instanceof Boolean) return ((Boolean) o) ? 1 : 0;
		} catch (final NumberFormatException e) {}
		return def;
	}
	
	
	/**
	 * Get DOUBLE
	 * 
	 * @param o object
	 * @param def default value
	 * @return double
	 */
	public static double toDouble(Object o, Double def)
	{
		try {
			if (o == null) return def;
			if (o instanceof Number) return ((Number) o).doubleValue();
			if (o instanceof String) return Double.parseDouble((String) o);
			if (o instanceof Boolean) return ((Boolean) o) ? 1 : 0;
		} catch (final NumberFormatException e) {}
		return def;
	}
	
	
	/**
	 * Get FLOAT
	 * 
	 * @param o object
	 * @param def default value
	 * @return float
	 */
	public static double toFloat(Object o, Float def)
	{
		try {
			if (o == null) return def;
			if (o instanceof Number) return ((Number) o).floatValue();
		} catch (final NumberFormatException e) {}
		return def;
	}
	
	
	/**
	 * Get BOOLEAN
	 * 
	 * @param o object
	 * @param def default value
	 * @return boolean
	 */
	public static boolean toBoolean(Object o, Boolean def)
	{
		if (o == null) return def;
		if (o instanceof Boolean) return ((Boolean) o).booleanValue();
		if (o instanceof Number) return ((Number) o).intValue() != 0;
		
		if (o instanceof String) {
			final String s = ((String) o).trim().toLowerCase();
			if (s.equals("0")) return false;
			if (s.equals("1")) return true;
			try {
				final double n = Double.parseDouble(s);
				return n != 0;
			} catch (final NumberFormatException e) {}
			
			if (s.equals("true")) return true;
			if (s.equals("yes")) return true;
			if (s.equals("y")) return true;
			if (s.equals("a")) return true;
			if (s.equals("enabled")) return true;
			
			if (s.equals("false")) return false;
			if (s.equals("no")) return false;
			if (s.equals("n")) return false;
			if (s.equals("disabled")) return true;
		}
		
		return def;
	}
	
	
	/**
	 * Get STRING
	 * 
	 * @param o object
	 * @param def default value
	 * @return String
	 */
	public static String toString(Object o, String def)
	{
		if (o == null) return def;
		if (o instanceof String) return ((String) o);
		
		if (o instanceof Float) return Support.str((float) o);
		
		if (o instanceof Double) return Support.str((double) o);
		
		if (o instanceof Class<?>) {
			return Support.str(o);
		}
		
		return o.toString();
	}
	
	
	/**
	 * Get INTEGER
	 * 
	 * @param o object
	 * @return integer
	 */
	public static int toInteger(Object o)
	{
		return toInteger(o, 0);
	}
	
	
	/**
	 * Get DOUBLE
	 * 
	 * @param o object
	 * @return double
	 */
	public static double toDouble(Object o)
	{
		return toDouble(o, 0d);
	}
	
	
	/**
	 * Get FLOAT
	 * 
	 * @param o object
	 * @return float
	 */
	public static double toFloat(Object o)
	{
		return toFloat(o, 0f);
	}
	
	
	/**
	 * Get BOOLEAN
	 * 
	 * @param o object
	 * @return boolean
	 */
	public static boolean toBoolean(Object o)
	{
		return toBoolean(o, false);
	}
	
	
	/**
	 * Get STRING
	 * 
	 * @param o object
	 * @return String
	 */
	public static String toString(Object o)
	{
		return toString(o, "");
	}
}

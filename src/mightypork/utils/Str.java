package mightypork.utils;


import mightypork.utils.annotations.Alias;
import mightypork.utils.math.AlignX;


/**
 * General purpose string utilities
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class Str {
	
	public static String fromLastDot(String s)
	{
		return fromLast(s, '.');
	}
	
	
	public static String toLastDot(String s)
	{
		return toLast(s, '.');
	}
	
	
	public static String fromLast(String s, char c)
	{
		if (s == null) return null;
		if (s.lastIndexOf(c) == -1) return "";
		return s.substring(s.lastIndexOf(c) + 1, s.length());
	}
	
	
	public static String toLast(String s, char c)
	{
		if (s == null) return null;
		if (s.lastIndexOf(c) == -1) return s;
		return s.substring(0, s.lastIndexOf(c));
	}
	
	
	public static String fromFirst(String s, char c)
	{
		if (s == null) return null;
		if (s.indexOf(c) == -1) return "";
		return s.substring(s.indexOf(c) + 1, s.length());
	}
	
	
	public static String toFirst(String s, char c)
	{
		if (s == null) return null;
		if (s.indexOf(c) == -1) return s;
		return s.substring(0, s.indexOf(c));
	}


	public static String fromEnd(String s, int chars)
	{
		return s.substring(s.length() - chars, s.length());
	}


	public static String fromStart(String s, int chars)
	{
		return s.substring(0, chars);
	}
	
	
	public static String pad(String s, int length)
	{
		return pad(s, length, AlignX.LEFT);
	}
	
	
	public static String pad(String s, int length, AlignX align)
	{
		return pad(s, length, align, ' ');
	}
	
	
	public static String pad(String s, int length, AlignX align, char fill)
	{
		final String filling = repeat("" + fill, length);
		
		switch (align) {
			case LEFT:
				s += filling;
				return fromStart(s, length);

			case RIGHT:
				s += filling;
				return fromEnd(s, length);

			case CENTER:
				
				if (s.length() >= length) return s;
				
				s = filling + s + filling;
				
				final int cut = (int) (s.length() / 2D - length / 2D);
				return s.substring(cut, s.length() - cut);
		}

		throw new IllegalArgumentException("Impossible error.");
	}
	
	
	/**
	 * Repeat a string
	 *
	 * @param repeated string
	 * @param count
	 * @return output
	 */
	public static String repeat(String repeated, int count)
	{
		String s = "";
		for (int i = 0; i < count; i++)
			s += repeated;
		return s;
	}
	
	
	public static boolean isValidFilenameChar(char ch)
	{
		return isValidFilenameString(Character.toString(ch));
	}
	
	
	public static boolean isValidFilenameString(String filename)
	{
		return filename.matches("[a-zA-Z0-9 +\\-.,_%@#!]+");
	}
	
	
	public static String ellipsisStart(String orig, int length)
	{
		if (orig.length() > length) {
			orig = "\u2026" + orig.substring(length, orig.length());
		}
		return orig;
	}
	
	
	public static String ellipsisEnd(String orig, int length)
	{
		if (orig.length() > length) {
			orig = orig.substring(0, length - 1) + "\u2026";
		}
		return orig;
	}
	
	
	/**
	 * Convert a class to string, preserving name and outer class, but excluding
	 * path.
	 *
	 * @param cls the class
	 * @return class name
	 */
	public static String val(Class<?> cls)
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

		return (enclosing == null ? "" : Str.val(enclosing) + sep) + name;
	}
	
	
	/**
	 * Convert double to string, remove the mess at the end.
	 *
	 * @param d double
	 * @return string
	 */
	public static String val(Double d)
	{
		String s = d.toString();
		s = s.replace(',', '.');
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
	public static String val(Object o)
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

			return val(o.getClass());
		}
	}
}

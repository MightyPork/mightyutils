package mightypork.utils.string;


/**
 * General purpose string utilities
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class StringUtil {

	public static String fromLastDot(String s)
	{
		return fromLastChar(s, '.');
	}


	public static String toLastDot(String s)
	{
		return toLastChar(s, '.');
	}


	public static String fromLastChar(String s, char c)
	{
		if (s == null) return null;
		return s.substring(s.lastIndexOf(c) + 1, s.length());
	}


	public static String toLastChar(String s, char c)
	{
		if (s == null) return null;
		if (s.lastIndexOf(c) == -1) return s;
		return s.substring(0, s.lastIndexOf(c));
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
}

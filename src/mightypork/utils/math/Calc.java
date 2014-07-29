package mightypork.utils.math;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mightypork.utils.math.angles.Deg;
import mightypork.utils.math.angles.Rad;
import mightypork.utils.math.animation.Easing;
import mightypork.utils.math.constraints.vect.Vect;


/**
 * Math utils
 *
 * @author Ondřej Hruška (MightyPork)
 */
public final class Calc {
	
	private Calc()
	{
		// not instantiable
	}
	
	/** Square root of two */
	public static final double SQ2 = 1.41421356237;
	
	
	/**
	 * Get distance from 2D line to 2D point [X,Y]
	 *
	 * @param lineDirVec line directional vector
	 * @param linePoint point of line
	 * @param point point coordinate
	 * @return distance
	 */
	public static double linePointDist(Vect lineDirVec, Vect linePoint, Vect point)
	{
		// line point L[lx,ly]
		final double lx = linePoint.x();
		final double ly = linePoint.y();
		
		// line equation ax+by+c=0
		final double a = -lineDirVec.y();
		final double b = lineDirVec.x();
		final double c = -a * lx - b * ly;
		
		// checked point P[x,y]
		final double x = point.x();
		final double y = point.y();
		
		// distance
		return Math.abs(a * x + b * y + c) / Math.sqrt(a * a + b * b);
	}
	
	public static final Random rand = new Random();
	
	
	public static double sphereSurface(double radius)
	{
		return 4D * Math.PI * square(radius);
	}
	
	
	public static double sphereVolume(double radius)
	{
		return (4D / 3D) * Math.PI * cube(radius);
	}
	
	
	public static double sphereRadius(double volume)
	{
		return Math.cbrt((3D * volume) / (4 * Math.PI));
	}
	
	
	public static double circleSurface(double radius)
	{
		return Math.PI * square(radius);
	}
	
	
	public static double circleRadius(double surface)
	{
		return Math.sqrt(surface / Math.PI);
	}
	
	
	/**
	 * Safe equals that works with nulls
	 *
	 * @param a
	 * @param b
	 * @return are equal
	 */
	public static boolean areEqual(Object a, Object b)
	{
		return a == null ? b == null : a.equals(b);
	}
	
	
	/**
	 * Clamp integer
	 *
	 * @param number
	 * @param min
	 * @param max
	 * @return result
	 */
	public static int clamp(int number, int min, int max)
	{
		return number < min ? min : number > max ? max : number;
	}
	
	
	/**
	 * Clamp double
	 *
	 * @param number
	 * @param min
	 * @param max
	 * @return result
	 */
	public static double clamp(double number, double min, double max)
	{
		return number < min ? min : number > max ? max : number;
	}
	
	
	public static boolean isInRange(double number, double left, double right)
	{
		return number >= left && number <= right;
	}
	
	
	/**
	 * Get number from A to B at delta time (A -> B)
	 *
	 * @param from
	 * @param to
	 * @param elapsed progress ratio 0..1
	 * @param easing
	 * @return result
	 */
	public static double interpolate(double from, double to, double elapsed, Easing easing)
	{
		return from + (to - from) * easing.get(elapsed);
	}
	
	
	/**
	 * Get angle [degrees] from A to B at delta time (tween A to B)
	 *
	 * @param from
	 * @param to
	 * @param elapsed progress ratio 0..1
	 * @param easing
	 * @return result
	 */
	public static double interpolateDeg(double from, double to, double elapsed, Easing easing)
	{
		return Deg.norm(from - Deg.delta(to, from) * easing.get(elapsed));
	}
	
	
	/**
	 * Get angle [radians] from A to B at delta time (tween A to B)
	 *
	 * @param from
	 * @param to
	 * @param elapsed progress ratio 0..1
	 * @param easing
	 * @return result
	 */
	public static double interpolateRad(double from, double to, double elapsed, Easing easing)
	{
		return Rad.norm(from - Rad.delta(to, from) * easing.get(elapsed));
	}
	
	
	public static double max(double... numbers)
	{
		double highest = numbers[0];
		for (final double num : numbers) {
			if (num > highest) highest = num;
		}
		return highest;
	}
	
	
	public static int max(int... numbers)
	{
		int highest = numbers[0];
		for (final int num : numbers) {
			if (num > highest) highest = num;
		}
		return highest;
	}
	
	
	public static double min(double... numbers)
	{
		double lowest = numbers[0];
		for (final double num : numbers) {
			if (num < lowest) lowest = num;
		}
		return lowest;
	}
	
	
	public static int min(int... numbers)
	{
		int lowest = numbers[0];
		for (final int num : numbers) {
			if (num < lowest) lowest = num;
		}
		return lowest;
	}
	
	
	/**
	 * Split comma separated list of integers.
	 *
	 * @param list String containing the list.
	 * @param delimiter delimiter character
	 * @return array of integers or null.
	 */
	public static List<Integer> parseIntList(String list, char delimiter)
	{
		if (list == null) {
			return null;
		}
		
		final String[] parts = list.split(Character.toString(delimiter));
		
		final ArrayList<Integer> intList = new ArrayList<>();
		
		for (final String part : parts) {
			try {
				intList.add(Integer.parseInt(part.trim()));
			} catch (final NumberFormatException e) {}
		}
		
		return intList;
	}
	
	
	/**
	 * Pick random element from a given list.
	 *
	 * @param list list of choices
	 * @return picked element
	 */
	public static <T> T pick(List<T> list)
	{
		return pick(rand, list);
	}
	
	
	/**
	 * Pick random element from a given list.
	 *
	 * @param rand RNG
	 * @param list list of choices
	 * @return picked element
	 */
	public static <T> T pick(Random rand, List<T> list)
	{
		if (list.size() == 0) return null;
		return list.get(rand.nextInt(list.size()));
	}
	
	
	/**
	 * Take a square
	 *
	 * @param a value
	 * @return value squared
	 */
	public static double square(double a)
	{
		return a * a;
	}
	
	
	/**
	 * Take a cube
	 *
	 * @param a value
	 * @return value cubed
	 */
	public static double cube(double a)
	{
		return a * a * a;
	}
	
	
	/**
	 * @param d number
	 * @return fractional part
	 */
	public static double frag(double d)
	{
		return d - Math.floor(d);
	}
	
	
	/**
	 * Make sure value is within array length.
	 *
	 * @param index tested index
	 * @param length array length
	 * @throws IndexOutOfBoundsException if the index is not in range.
	 */
	public static void assertValidIndex(int index, int length)
	{
		if (!isInRange(index, 0, length - 1)) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	
	/**
	 * Get distance of two coordinates in 2D plane
	 *
	 * @param x1 first coordinate X
	 * @param y1 first coordinate y
	 * @param x2 second coordinate X
	 * @param y2 second coordinate Y
	 * @return distance
	 */
	public static double dist(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	
	public static int randInt(Random rand, int low, int high)
	{
		final int range = Math.abs(high - low) + 1;
		return low + rand.nextInt(range);
	}
	
	
	public static int randInt(int low, int high)
	{
		return randInt(rand, low, high);
	}
	
	
	/**
	 * Get ordinal version of numbers (1 = 1st, 5 = 5th etc.)
	 *
	 * @param number number
	 * @return ordinal, string
	 */
	public static String ordinal(int number)
	{
		if (number % 100 < 4 || number % 100 > 13) {
			if (number % 10 == 1) return number + "st";
			if (number % 10 == 2) return number + "nd";
			if (number % 10 == 3) return number + "rd";
		}
		return number + "th";
	}
	
	
	/**
	 * Format number with thousands separated.
	 *
	 * @param number number
	 * @param thousandSep
	 * @return string
	 */
	public static String separateThousands(long number, char thousandSep)
	{
		final String num = String.valueOf(number);
		final String dot = String.valueOf(thousandSep);
		String out = "";
		
		int cnt = 1;
		for (int i = num.length() - 1; i >= 0; i--) {
			out = num.charAt(i) + out;
			if (cnt % 3 == 0 && i > 0) out = dot + out;
			cnt++;
		}
		
		return out;
	}
	
	
	public static int countBits(byte b)
	{
		int c = 0;
		for (int i = 0; i < 8; i++) {
			c += (b >> i) & 1;
		}
		return c;
	}
}

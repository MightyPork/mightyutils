package mightypork.utils.math.angles;


/**
 * Angle calculations for degrees.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class Deg {
	
	/** 180° in degrees */
	public static final double a180 = 180;
	/** 270° in degrees */
	public static final double a270 = 270;
	/** 360° in degrees */
	public static final double a360 = 360;
	/** 45° in degrees */
	public static final double a45 = 45;
	/** 90° in degrees */
	public static final double a90 = 90;
	
	
	/**
	 * Subtract two angles alpha - beta
	 *
	 * @param alpha first angle
	 * @param beta second angle
	 * @return (alpha - beta) in degrees
	 */
	public static double delta(double alpha, double beta)
	{
		return Angles.delta(alpha, beta, a360);
	}
	
	
	/**
	 * Difference of two angles (absolute value of delta)
	 *
	 * @param alpha first angle
	 * @param beta second angle
	 * @return difference in radians
	 */
	public static double diff(double alpha, double beta)
	{
		return Angles.diff(alpha, beta, a360);
	}
	
	
	/**
	 * Cosinus in degrees
	 *
	 * @param deg angle in degrees
	 * @return cosinus
	 */
	public static double cos(double deg)
	{
		return Math.cos(toRad(deg));
	}
	
	
	/**
	 * Sinus in degrees
	 *
	 * @param deg angle in degrees
	 * @return sinus
	 */
	public static double sin(double deg)
	{
		return Math.sin(toRad(deg));
	}
	
	
	/**
	 * Tangents in degrees
	 *
	 * @param deg angle in degrees
	 * @return tangents
	 */
	public static double tan(double deg)
	{
		return Math.tan(toRad(deg));
	}
	
	
	/**
	 * Angle normalized to 0-360 range
	 *
	 * @param angle angle to normalize
	 * @return normalized angle
	 */
	public static double norm(double angle)
	{
		return Angles.norm(angle, a360);
	}
	
	
	/**
	 * Convert to radians
	 *
	 * @param deg degrees
	 * @return radians
	 */
	public static double toRad(double deg)
	{
		return Math.toRadians(deg);
	}
	
	
	/**
	 * Round angle to 0,45,90,135...
	 *
	 * @param deg angle in deg. to round
	 * @param increment rounding increment (45 - round to 0,45,90...)
	 * @return rounded
	 */
	public static int roundToIncrement(double deg, double increment)
	{
		final double half = increment / 2d;
		deg += half;
		deg = norm(deg);
		final int times = (int) Math.floor(deg / increment);
		double a = times * increment;
		if (a == 360) a = 0;
		return (int) Math.round(a);
	}
	
	
	/**
	 * Round angle to 0,15,30,45,60,75,90...
	 *
	 * @param deg angle in deg to round
	 * @return rounded
	 */
	public static int roundTo15(double deg)
	{
		return roundToIncrement(deg, 15);
	}
	
	
	/**
	 * Round angle to 0,45,90,135...
	 *
	 * @param deg angle in deg. to round
	 * @return rounded
	 */
	public static int roundTo45(double deg)
	{
		return roundToIncrement(deg, 45);
	}
	
	
	/**
	 * Round angle to 0,90,180,270
	 *
	 * @param deg angle in deg. to round
	 * @return rounded
	 */
	public static int roundTo90(double deg)
	{
		return roundToIncrement(deg, 90);
	}
}

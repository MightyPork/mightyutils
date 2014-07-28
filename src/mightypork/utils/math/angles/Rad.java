package mightypork.utils.math.angles;


/**
 * Angle calculations for radians.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class Rad {

	/** 180° in radians */
	public static final double a180 = Math.PI;
	/** 270° in radians */
	public static final double a270 = Math.PI * 1.5D;
	/** 360° in radians */
	public static final double a360 = Math.PI * 2D;
	/** 45° in radians */
	public static final double a45 = Math.PI / 4D;
	/** 90° in radians */
	public static final double a90 = Math.PI / 2D;


	/**
	 * Subtract two angles alpha - beta
	 *
	 * @param alpha first angle
	 * @param beta second angle
	 * @return (alpha - beta) in radians
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
		return Angles.delta(alpha, beta, a360);
	}


	/**
	 * Cos
	 *
	 * @param rad angle in rads
	 * @return cos
	 */
	public static double cos(double rad)
	{
		return Math.cos(rad);
	}


	/**
	 * Sin
	 *
	 * @param rad angle in rads
	 * @return sin
	 */
	public static double sin(double rad)
	{
		return Math.sin(rad);
	}


	/**
	 * Tan
	 *
	 * @param rad angle in rads
	 * @return tan
	 */
	public static double tan(double rad)
	{
		return Math.tan(rad);
	}


	/**
	 * Angle normalized to 0-2*PI range
	 *
	 * @param angle angle to normalize
	 * @return normalized angle
	 */
	public static double norm(double angle)
	{
		return Angles.norm(angle, a360);
	}


	/**
	 * Convert to degrees
	 *
	 * @param rad radians
	 * @return degrees
	 */
	public static double toDeg(double rad)
	{
		return Math.toDegrees(rad);
	}
}

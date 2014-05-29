package mightypork.utils.math;


import mightypork.utils.math.constraints.vect.Vect;


/**
 * Polar coordinate
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class Polar {
	
	/** angle in radians */
	private double angle = 0;
	
	/** distance in units */
	private double radius = 0;
	
	private Vect coord = null;
	
	
	/**
	 * Create a polar
	 * 
	 * @param angle angle in RAD
	 * @param distance distance from origin
	 */
	public Polar(double angle, double distance)
	{
		this(angle, false, distance);
	}
	
	
	/**
	 * Create a polar
	 * 
	 * @param angle angle
	 * @param deg angle is in DEG
	 * @param distance radius
	 */
	public Polar(double angle, boolean deg, double distance)
	{
		this.radius = distance;
		this.angle = deg ? Math.toRadians(angle) : angle;
	}
	
	
	/**
	 * @return angle in RAD
	 */
	public double getAngle()
	{
		return angle;
	}
	
	
	/**
	 * @return angle in DEG
	 */
	public double getAngleDeg()
	{
		return Math.toDegrees(angle);
	}
	
	
	/**
	 * @param angle angle in RAD
	 */
	public void setAngle(double angle)
	{
		this.angle = angle;
	}
	
	
	/**
	 * @param angle angle in DEG
	 */
	public void setAngleDeg(double angle)
	{
		this.angle = Math.toRadians(angle);
	}
	
	
	/**
	 * @return radius
	 */
	public double getRadius()
	{
		return radius;
	}
	
	
	/**
	 * @param r radius
	 */
	public void setRadius(double r)
	{
		this.radius = r;
	}
	
	
	/**
	 * Make polar from coord
	 * 
	 * @param coord coord
	 * @return polar
	 */
	public static Polar fromCoord(Vect coord)
	{
		return Polar.fromCoord(coord.x(), coord.y());
		
	}
	
	
	/**
	 * Make polar from coords
	 * 
	 * @param x x coord
	 * @param y y coord
	 * @return polar
	 */
	public static Polar fromCoord(double x, double y)
	{
		final double a = Math.atan2(y, x);
		final double r = Math.sqrt(x * x + y * y);
		
		return new Polar(a, r);
	}
	
	
	/**
	 * Get coord from polar
	 * 
	 * @return coord
	 */
	public Vect toCoord()
	{
		// lazy init
		if (coord == null) coord = new Vect() {
			
			@Override
			public double x()
			{
				return radius * Math.cos(angle);
			}
			
			
			@Override
			public double y()
			{
				return radius * Math.sin(angle);
			}
		};
		
		return coord;
	}
	
	
	@Override
	public String toString()
	{
		return "Polar(" + angle + "rad, " + radius + ")";
	}
	
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(angle);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(radius);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Polar)) return false;
		final Polar other = (Polar) obj;
		if (Double.doubleToLongBits(angle) != Double.doubleToLongBits(other.angle)) return false;
		if (Double.doubleToLongBits(radius) != Double.doubleToLongBits(other.radius)) return false;
		return true;
	}
}

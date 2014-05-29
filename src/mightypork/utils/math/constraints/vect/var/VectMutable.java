package mightypork.utils.math.constraints.vect.var;


import mightypork.utils.math.constraints.vect.Vect;


/**
 * Mutable coord
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class VectMutable extends Vect {
	
	/**
	 * Set all to zeros.
	 */
	public void reset()
	{
		setTo(0, 0, 0);
	}
	
	
	/**
	 * Set coordinates to match other coord.
	 * 
	 * @param copied coord whose coordinates are used
	 */
	public void setTo(Vect copied)
	{
		setTo(copied.x(), copied.y(), copied.z());
	}
	
	
	/**
	 * Set 2D coordinates.<br>
	 * Z is unchanged.
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void setTo(double x, double y)
	{
		setX(x);
		setY(y);
	}
	
	
	/**
	 * Set coordinates.
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 */
	public abstract void setTo(double x, double y, double z);
	
	
	/**
	 * Set X coordinate.
	 * 
	 * @param x x coordinate
	 */
	public abstract void setX(double x);
	
	
	/**
	 * Set Y coordinate.
	 * 
	 * @param y y coordinate
	 */
	public abstract void setY(double y);
	
	
	/**
	 * Set Z coordinate.
	 * 
	 * @param z z coordinate
	 */
	public abstract void setZ(double z);
}

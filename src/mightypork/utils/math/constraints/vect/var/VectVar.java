package mightypork.utils.math.constraints.vect.var;


/**
 * Mutable coordinate.<br>
 * All Vec methods (except copy) alter data values and return this instance.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class VectVar extends VectMutable {
	
	private double x, y, z;
	
	
	/**
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 */
	public VectVar(double x, double y, double z)
	{
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	@Override
	public double x()
	{
		return x;
	}
	
	
	@Override
	public double y()
	{
		return y;
	}
	
	
	@Override
	public double z()
	{
		return z;
	}
	
	
	@Override
	public void setTo(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	@Override
	public void setX(double x)
	{
		this.x = x;
	}
	
	
	@Override
	public void setY(double y)
	{
		this.y = y;
	}
	
	
	@Override
	public void setZ(double z)
	{
		this.z = z;
	}
}

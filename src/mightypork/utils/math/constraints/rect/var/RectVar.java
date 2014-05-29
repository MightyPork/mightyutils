package mightypork.utils.math.constraints.rect.var;


import mightypork.utils.math.constraints.vect.Vect;
import mightypork.utils.math.constraints.vect.var.VectVar;


public class RectVar extends RectMutable {
	
	final VectVar pos = Vect.makeVar();
	final VectVar size = Vect.makeVar();
	
	
	/**
	 * Create at given origin, with given size.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public RectVar(double x, double y, double width, double height)
	{
		this.pos.setTo(x, y);
		this.size.setTo(width, height);
	}
	
	
	@Override
	public Vect origin()
	{
		return pos;
	}
	
	
	@Override
	public Vect size()
	{
		return size;
	}
	
	
	@Override
	public void setOrigin(double x, double y)
	{
		this.pos.setTo(x, y);
	}
	
	
	@Override
	public void setSize(double x, double y)
	{
		this.size.setTo(x, y);
	}
}

package mightypork.utils.math.constraints.vect.proxy;


import mightypork.utils.math.constraints.vect.Vect;


/**
 * Vect proxy with abstract method for plugging in / generating coordinates
 * dynamically.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class VectAdapter extends Vect {
	
	/**
	 * @return the proxied coord
	 */
	protected abstract Vect getSource();
	
	
	@Override
	public double x()
	{
		return getSource().x();
	}
	
	
	@Override
	public double y()
	{
		return getSource().y();
	}
	
	
	@Override
	public double z()
	{
		return getSource().z();
	}
	
}

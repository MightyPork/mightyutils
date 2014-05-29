package mightypork.utils.math.constraints.vect.proxy;


import mightypork.utils.math.constraints.vect.PluggableVectBound;
import mightypork.utils.math.constraints.vect.Vect;
import mightypork.utils.math.constraints.vect.VectBound;


/**
 * Pluggable vect proxy
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class VectProxy extends VectAdapter implements PluggableVectBound {
	
	private VectBound backing = null;
	
	
	public VectProxy()
	{
	}
	
	
	public VectProxy(VectBound proxied)
	{
		backing = proxied;
	}
	
	
	@Override
	public void setVect(VectBound proxied)
	{
		this.backing = proxied;
	}
	
	
	@Override
	protected Vect getSource()
	{
		return backing.getVect();
	}
	
}

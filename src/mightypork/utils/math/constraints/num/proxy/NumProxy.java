package mightypork.utils.math.constraints.num.proxy;


import mightypork.utils.math.constraints.num.Num;
import mightypork.utils.math.constraints.num.NumBound;
import mightypork.utils.math.constraints.num.PluggableNumBound;


/**
 * Pluggable num proxy
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class NumProxy extends NumAdapter implements PluggableNumBound {
	
	private NumBound backing = null;
	
	
	public NumProxy()
	{
	}
	
	
	public NumProxy(NumBound bound)
	{
		backing = bound;
	}
	
	
	@Override
	public void setNum(NumBound num)
	{
		this.backing = num;
	}
	
	
	@Override
	protected Num getSource()
	{
		return backing.getNum();
	}
	
}

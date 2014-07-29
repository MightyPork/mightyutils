package mightypork.utils.math.constraints.num.batch;


import java.util.ArrayList;
import java.util.List;

import mightypork.utils.math.constraints.num.Num;


/**
 * Expandable multiplication of multiple numbers
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class NumMul extends Num {
	
	private final List<Num> factors = new ArrayList<>();
	
	
	@Override
	public double value()
	{
		double v = 1;
		for (final Num n : factors) {
			if (n != null) v *= n.value();
		}
		return v;
	}
	
	
	/**
	 * Add a number to the multiplication
	 *
	 * @param factor added number
	 */
	public void addFactor(Num factor)
	{
		factors.add(factor);
	}
	
	
	/**
	 * Add a number to the multiplication
	 *
	 * @param factor added number
	 */
	public void addFactor(double factor)
	{
		factors.add(Num.make(factor));
	}
}

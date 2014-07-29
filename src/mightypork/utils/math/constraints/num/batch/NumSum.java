package mightypork.utils.math.constraints.num.batch;


import java.util.ArrayList;
import java.util.List;

import mightypork.utils.math.constraints.num.Num;


/**
 * Expandable sum of multiple numbers
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class NumSum extends Num {
	
	private final List<Num> summands = new ArrayList<>();
	
	
	@Override
	public double value()
	{
		double v = 0;
		for (final Num n : summands) {
			if (n != null) v += n.value();
		}
		return v;
	}
	
	
	/**
	 * Add a number to the sum
	 *
	 * @param summand added number
	 */
	public void addSummand(Num summand)
	{
		summands.add(summand);
	}
	
	
	/**
	 * Add a number to the sum
	 *
	 * @param summand added number
	 */
	public void addSummand(double summand)
	{
		summands.add(Num.make(summand));
	}
}

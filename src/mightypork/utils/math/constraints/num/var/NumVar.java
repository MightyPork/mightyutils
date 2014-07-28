package mightypork.utils.math.constraints.num.var;


import mightypork.utils.math.constraints.num.Num;


/**
 * Mutable numeric variable.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class NumVar extends NumMutable {

	private double value;


	public NumVar(Num value)
	{
		this(value.value());
	}


	public NumVar(double value)
	{
		this.value = value;
	}


	@Override
	public double value()
	{
		return value;
	}


	@Override
	public void setTo(double value)
	{
		this.value = value;
	}

}

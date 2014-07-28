package mightypork.utils.math.constraints.num.var;


import mightypork.utils.math.constraints.num.Num;


/**
 * Mutable numeric variable
 *
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class NumMutable extends Num {

	/**
	 * Assign a value
	 *
	 * @param value new value
	 */
	public abstract void setTo(double value);


	/**
	 * Assign a value
	 *
	 * @param value new value
	 */
	public void setTo(Num value)
	{
		setTo(value.value());
	}


	/**
	 * Set to zero
	 */
	public void reset()
	{
		setTo(0);
	}

}

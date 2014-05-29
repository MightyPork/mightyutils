package mightypork.utils.math.animation;


import mightypork.utils.math.Calc;
import mightypork.utils.math.angles.Deg;


/**
 * Degree animator
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class NumAnimatedDeg extends NumAnimated {
	
	public NumAnimatedDeg(NumAnimated other)
	{
		super(other);
	}
	
	
	public NumAnimatedDeg(double value)
	{
		super(value);
	}
	
	
	public NumAnimatedDeg(double value, Easing easing)
	{
		super(value, easing);
	}
	
	
	@Override
	public double value()
	{
		if (duration == 0) return Deg.norm(to);
		return Calc.interpolateDeg(from, to, (elapsedTime / duration), easingCurrent);
	}
	
	
	@Override
	protected double getProgressFromValue(double value)
	{
		final double whole = Deg.diff(from, to);
		if (Deg.diff(value, from) < whole && Deg.diff(value, to) < whole) {
			final double partial = Deg.diff(from, value);
			return partial / whole;
		}
		
		return 0;
	}
}

package mightypork.utils.math;


import java.util.Random;


/**
 * Numeric range, able to generate random numbers and give min/max values.
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class Range {
	
	public static Range make(double low, double high)
	{
		return new Range(low, high);
	}
	
	private double min = 0;
	private double max = 1;
	
	
	/**
	 * Implicit range constructor 0-1
	 */
	public Range() {
	}
	
	
	/**
	 * Create new range
	 * 
	 * @param min min number
	 * @param max max number
	 */
	public Range(double min, double max) {
		this.min = min;
		this.max = max;
		norm();
	}
	
	
	/**
	 * Create new range
	 * 
	 * @param minmax min = max number
	 */
	public Range(double minmax) {
		this.min = minmax;
		this.max = minmax;
	}
	
	
	public static Range fromString(String string)
	{
		try {
			String s = string.trim();
			
			// drop whitespace
			s = s.replaceAll("\\s", "");
			
			// drop brackets
			s = s.replaceAll("[\\(\\[\\{\\)\\]\\}]", "");
			
			// norm separators
			s = s.replaceAll("[:;]", "|").replace("..", "|");
			
			// norm floating point
			s = s.replaceAll("[,]", ".");
			
			// dash to pipe, if not a minus sign
			s = s.replaceAll("([0-9])\\s?[\\-]", "$1|");
			
			final String[] parts = s.split("[|]");
			
			if (parts.length >= 1) {
				
				final double low = Double.parseDouble(parts[0].trim());
				
				if (parts.length == 2) {
					final double high = Double.parseDouble(parts[1].trim());
					return Range.make(low, high);
				}
				
				return Range.make(low, low);
			}
		} catch (final RuntimeException e) {
			// ignore
		}
		return null;
	}
	
	
	@Override
	public String toString()
	{
		return String.format("(%f : %f)", getMin(), getMax());
	}
	
	
	/**
	 * Make sure min is <= max
	 */
	private void norm()
	{
		if (min > max) {
			final double t = min;
			min = max;
			max = t;
		}
	}
	
	
	/**
	 * Get random integer from range
	 * 
	 * @return random int
	 */
	public int randInt()
	{
		return randInt(Calc.rand);
	}
	
	
	/**
	 * Get random double from this range
	 * 
	 * @return random double
	 */
	public double randDouble()
	{
		return randDouble(Calc.rand);
	}
	
	
	/**
	 * Get random integer from range
	 * 
	 * @param rand RNG
	 * @return random int
	 */
	public int randInt(Random rand)
	{
		return Calc.randInt(rand, (int) Math.round(min), (int) Math.round(min));
	}
	
	
	/**
	 * Get random double from this range
	 * 
	 * @param rand RNG
	 * @return random double
	 */
	public double randDouble(Random rand)
	{
		return min + rand.nextDouble() * (max - min);
	}
	
	
	/**
	 * Get min
	 * 
	 * @return min number
	 */
	public double getMin()
	{
		return min;
	}
	
	
	/**
	 * Get max
	 * 
	 * @return max number
	 */
	public double getMax()
	{
		return max;
	}
	
	
	/**
	 * Set min
	 * 
	 * @param min min value
	 */
	public void setMin(double min)
	{
		this.min = min;
		norm();
	}
	
	
	/**
	 * Set max
	 * 
	 * @param max max value
	 */
	public void setMax(double max)
	{
		this.max = max;
		norm();
	}
	
	
	/**
	 * Get identical copy
	 * 
	 * @return copy
	 */
	public Range copy()
	{
		return new Range(min, max);
	}
	
	
	/**
	 * Set to value of other range
	 * 
	 * @param other copied range
	 */
	public void setTo(Range other)
	{
		if (other == null) return;
		min = other.min;
		max = other.max;
		norm();
	}
	
	
	/**
	 * Set to min-max values
	 * 
	 * @param min min value
	 * @param max max value
	 */
	public void setTo(double min, double max)
	{
		this.min = min;
		this.max = max;
		norm();
	}
}

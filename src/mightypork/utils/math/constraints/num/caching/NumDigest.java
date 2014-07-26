package mightypork.utils.math.constraints.num.caching;


import mightypork.utils.math.constraints.num.Num;


public class NumDigest {
	
	public final double value;
	
	
	public NumDigest(Num num) {
		this.value = num.value();
	}
	
	
	@Override
	public String toString()
	{
		return String.format("Num(%.1f)", value);
	}
}

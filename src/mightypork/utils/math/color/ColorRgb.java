package mightypork.utils.math.color;


import mightypork.utils.math.constraints.num.Num;


public class ColorRgb extends Color {
	
	private final Num r;
	private final Num g;
	private final Num b;
	private final Num a;
	
	
	public ColorRgb(Num r, Num g, Num b, Num a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	
	@Override
	public double r()
	{
		return clamp(r);
	}
	
	
	@Override
	public double g()
	{
		return clamp(g);
	}
	
	
	@Override
	public double b()
	{
		return clamp(b);
	}
	
	
	@Override
	protected double rawAlpha()
	{
		return clamp(a);
	}
	
}

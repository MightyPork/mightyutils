package mightypork.utils.math.color;


import mightypork.utils.math.constraints.num.Num;


public class ColorAlphaAdjuster extends Color {
	
	private final Color source;
	private final Num alphaAdjust;
	
	
	public ColorAlphaAdjuster(Color source, Num alphaMul)
	{
		this.source = source;
		this.alphaAdjust = alphaMul;
	}
	
	
	@Override
	public double r()
	{
		return source.r();
	}
	
	
	@Override
	public double g()
	{
		return source.g();
	}
	
	
	@Override
	public double b()
	{
		return source.b();
	}
	
	
	@Override
	protected double rawAlpha()
	{
		return source.rawAlpha() * alphaAdjust.value();
	}
	
}

package mightypork.utils.math.color;


import java.util.EmptyStackException;
import java.util.Stack;

import mightypork.utils.annotations.FactoryMethod;
import mightypork.utils.math.Calc;
import mightypork.utils.math.constraints.num.Num;


/**
 * Color.<br>
 * All values are 0-1
 *
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class Color {
	
	private static final Stack<Num> alphaStack = new Stack<>();
	private static volatile boolean alphaStackEnabled = true;
	
	
	@FactoryMethod
	public static final Color fromHex(int rgb_hex)
	{
		final int bi = rgb_hex & 0xff;
		final int gi = (rgb_hex >> 8) & 0xff;
		final int ri = (rgb_hex >> 16) & 0xff;
		return rgb(ri / 255D, gi / 255D, bi / 255D);
	}
	
	
	@FactoryMethod
	public static final Color rgb(double r, double g, double b)
	{
		return rgba(Num.make(r), Num.make(g), Num.make(b), Num.ONE);
	}
	
	
	@FactoryMethod
	public static final Color rgba(double r, double g, double b, double a)
	{
		return rgba(Num.make(r), Num.make(g), Num.make(b), Num.make(a));
	}
	
	
	@FactoryMethod
	public static final Color rgb(Num r, Num g, Num b)
	{
		return rgba(r, g, b, Num.ONE);
	}
	
	
	@FactoryMethod
	public static final Color rgba(Num r, Num g, Num b, Num a)
	{
		return new ColorRgb(r, g, b, a);
	}
	
	
	@FactoryMethod
	public static final Color hsb(double h, double s, double b)
	{
		return hsba(Num.make(h), Num.make(s), Num.make(b), Num.ONE);
	}
	
	
	@FactoryMethod
	public static final Color hsba(double h, double s, double b, double a)
	{
		return hsba(Num.make(h), Num.make(s), Num.make(b), Num.make(a));
	}
	
	
	@FactoryMethod
	public static final Color hsb(Num h, Num s, Num b)
	{
		return hsba(h, s, b, Num.ONE);
	}
	
	
	@FactoryMethod
	public static final Color hsba(Num h, Num s, Num b, Num a)
	{
		return new ColorHsb(h, s, b, a);
	}
	
	
	@FactoryMethod
	public static final Color light(double a)
	{
		return light(Num.make(a));
	}
	
	
	@FactoryMethod
	public static final Color light(Num a)
	{
		return rgba(Num.ONE, Num.ONE, Num.ONE, a);
	}
	
	
	@FactoryMethod
	public static final Color dark(double a)
	{
		return dark(Num.make(a));
	}
	
	
	@FactoryMethod
	public static final Color dark(Num a)
	{
		return rgba(Num.ZERO, Num.ZERO, Num.ZERO, a);
	}
	
	
	protected static final double clamp(Num n)
	{
		return Calc.clamp(n.value(), 0, 1);
	}
	
	
	protected static final double clamp(double n)
	{
		return Calc.clamp(n, 0, 1);
	}
	
	
	/**
	 * @return red 0-1
	 */
	public abstract double r();
	
	
	/**
	 * @return green 0-1
	 */
	public abstract double g();
	
	
	/**
	 * @return blue 0-1
	 */
	public abstract double b();
	
	
	/**
	 * @return alpha 0-1
	 */
	public final double a()
	{
		double alpha = rawAlpha();
		
		if (alphaStackEnabled) {
			
			for (final Num n : alphaStack) {
				alpha *= clamp(n.value());
			}
		}
		
		return clamp(alpha);
	}
	
	
	/**
	 * @return alpha 0-1, before multiplying with the global alpha value.
	 */
	protected abstract double rawAlpha();
	
	
	/**
	 * <p>
	 * Push alpha multiplier on the stack (can be animated or whatever you
	 * like). Once done with rendering, the popAlpha() method should be called,
	 * otherwise you may experience unexpected glitches (such as all going
	 * transparent).
	 * </p>
	 * <p>
	 * multiplier value should not exceed the range 0..1, otherwise it will be
	 * clamped to it.
	 * </p>
	 *
	 * @param alpha alpha multiplier
	 */
	public static void pushAlpha(Num alpha)
	{
		if (!alphaStackEnabled) {
			return;
		}
		
		alphaStack.push(alpha);
	}
	
	
	/**
	 * Remove a pushed alpha multiplier from the stack. If there's no remaining
	 * multiplier on the stack, an exception is raised.
	 *
	 * @throws EmptyStackException if the stack is empty
	 */
	public static void popAlpha()
	{
		if (!alphaStackEnabled) {
			return;
		}
		
		if (alphaStack.isEmpty()) {
			throw new EmptyStackException();
		}
		
		alphaStack.pop();
	}
	
	
	/**
	 * Enable alpha stack. When disabled, pushAlpha() and popAlpha() have no
	 * effect.
	 *
	 * @param yes
	 */
	public static void enableAlphaStack(boolean yes)
	{
		alphaStackEnabled = yes;
	}
	
	
	/**
	 * @return true if alpha stack is enabled.
	 */
	public static boolean isAlphaStackEnabled()
	{
		return alphaStackEnabled;
	}
	
	
	public Color withAlpha(double multiplier)
	{
		return new ColorAlphaAdjuster(this, Num.make(multiplier));
	}
	
	
	public Color withAlpha(Num multiplier)
	{
		return new ColorAlphaAdjuster(this, multiplier);
	}
	
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(b());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(g());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(r());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(a());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Color)) return false;
		final Color other = (Color) obj;
		if (Double.doubleToLongBits(b()) != Double.doubleToLongBits(other.b())) return false;
		if (Double.doubleToLongBits(g()) != Double.doubleToLongBits(other.g())) return false;
		if (Double.doubleToLongBits(r()) != Double.doubleToLongBits(other.r())) return false;
		if (Double.doubleToLongBits(a()) != Double.doubleToLongBits(other.a())) return false;
		return true;
	}
}

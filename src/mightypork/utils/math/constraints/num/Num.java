package mightypork.utils.math.constraints.num;


import mightypork.utils.annotations.FactoryMethod;
import mightypork.utils.math.constraints.CachedDigestable;
import mightypork.utils.math.constraints.DigestCache;
import mightypork.utils.math.constraints.num.caching.NumCache;
import mightypork.utils.math.constraints.num.caching.NumDigest;
import mightypork.utils.math.constraints.num.proxy.NumProxy;
import mightypork.utils.math.constraints.num.var.NumVar;
import mightypork.utils.math.constraints.vect.Vect;


public abstract class Num implements NumBound, CachedDigestable<NumDigest> {
	
	public static final NumConst ZERO = Num.make(0);
	public static final NumConst ONE = Num.make(1);
	
	
	@FactoryMethod
	public static Num make(NumBound bound)
	{
		return new NumProxy(bound);
	}
	
	
	@FactoryMethod
	public static NumConst make(double value)
	{
		return new NumConst(value);
	}
	
	
	@FactoryMethod
	public static NumVar makeVar()
	{
		return makeVar(0);
	}
	
	
	@FactoryMethod
	public static NumVar makeVar(double value)
	{
		return new NumVar(value);
	}
	
	
	@FactoryMethod
	public static NumVar makeVar(Num copied)
	{
		return new NumVar(copied.value());
	}
	
	private Num p_ceil;
	private Num p_floor;
	private Num p_sgn;
	private Num p_round;
	private Num p_atan;
	private Num p_acos;
	private Num p_asin;
	private Num p_tan;
	private Num p_cos;
	private Num p_sin;
	private Num p_cbrt;
	private Num p_sqrt;
	private Num p_cube;
	private Num p_square;
	private Num p_neg;
	private Num p_abs;
	
	private final DigestCache<NumDigest> dc = new DigestCache<NumDigest>() {
		
		@Override
		protected NumDigest createDigest()
		{
			return new NumDigest(Num.this);
		}
	};
	
	
	public NumConst freeze()
	{
		return new NumConst(value());
	}
	
	
	/**
	 * Wrap this constraint into a caching adapter. Value will stay fixed (ie.
	 * no re-calculations) until cache receives a poll() call.
	 * 
	 * @return the caching adapter
	 */
	public NumCache cached()
	{
		return new NumCache(this);
	}
	
	
	/**
	 * Get a snapshot of the current state, to be used for processing.
	 * 
	 * @return digest
	 */
	
	@Override
	public NumDigest digest()
	{
		return dc.digest();
	}
	
	
	@Override
	public void enableDigestCaching(boolean yes)
	{
		dc.enableDigestCaching(yes);
	}
	
	
	@Override
	public boolean isDigestCachingEnabled()
	{
		return dc.isDigestCachingEnabled();
	}
	
	
	@Override
	public void markDigestDirty()
	{
		dc.markDigestDirty();
	}
	
	
	@Override
	public Num getNum()
	{
		return this;
	}
	
	
	/**
	 * @return the number
	 */
	public abstract double value();
	
	
	public Num add(final double addend)
	{
		return new Num() {
			
			private final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return t.value() + addend;
			}
		};
	}
	
	
	public Num add(final Num addend)
	{
		return new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return t.value() + addend.value();
			}
		};
	}
	
	
	public Num sub(final double subtrahend)
	{
		return add(-subtrahend);
	}
	
	
	public Num abs()
	{
		if (p_abs == null) p_abs = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.abs(t.value());
			}
		};
		
		return p_abs;
	}
	
	
	public Num sub(final Num subtrahend)
	{
		return new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return t.value() - subtrahend.value();
			}
		};
	}
	
	
	public Num div(final double factor)
	{
		return mul(1 / factor);
	}
	
	
	public Num div(final Num factor)
	{
		
		return new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return t.value() / factor.value();
			}
		};
	}
	
	
	public Num mul(final double factor)
	{
		return new Num() {
			
			private final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return t.value() * factor;
			}
		};
	}
	
	
	public Num mul(final Num factor)
	{
		
		return new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return t.value() * factor.value();
			}
		};
	}
	
	
	public Num average(final double other)
	{
		return new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return (t.value() + other) / 2;
			}
		};
	}
	
	
	public Num average(final Num other)
	{
		return new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return (t.value() + other.value()) / 2;
			}
		};
	}
	
	
	public Num perc(final double percent)
	{
		return mul(percent / 100D);
	}
	
	
	public Num perc(final Num percent)
	{
		return new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return t.value() * (percent.value() / 100);
			}
		};
	}
	
	
	public Num cos()
	{
		if (p_cos == null) p_cos = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.cos(t.value());
			}
		};
		
		return p_cos;
	}
	
	
	public Num acos()
	{
		if (p_acos == null) p_acos = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.acos(t.value());
			}
		};
		
		return p_acos;
	}
	
	
	public Num sin()
	{
		if (p_sin == null) p_sin = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.sin(t.value());
			}
		};
		
		return p_sin;
	}
	
	
	public Num asin()
	{
		if (p_asin == null) p_asin = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.asin(t.value());
			}
		};
		
		return p_asin;
	}
	
	
	public Num tan()
	{
		if (p_tan == null) p_tan = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.tan(t.value());
			}
		};
		
		return p_tan;
	}
	
	
	public Num atan()
	{
		if (p_atan == null) p_atan = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.atan(t.value());
			}
		};
		
		return p_atan;
	}
	
	
	public Num cbrt()
	{
		if (p_cbrt == null) p_cbrt = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.cbrt(t.value());
			}
		};
		
		return p_cbrt;
	}
	
	
	public Num sqrt()
	{
		if (p_sqrt == null) p_sqrt = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.sqrt(t.value());
			}
		};
		
		return p_sqrt;
	}
	
	
	public Num neg()
	{
		if (p_neg == null) p_neg = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return -1 * t.value();
			}
		};
		
		return p_neg;
	}
	
	
	public Num round()
	{
		if (p_round == null) p_round = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.round(t.value());
			}
		};
		
		return p_round;
	}
	
	
	public Num floor()
	{
		if (p_floor == null) p_floor = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.floor(t.value());
			}
		};
		
		return p_floor;
	}
	
	
	public Num ceil()
	{
		if (p_ceil == null) p_ceil = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.round(t.value());
			}
		};
		
		return p_ceil;
	}
	
	
	public Num pow(final double other)
	{
		return new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.pow(t.value(), other);
			}
		};
	}
	
	
	public Num pow(final Num power)
	{
		return new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.pow(t.value(), power.value());
			}
		};
	}
	
	
	public Num cube()
	{
		if (p_cube == null) p_cube = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				final double v = t.value();
				return v * v * v;
			}
		};
		
		return p_cube;
	}
	
	
	public Num square()
	{
		if (p_square == null) p_square = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				final double v = t.value();
				return v * v;
			}
		};
		
		return p_square;
	}
	
	
	public Num half()
	{
		return mul(0.5);
	}
	
	
	public Num max(final double other)
	{
		return new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.max(t.value(), other);
			}
		};
	}
	
	
	public Num max(final Num other)
	{
		return new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.max(t.value(), other.value());
			}
		};
	}
	
	
	public Num min(final Num other)
	{
		return new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.min(t.value(), other.value());
			}
		};
	}
	
	
	public Num min(final double other)
	{
		return new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.min(t.value(), other);
			}
		};
	}
	
	
	public Num signum()
	{
		if (p_sgn == null) p_sgn = new Num() {
			
			final Num t = Num.this;
			
			
			@Override
			public double value()
			{
				return Math.signum(t.value());
			}
		};
		
		return p_sgn;
	}
	
	
	public boolean isNegative()
	{
		return value() < 0;
	}
	
	
	public boolean isPositive()
	{
		return value() > 0;
	}
	
	
	public boolean isZero()
	{
		return value() == 0;
	}
	
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(value());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Num)) return false;
		final Num other = (Num) obj;
		
		return value() == other.value();
	}
	
	
	@Override
	public String toString()
	{
		return Double.toString(value());
	}
	
	
	/**
	 * @return vect with both coords of this size
	 */
	public Vect toVectXY()
	{
		return Vect.make(this);
	}
}

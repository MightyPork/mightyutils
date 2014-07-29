package mightypork.utils.math.animation;


/**
 * EasingFunction function.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class Easing {
	
	/**
	 * Get value at time t.
	 *
	 * @param t time parameter (t = 1..1)
	 * @return value at given t (0..1, can exceed if needed)
	 */
	public abstract double get(double t);
	
	
	/**
	 * Reverse an easing (factory method)
	 *
	 * @param original original easing
	 * @return reversed easing
	 */
	public static Easing reverse(Easing original)
	{
		return new Reverse(original);
	}
	
	
	/**
	 * Combine two easings (factory method)
	 *
	 * @param in initial easing
	 * @param out terminal easing
	 * @return product
	 */
	public static Easing combine(Easing in, Easing out)
	{
		return new Composite(in, out);
	}
	
	
	/**
	 * Create "bilinear" easing - compose of straight and reverse. (factory
	 * method)
	 *
	 * @param in initial easing
	 * @return product
	 */
	public static Easing inOut(Easing in)
	{
		return combine(in, reverse(in));
	}
	
	/**
	 * Reverse EasingFunction
	 *
	 * @author Ondřej Hruška (MightyPork)
	 */
	private static class Reverse extends Easing {
		
		private final Easing ea;
		
		
		/**
		 * @param in Easing to reverse
		 */
		public Reverse(Easing in)
		{
			this.ea = in;
		}
		
		
		@Override
		public double get(double t)
		{
			return 1 - ea.get(1 - t);
		}
	}
	
	/**
	 * Composite EasingFunction (0-0.5 EasingFunction A, 0.5-1 EasingFunction B)
	 *
	 * @author Ondřej Hruška (MightyPork)
	 */
	private static class Composite extends Easing {
		
		private final Easing in;
		private final Easing out;
		
		
		/**
		 * Create a composite EasingFunction
		 *
		 * @param in initial EasingFunction
		 * @param out terminal EasingFunction
		 */
		public Composite(Easing in, Easing out)
		{
			this.in = in;
			this.out = out;
		}
		
		
		@Override
		public double get(double t)
		{
			if (t < 0.5) return in.get(2 * t) * 0.5;
			return 0.5 + out.get(2 * t - 1) * 0.5;
		}
	}
	
	/** No easing; At t=0.5 goes high. */
	public static final Easing NONE = new Easing() {
		
		@Override
		public double get(double t)
		{
			return (t < 0.5 ? 0 : 1);
		}
	};
	
	/** Linear (y=t) easing */
	public static final Easing LINEAR = new Easing() {
		
		@Override
		public double get(double t)
		{
			return t;
		}
	};
	
	/** Quadratic (y=t^2) easing in */
	public static final Easing QUADRATIC_IN = new Easing() {
		
		@Override
		public double get(double t)
		{
			return t * t;
		}
	};
	
	/** Quadratic (y=t^2) easing out */
	public static final Easing QUADRATIC_OUT = reverse(QUADRATIC_IN);
	
	/** Quadratic (y=t^2) easing both */
	public static final Easing QUADRATIC_BOTH = inOut(QUADRATIC_IN);
	
	/** Cubic (y=t^3) easing in */
	public static final Easing CUBIC_IN = new Easing() {
		
		@Override
		public double get(double t)
		{
			return t * t * t;
		}
	};
	
	/** Cubic (y=t^3) easing out */
	public static final Easing CUBIC_OUT = reverse(CUBIC_IN);
	
	/** Cubic (y=t^3) easing both */
	public static final Easing CUBIC_BOTH = inOut(CUBIC_IN);
	
	/** Quartic (y=t^4) easing in */
	public static final Easing QUARTIC_IN = new Easing() {
		
		@Override
		public double get(double t)
		{
			return t * t * t * t;
		}
	};
	
	/** Quartic (y=t^4) easing out */
	public static final Easing QUARTIC_OUT = reverse(QUADRATIC_IN);
	
	/** Quartic (y=t^4) easing both */
	public static final Easing QUARTIC_BOTH = inOut(QUADRATIC_IN);
	
	/** Quintic (y=t^5) easing in */
	public static final Easing QUINTIC_IN = new Easing() {
		
		@Override
		public double get(double t)
		{
			return t * t * t * t * t;
		}
	};
	
	/** Quintic (y=t^5) easing out */
	public static final Easing QUINTIC_OUT = reverse(QUINTIC_IN);
	
	/** Quintic (y=t^5) easing both */
	public static final Easing QUINTIC_BOTH = inOut(QUINTIC_IN);
	
	/** Sine easing in */
	public static final Easing SINE_IN = new Easing() {
		
		@Override
		public double get(double t)
		{
			return 1 - Math.cos(t * (Math.PI / 2));
		}
	};
	
	/** Sine easing out */
	public static final Easing SINE_OUT = reverse(SINE_IN);
	
	/** Sine easing both */
	public static final Easing SINE_BOTH = inOut(SINE_IN);
	
	/** Exponential easing in */
	public static final Easing EXPO_IN = new Easing() {
		
		@Override
		public double get(double t)
		{
			return Math.pow(2, 10 * (t - 1));
		}
	};
	
	/** Exponential easing out */
	public static final Easing EXPO_OUT = reverse(EXPO_IN);
	
	/** Exponential easing both */
	public static final Easing EXPO_BOTH = inOut(EXPO_IN);
	
	/** Circular easing in */
	public static final Easing CIRC_IN = new Easing() {
		
		@Override
		public double get(double t)
		{
			return 1 - Math.sqrt(1 - t * t);
		}
	};
	
	/** Circular easing out */
	public static final Easing CIRC_OUT = reverse(CIRC_IN);
	
	/** Circular easing both */
	public static final Easing CIRC_BOTH = inOut(CIRC_IN);
	
	/** Bounce easing in */
	public static final Easing BOUNCE_OUT = new Easing() {
		
		@Override
		public double get(double t)
		{
			if (t < (1 / 2.75f)) {
				return (7.5625f * t * t);
				
			} else if (t < (2 / 2.75f)) {
				t -= (1.5f / 2.75f);
				return (7.5625f * t * t + 0.75f);
				
			} else if (t < (2.5 / 2.75)) {
				t -= (2.25f / 2.75f);
				return (7.5625f * t * t + 0.9375f);
				
			} else {
				t -= (2.625f / 2.75f);
				return (7.5625f * t * t + 0.984375f);
			}
		}
	};
	
	/** Bounce easing out */
	public static final Easing BOUNCE_IN = reverse(BOUNCE_OUT);
	
	/** Bounce easing both */
	public static final Easing BOUNCE_BOTH = inOut(BOUNCE_IN);
	
	/** Back easing in */
	public static final Easing BACK_IN = new Easing() {
		
		@Override
		public double get(double t)
		{
			final float s = 1.70158f;
			return t * t * ((s + 1) * t - s);
		}
	};
	
	/** Back easing out */
	public static final Easing BACK_OUT = reverse(BACK_IN);
	
	/** Back easing both */
	public static final Easing BACK_BOTH = inOut(BACK_IN);
	
	/** Elastic easing in */
	public static final Easing ELASTIC_IN = new Easing() {
		
		@Override
		public double get(double t)
		{
			if (t == 0) return 0;
			if (t == 1) return 1;
			
			final double p = .3f;
			final double s = p / 4;
			return -(Math.pow(2, 10 * (t -= 1)) * Math.sin((t - s) * (2 * Math.PI) / p));
		}
	};
	
	/** Elastic easing out */
	public static final Easing ELASTIC_OUT = reverse(ELASTIC_IN);
	
	/** Elastic easing both */
	public static final Easing ELASTIC_BOTH = inOut(ELASTIC_IN);
}

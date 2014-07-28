package mightypork.utils.math.constraints.vect;


import mightypork.utils.annotations.FactoryMethod;
import mightypork.utils.math.constraints.CachedDigestable;
import mightypork.utils.math.constraints.DigestCache;
import mightypork.utils.math.constraints.num.Num;
import mightypork.utils.math.constraints.num.NumConst;
import mightypork.utils.math.constraints.rect.Rect;
import mightypork.utils.math.constraints.vect.caching.VectCache;
import mightypork.utils.math.constraints.vect.caching.VectDigest;
import mightypork.utils.math.constraints.vect.proxy.VectNumAdapter;
import mightypork.utils.math.constraints.vect.proxy.VectProxy;
import mightypork.utils.math.constraints.vect.var.VectVar;


/**
 * The most basic Vec methods
 *
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class Vect implements VectBound, CachedDigestable<VectDigest> {

	public static final VectConst ZERO = new VectConst(0, 0, 0);
	public static final VectConst ONE = new VectConst(1, 1, 1);


	@FactoryMethod
	public static Vect make(Num xy)
	{
		return make(xy, xy);
	}


	@FactoryMethod
	public static Vect make(Num xc, Num yc)
	{
		return Vect.make(xc, yc, Num.ZERO);
	}


	@FactoryMethod
	public static Vect make(Num xc, Num yc, Num zc)
	{
		return new VectNumAdapter(xc, yc, zc);
	}


	@FactoryMethod
	public static Vect make(VectBound bound)
	{
		return new VectProxy(bound);
	}


	@FactoryMethod
	public static VectConst make(NumConst xy)
	{
		return make(xy, xy);
	}


	@FactoryMethod
	public static VectConst make(NumConst xc, NumConst yc)
	{
		return Vect.make(xc, yc, Num.ZERO);
	}


	@FactoryMethod
	public static VectConst make(NumConst xc, NumConst yc, NumConst zc)
	{
		return new VectConst(xc.value(), yc.value(), zc.value());
	}


	@FactoryMethod
	public static VectConst make(double xy)
	{
		return make(xy, xy);
	}


	@FactoryMethod
	public static VectConst make(double x, double y)
	{
		return Vect.make(x, y, 0);
	}


	@FactoryMethod
	public static VectConst make(double x, double y, double z)
	{
		return new VectConst(x, y, z);
	}


	@FactoryMethod
	public static VectVar makeVar()
	{
		return Vect.makeVar(Vect.ZERO);
	}


	@FactoryMethod
	public static VectVar makeVar(double x, double y)
	{
		return Vect.makeVar(x, y, 0);
	}


	@FactoryMethod
	public static VectVar makeVar(Vect copied)
	{
		return Vect.makeVar(copied.x(), copied.y(), copied.z());
	}


	@FactoryMethod
	public static VectVar makeVar(double x, double y, double z)
	{
		return new VectVar(x, y, z);
	}


	public static VectConst fromString(String string)
	{
		try {
			String s = string.trim();

			// drop whitespace
			s = s.replaceAll("\\s", "");

			// drop brackets
			s = s.replaceAll("[\\(\\[\\{\\)\\]\\}]", "");

			// norm separators
			s = s.replaceAll("[:;]", "|");

			// norm floating point
			s = s.replaceAll("[,]", ".");

			final String[] parts = s.split("[|]");

			if (parts.length >= 2) {

				final double x = Double.parseDouble(parts[0].trim());
				final double y = Double.parseDouble(parts[1].trim());

				if (parts.length == 2) {
					return Vect.make(x, y);
				}

				final double z = Double.parseDouble(parts[2].trim());

				return Vect.make(x, y, z);
			}

		} catch (final RuntimeException e) {
			return null;
		}
		return null;
	}


	@Override
	public String toString()
	{
		return String.format("(%.1f ; %.1f ; %.1f)", x(), y(), z());
	}

	private Num p_size;
	private Vect p_neg;
	private Vect p_half;
	private Vect p_abs;

	private Num p_xc;
	private Num p_yc;
	private Num p_zc;

	private final DigestCache<VectDigest> dc = new DigestCache<VectDigest>() {

		@Override
		protected VectDigest createDigest()
		{
			return new VectDigest(Vect.this);
		}
	};


	/**
	 * @return X coordinate
	 */
	public abstract double x();


	/**
	 * @return Y coordinate
	 */
	public abstract double y();


	/**
	 * (Implemented in Vect for convenience when creating 2D vects)
	 *
	 * @return Z coordinate
	 */
	public double z()
	{
		return 0;
	}


	/**
	 * @return X rounded
	 */
	public int xi()
	{
		return (int) Math.round(x());
	}


	/**
	 * @return Y rounded
	 */
	public int yi()
	{
		return (int) Math.round(y());
	}


	/**
	 * @return Z rounded
	 */
	public int zi()
	{
		return (int) Math.round(z());
	}


	/**
	 * @return X constraint
	 */
	public Num xn()
	{
		if (p_xc == null) p_xc = new Num() {

			@Override
			public double value()
			{
				return x();
			}
		};

		return p_xc;
	}


	/**
	 * @return Y constraint
	 */
	public Num yn()
	{
		if (p_yc == null) p_yc = new Num() {

			@Override
			public double value()
			{
				return y();
			}
		};

		return p_yc;
	}


	/**
	 * @return Z constraint
	 */
	public Num zn()
	{
		if (p_zc == null) p_zc = new Num() {

			@Override
			public double value()
			{
				return z();
			}
		};

		return p_zc;
	}


	@Override
	public final Vect getVect()
	{
		return this;
	}


	/**
	 * @return true if zero
	 */
	public boolean isZero()
	{
		return x() == 0 && y() == 0 && z() == 0;
	}


	/**
	 * Get a static immutable copy of the current state.
	 *
	 * @return a immutable copy
	 */
	public VectConst freeze()
	{
		return new VectConst(this);
	}


	/**
	 * Wrap this constraint into a caching adapter. Value will stay fixed (ie.
	 * no re-calculations) until cache receives a poll() call.
	 *
	 * @return the caching adapter
	 */
	public VectCache cached()
	{
		return new VectCache(this);
	}


	@Override
	public VectDigest digest()
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


	/**
	 * Get a view with X set to given value
	 *
	 * @param x x coordinate
	 * @return result
	 */
	public Vect withX(double x)
	{
		return withX(Num.make(x));
	}


	/**
	 * Get a view with Y set to given value
	 *
	 * @param y y coordinate
	 * @return result
	 */
	public Vect withY(double y)
	{
		return withY(Num.make(y));
	}


	/**
	 * Get a view with Z set to given value
	 *
	 * @param z z coordinate
	 * @return result
	 */
	public Vect withZ(double z)
	{
		return withZ(Num.make(z));
	}


	public Vect withX(final Num x)
	{
		return new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				return x.value();
			}


			@Override
			public double y()
			{
				return t.z();
			}


			@Override
			public double z()
			{
				return t.z();
			}
		};
	}


	public Vect withY(final Num y)
	{
		return new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				return t.x();
			}


			@Override
			public double y()
			{
				return y.value();
			}


			@Override
			public double z()
			{
				return t.z();
			}
		};
	}


	public Vect withZ(final Num z)
	{
		return new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				return t.x();
			}


			@Override
			public double y()
			{
				return t.y();
			}


			@Override
			public double z()
			{
				return z.value();
			}
		};
	}


	/**
	 * Get absolute value (positive)
	 *
	 * @return result
	 */
	public Vect abs()
	{
		if (p_abs == null) p_abs = new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				return Math.abs(t.x());
			}


			@Override
			public double y()
			{
				return Math.abs(t.y());
			}


			@Override
			public double z()
			{
				return Math.abs(t.z());
			}
		};

		return p_abs;
	}


	/**
	 * Add a vector.
	 *
	 * @param vec offset
	 * @return result
	 */
	public Vect add(Vect vec)
	{
		return add(vec.xn(), vec.yn(), vec.zn());
	}


	/**
	 * Add to each component.<br>
	 * Z is unchanged.
	 *
	 * @param x x offset
	 * @param y y offset
	 * @return result
	 */
	public Vect add(double x, double y)
	{
		return add(x, y, 0);
	}


	/**
	 * Add to each component.
	 *
	 * @param x x offset
	 * @param y y offset
	 * @param z z offset
	 * @return result
	 */
	public Vect add(final double x, final double y, final double z)
	{
		return new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				return t.x() + x;
			}


			@Override
			public double y()
			{
				return t.y() + y;
			}


			@Override
			public double z()
			{
				return t.z() + z;
			}
		};
	}


	public Vect add(Num x, Num y)
	{
		return add(x, y, Num.ZERO);
	}


	public Vect add(final Num x, final Num y, final Num z)
	{
		return new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				return t.x() + x.value();
			}


			@Override
			public double y()
			{
				return t.y() + y.value();
			}


			@Override
			public double z()
			{
				return t.z() + z.value();
			}
		};
	}


	/**
	 * Get copy divided by two
	 *
	 * @return result
	 */
	public Vect half()
	{
		if (p_half == null) p_half = mul(0.5);
		return p_half;
	}


	/**
	 * Multiply each component.
	 *
	 * @param d multiplier
	 * @return result
	 */
	public Vect mul(double d)
	{
		return mul(d, d, d);
	}


	/**
	 * Multiply each component.
	 *
	 * @param vec vector of multipliers
	 * @return result
	 */
	public Vect mul(Vect vec)
	{
		return mul(vec.xn(), vec.yn(), vec.zn());
	}


	/**
	 * Multiply each component.<br>
	 * Z is unchanged.
	 *
	 * @param x x multiplier
	 * @param y y multiplier
	 * @return result
	 */
	public Vect mul(double x, double y)
	{
		return mul(x, y, 1);
	}


	/**
	 * Multiply each component.
	 *
	 * @param x x multiplier
	 * @param y y multiplier
	 * @param z z multiplier
	 * @return result
	 */
	public Vect mul(final double x, final double y, final double z)
	{
		return new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				return t.x() * x;
			}


			@Override
			public double y()
			{
				return t.y() * y;
			}


			@Override
			public double z()
			{
				return t.z() * z;
			}
		};
	}


	/**
	 * Multiply each component.
	 *
	 * @param d multiplier
	 * @return result
	 */
	public Vect mul(final Num d)
	{
		return mul(d, d, d);
	}


	/**
	 * Multiply each component.
	 *
	 * @param x x multiplier
	 * @param y y multiplier
	 * @return result
	 */
	public Vect mul(final Num x, final Num y)
	{
		return mul(x, y, Num.ONE);
	}


	/**
	 * Multiply each component.
	 *
	 * @param x x multiplier
	 * @param y y multiplier
	 * @param z z multiplier
	 * @return result
	 */
	public Vect mul(final Num x, final Num y, final Num z)
	{
		return new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				return t.x() * x.value();
			}


			@Override
			public double y()
			{
				return t.y() * y.value();
			}


			@Override
			public double z()
			{
				return t.z() * z.value();
			}
		};
	}


	/**
	 * Round coordinates.
	 *
	 * @return result
	 */
	public Vect round()
	{
		return new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				return Math.round(t.x());
			}


			@Override
			public double y()
			{
				return Math.round(t.y());
			}


			@Override
			public double z()
			{
				return Math.round(t.z());
			}
		};
	}


	/**
	 * Round coordinates down.
	 *
	 * @return result
	 */
	public Vect floor()
	{
		return new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				return Math.floor(t.x());
			}


			@Override
			public double y()
			{
				return Math.floor(t.y());
			}


			@Override
			public double z()
			{
				return Math.floor(t.z());
			}
		};
	}


	/**
	 * Round coordinates up.
	 *
	 * @return result
	 */
	public Vect ceil()
	{
		return new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				return Math.ceil(t.x());
			}


			@Override
			public double y()
			{
				return Math.ceil(t.y());
			}


			@Override
			public double z()
			{
				return Math.ceil(t.z());
			}
		};
	}


	/**
	 * Subtract vector.
	 *
	 * @param vec offset
	 * @return result
	 */
	public Vect sub(Vect vec)
	{
		return sub(vec.xn(), vec.yn(), vec.zn());
	}


	/**
	 * Subtract a 2D vector.<br>
	 * Z is unchanged.
	 *
	 * @param x x offset
	 * @param y y offset
	 * @return result
	 */
	public Vect sub(double x, double y)
	{
		return add(-x, -y, 0);
	}


	/**
	 * Subtract a 3D vector.
	 *
	 * @param x x offset
	 * @param y y offset
	 * @param z z offset
	 * @return result
	 */
	public Vect sub(double x, double y, double z)
	{
		return add(-x, -y, -z);
	}


	public Vect sub(Num x, Num y)
	{
		return sub(x, y, Num.ZERO);
	}


	public Vect sub(final Num x, final Num y, final Num z)
	{
		return new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				return t.x() - x.value();
			}


			@Override
			public double y()
			{
				return t.y() - y.value();
			}


			@Override
			public double z()
			{
				return t.z() - z.value();
			}
		};
	}


	/**
	 * Negate all coordinates (* -1)
	 *
	 * @return result
	 */
	public Vect neg()
	{
		if (p_neg == null) p_neg = mul(-1);
		return p_neg;
	}


	/**
	 * Scale vector to given size.
	 *
	 * @param size size we need
	 * @return result
	 */
	public Vect norm(final Num size)
	{
		return new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				final double tSize = t.size().value();
				final double nSize = size.value();

				if (tSize == 0 || nSize == 0) return 0;

				return t.x() / (nSize / tSize);
			}


			@Override
			public double y()
			{
				final double tSize = t.size().value();
				final double nSize = size.value();

				if (tSize == 0 || nSize == 0) return 0;

				return t.y() / (nSize / tSize);
			}


			@Override
			public double z()
			{
				final double tSize = t.size().value();
				final double nSize = size.value();

				if (tSize == 0 || nSize == 0) return 0;

				return t.z() / (nSize / tSize);
			}
		};
	}


	public Vect norm(final double size)
	{
		return norm(Num.make(size));
	}


	/**
	 * Get distance to other point
	 *
	 * @param point other point
	 * @return distance
	 */
	public Num dist(final Vect point)
	{
		return new Num() {

			final Vect t = Vect.this;


			@Override
			public double value()
			{
				final double dx = t.x() - point.x();
				final double dy = t.y() - point.y();
				final double dz = t.z() - point.z();

				return Math.sqrt(dx * dx + dy * dy + dz * dz);
			}
		};
	}


	/**
	 * Get middle of line to other point
	 *
	 * @param point other point
	 * @return result
	 */
	public Vect midTo(final Vect point)
	{
		return new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				return (point.x() + t.x()) * 0.5;
			}


			@Override
			public double y()
			{
				return (point.y() + t.y()) * 0.5;
			}


			@Override
			public double z()
			{
				return (point.z() + t.z()) * 0.5;
			}
		};
	}


	/**
	 * Create vector from this point to other point
	 *
	 * @param point second point
	 * @return result
	 */
	public Vect vectTo(final Vect point)
	{
		return new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				return (point.x() - t.x());
			}


			@Override
			public double y()
			{
				return (point.y() - t.y());
			}


			@Override
			public double z()
			{
				return (point.z() - t.z());
			}
		};
	}


	/**
	 * Get cross product (vector multiplication)
	 *
	 * @param vec other vector
	 * @return result
	 */
	public Vect cross(final Vect vec)
	{
		return new Vect() {

			final Vect t = Vect.this;


			@Override
			public double x()
			{
				return t.y() * vec.z() - t.z() * vec.y();
			}


			@Override
			public double y()
			{
				return t.z() * vec.x() - t.x() * vec.z();
			}


			@Override
			public double z()
			{
				return t.x() * vec.y() - t.y() * vec.x();
			}
		};
	}


	/**
	 * Get dot product (scalar multiplication)
	 *
	 * @param vec other vector
	 * @return dot product
	 */
	public Num dot(final Vect vec)
	{
		return new Num() {

			final Vect t = Vect.this;


			@Override
			public double value()
			{
				return t.x() * vec.x() + t.y() * vec.y() + t.z() * vec.z();
			}
		};
	}


	/**
	 * Get vector size
	 *
	 * @return size
	 */
	public Num size()
	{
		if (p_size == null) p_size = new Num() {

			final Vect t = Vect.this;


			@Override
			public double value()
			{
				final double x = t.x(), y = t.y(), z = t.z();
				return Math.sqrt(x * x + y * y + z * z);
			}
		};

		return p_size;
	}


	/**
	 * Expand to a rect, with given growth to each side.
	 *
	 * @param left
	 * @param right
	 * @param top
	 * @param bottom
	 * @return the rect
	 */
	public Rect expand(double left, double right, double top, double bottom)
	{
		return Rect.make(this, Vect.ZERO).grow(left, right, top, bottom);
	}


	/**
	 * Expand to a rect, with given growth to each side.
	 *
	 * @param left
	 * @param right
	 * @param top
	 * @param bottom
	 * @return the rect
	 */
	public Rect expand(Num left, Num right, Num top, Num bottom)
	{
		return Rect.make(this, Vect.ZERO).grow(left, right, top, bottom);
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Double.valueOf(x()).hashCode();
		result = prime * result + Double.valueOf(y()).hashCode();
		result = prime * result + Double.valueOf(z()).hashCode();
		return result;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Vect)) return false;
		final Vect other = (Vect) obj;

		return x() == other.x() && y() == other.y() && z() == other.z();
	}


	public final boolean isInside(Rect bounds)
	{
		return bounds.contains(this);
	}


	public Rect startRect()
	{
		return expand(0, 0, 0, 0);
	}
}

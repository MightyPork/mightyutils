package mightypork.utils.math.constraints.rect;


import mightypork.utils.annotations.FactoryMethod;
import mightypork.utils.math.constraints.CachedDigestable;
import mightypork.utils.math.constraints.DigestCache;
import mightypork.utils.math.constraints.num.Num;
import mightypork.utils.math.constraints.num.NumConst;
import mightypork.utils.math.constraints.rect.builders.TiledRect;
import mightypork.utils.math.constraints.rect.caching.RectCache;
import mightypork.utils.math.constraints.rect.caching.RectDigest;
import mightypork.utils.math.constraints.rect.proxy.RectProxy;
import mightypork.utils.math.constraints.rect.proxy.RectVectAdapter;
import mightypork.utils.math.constraints.rect.var.RectVar;
import mightypork.utils.math.constraints.vect.Vect;
import mightypork.utils.math.constraints.vect.VectConst;


/**
 * Common methods for all kinds of Rects
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class Rect implements RectBound, CachedDigestable<RectDigest> {
	
	public static final RectConst ZERO = new RectConst(0, 0, 0, 0);
	public static final RectConst ONE = new RectConst(0, 0, 1, 1);
	
	
	@FactoryMethod
	public static Rect make(Num width, Num height)
	{
		final Vect origin = Vect.ZERO;
		final Vect size = Vect.make(width, height);
		
		return Rect.make(origin, size);
	}
	
	
	public static Rect make(Vect size)
	{
		return Rect.make(size.xn(), size.yn());
	}
	
	
	@FactoryMethod
	public static Rect make(RectBound bound)
	{
		return new RectProxy(bound);
	}
	
	
	@FactoryMethod
	public static Rect make(Num x, Num y, Num width, Num height)
	{
		final Vect origin = Vect.make(x, y);
		final Vect size = Vect.make(width, height);
		
		return Rect.make(origin, size);
	}
	
	
	@FactoryMethod
	public static Rect make(Vect origin, Num width, Num height)
	{
		return make(origin, Vect.make(width, height));
	}
	
	
	@FactoryMethod
	public static Rect make(final Vect origin, final Vect size)
	{
		return new RectVectAdapter(origin, size);
	}
	
	
	@FactoryMethod
	public static RectConst make(NumConst width, NumConst height)
	{
		final VectConst origin = Vect.ZERO;
		final VectConst size = Vect.make(width, height);
		
		return Rect.make(origin, size);
	}
	
	
	public static Rect make(Num side)
	{
		return make(side, side);
	}
	
	
	public static RectConst make(NumConst side)
	{
		return make(side, side);
	}
	
	
	public static RectConst make(double side)
	{
		return make(side, side);
	}
	
	
	@FactoryMethod
	public static RectConst make(NumConst x, NumConst y, NumConst width, NumConst height)
	{
		final VectConst origin = Vect.make(x, y);
		final VectConst size = Vect.make(width, height);
		
		return Rect.make(origin, size);
	}
	
	
	@FactoryMethod
	public static RectConst make(final VectConst origin, final VectConst size)
	{
		return new RectConst(origin, size);
	}
	
	
	@FactoryMethod
	public static RectConst make(double width, double height)
	{
		return Rect.make(0, 0, width, height);
	}
	
	
	@FactoryMethod
	public static RectConst make(double x, double y, double width, double height)
	{
		return new RectConst(x, y, width, height);
	}
	
	
	@FactoryMethod
	public static RectVar makeVar(double x, double y)
	{
		return Rect.makeVar(0, 0, x, y);
	}
	
	
	@FactoryMethod
	public static RectVar makeVar(Rect copied)
	{
		return Rect.makeVar(copied.origin(), copied.size());
	}
	
	
	@FactoryMethod
	public static RectVar makeVar(Vect origin, Vect size)
	{
		return Rect.makeVar(origin.x(), origin.y(), size.x(), size.y());
	}
	
	
	@FactoryMethod
	public static RectVar makeVar(double x, double y, double width, double height)
	{
		return new RectVar(x, y, width, height);
	}
	
	
	@FactoryMethod
	public static RectVar makeVar()
	{
		return Rect.makeVar(Rect.ZERO);
	}
	
	private Vect p_bl;
	private Vect p_bc;
	private Vect p_br;
	// p_t == origin
	private Vect p_tc;
	private Vect p_tr;
	
	private Vect p_cl;
	private Vect p_cc;
	private Vect p_cr;
	
	private Num p_x;
	private Num p_y;
	private Num p_w;
	private Num p_h;
	private Num p_l;
	private Num p_r;
	private Num p_t;
	private Num p_b;
	private Rect p_edge_l;
	private Rect p_edge_r;
	private Rect p_edge_t;
	private Rect p_edge_b;
	private Rect p_axis_v;
	private Rect p_axis_h;
	
	private final DigestCache<RectDigest> dc = new DigestCache<RectDigest>() {
		
		@Override
		protected RectDigest createDigest()
		{
			return new RectDigest(Rect.this);
		}
	};
	
	
	/**
	 * Get a copy of current value
	 * 
	 * @return copy
	 */
	public RectConst freeze()
	{
		// must NOT call RectVal.make, it'd cause infinite recursion.
		return new RectConst(this);
	}
	
	
	/**
	 * Wrap this constraint into a caching adapter. Value will stay fixed (ie.
	 * no re-calculations) until cache receives a poll() call.
	 * 
	 * @return the caching adapter
	 */
	public RectCache cached()
	{
		return new RectCache(this);
	}
	
	
	@Override
	public RectDigest digest()
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
	public Rect getRect()
	{
		return this;
	}
	
	
	@Override
	public String toString()
	{
		return String.format("Rect { at %s , size %s }", origin(), size());
	}
	
	
	/**
	 * Origin (top left).
	 * 
	 * @return origin (top left)
	 */
	public abstract Vect origin();
	
	
	/**
	 * Size (spanning right down from Origin).
	 * 
	 * @return size vector
	 */
	public abstract Vect size();
	
	
	/**
	 * Add vector to origin
	 * 
	 * @param move offset vector
	 * @return result
	 */
	public Rect move(final Vect move)
	{
		return new Rect() {
			
			private final Rect t = Rect.this;
			
			
			@Override
			public Vect size()
			{
				return t.size();
			}
			
			
			@Override
			public Vect origin()
			{
				return t.origin().add(move);
			}
			
		};
	}
	
	
	public Rect moveX(Num x)
	{
		return move(x, Num.ZERO);
	}
	
	
	public Rect moveY(Num y)
	{
		return move(Num.ZERO, y);
	}
	
	
	public Rect moveX(double x)
	{
		return move(x, 0);
	}
	
	
	public Rect moveY(double y)
	{
		return move(0, y);
	}
	
	
	/**
	 * Add X and Y to origin
	 * 
	 * @param x x to add
	 * @param y y to add
	 * @return result
	 */
	public Rect move(final double x, final double y)
	{
		return new Rect() {
			
			private final Rect t = Rect.this;
			
			
			@Override
			public Vect size()
			{
				return t.size();
			}
			
			
			@Override
			public Vect origin()
			{
				return t.origin().add(x, y);
			}
			
		};
	}
	
	
	public Rect move(final Num x, final Num y)
	{
		return new Rect() {
			
			private final Rect t = Rect.this;
			
			
			@Override
			public Vect size()
			{
				return t.size();
			}
			
			
			@Override
			public Vect origin()
			{
				return t.origin().add(x, y);
			}
			
		};
	}
	
	
	/**
	 * Shrink to sides
	 * 
	 * @param shrink shrink size (horizontal and vertical)
	 * @return result
	 */
	
	public Rect shrink(Vect shrink)
	{
		return shrink(shrink.x(), shrink.y());
	}
	
	
	/**
	 * Shrink to all sides
	 * 
	 * @param shrink shrink
	 * @return result
	 */
	public final Rect shrink(double shrink)
	{
		return shrink(shrink, shrink, shrink, shrink);
	}
	
	
	/**
	 * Shrink to all sides
	 * 
	 * @param shrink shrink
	 * @return result
	 */
	public final Rect shrink(Num shrink)
	{
		return shrink(shrink, shrink, shrink, shrink);
	}
	
	
	/**
	 * Shrink to sides at sides
	 * 
	 * @param x horizontal shrink
	 * @param y vertical shrink
	 * @return result
	 */
	public Rect shrink(double x, double y)
	{
		return shrink(x, x, y, y);
	}
	
	
	public Rect shrink(Num x, Num y)
	{
		return shrink(x, x, y, y);
	}
	
	
	/**
	 * Shrink the rect
	 * 
	 * @param left shrink
	 * @param right shrink
	 * @param top shrink
	 * @param bottom shrink
	 * @return result
	 */
	public Rect shrink(final double left, final double right, final double top, final double bottom)
	{
		return grow(-left, -right, -top, -bottom);
	}
	
	
	public Rect shrinkLeft(final double shrink)
	{
		return growLeft(-shrink);
	}
	
	
	public Rect shrinkRight(final double shrink)
	{
		return growRight(-shrink);
	}
	
	
	public Rect shrinkTop(final double shrink)
	{
		return growUp(-shrink);
	}
	
	
	public Rect shrinkBottom(final double shrink)
	{
		return growDown(-shrink);
	}
	
	
	public Rect growLeft(final double shrink)
	{
		return grow(shrink, 0, 0, 0);
	}
	
	
	public Rect growRight(final double shrink)
	{
		return grow(0, shrink, 0, 0);
	}
	
	
	public Rect growUp(final double shrink)
	{
		return grow(0, 0, shrink, 0);
	}
	
	
	public Rect growDown(final double shrink)
	{
		return grow(0, 0, 0, shrink);
	}
	
	
	public Rect shrinkLeft(final Num shrink)
	{
		return shrink(shrink, Num.ZERO, Num.ZERO, Num.ZERO);
	}
	
	
	public Rect shrinkRight(final Num shrink)
	{
		return shrink(Num.ZERO, shrink, Num.ZERO, Num.ZERO);
	}
	
	
	public Rect shrinkTop(final Num shrink)
	{
		return shrink(Num.ZERO, Num.ZERO, shrink, Num.ZERO);
	}
	
	
	public Rect shrinkBottom(final Num shrink)
	{
		return shrink(Num.ZERO, Num.ZERO, Num.ZERO, shrink);
	}
	
	
	public Rect growLeft(final Num shrink)
	{
		return grow(shrink, Num.ZERO, Num.ZERO, Num.ZERO);
	}
	
	
	public Rect growRight(final Num shrink)
	{
		return grow(Num.ZERO, shrink, Num.ZERO, Num.ZERO);
	}
	
	
	public Rect growUp(final Num shrink)
	{
		return grow(Num.ZERO, Num.ZERO, shrink, Num.ZERO);
	}
	
	
	public Rect growDown(final Num shrink)
	{
		return grow(Num.ZERO, Num.ZERO, Num.ZERO, shrink);
	}
	
	
	/**
	 * Grow to sides
	 * 
	 * @param grow grow size (added to each side)
	 * @return grown copy
	 */
	public final Rect grow(Vect grow)
	{
		return grow(grow.x(), grow.y());
	}
	
	
	/**
	 * Grow to all sides
	 * 
	 * @param grow grow
	 * @return result
	 */
	public final Rect grow(double grow)
	{
		return grow(grow, grow, grow, grow);
	}
	
	
	/**
	 * Grow to all sides
	 * 
	 * @param grow grow
	 * @return result
	 */
	public final Rect grow(Num grow)
	{
		return grow(grow, grow, grow, grow);
	}
	
	
	/**
	 * Grow to sides
	 * 
	 * @param x horizontal grow
	 * @param y vertical grow
	 * @return result
	 */
	public final Rect grow(double x, double y)
	{
		return grow(x, x, y, y);
	}
	
	
	public Rect grow(Num x, Num y)
	{
		return grow(x, x, y, y);
	}
	
	
	/**
	 * Grow the rect
	 * 
	 * @param left growth
	 * @param right growth
	 * @param top growth
	 * @param bottom growth
	 * @return result
	 */
	public Rect grow(final double left, final double right, final double top, final double bottom)
	{
		return new Rect() {
			
			private final Rect t = Rect.this;
			
			
			@Override
			public Vect size()
			{
				return t.size().add(left + right, top + bottom);
			}
			
			
			@Override
			public Vect origin()
			{
				return t.origin().sub(left, top);
			}
			
		};
	}
	
	
	public Rect shrink(final Num left, final Num right, final Num top, final Num bottom)
	{
		return new Rect() {
			
			private final Rect t = Rect.this;
			
			
			@Override
			public Vect size()
			{
				return t.size().sub(left.add(right), top.add(bottom));
			}
			
			
			@Override
			public Vect origin()
			{
				return t.origin().add(left, top);
			}
			
		};
	}
	
	
	public Rect grow(final Num left, final Num right, final Num top, final Num bottom)
	{
		
		return new Rect() {
			
			private final Rect t = Rect.this;
			
			
			@Override
			public Vect size()
			{
				return t.size().add(left.add(right), top.add(bottom));
			}
			
			
			@Override
			public Vect origin()
			{
				return t.origin().sub(left, top);
			}
			
		};
	}
	
	
	/**
	 * Round coords
	 * 
	 * @return result
	 */
	public Rect round()
	{
		
		return new Rect() {
			
			private final Rect t = Rect.this;
			
			
			@Override
			public Vect size()
			{
				return t.size().round();
			}
			
			
			@Override
			public Vect origin()
			{
				return t.origin().round();
			}
			
		};
	}
	
	
	/**
	 * Round coords down
	 * 
	 * @return result
	 */
	public Rect floor()
	{
		
		return new Rect() {
			
			private final Rect t = Rect.this;
			
			
			@Override
			public Vect size()
			{
				return t.size().floor();
			}
			
			
			@Override
			public Vect origin()
			{
				return t.origin().floor();
			}
			
		};
	}
	
	
	/**
	 * Round coords up
	 * 
	 * @return result
	 */
	public Rect ceil()
	{
		
		return new Rect() {
			
			private final Rect t = Rect.this;
			
			
			@Override
			public Vect size()
			{
				return t.size().ceil();
			}
			
			
			@Override
			public Vect origin()
			{
				return t.origin().ceil();
			}
			
		};
	}
	
	
	public Num x()
	{
		return p_x != null ? p_x : (p_x = origin().xn());
	}
	
	
	public Num y()
	{
		return p_y != null ? p_y : (p_y = origin().yn());
	}
	
	
	public Num width()
	{
		return p_w != null ? p_w : (p_w = size().xn());
	}
	
	
	public Num height()
	{
		return p_h != null ? p_h : (p_h = size().yn());
	}
	
	
	public Num left()
	{
		return p_l != null ? p_l : (p_l = origin().xn());
	}
	
	
	public Num right()
	{
		return p_r != null ? p_r : (p_r = origin().xn().add(size().xn()));
	}
	
	
	public Num top()
	{
		return p_t != null ? p_t : (p_t = origin().yn());
	}
	
	
	public Num bottom()
	{
		return p_b != null ? p_b : (p_b = origin().yn().add(size().yn()));
	}
	
	
	public Vect topLeft()
	{
		return origin();
	}
	
	
	public Vect topCenter()
	{
		return p_tc != null ? p_tc : (p_tc = origin().add(size().xn().half(), Num.ZERO));
	}
	
	
	public Vect topRight()
	{
		return p_tr != null ? p_tr : (p_tr = origin().add(size().xn(), Num.ZERO));
	}
	
	
	public Vect centerLeft()
	{
		return p_cl != null ? p_cl : (p_cl = origin().add(Num.ZERO, size().yn().half()));
	}
	
	
	public Vect center()
	{
		return p_cc != null ? p_cc : (p_cc = origin().add(size().half()));
	}
	
	
	public Vect centerRight()
	{
		return p_cr != null ? p_cr : (p_cr = origin().add(size().xn(), size().yn().half()));
	}
	
	
	public Vect bottomLeft()
	{
		return p_bl != null ? p_bl : (p_bl = origin().add(Num.ZERO, size().yn()));
	}
	
	
	public Vect bottomCenter()
	{
		return p_bc != null ? p_bc : (p_bc = origin().add(size().xn().half(), size().yn()));
	}
	
	
	public Vect bottomRight()
	{
		return p_br != null ? p_br : (p_br = origin().add(size().xn(), size().yn()));
	}
	
	
	public Rect leftEdge()
	{
		return p_edge_l != null ? p_edge_l : (p_edge_l = topLeft().expand(Num.ZERO, Num.ZERO, Num.ZERO, height()));
	}
	
	
	public Rect rightEdge()
	{
		return p_edge_r != null ? p_edge_r : (p_edge_r = topRight().expand(Num.ZERO, Num.ZERO, Num.ZERO, height()));
	}
	
	
	public Rect topEdge()
	{
		return p_edge_t != null ? p_edge_t : (p_edge_t = topLeft().expand(Num.ZERO, width(), Num.ZERO, Num.ZERO));
	}
	
	
	public Rect bottomEdge()
	{
		return p_edge_b != null ? p_edge_b : (p_edge_b = bottomLeft().expand(Num.ZERO, width(), Num.ZERO, Num.ZERO));
	}
	
	
	public Rect axisV()
	{
		return p_axis_v != null ? p_axis_v : (p_axis_v = topCenter().expand(Num.ZERO, Num.ZERO, Num.ZERO, height()));
	}
	
	
	public Rect axisH()
	{
		return p_axis_h != null ? p_axis_h : (p_axis_h = centerLeft().expand(Num.ZERO, width(), Num.ZERO, Num.ZERO));
	}
	
	
	/**
	 * Center to given point
	 * 
	 * @param point new center
	 * @return centered
	 */
	public Rect centerTo(final Vect point)
	{
		return new Rect() {
			
			Rect t = Rect.this;
			
			
			@Override
			public Vect size()
			{
				return t.size();
			}
			
			
			@Override
			public Vect origin()
			{
				return point.sub(t.size().half());
			}
		};
	}
	
	
	/**
	 * Check if point is inside this rectangle
	 * 
	 * @param point point to test
	 * @return is inside
	 */
	public boolean contains(Vect point)
	{
		final double x = point.x();
		final double y = point.y();
		
		final double x1 = origin().x();
		final double y1 = origin().y();
		final double x2 = x1 + size().x();
		final double y2 = y1 + size().y();
		
		return x >= x1 && y >= y1 && x <= x2 && y <= y2;
	}
	
	
	/**
	 * Center to given rect's center
	 * 
	 * @param parent rect to center to
	 * @return centered
	 */
	public Rect centerTo(Rect parent)
	{
		return centerTo(parent.center());
	}
	
	
	/**
	 * Get TiledRect with given number of evenly spaced tiles. Tile indexes are
	 * one-based by default.
	 * 
	 * @param horizontal horizontal tile count
	 * @param vertical vertical tile count
	 * @return tiled rect
	 */
	public TiledRect tiles(int horizontal, int vertical)
	{
		return new TiledRect(this, horizontal, vertical);
	}
	
	
	/**
	 * Get TiledRect with N columns and 1 row. Column indexes are one-based by
	 * default.
	 * 
	 * @param columns number of columns
	 * @return tiled rect
	 */
	public TiledRect columns(int columns)
	{
		return new TiledRect(this, columns, 1);
	}
	
	
	/**
	 * Get TiledRect with N rows and 1 column. Row indexes are one-based by
	 * default.
	 * 
	 * @param rows number of columns
	 * @return tiled rect
	 */
	public TiledRect rows(int rows)
	{
		return new TiledRect(this, 1, rows);
	}
	
	
	/**
	 * Check for intersection
	 * 
	 * @param other other rect
	 * @return true if they intersect
	 */
	public boolean intersectsWith(Rect other)
	{
		double tw = this.size().x();
		double th = this.size().y();
		double rw = other.size().x();
		double rh = other.size().y();
		
		if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
			return false;
		}
		
		final double tx = this.origin().x();
		final double ty = this.origin().y();
		final double rx = other.origin().x();
		final double ry = other.origin().y();
		
		rw += rx;
		rh += ry;
		tw += tx;
		th += ty;
		
		//      overflow || intersect
		return ((rw < rx || rw > tx) && (rh < ry || rh > ty) && (tw < tx || tw > rx) && (th < ty || th > ry));
	}
	
	
}

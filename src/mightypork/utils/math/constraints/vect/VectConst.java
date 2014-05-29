package mightypork.utils.math.constraints.vect;


import mightypork.utils.math.constraints.num.Num;
import mightypork.utils.math.constraints.num.NumConst;
import mightypork.utils.math.constraints.rect.RectConst;
import mightypork.utils.math.constraints.vect.caching.VectDigest;


/**
 * Coordinate with immutable numeric values.<br>
 * This coordinate is guaranteed to never change, as opposed to view.<br>
 * It's arranged so that operations with constant arguments yield constant
 * results.
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public final class VectConst extends Vect {
	
	private final double x, y, z;
	// non-parametric operations are cached using lazy load.
	private NumConst v_size;
	private VectConst v_neg;
	private VectConst v_ceil;
	private VectConst v_floor;
	private VectConst v_round;
	private VectConst v_half;
	private VectConst v_abs;
	private NumConst v_xc;
	private NumConst v_yc;
	private NumConst v_zc;
	private VectDigest digest;
	
	
	VectConst(Vect other)
	{
		this(other.x(), other.y(), other.z());
	}
	
	
	VectConst(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	@Override
	public double x()
	{
		return x;
	}
	
	
	@Override
	public double y()
	{
		return y;
	}
	
	
	@Override
	public double z()
	{
		return z;
	}
	
	
	/**
	 * @return X constraint
	 */
	
	@Override
	public final NumConst xn()
	{
		return (v_xc != null) ? v_xc : (v_xc = Num.make(this.x));
	}
	
	
	/**
	 * @return Y constraint
	 */
	
	@Override
	public final NumConst yn()
	{
		return (v_yc != null) ? v_yc : (v_yc = Num.make(this.y));
	}
	
	
	/**
	 * @return Z constraint
	 */
	
	@Override
	public final NumConst zn()
	{
		return (v_zc != null) ? v_zc : (v_zc = Num.make(this.z));
	}
	
	
	/**
	 * @deprecated it's useless to copy a constant
	 */
	
	@Override
	@Deprecated
	public VectConst freeze()
	{
		return this; // it's constant already
	}
	
	
	@Override
	public VectDigest digest()
	{
		return (digest != null) ? digest : (digest = super.digest());
	}
	
	
	@Override
	public VectConst abs()
	{
		return (v_abs != null) ? v_abs : (v_abs = super.abs().freeze());
	}
	
	
	@Override
	public VectConst add(double x, double y)
	{
		return super.add(x, y).freeze();
	}
	
	
	@Override
	public VectConst add(double x, double y, double z)
	{
		return super.add(x, y, z).freeze();
	}
	
	
	@Override
	public VectConst half()
	{
		return (v_half != null) ? v_half : (v_half = super.half().freeze());
	}
	
	
	@Override
	public VectConst mul(double d)
	{
		return super.mul(d).freeze();
	}
	
	
	@Override
	public VectConst mul(double x, double y)
	{
		return super.mul(x, y).freeze();
	}
	
	
	@Override
	public VectConst mul(double x, double y, double z)
	{
		return super.mul(x, y, z).freeze();
	}
	
	
	@Override
	public VectConst round()
	{
		return (v_round != null) ? v_round : (v_round = super.round().freeze());
	}
	
	
	@Override
	public VectConst floor()
	{
		return (v_floor != null) ? v_floor : (v_floor = super.floor().freeze());
	}
	
	
	@Override
	public VectConst ceil()
	{
		if (v_ceil != null) return v_ceil;
		return v_ceil = super.ceil().freeze();
	}
	
	
	@Override
	public VectConst sub(double x, double y)
	{
		return super.sub(x, y).freeze();
	}
	
	
	@Override
	public VectConst sub(double x, double y, double z)
	{
		return super.sub(x, y, z).freeze();
	}
	
	
	@Override
	public VectConst neg()
	{
		return (v_neg != null) ? v_neg : (v_neg = super.neg().freeze());
	}
	
	
	@Override
	public VectConst norm(double size)
	{
		return super.norm(size).freeze();
	}
	
	
	@Override
	public NumConst size()
	{
		return (v_size != null) ? v_size : (v_size = super.size().freeze());
	}
	
	
	@Override
	public VectConst withX(double x)
	{
		return super.withX(x).freeze();
	}
	
	
	@Override
	public VectConst withY(double y)
	{
		return super.withY(y).freeze();
	}
	
	
	@Override
	public VectConst withZ(double z)
	{
		return super.withZ(z).freeze();
	}
	
	
	public VectConst withX(NumConst x)
	{
		return super.withX(x).freeze();
	}
	
	
	public VectConst withY(NumConst y)
	{
		return super.withY(y).freeze();
	}
	
	
	public VectConst withZ(NumConst z)
	{
		return super.withZ(z).freeze();
	}
	
	
	public VectConst add(VectConst vec)
	{
		return super.add(vec).freeze();
	}
	
	
	public VectConst add(NumConst x, NumConst y)
	{
		return super.add(x, y).freeze();
	}
	
	
	public VectConst add(NumConst x, NumConst y, NumConst z)
	{
		return super.add(x, y, z).freeze();
	}
	
	
	public VectConst mul(VectConst vec)
	{
		return super.mul(vec).freeze();
	}
	
	
	public VectConst mul(NumConst d)
	{
		return super.mul(d).freeze();
	}
	
	
	public VectConst mul(NumConst x, NumConst y)
	{
		return super.mul(x, y).freeze();
	}
	
	
	public VectConst mul(NumConst x, NumConst y, NumConst z)
	{
		return super.mul(x, y, z).freeze();
	}
	
	
	public VectConst sub(VectConst vec)
	{
		return super.sub(vec).freeze();
	}
	
	
	public VectConst sub(NumConst x, NumConst y)
	{
		return super.sub(x, y).freeze();
	}
	
	
	public VectConst sub(NumConst x, NumConst y, NumConst z)
	{
		return super.sub(x, y, z).freeze();
	}
	
	
	public VectConst norm(NumConst size)
	{
		return super.norm(size).freeze();
	}
	
	
	public NumConst dist(VectConst point)
	{
		return super.dist(point).freeze();
	}
	
	
	public VectConst midTo(VectConst point)
	{
		return super.midTo(point).freeze();
	}
	
	
	public VectConst vectTo(VectConst point)
	{
		return super.vectTo(point).freeze();
	}
	
	
	public NumConst dot(VectConst vec)
	{
		return super.dot(vec).freeze();
	}
	
	
	public VectConst cross(VectConst vec)
	{
		return super.cross(vec).freeze();
	}
	
	
	@Override
	public RectConst expand(double left, double right, double top, double bottom)
	{
		return super.expand(left, right, top, bottom).freeze();
	}
	
	
	public RectConst expand(NumConst left, NumConst right, NumConst top, NumConst bottom)
	{
		return super.expand(left, right, top, bottom).freeze();
	}
	
}

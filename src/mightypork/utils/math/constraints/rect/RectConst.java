package mightypork.utils.math.constraints.rect;


import mightypork.utils.math.constraints.num.NumConst;
import mightypork.utils.math.constraints.rect.caching.RectDigest;
import mightypork.utils.math.constraints.vect.Vect;
import mightypork.utils.math.constraints.vect.VectConst;


/**
 * Rectangle with constant bounds, that can never change.<br>
 * It's arranged so that operations with constant arguments yield constant
 * results.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class RectConst extends Rect {

	private final VectConst pos;
	private final VectConst size;

	// cached with lazy loading
	private NumConst v_b;
	private NumConst v_r;
	private VectConst v_br;
	private VectConst v_tc;
	private VectConst v_tr;
	private VectConst v_cl;
	private VectConst v_c;
	private VectConst v_cr;
	private VectConst v_bl;
	private VectConst v_bc;
	private RectConst v_round;
	private RectConst v_edge_l;
	private RectConst v_edge_r;
	private RectConst v_edge_t;
	private RectConst v_edge_b;
	private RectDigest digest;
	private RectConst v_floor;
	private RectConst v_ceil;
	private RectConst v_axis_v;
	private RectConst v_axis_h;


	/**
	 * Create at given origin, with given size.
	 *
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	RectConst(double x, double y, double width, double height)
	{
		this.pos = Vect.make(x, y);
		this.size = Vect.make(width, height);
	}


	/**
	 * Create at given origin, with given size.
	 *
	 * @param origin
	 * @param size
	 */
	RectConst(Vect origin, Vect size)
	{
		this.pos = origin.freeze();
		this.size = size.freeze();
	}


	/**
	 * Create at given origin, with given size.
	 *
	 * @param another other coord
	 */
	RectConst(Rect another)
	{
		this.pos = another.origin().freeze();
		this.size = another.size().freeze();
	}


	/**
	 * @deprecated it's useless to copy a constant
	 */
	@Override
	@Deprecated
	public RectConst freeze()
	{
		return this; // already constant
	}


	@Override
	public RectDigest digest()
	{
		return (digest != null) ? digest : (digest = super.digest());
	}


	@Override
	public VectConst origin()
	{
		return pos;
	}


	@Override
	public VectConst size()
	{
		return size;
	}


	@Override
	public RectConst move(Vect move)
	{
		return move(move.x(), move.y());
	}


	@Override
	public RectConst move(double x, double y)
	{
		return Rect.make(pos.add(x, y), size);
	}


	public RectConst move(NumConst x, NumConst y)
	{
		return super.move(x, y).freeze();
	}


	@Override
	public RectConst shrink(double left, double right, double top, double bottom)
	{
		return super.shrink(left, right, top, bottom).freeze();

	}


	@Override
	public RectConst grow(double left, double right, double top, double bottom)
	{
		return super.grow(left, right, top, bottom).freeze();
	}


	@Override
	public RectConst round()
	{
		return (v_round != null) ? v_round : (v_round = Rect.make(pos.round(), size.round()));
	}


	@Override
	public RectConst floor()
	{
		return (v_floor != null) ? v_floor : (v_floor = Rect.make(pos.floor(), size.floor()));
	}


	@Override
	public RectConst ceil()
	{
		return (v_ceil != null) ? v_ceil : (v_ceil = Rect.make(pos.ceil(), size.ceil()));
	}


	@Override
	public NumConst x()
	{
		return pos.xn();
	}


	@Override
	public NumConst y()
	{
		return pos.yn();
	}


	@Override
	public NumConst width()
	{
		return size.xn();
	}


	@Override
	public NumConst height()
	{
		return size.yn();
	}


	@Override
	public NumConst left()
	{
		return pos.xn();
	}


	@Override
	public NumConst right()
	{
		return (v_r != null) ? v_r : (v_r = super.right().freeze());
	}


	@Override
	public NumConst top()
	{
		return pos.yn();
	}


	@Override
	public NumConst bottom()
	{
		return (v_b != null) ? v_b : (v_b = super.bottom().freeze());
	}


	@Override
	public VectConst topLeft()
	{
		return pos;
	}


	@Override
	public VectConst topCenter()
	{
		return (v_tc != null) ? v_tc : (v_tc = super.topCenter().freeze());
	}


	@Override
	public VectConst topRight()
	{
		return (v_tr != null) ? v_tr : (v_tr = super.topRight().freeze());
	}


	@Override
	public VectConst centerLeft()
	{
		return (v_cl != null) ? v_cl : (v_cl = super.centerLeft().freeze());
	}


	@Override
	public VectConst center()
	{
		return (v_c != null) ? v_c : (v_c = super.center().freeze());
	}


	@Override
	public VectConst centerRight()
	{
		return (v_cr != null) ? v_cr : (v_cr = super.centerRight().freeze());
	}


	@Override
	public VectConst bottomLeft()
	{
		return (v_bl != null) ? v_bl : (v_bl = super.bottomLeft().freeze());
	}


	@Override
	public VectConst bottomCenter()
	{
		return (v_bc != null) ? v_bc : (v_bc = super.bottomCenter().freeze());
	}


	@Override
	public VectConst bottomRight()
	{
		return (v_br != null) ? v_br : (v_br = super.bottomRight().freeze());
	}


	@Override
	public RectConst leftEdge()
	{
		return (v_edge_l != null) ? v_edge_l : (v_edge_l = super.leftEdge().freeze());
	}


	@Override
	public RectConst rightEdge()
	{
		return (v_edge_r != null) ? v_edge_r : (v_edge_r = super.rightEdge().freeze());
	}


	@Override
	public RectConst topEdge()
	{
		return (v_edge_t != null) ? v_edge_t : (v_edge_t = super.topEdge().freeze());
	}


	@Override
	public RectConst bottomEdge()
	{
		return (v_edge_b != null) ? v_edge_b : (v_edge_b = super.bottomEdge().freeze());
	}


	@Override
	public Rect axisV()
	{
		return (v_axis_v != null) ? v_axis_v : (v_axis_v = super.axisV().freeze());
	}


	@Override
	public Rect axisH()
	{
		return (v_axis_h != null) ? v_axis_h : (v_axis_h = super.axisH().freeze());
	}


	@Override
	public Rect shrink(Vect shrink)
	{
		return super.shrink(shrink);
	}


	@Override
	public RectConst shrink(double x, double y)
	{
		return super.shrink(x, y).freeze();
	}


	public RectConst shrink(NumConst left, NumConst right, NumConst top, NumConst bottom)
	{
		return super.shrink(left, right, top, bottom).freeze();
	}


	public RectConst grow(NumConst left, NumConst right, NumConst top, NumConst bottom)
	{
		return super.grow(left, right, top, bottom).freeze();
	}


	@Override
	public RectConst shrinkLeft(double shrink)
	{
		return super.shrinkLeft(shrink).freeze();
	}


	@Override
	public RectConst shrinkRight(double shrink)
	{
		return super.shrinkRight(shrink).freeze();
	}


	@Override
	public RectConst shrinkTop(double shrink)
	{
		return super.shrinkTop(shrink).freeze();
	}


	@Override
	public RectConst shrinkBottom(double shrink)
	{
		return super.shrinkBottom(shrink).freeze();
	}


	@Override
	public RectConst growLeft(double shrink)
	{
		return super.growLeft(shrink).freeze();
	}


	@Override
	public RectConst growRight(double shrink)
	{
		return super.growRight(shrink).freeze();
	}


	@Override
	public RectConst growUp(double shrink)
	{
		return super.growUp(shrink).freeze();
	}


	@Override
	public RectConst growDown(double shrink)
	{
		return super.growDown(shrink).freeze();
	}


	public RectConst shrinkLeft(NumConst shrink)
	{
		return super.shrinkLeft(shrink).freeze();
	}


	public RectConst shrinkRight(NumConst shrink)
	{
		return super.shrinkRight(shrink).freeze();
	}


	public RectConst shrinkTop(NumConst shrink)
	{
		return super.shrinkTop(shrink).freeze();
	}


	public RectConst shrinkBottom(NumConst shrink)
	{
		return super.shrinkBottom(shrink).freeze();
	}


	public RectConst growLeft(NumConst shrink)
	{
		return super.growLeft(shrink).freeze();
	}


	public RectConst growRight(NumConst shrink)
	{
		return super.growRight(shrink).freeze();
	}


	public RectConst growUp(NumConst shrink)
	{
		return super.growUp(shrink).freeze();
	}


	public RectConst growBottom(NumConst shrink)
	{
		return super.growDown(shrink).freeze();
	}


	public RectConst centerTo(VectConst point)
	{
		return super.centerTo(point).freeze();
	}


	public RectConst centerTo(RectConst parent)
	{
		return super.centerTo(parent).freeze();
	}


	public RectConst shrink(NumConst x, NumConst y)
	{
		return super.shrink(x, y).freeze();
	}


	public RectConst grow(NumConst x, NumConst y)
	{
		return super.grow(x, y).freeze();
	}
}

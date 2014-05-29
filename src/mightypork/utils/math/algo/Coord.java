package mightypork.utils.math.algo;


import mightypork.utils.annotations.FactoryMethod;
import mightypork.utils.math.Calc;
import mightypork.utils.math.constraints.vect.Vect;
import mightypork.utils.math.constraints.vect.VectConst;


/**
 * Very simple integer coordinate
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class Coord {
	
	public int x;
	public int y;
	
	
	@FactoryMethod
	public static Coord make(int x, int y)
	{
		return new Coord(x, y);
	}
	
	
	@FactoryMethod
	public static Coord make(Coord other)
	{
		return new Coord(other);
	}
	
	
	@FactoryMethod
	public static Coord zero()
	{
		return make(0, 0);
	}
	
	
	public Coord()
	{
		// for ion
	}
	
	
	public Coord(int x, int y)
	{
		super();
		this.x = x;
		this.y = y;
	}
	
	
	public Coord(Coord other)
	{
		this.x = other.x;
		this.y = other.y;
	}
	
	
	public Coord add(int addX, int addY)
	{
		return new Coord(x + addX, y + addY);
	}
	
	
	/**
	 * Add other coord in a copy
	 * 
	 * @param added
	 * @return changed copy
	 */
	public Coord add(Coord added)
	{
		return add(added.x, added.y);
	}
	
	
	public Coord add(Move added)
	{
		return add(added.x(), added.y());
	}
	
	
	public Coord copy()
	{
		return make(this);
	}
	
	
	public void setTo(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	
	public void setTo(Coord pos)
	{
		setTo(pos.x, pos.y);
	}
	
	
	/**
	 * Check if coord is in a range (inclusive)
	 * 
	 * @param x0 range min x
	 * @param y0 range min y
	 * @param x1 range max x
	 * @param y1 range max y
	 * @return is inside
	 */
	public boolean isInRange(int x0, int y0, int x1, int y1)
	{
		return !(x < x0 || x > x1 || y < y0 || y > y1);
	}
	
	
	public double dist(Coord coord)
	{
		return Calc.dist(x, y, coord.x, coord.y);
	}
	
	
	public VectConst toVect()
	{
		return Vect.make(x, y);
	}
	
	
	@Override
	public String toString()
	{
		return "Coord(" + x + "," + y + ")";
	}
	
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Coord)) return false;
		final Coord other = (Coord) obj;
		if (x != other.x) return false;
		if (y != other.y) return false;
		return true;
	}
	
	
	public static Coord fromVect(Vect vect)
	{
		return make((int) Math.floor(vect.x()), (int) Math.floor(vect.y()));
	}
}

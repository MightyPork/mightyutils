package mightypork.utils.math.constraints.rect.var;


import mightypork.utils.math.constraints.rect.Rect;
import mightypork.utils.math.constraints.vect.Vect;


/**
 * Mutable rectangle; operations change it's state.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class RectMutable extends Rect {

	/**
	 * Set to other rect's coordinates
	 *
	 * @param rect other rect
	 */
	public void setTo(Rect rect)
	{
		setTo(rect.origin(), rect.size());
	}


	/**
	 * Set to given size and position
	 *
	 * @param origin new origin
	 * @param width new width
	 * @param height new height
	 */
	public void setTo(Vect origin, double width, double height)
	{
		setTo(origin, Vect.make(width, height));
	}


	/**
	 * Set to given size and position
	 *
	 * @param x origin.x
	 * @param y origin.y
	 * @param width new width
	 * @param height new height
	 */
	public void setTo(double x, double y, double width, double height)
	{
		setTo(Vect.make(x, y), Vect.make(width, height));
	}


	/**
	 * Set to given size and position
	 *
	 * @param origin new origin
	 * @param size new size
	 */
	public void setTo(Vect origin, Vect size)
	{
		setOrigin(origin);
		setSize(size);
	}


	/**
	 * Set to zero
	 */
	public void reset()
	{
		setTo(Vect.ZERO, Vect.ZERO);
	}


	public abstract void setOrigin(double x, double y);


	public void setOrigin(Vect origin)
	{
		setOrigin(origin.x(), origin.y());
	}


	public void setSize(Vect size)
	{
		setSize(size.x(), size.y());
	}


	public abstract void setSize(double x, double y);
}

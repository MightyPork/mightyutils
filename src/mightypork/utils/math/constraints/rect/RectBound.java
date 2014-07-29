package mightypork.utils.math.constraints.rect;


/**
 * Interface for something that has/is a Rect. Rect itself implements it as
 * well.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public interface RectBound {
	
	/**
	 * @return the Rect value
	 */
	Rect getRect();
}

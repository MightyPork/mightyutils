package mightypork.utils.math.constraints.rect;


/**
 * Pluggable rect bound
 *
 * @author Ondřej Hruška (MightyPork)
 */
public interface PluggableRectBound extends RectBound {

	/**
	 * @param rect context to set
	 */
	abstract void setRect(RectBound rect);

}

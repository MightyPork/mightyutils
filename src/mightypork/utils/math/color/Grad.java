package mightypork.utils.math.color;


/**
 * Linear gradient (each corner can have different color)
 *
 * @author MightyPork
 */
public class Grad {
	
	public final Color leftTop, rightTop, rightBottom, leftBottom;
	
	
	/**
	 * Create a gradient
	 *
	 * @param leftTop left top color
	 * @param rightTop right top color
	 * @param rightBottom right bottom color
	 * @param leftBottom left bottom color
	 */
	public Grad(Color leftTop, Color rightTop, Color rightBottom, Color leftBottom)
	{
		this.leftTop = leftTop;
		this.rightTop = rightTop;
		this.rightBottom = rightBottom;
		this.leftBottom = leftBottom;
	}
}

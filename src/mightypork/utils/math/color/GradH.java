package mightypork.utils.math.color;


/**
 * Linear horizontal gradient
 *
 * @author MightyPork
 */
public class GradH extends Grad {

	public GradH(Color left, Color right)
	{
		super(left, right, right, left);
	}
}

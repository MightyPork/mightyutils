package mightypork.utils.math.animation;


/**
 * Animator that upon reaching max, animates back down and then up again
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class AnimatorBounce extends Animator {
	
	private boolean wasUp = false;
	
	
	public AnimatorBounce(double start, double end, double period, Easing easing)
	{
		super(start, end, period, easing);
	}
	
	
	public AnimatorBounce(double start, double end, double period)
	{
		super(start, end, period);
	}
	
	
	public AnimatorBounce(double period, Easing easing)
	{
		super(period, easing);
	}
	
	
	public AnimatorBounce(double period)
	{
		super(period);
	}
	
	
	@Override
	protected void nextCycle(NumAnimated anim)
	{
		if (wasUp) {
			anim.fadeOut();
		} else {
			anim.fadeIn();
		}
		
		wasUp = !wasUp;
	}
	
}

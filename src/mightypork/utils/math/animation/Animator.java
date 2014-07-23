package mightypork.utils.math.animation;


import mightypork.utils.annotations.Stub;
import mightypork.utils.interfaces.Pauseable;
import mightypork.utils.interfaces.Updateable;
import mightypork.utils.math.Calc;
import mightypork.utils.math.constraints.num.Num;
import mightypork.utils.math.constraints.num.NumBound;


public abstract class Animator implements NumBound, Updateable, Pauseable {
	
	private final NumAnimated numAnim;
	private final Num animatorValue;
	private final double highValue;
	private final double lowValue;
	
	
	public Animator(double period)
	{
		this(0, 1, period, Easing.LINEAR);
	}
	
	
	public Animator(double start, double end, double period)
	{
		this(start, end, period, Easing.LINEAR);
	}
	
	
	public Animator(double period, Easing easing)
	{
		this(0, 1, period, easing);
	}
	
	
	public Animator(double start, double end, double period, Easing easing)
	{
		numAnim = new NumAnimated(0, easing);
		numAnim.setDefaultDuration(period);
		
		this.lowValue = start;
		this.highValue = end;
		
		this.animatorValue = numAnim.mul(end - start).add(start);
	}
	
	
	@Override
	public void pause()
	{
		numAnim.pause();
	}
	
	
	public void start()
	{
		resume();
	}
	
	
	@Override
	public void resume()
	{
		numAnim.resume();
	}
	
	
	@Override
	public boolean isPaused()
	{
		return numAnim.isPaused();
	}
	
	
	public void reset()
	{
		numAnim.reset();
	}
	
	
	public void restart()
	{
		reset();
		resume();
	}
	
	
	public void setDuration(double secs)
	{
		numAnim.setDefaultDuration(secs);
	}
	
	
	public double getDuration()
	{
		return numAnim.getDefaultDuration();
	}
	
	
	@Override
	public Num getNum()
	{
		return animatorValue;
	}
	
	
	public double getValue()
	{
		return animatorValue.value();
	}
	
	
	@Override
	public void update(double delta)
	{
		numAnim.update(delta);
		if (numAnim.isFinished()) nextCycle(numAnim);
	}
	
	
	@Stub
	protected abstract void nextCycle(NumAnimated anim);
	
	
	public void setProgress(double value)
	{
		final double target = numAnim.getEnd();
		numAnim.setTo(Calc.clamp(value, lowValue, highValue));
		numAnim.animate((target < value ? highValue : lowValue), target, numAnim.getDefaultDuration());
	}
	
	
	public double getProgress()
	{
		return numAnim.value();
	}
}

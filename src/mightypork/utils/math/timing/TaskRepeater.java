package mightypork.utils.math.timing;


import mightypork.utils.interfaces.Enableable;
import mightypork.utils.math.animation.AnimatorRewind;
import mightypork.utils.math.animation.NumAnimated;


public abstract class TaskRepeater extends AnimatorRewind implements Runnable, Enableable {
	
	private boolean enabled = true;
	
	
	public TaskRepeater(double period) {
		super(period);
	}
	
	
	@Override
	protected void nextCycle(NumAnimated anim)
	{
		if (isEnabled()) run();
		super.nextCycle(anim);
	}
	
	
	@Override
	public void setEnabled(boolean yes)
	{
		this.enabled = yes;
	}
	
	
	@Override
	public boolean isEnabled()
	{
		return enabled;
	}
	
	
	@Override
	public abstract void run();
	
}

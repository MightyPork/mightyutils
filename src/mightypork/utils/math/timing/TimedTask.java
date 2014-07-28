package mightypork.utils.math.timing;


import mightypork.utils.interfaces.Updateable;
import mightypork.utils.math.animation.NumAnimated;


/**
 * Delayed runnable controlled by delta timing.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class TimedTask implements Runnable, Updateable {

	private final NumAnimated timer = new NumAnimated(0);
	private boolean running = false;


	@Override
	public void update(double delta)
	{
		if (running) {
			timer.update(delta);
			if (timer.isFinished()) {
				running = false;
				run();
			}
		}
	}


	public boolean isRunning()
	{
		return !timer.isFinished();
	}


	public void start(double seconds)
	{
		timer.reset();
		timer.animate(1, seconds);
		running = true;
	}


	public void stop()
	{
		running = false;
		timer.reset();
	}

}

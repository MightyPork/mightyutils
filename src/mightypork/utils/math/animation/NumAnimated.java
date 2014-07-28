package mightypork.utils.math.animation;


import mightypork.utils.interfaces.Pauseable;
import mightypork.utils.interfaces.Updateable;
import mightypork.utils.math.Calc;
import mightypork.utils.math.constraints.num.var.NumMutable;


/**
 * Double which supports delta timing.<br>
 * When both in and out easings are set differently, then they'll be used for
 * fade-in and fade-out respectively. Otherwise both use the same.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class NumAnimated extends NumMutable implements Updateable, Pauseable {

	/** target double */
	protected double to = 0;

	/** last tick double */
	protected double from = 0;

	/** how long the transition should last */
	protected double duration = 0;

	/** current anim time */
	protected double elapsedTime = 0;

	/** True if this animator is paused */
	protected boolean paused = false;

	/** Easing fn */
	protected Easing easingCurrent = Easing.LINEAR;
	protected Easing easingOut = Easing.LINEAR;
	protected Easing easingIn = Easing.LINEAR;

	/** Default duration (seconds) */
	private double defaultDuration = 1;


	/**
	 * With linear easing
	 *
	 * @param value initial value
	 */
	public NumAnimated(double value)
	{
		setTo(value);
	}


	/**
	 * Create animator with easing
	 *
	 * @param value initial value
	 * @param easing easing function
	 */
	public NumAnimated(double value, Easing easing)
	{
		this(value);
		setEasing(easing);
	}


	/**
	 * Create animator with easing
	 *
	 * @param value initial value
	 * @param easingIn easing function (fade in)
	 * @param easingOut easing function (fade out)
	 */
	public NumAnimated(double value, Easing easingIn, Easing easingOut)
	{
		this(value);
		setEasing(easingIn, easingOut);
	}


	/**
	 * Create animator with easing
	 *
	 * @param value initial value
	 * @param easing easing function
	 * @param defaultDuration default fade duration
	 */
	public NumAnimated(double value, Easing easing, double defaultDuration)
	{
		this(value);
		setEasing(easing);
		setDefaultDuration(defaultDuration);
	}


	/**
	 * Create animator with easing
	 *
	 * @param value initial value
	 * @param easingIn easing function (fade in)
	 * @param easingOut easing function (fade out)
	 * @param defaultDuration default fade duration
	 */
	public NumAnimated(double value, Easing easingIn, Easing easingOut, double defaultDuration)
	{
		this(value);
		setEasing(easingIn, easingOut);
		setDefaultDuration(defaultDuration);
	}


	/**
	 * Create as copy of another
	 *
	 * @param other other animator
	 */
	public NumAnimated(NumAnimated other)
	{
		setTo(other);
	}


	/**
	 * @param easing easing function
	 */
	public void setEasing(Easing easing)
	{
		this.easingCurrent = this.easingIn = this.easingOut = easing;
	}


	/**
	 * @param easingIn easing for fade in
	 * @param easingOut easing for fade out
	 */
	public void setEasing(Easing easingIn, Easing easingOut)
	{
		this.easingIn = easingIn;
		this.easingOut = easingOut;
		this.easingCurrent = easingIn;
	}


	/**
	 * Get start value
	 *
	 * @return number
	 */
	public double getStart()
	{
		return from;
	}


	/**
	 * Get end value
	 *
	 * @return number
	 */
	public double getEnd()
	{
		return to;
	}


	/**
	 * @return current animation duration (seconds)
	 */
	public double getDuration()
	{
		return duration;
	}


	/**
	 * @return elapsed time in current animation (seconds)
	 */
	public double getElapsed()
	{
		return elapsedTime;
	}


	/**
	 * @return default animation duration (seconds)
	 */
	public double getDefaultDuration()
	{
		return defaultDuration;
	}


	/**
	 * @param defaultDuration default animation duration (seconds)
	 */
	public void setDefaultDuration(double defaultDuration)
	{
		this.defaultDuration = defaultDuration;
	}


	/**
	 * Get value at delta time
	 *
	 * @return the value
	 */
	@Override
	public double value()
	{
		if (duration == 0) return to;
		return Calc.interpolate(from, to, (elapsedTime / duration), easingCurrent);
	}


	/**
	 * Get how much of the animation is already finished
	 *
	 * @return completion ratio (0 to 1)
	 */
	public double getProgress()
	{
		if (duration == 0) return 1;
		return elapsedTime / duration;
	}


	@Override
	public void update(double delta)
	{
		if (paused || isFinished()) return;

		elapsedTime = Calc.clamp(elapsedTime + delta, 0, duration);
		if (isFinished()) {
			duration = 0;
			elapsedTime = 0;
			from = to;
		}
	}


	/**
	 * Get if animation is finished
	 *
	 * @return is finished
	 */
	public boolean isFinished()
	{
		return duration == 0 || elapsedTime >= duration;
	}


	/**
	 * Set to a value (without animation)
	 *
	 * @param value
	 */
	@Override
	public void setTo(double value)
	{
		from = to = value;
		elapsedTime = 0;
		duration = 0;
	}


	/**
	 * Copy other
	 *
	 * @param other
	 */
	public void setTo(NumAnimated other)
	{
		this.from = other.from;
		this.to = other.to;
		this.duration = other.duration;
		this.elapsedTime = other.elapsedTime;
		this.paused = other.paused;
		this.easingCurrent = other.easingCurrent;
		this.easingIn = other.easingIn;
		this.easingOut = other.easingOut;
		this.defaultDuration = other.defaultDuration;
	}


	/**
	 * Animate between two states, start from current value (if it's in between)
	 *
	 * @param from start value
	 * @param to target state
	 * @param time animation time (secs)
	 */
	public void animate(double from, double to, double time)
	{
		final double current = value();

		this.from = from;
		this.to = to;

		final double progress = getProgressFromValue(current);

		this.from = (progress > 0 ? current : from);

		this.duration = time * (1 - progress);
		this.elapsedTime = 0;
	}


	/**
	 * Get progress already elapsed based on current value.<br>
	 * Used to resume animation from current point in fading etc.
	 *
	 * @param value current value
	 * @return progress ratio 0-1
	 */
	protected double getProgressFromValue(double value)
	{
		double p = 0;

		if (from == to) return 0;

		if (value >= from && value <= to) { // up
			p = ((value - from) / (to - from));
		} else if (value >= to && value <= from) { // down
			p = ((from - value) / (from - to));
		}

		return p;
	}


	/**
	 * Animate to a value from current value
	 *
	 * @param to target state
	 * @param duration animation duration (speeds)
	 */
	public void animate(double to, double duration)
	{
		this.from = value();
		this.to = to;
		this.duration = duration;
		this.elapsedTime = 0;
	}


	/**
	 * Animate 0 to 1
	 *
	 * @param time animation time (secs)
	 */
	public void fadeIn(double time)
	{
		easingCurrent = easingIn;
		animate(0, 1, time);
	}


	/**
	 * Animate 1 to 0
	 *
	 * @param time animation time (secs)
	 */
	public void fadeOut(double time)
	{
		easingCurrent = easingOut;
		animate(1, 0, time);
	}


	/**
	 * Animate 0 to 1 with default duration
	 */
	public void fadeIn()
	{
		easingCurrent = easingIn;
		animate(0, 1, defaultDuration);
	}


	/**
	 * Animate 1 to 0 with default duration
	 */
	public void fadeOut()
	{
		easingCurrent = easingOut;
		animate(1, 0, defaultDuration);
	}


	/**
	 * Make a copy
	 *
	 * @return copy
	 */
	@Override
	public NumAnimated clone()
	{
		return new NumAnimated(this);
	}


	@Override
	public String toString()
	{
		return "Animation(" + from + " -> " + to + ", t=" + duration + "s, elapsed=" + elapsedTime + "s)";
	}


	/**
	 * Set to zero and stop animation
	 */
	public void clear()
	{
		from = to = 0;
		elapsedTime = 0;
		duration = 0;
		paused = false;
	}


	/**
	 * Stop animation, keep current value
	 */
	public void stop()
	{
		from = to = value();
		elapsedTime = 0;
		duration = 0;
	}


	@Override
	public void pause()
	{
		paused = true;
	}


	@Override
	public void resume()
	{
		paused = false;
	}


	@Override
	public boolean isPaused()
	{
		return paused;
	}


	public boolean isInProgress()
	{
		return !isFinished() && !isPaused();
	}
}

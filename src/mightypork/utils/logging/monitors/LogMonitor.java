package mightypork.utils.logging.monitors;


import java.util.logging.Level;


public abstract class LogMonitor {

	private boolean enabled = true;
	private Level accepted = Level.ALL;


	public void onMessageLogged(Level level, String message)
	{
		if (!enabled) return;
		if (accepted.intValue() > level.intValue()) return;

		logMessage(level, message);
	}


	protected abstract void logMessage(Level level, String message);


	public void setLevel(Level level)
	{
		this.accepted = level;
	}


	public void enable(boolean flag)
	{
		this.enabled = flag;
	}

}

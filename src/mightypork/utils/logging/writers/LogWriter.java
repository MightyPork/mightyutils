package mightypork.utils.logging.writers;


import java.util.logging.Level;

import mightypork.utils.logging.monitors.LogMonitor;


/**
 * Log interface
 *
 * @author Ondřej Hruška (MightyPork)
 */
public interface LogWriter {
	
	/**
	 * Prepare logs for logging
	 */
	void init();
	
	
	/**
	 * Add log monitor
	 *
	 * @param mon monitor
	 */
	void addMonitor(LogMonitor mon);
	
	
	/**
	 * Remove a monitor
	 *
	 * @param removed monitor to remove
	 */
	void removeMonitor(LogMonitor removed);
	
	
	/**
	 * Set logging level
	 *
	 * @param level
	 */
	void setLevel(Level level);
	
	
	/**
	 * Enable logging.
	 *
	 * @param flag do enable logging
	 */
	void enable(boolean flag);
	
	
	/**
	 * Log a message
	 *
	 * @param level message level
	 * @param msg message text
	 */
	void log(Level level, String msg);
	
	
	/**
	 * Log a message
	 *
	 * @param level message level
	 * @param msg message text
	 * @param t thrown exception
	 */
	void log(Level level, String msg, Throwable t);
	
}

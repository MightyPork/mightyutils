package mightypork.utils.logging.writers;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import mightypork.utils.logging.Log;
import mightypork.utils.logging.monitors.LogMonitor;


/**
 * Basic logger
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class SimpleLog implements LogWriter {
	
	/**
	 * Log file formatter.
	 */
	class LogFormatter extends Formatter {
		
		@Override
		public String format(LogRecord record)
		{
			return Log.formatMessage(record.getLevel(), record.getMessage(), record.getThrown(), started_ms);
		}
	}
	
	/** Log file */
	private final File file;
	
	/** Log name */
	private final String name;
	
	/** Logger instance. */
	private Logger logger;
	
	private boolean enabled = true;
	private final HashSet<LogMonitor> monitors = new HashSet<>();
	private final long started_ms;
	
	
	public SimpleLog(String name, File file)
	{
		this.name = name;
		this.file = file;
		this.started_ms = System.currentTimeMillis();
	}
	
	
	@Override
	public void init()
	{
		logger = Logger.getLogger(getName());
		
		FileHandler handler = null;
		try {
			handler = new FileHandler(getFile().getPath());
		} catch (final Throwable t) {
			throw new RuntimeException("Failed to init log.", t);
		}
		
		handler.setFormatter(new LogFormatter());
		logger.addHandler(handler);
		logger.setUseParentHandlers(false);
		logger.setLevel(Level.ALL);
		
		printHeader();
	}
	
	
	protected void printHeader()
	{
		final String stamp = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
		log(Level.INFO, "Logger \"" + getName() + "\" initialized.\n" + stamp);
	}
	
	
	/**
	 * Add log monitor
	 *
	 * @param mon monitor
	 */
	@Override
	public synchronized void addMonitor(LogMonitor mon)
	{
		monitors.add(mon);
	}
	
	
	/**
	 * Remove a monitor
	 *
	 * @param removed monitor to remove
	 */
	@Override
	public synchronized void removeMonitor(LogMonitor removed)
	{
		monitors.remove(removed);
	}
	
	
	@Override
	public void setLevel(Level level)
	{
		logger.setLevel(level);
	}
	
	
	@Override
	public void enable(boolean flag)
	{
		enabled = flag;
	}
	
	
	public File getFile()
	{
		return file;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	@Override
	public void log(Level level, String msg)
	{
		if (enabled) {
			logger.log(level, msg);
			
			final String fmt = Log.formatMessage(level, msg, null, started_ms);
			
			for (final LogMonitor mon : monitors) {
				mon.onMessageLogged(level, fmt);
			}
		}
	}
	
	
	@Override
	public void log(Level level, String msg, Throwable t)
	{
		if (enabled) {
			logger.log(level, msg, t);
			
			final String fmt = Log.formatMessage(level, msg, t, started_ms);
			
			for (final LogMonitor mon : monitors) {
				mon.onMessageLogged(level, fmt);
			}
		}
	}
	
}

package mightypork.utils.logging.writers;


import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import mightypork.utils.files.FileUtils;
import mightypork.utils.string.StringUtil;


/**
 * Logger that cleans directory & archives old logs
 * 
 * @author Ondřej Hruška (MightyPork)
 * @copy (c) 2014
 */
public class ArchivingLog extends SimpleLog {
	
	/** Number of old logs to keep */
	private final int logs_to_keep;
	
	
	/**
	 * Log
	 * 
	 * @param name log name
	 * @param file log file (in log directory)
	 * @param oldLogCount number of old log files to keep: -1 all, 0 none.
	 */
	public ArchivingLog(String name, File file, int oldLogCount)
	{
		super(name, file);
		this.logs_to_keep = oldLogCount;
	}
	
	
	/**
	 * Log, not keeping 5 last log files (default);
	 * 
	 * @param name log name
	 * @param file log file (in log directory)
	 */
	public ArchivingLog(String name, File file)
	{
		super(name, file);
		this.logs_to_keep = 5;
	}
	
	
	@Override
	public void init()
	{
		cleanLoggingDirectory();
		
		super.init();
	}
	
	
	private void cleanLoggingDirectory()
	{
		if (logs_to_keep == 0) return; // overwrite
		
		final File log_file = getFile();
		final File log_dir = log_file.getParentFile();
		final String fname = FileUtils.getBasename(log_file.toString());
		
		// move old file
		for (final File f : FileUtils.listDirectory(log_dir)) {
			if (!f.isFile()) continue;
			if (f.equals(getFile())) {
				
				final Date d = new Date(f.lastModified());
				final String fbase = fname + '_' + (new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")).format(d);
				final String suff = "." + getSuffix();
				String cntStr = "";
				File f2;
				
				for (int cnt = 0; (f2 = new File(log_dir, fbase + cntStr + suff)).exists(); cntStr = "_" + (++cnt)) {}
				
				if (!f.renameTo(f2)) throw new RuntimeException("Could not move log file.");
			}
		}
		
		if (logs_to_keep == -1) return; // keep all
		
		final List<File> oldLogs = FileUtils.listDirectory(log_dir, new FileFilter() {
			
			@Override
			public boolean accept(File f)
			{
				if (f.isDirectory()) return false;
				if (!f.getName().endsWith(getSuffix())) return false;
				if (!f.getName().startsWith(fname)) return false;
				
				return true;
			}
			
		});
		
		Collections.sort(oldLogs, new Comparator<File>() {
			
			@Override
			public int compare(File o1, File o2)
			{
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		// playing with fireee
		for (int i = 0; i < oldLogs.size() - logs_to_keep; i++) {
			if (!oldLogs.get(i).delete()) {
				throw new RuntimeException("Could not delete old log file.");
			}
		}
	}
	
	
	/**
	 * @return log filename suffix
	 */
	private String getSuffix()
	{
		return StringUtil.fromLastChar(getFile().toString(), '.');
	}
}

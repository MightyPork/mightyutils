package mightypork.utils.files;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import mightypork.utils.logging.Log;


/**
 * Working directory helper.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class WorkDir {
	
	private static File baseDir = new File(".");
	private static Map<String, String> namedPaths = new HashMap<>();


	/**
	 * Initialize the workdir for the given root path
	 *
	 * @param workdir workdir root path
	 */
	public static void setBaseDir(File workdir)
	{
		WorkDir.baseDir = workdir;
	}
	
	
	/**
	 * Add a path alias (dir or file), relative to the workdir.
	 *
	 * @param alias path alias
	 * @param path path relative to workdir
	 */
	public static void addPath(String alias, String path)
	{
		namedPaths.put(alias, path);
	}
	
	
	/**
	 * Get workdir folder, create if not exists.
	 *
	 * @param path dir path relative to workdir
	 * @return dir file
	 */
	public static File getDir(String path)
	{
		if (namedPaths.containsKey(path)) path = namedPaths.get(path);
		
		final File f = new File(baseDir, path);
		if (!f.exists()) {
			if (!f.mkdirs()) {
				Log.w("Could not create a directory: " + f + " (path: " + path + ")");
			}
		}
		
		return f;
	}
	
	
	/**
	 * Get workdir file, create parent if not exists.
	 *
	 * @param path dir path relative to workdir
	 * @return dir file
	 */
	public static File getFile(String path)
	{
		if (namedPaths.containsKey(path)) path = namedPaths.get(path);
		
		final File f = new File(baseDir, path);
		
		// create the parent dir
		if (!f.getParent().equals(baseDir)) {
			f.getParentFile().mkdirs();
		}
		
		return f;
		
	}
	
	
	/**
	 * @return the workdir File
	 */
	public static File getBaseDir()
	{
		return baseDir;
	}
	
}

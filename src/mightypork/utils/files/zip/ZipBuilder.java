package mightypork.utils.files.zip;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import mightypork.utils.files.FileUtil;
import mightypork.utils.logging.Log;


/**
 * Class for building a zip file
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class ZipBuilder {
	
	private final ZipOutputStream out;
	private final HashSet<String> included = new HashSet<>();
	
	
	/**
	 * @param target target zip file
	 * @throws IOException if the file is directory or cannot be created
	 */
	public ZipBuilder(File target) throws IOException {
		
		if (!target.getParentFile().mkdirs()) throw new IOException("Could not create output directory.");
		
		final FileOutputStream dest = new FileOutputStream(target);
		out = new ZipOutputStream(new BufferedOutputStream(dest));
	}
	
	
	/**
	 * Add stream to a path
	 * 
	 * @param path path
	 * @param in stream
	 * @throws IOException
	 */
	public void addStream(String path, InputStream in) throws IOException
	{
		path = preparePath(path);
		if (included.contains(path)) {
			Log.f3("Zip already contains file " + path + ", skipping.");
			return; // ignore
		}
		included.add(path);
		
		out.putNextEntry(new ZipEntry(path));
		
		FileUtil.copyStream(in, out);
	}
	
	
	/**
	 * Add string as a file
	 * 
	 * @param path path
	 * @param text text to write
	 * @throws IOException
	 */
	public void addString(String path, String text) throws IOException
	{
		path = preparePath(path);
		if (included.contains(path)) return; // ignore
		included.add(path);
		
		out.putNextEntry(new ZipEntry(path));
		
		try (InputStream in = FileUtil.stringToStream(text)) {
			FileUtil.copyStream(in, out);
		}
	}
	
	
	/**
	 * Add resource obtained via FileUtils.getResource()
	 * 
	 * @param path path
	 * @param resPath resource path
	 * @throws IOException
	 */
	public void addResource(String path, String resPath) throws IOException
	{
		path = preparePath(path);
		if (included.contains(path)) return; // ignore
		included.add(path);
		
		out.putNextEntry(new ZipEntry(path));
		
		try (InputStream in = FileUtil.getResource(resPath)) {
			FileUtil.copyStream(in, out);
		}
	}
	
	
	/**
	 * Normalize path
	 * 
	 * @param path original path
	 * @return normalized path
	 */
	private static String preparePath(String path)
	{
		path = path.replace("\\", "/");
		
		if (path.charAt(0) == '/') path = path.substring(1);
		
		return path;
	}
	
	
	/**
	 * Close the zip stream
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException
	{
		out.close();
	}
}

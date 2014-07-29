package mightypork.utils.files;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import mightypork.utils.Str;
import mightypork.utils.logging.Log;
import mightypork.utils.string.validation.StringFilter;


public class FileUtil {
	
	
	/**
	 * Copy directory recursively.
	 *
	 * @param source source file
	 * @param target target file
	 * @throws IOException on error
	 */
	public static void copyDirectory(File source, File target) throws IOException
	{
		copyDirectory(source, target, null, null);
	}
	
	
	/**
	 * Copy directory recursively - advanced variant.
	 *
	 * @param source source file
	 * @param target target file
	 * @param filter filter accepting only files and dirs to be copied
	 * @param filesCopied list into which all the target files will be added
	 * @throws IOException on error
	 */
	public static void copyDirectory(File source, File target, FileFilter filter, List<File> filesCopied) throws IOException
	{
		if (!source.exists()) return;
		
		if (source.isDirectory()) {
			if (!target.exists() && !target.mkdir()) {
				throw new IOException("Could not open destination directory.");
			}
			
			final String[] children = source.list();
			for (final String element : children) {
				copyDirectory(new File(source, element), new File(target, element), filter, filesCopied);
			}
			
		} else {
			if (filter != null && !filter.accept(source)) {
				return;
			}
			
			if (filesCopied != null) filesCopied.add(target);
			copyFile(source, target);
		}
	}
	
	
	/**
	 * List directory recursively
	 *
	 * @param source source file
	 * @param filter filter accepting only files and dirs to be copied (or null)
	 * @param files list of the found files
	 * @throws IOException on error
	 */
	public static void listDirectoryRecursive(File source, StringFilter filter, List<File> files) throws IOException
	{
		if (source.isDirectory()) {
			final String[] children = source.list();
			for (final String element : children) {
				listDirectoryRecursive(new File(source, element), filter, files);
			}
			
		} else {
			if (filter != null && !filter.isValid(source.getAbsolutePath())) {
				return;
			}
			
			files.add(source);
		}
	}
	
	
	/**
	 * Copy file using streams. Make sure target directory exists!
	 *
	 * @param source source file
	 * @param target target file
	 * @throws IOException on error
	 */
	public static void copyFile(File source, File target) throws IOException
	{
		
		try(InputStream in = new FileInputStream(source);
			OutputStream out = new FileOutputStream(target)) {
			
			copyStream(in, out);
		}
	}
	
	
	/**
	 * Copy bytes from input to output stream, leaving out stream open
	 *
	 * @param in input stream
	 * @param out output stream
	 * @throws IOException on error
	 */
	public static void copyStream(InputStream in, OutputStream out) throws IOException
	{
		if (in == null) {
			throw new NullPointerException("Input stream is null");
		}
		
		if (out == null) {
			throw new NullPointerException("Output stream is null");
		}
		
		final byte[] buf = new byte[2048];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
	}
	
	
	/**
	 * Improved delete
	 *
	 * @param path deleted path
	 * @param recursive recursive delete
	 * @return success
	 */
	public static boolean delete(File path, boolean recursive)
	{
		if (!path.exists()) {
			return true;
		}
		
		if (!recursive || !path.isDirectory()) return path.delete();
		
		final String[] list = path.list();
		for (int i = 0; i < list.length; i++) {
			if (!delete(new File(path, list[i]), true)) return false;
		}
		
		return path.delete();
	}
	
	
	/**
	 * Read entire file to a string.
	 *
	 * @param file file
	 * @return file contents
	 * @throws IOException
	 */
	public static String fileToString(File file) throws IOException
	{
		try(FileInputStream fin = new FileInputStream(file)) {
			
			return streamToString(fin);
		}
	}
	
	
	/**
	 * Get files in a folder (create folder if needed)
	 *
	 * @param dir folder
	 * @return list of files
	 */
	public static List<File> listDirectory(File dir)
	{
		return FileUtil.listDirectory(dir, null);
	}
	
	
	/**
	 * Get files in a folder (create folder if needed)
	 *
	 * @param dir folder
	 * @param filter file filter
	 * @return list of files
	 */
	public static List<File> listDirectory(File dir, FileFilter filter)
	{
		dir.mkdir();
		
		final List<File> list = new ArrayList<>();
		
		for (final File f : dir.listFiles(filter)) {
			list.add(f);
		}
		
		return list;
	}
	
	
	/**
	 * Remove extension.
	 *
	 * @param file file
	 * @return filename without extension
	 */
	public static String[] getFilenameParts(File file)
	{
		return getFilenameParts(file.getName());
	}
	
	
	public static String getExtension(File file)
	{
		return getExtension(file.getName());
	}
	
	
	public static String getExtension(String file)
	{
		return Str.fromLast(file, '.');
	}
	
	
	/**
	 * Remove extension.
	 *
	 * @param filename
	 * @return filename and extension
	 */
	public static String[] getFilenameParts(String filename)
	{
		String ext, name;
		
		try {
			ext = Str.fromLastDot(filename);
		} catch (final StringIndexOutOfBoundsException e) {
			ext = "";
		}
		
		try {
			name = Str.toLastDot(filename);
		} catch (final StringIndexOutOfBoundsException e) {
			name = "";
			Log.w("Error extracting extension from file " + filename);
		}
		
		return new String[] { name, ext };
	}
	
	
	/**
	 * Read entire input stream to a string, and close it.
	 *
	 * @param in input stream
	 * @return file contents
	 */
	public static String streamToString(InputStream in)
	{
		return streamToString(in, -1);
	}
	
	
	/**
	 * Read input stream to a string, and close it.
	 *
	 * @param in input stream
	 * @param lines max number of lines (-1 to disable limit)
	 * @return file contents
	 */
	public static String streamToString(InputStream in, int lines)
	{
		if (in == null) {
			Log.e(new NullPointerException("Null stream to be converted to String."));
			return ""; // to avoid NPE's
		}
		
		BufferedReader br = null;
		final StringBuilder sb = new StringBuilder();
		
		String line;
		try {
			int cnt = 0;
			br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			while ((line = br.readLine()) != null && (cnt < lines || lines <= 0)) {
				sb.append(line + "\n");
				cnt++;
			}
			
			if (cnt == lines && lines > 0) {
				sb.append("--- end of preview ---\n");
			}
			
		} catch (final IOException e) {
			Log.e(e);
		} finally {
			try {
				if (br != null) br.close();
			} catch (final IOException e) {
				// ignore
			}
		}
		
		return sb.toString();
	}
	
	
	public static InputStream stringToStream(String text)
	{
		if (text == null) return null;
		
		try {
			return new ByteArrayInputStream(text.getBytes("UTF-8"));
		} catch (final UnsupportedEncodingException e) {
			Log.e(e);
			return null;
		}
	}
	
	
	public static InputStream getResource(String path)
	{
		final InputStream in = FileUtil.class.getResourceAsStream(path);
		
		if (in != null) return in;
		
		try {
			return new FileInputStream(new File(".", path));

		} catch (final FileNotFoundException e) {
			
			try {
				return new FileInputStream(WorkDir.getFile(path));

			} catch (final FileNotFoundException e2) {
				Log.w("Could not open resource stream, file not found: " + path);
				return null;
			}
			
		}
		
	}
	
	
	public static String getResourceAsString(String path)
	{
		return streamToString(getResource(path));
	}
	
	
	/**
	 * Save string to file
	 *
	 * @param file file
	 * @param text string
	 * @throws IOException on error
	 */
	public static void stringToFile(File file, String text) throws IOException
	{
		try(PrintStream out = new PrintStream(new FileOutputStream(file), false, "UTF-8")) {
			
			out.print(text);
			
			out.flush();
			
		}
	}
	
	
	public static void deleteEmptyDirs(File base) throws IOException
	{
		for (final File f : listDirectory(base)) {
			if (!f.isDirectory()) continue;
			
			deleteEmptyDirs(f);
			
			final List<File> children = listDirectory(f);
			if (children.size() == 0) {
				if (!f.delete()) throw new IOException("Could not delete a directory: " + f);
				continue;
			}
		}
		
	}
	
	
	public static String getBasename(String name)
	{
		return Str.toLast(Str.fromLast(name, '/'), '.');
	}
	
	
	public static String getFilename(String name)
	{
		return Str.fromLast(name, '/');
	}
	
	
	/**
	 * Copy resource to file
	 *
	 * @param resname resource name
	 * @param file out file
	 * @throws IOException
	 */
	public static void resourceToFile(String resname, File file) throws IOException
	{
		try(InputStream in = FileUtil.getResource(resname);
			OutputStream out = new FileOutputStream(file)) {
			
			FileUtil.copyStream(in, out);
		}
		
	}
	
	
	/**
	 * Get resource as string, safely closing streams.
	 *
	 * @param resname resource name
	 * @return resource as string, empty string on failure
	 * @throws IOException on fail
	 */
	public static String resourceToString(String resname) throws IOException
	{
		try(InputStream in = FileUtil.getResource(resname)) {
			return streamToString(in);
		}
	}
}

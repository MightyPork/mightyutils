package mightypork.utils.files.zip;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import mightypork.utils.files.FileUtil;
import mightypork.utils.logging.Log;
import mightypork.utils.string.validation.StringFilter;


/**
 * Utilities for manipulating zip files
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class ZipUtils {

	private static final int BUFFER_SIZE = 2048;


	/**
	 * Extract zip file to target directory
	 *
	 * @param file zip file
	 * @param outputDir target directory
	 * @param filter string filter (will be used to test entry names (paths))
	 * @return list of entries extracted (paths)
	 * @throws IOException
	 */
	public static List<String> extractZip(File file, File outputDir, StringFilter filter) throws IOException
	{
		try(ZipFile zip = new ZipFile(file)) {
			return extractZip(zip, outputDir, filter);
		}
	}


	/**
	 * Extract zip file to target directory
	 *
	 * @param zip open zip file
	 * @param outputDir target directory
	 * @param filter string filter (will be used to test entry names (paths))
	 * @return list of entries extracted (paths)
	 * @throws IOException if the file)s) cannot be created
	 */
	public static List<String> extractZip(ZipFile zip, File outputDir, StringFilter filter) throws IOException
	{
		final ArrayList<String> files = new ArrayList<>();

		if (!outputDir.mkdirs()) throw new IOException("Could not create output directory.");

		final Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();

		// process each entry
		while (zipFileEntries.hasMoreElements()) {
			final ZipEntry entry = zipFileEntries.nextElement();

			// parse filename and path
			final String entryPath = entry.getName();
			final File destFile = new File(outputDir, entryPath);
			final File destinationParent = destFile.getParentFile();

			if (entry.isDirectory() || (filter != null && !filter.isValid(entryPath))) continue;

			// make sure directories exist
			if (!destinationParent.mkdirs()) throw new IOException("Could not create directory.");

			if (!entry.isDirectory()) {
				extractZipEntry(zip, entry, destFile);
				files.add(entryPath);
			}
		}

		return files;
	}


	/**
	 * Read zip entries and add their paths to a list
	 *
	 * @param zipFile open zip file
	 * @return list of entry names
	 * @throws IOException on error
	 */
	public static List<String> listZip(File zipFile) throws IOException
	{
		try(ZipFile zip = new ZipFile(zipFile)) {
			return listZip(zip);
		}
	}


	/**
	 * Read zip entries and add their paths to a list
	 *
	 * @param zip open zip file
	 * @return list of entry names
	 * @throws IOException on error
	 */
	public static List<String> listZip(ZipFile zip) throws IOException
	{
		final ArrayList<String> files = new ArrayList<>();

		final Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();

		// process each entry
		while (zipFileEntries.hasMoreElements()) {
			final ZipEntry entry = zipFileEntries.nextElement();

			if (!entry.isDirectory()) {
				files.add(entry.getName());
			}
		}

		return files;
	}


	/**
	 * Extract one zip entry to target file
	 *
	 * @param zip open zip file
	 * @param entry entry from the zip file
	 * @param destFile destination file ((NOT directory!)
	 * @throws IOException on error
	 */
	public static void extractZipEntry(ZipFile zip, ZipEntry entry, File destFile) throws IOException
	{
		if (!destFile.getParentFile().mkdirs()) throw new IOException("Could not create output directory.");

		try(InputStream in = zip.getInputStream(entry);
			BufferedInputStream is = new BufferedInputStream(in);
			FileOutputStream fos = new FileOutputStream(destFile);
			BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE)) {

			FileUtil.copyStream(is, dest);
		}
	}


	/**
	 * Load zip entry to String
	 *
	 * @param zip open zip file
	 * @param entry entry from the zip file
	 * @return loaded string
	 * @throws IOException on error
	 */
	public static String zipEntryToString(ZipFile zip, ZipEntry entry) throws IOException
	{
		BufferedInputStream is = null;
		try {
			is = new BufferedInputStream(zip.getInputStream(entry));
			final String s = FileUtil.streamToString(is);
			return s;
		} finally {
			try {
				if (is != null) is.close();
			} catch (final IOException e) {
				// ignore
			}
		}
	}


	public static boolean entryExists(File selectedFile, String string)
	{
		try(ZipFile zf = new ZipFile(selectedFile)) {
			return zf.getEntry(string) != null;
		} catch (final IOException | RuntimeException e) {
			Log.w("Error reading zip.", e);
			return false;
		}

	}
}

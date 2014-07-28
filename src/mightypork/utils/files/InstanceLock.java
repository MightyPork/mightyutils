package mightypork.utils.files;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;


/**
 * Instance lock (avoid running twice)
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class InstanceLock {

	@SuppressWarnings("resource")
	public static boolean onFile(final File lockFile)
	{
		try {
			lockFile.getParentFile().mkdirs();
			final RandomAccessFile randomAccessFile = new RandomAccessFile(lockFile, "rw");

			final FileLock fileLock = randomAccessFile.getChannel().tryLock();
			if (fileLock != null) {

				Runtime.getRuntime().addShutdownHook(new Thread() {

					@Override
					public void run()
					{
						try {
							fileLock.release();
							randomAccessFile.close();
							if (!lockFile.delete()) throw new IOException();
						} catch (final Throwable t) {
							System.err.println("Unable to remove lock file.");
							t.printStackTrace();
						}
					}
				});

				return true;
			}

			return false;
		} catch (final IOException e) {
			System.err.println("IO error while obtaining lock.");
			e.printStackTrace();
			return false;
		}
	}

}

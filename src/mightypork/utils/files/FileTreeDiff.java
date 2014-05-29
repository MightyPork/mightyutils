package mightypork.utils.files;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

import mightypork.utils.logging.Log;


public class FileTreeDiff {
	
	private static final byte[] BUFFER = new byte[2048];
	private final Checksum ck1 = new Adler32();
	private final Checksum ck2 = new Adler32();
	
	private boolean logging = true;
	
	private final List<Tuple<File>> compared = new ArrayList<>();
	private final Comparator<File> fileFirstSorter = new Comparator<File>() {
		
		@Override
		public int compare(File o1, File o2)
		{
			if (!o1.isDirectory() && o2.isDirectory()) return -1;
			if (o1.isDirectory() && !o2.isDirectory()) return 1;
			
			return o1.getName().compareTo(o2.getName());
		}
	};
	
	
	public void enableLogging(boolean state)
	{
		logging = state;
	}
	
	
	public boolean areEqual(File dir1, File dir2)
	{
		if (logging) Log.f3("Comparing directory trees:\n 1. " + dir1 + "\n 2. " + dir2);
		
		try {
			compared.clear();
			buildList(dir1, dir2);
			
			calcChecksum();
			
			if (logging) Log.f3("No difference found.");
			
			return true;
			
		} catch (final NotEqualException e) {
			if (logging) Log.f3("Difference found:\n" + e.getMessage());
			
			return false;
		}
	}
	
	
	private void calcChecksum() throws NotEqualException
	{
		
		for (final Tuple<File> pair : compared) {
			ck1.reset();
			ck2.reset();
			
			try(FileInputStream in1 = new FileInputStream(pair.a);
				FileInputStream in2 = new FileInputStream(pair.b)) {
				
				try(CheckedInputStream cin1 = new CheckedInputStream(in1, ck1);
					CheckedInputStream cin2 = new CheckedInputStream(in2, ck2)) {
					
					while (true) {
						final int read1 = cin1.read(BUFFER);
						final int read2 = cin2.read(BUFFER);
						
						if (read1 != read2 || ck1.getValue() != ck2.getValue()) {
							throw new NotEqualException("Bytes differ:\n" + pair.a + "\n" + pair.b);
						}
						
						if (read1 == -1) break;
					}
				}
				
			} catch (final IOException e) {
				// ignore
			}
		}
	}
	
	
	private void buildList(File f1, File f2) throws NotEqualException
	{
		if (f1.isDirectory() != f2.isDirectory()) throw new NotEqualException("isDirectory differs:\n" + f1 + "\n" + f2);
		
		if (f1.isFile() && f2.isFile()) {
			if (f1.length() != f2.length()) throw new NotEqualException("Sizes differ:\n" + f1 + "\n" + f2);
		}
		
		if (f1.isDirectory()) {
			final File[] children1 = f1.listFiles();
			final File[] children2 = f2.listFiles();
			
			Arrays.sort(children1, fileFirstSorter);
			Arrays.sort(children2, fileFirstSorter);
			
			if (children1.length != children2.length) throw new NotEqualException("Child counts differ:\n" + f1 + "\n" + f2);
			
			for (int i = 0; i < children1.length; i++) {
				final File ch1 = children1[i];
				final File ch2 = children2[i];
				
				if (!ch1.getName().equals(ch2.getName())) throw new NotEqualException("Filenames differ:\n" + ch1 + "\n" + ch2);
				
				buildList(ch1, ch2);
			}
			
		} else {
			compared.add(new Tuple<>(f1, f2));
		}
	}
	
	private class NotEqualException extends Exception {
		
		public NotEqualException(String msg)
		{
			super(msg);
		}
		
	}
	
	private class Tuple<T> {
		
		public T a;
		public T b;
		
		
		public Tuple(T a, T b)
		{
			this.a = a;
			this.b = b;
		}
	}
	
}

package mightypork.utils.ion;


import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import mightypork.utils.exceptions.CorruptDataException;


/**
 * Ion input stream
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class IonInput implements Closeable {

	private final DataInput in;
	private final InputStream stream;


	@SuppressWarnings("resource")
	public IonInput(File inFile) throws FileNotFoundException
	{
		this(new FileInputStream(inFile));
	}


	public IonInput(InputStream in)
	{
		this.stream = in;
		this.in = new DataInputStream(in);
	}


	/**
	 * Read int 0-255. Suitable when the int was written using
	 * <code>writeIntByte()</code> method.
	 *
	 * @return int
	 * @throws IOException
	 */
	public int readIntByte() throws IOException
	{
		return in.readUnsignedByte();
	}


	/**
	 * Read an int 0-65535. Suitable when the int was written using
	 * <code>writeIntShort()</code> method.
	 *
	 * @return int
	 * @throws IOException
	 */
	public int readIntShort() throws IOException
	{
		return in.readUnsignedShort();
	}


	public boolean readBoolean() throws IOException
	{
		return in.readBoolean();
	}


	public byte readByte() throws IOException
	{
		return in.readByte();
	}


	public short readShort() throws IOException
	{
		return in.readShort();
	}


	public char readChar() throws IOException
	{
		return in.readChar();
	}


	public int readInt() throws IOException
	{
		return in.readInt();
	}


	public long readLong() throws IOException
	{
		return in.readLong();
	}


	public float readFloat() throws IOException
	{
		return in.readFloat();
	}


	public double readDouble() throws IOException
	{
		return in.readDouble();
	}


	public String readString() throws IOException
	{
		return in.readUTF();
	}


	public boolean[] readBooleans() throws IOException
	{
		final int length = readLength();
		final boolean[] arr = new boolean[length];
		for (int i = 0; i < length; i++) {
			arr[i] = in.readBoolean();
		}
		return arr;
	}


	public byte[] readBytes() throws IOException
	{
		final int length = readLength();
		final byte[] arr = new byte[length];
		for (int i = 0; i < length; i++) {
			arr[i] = in.readByte();
		}
		return arr;
	}


	public char[] readChars() throws IOException
	{
		final int length = readLength();
		final char[] arr = new char[length];
		for (int i = 0; i < length; i++) {
			arr[i] = in.readChar();
		}
		return arr;
	}


	public short[] readShorts() throws IOException
	{
		final int length = readLength();
		final short[] arr = new short[length];
		for (int i = 0; i < length; i++) {
			arr[i] = in.readShort();
		}
		return arr;
	}


	public int[] readInts() throws IOException
	{
		final int length = readLength();
		final int[] arr = new int[length];
		for (int i = 0; i < length; i++) {
			arr[i] = in.readInt();
		}
		return arr;
	}


	public long[] readLongs() throws IOException
	{
		final int length = readLength();
		final long[] arr = new long[length];
		for (int i = 0; i < length; i++) {
			arr[i] = in.readLong();
		}
		return arr;
	}


	public float[] readFloats() throws IOException
	{
		final int length = readLength();
		final float[] arr = new float[length];
		for (int i = 0; i < length; i++) {
			arr[i] = in.readFloat();
		}
		return arr;
	}


	public double[] readDoubles() throws IOException
	{
		final int length = readLength();
		final double[] arr = new double[length];
		for (int i = 0; i < length; i++) {
			arr[i] = in.readDouble();
		}
		return arr;
	}


	public String[] readStrings() throws IOException
	{
		final int length = readLength();
		final String[] arr = new String[length];
		for (int i = 0; i < length; i++) {
			arr[i] = in.readUTF();
		}
		return arr;
	}


	public Object[] readObjects() throws IOException
	{
		final int length = readLength();
		final Object[] arr = new Object[length];
		for (int i = 0; i < length; i++) {
			arr[i] = readObject();
		}
		return arr;
	}


	/**
	 * Read bundle without a mark
	 */
	public IonDataBundle readBundle() throws IOException
	{
		final IonDataBundle ib = new IonDataBundle();
		ib.load(this);
		return ib;
	}


	/**
	 * Read bundle without a mark, load into a provided one
	 */
	public void readBundle(IonDataBundle filled) throws IOException
	{
		filled.clear();
		filled.load(this);
	}


	private int readMark() throws IOException
	{
		return readIntByte();
	}


	private int readLength() throws IOException
	{
		return readInt();
	}


	/**
	 * <p>
	 * Read object based on mark; if null mark is found, returns default value.
	 * </p>
	 * <p>
	 * If, however, an object of invalid or different type is found, an
	 * exception will be thrown.
	 * </p>
	 *
	 * @param def default value.
	 * @return the loaded object
	 * @throws CorruptDataException
	 */
	@SuppressWarnings("unchecked")
	public <T> T readObject(T def) throws CorruptDataException
	{
		try {
			final Object o = readObject();
			return (T) (o == null ? def : o);
		} catch (final Exception e) {
			throw new CorruptDataException("Could not load object.", e);
		}
	}


	/**
	 * Read single object, preceded by a mark.
	 *
	 * @return the loaded object
	 * @throws IOException
	 */
	public Object readObject() throws IOException
	{
		final int mark = readMark();

		try {

			if (Ion.isMarkForBinary(mark)) {
				IonBinary loaded;

				loaded = (IonBinary) Ion.getClassForMark(mark).newInstance();

				loaded.load(this);
				return loaded;
			}

			if (Ion.isMarkForBundled(mark)) {
				IonBundled loaded;

				loaded = (IonBundled) Ion.getClassForMark(mark).newInstance();

				final IonDataBundle ib = readBundle();
				loaded.load(ib);
				return loaded;
			}

			if (Ion.isMarkForIndirectBundled(mark)) {
				final IonizerBundled<?> ionizer = Ion.getIonizerBundledForClass(Ion.getClassForMark(mark));
				return ionizer.load(readBundle());
			}

			if (Ion.isMarkForIndirectBinary(mark)) {
				final IonizerBinary<?> ionizer = Ion.getIonizerBinaryForClass(Ion.getClassForMark(mark));
				return ionizer.load(this);
			}

		} catch (final Exception e) {
			throw new RuntimeException("Could not load object for mark: " + mark, e);
		}

		switch (mark) {
			case Ion.NULL:
				return null;

			case Ion.BOOLEAN:
				return readBoolean();

			case Ion.BYTE:
				return readByte();

			case Ion.CHAR:
				return readChar();

			case Ion.SHORT:
				return readShort();

			case Ion.INT:
				return readInt();

			case Ion.LONG:
				return readLong();

			case Ion.FLOAT:
				return readFloat();

			case Ion.DOUBLE:
				return readDouble();

			case Ion.STRING:
				return readString();

			case Ion.BOOLEAN_ARRAY:
				return readBooleans();

			case Ion.BYTE_ARRAY:
				return readBytes();

			case Ion.CHAR_ARRAY:
				return readChars();

			case Ion.SHORT_ARRAY:
				return readShorts();

			case Ion.INT_ARRAY:
				return readInts();

			case Ion.LONG_ARRAY:
				return readLongs();

			case Ion.FLOAT_ARRAY:
				return readFloats();

			case Ion.DOUBLE_ARRAY:
				return readDoubles();

			case Ion.STRING_ARRAY:
				return readStrings();

			case Ion.OBJECT_ARRAY:
				return readObjects();

			case Ion.MAP:
				return readMap();

			case Ion.SEQUENCE:
				return readSequence();

			default:
				throw new CorruptDataException("Invalid mark: " + mark);
		}
	}


	/**
	 * Reads mark and returns true if the mark is ENTRY, false if the mark is
	 * END. Throws an exception otherwise.
	 *
	 * @return mark was ENTRY
	 * @throws IOException when the mark is neither ENTRY or END.
	 */
	public boolean hasNextEntry() throws IOException
	{
		final int mark = readMark();
		if (mark == Ion.ENTRY) return true;
		if (mark == Ion.END) return false;

		throw new CorruptDataException("Unexpected mark in sequence: " + mark);
	}


	/**
	 * Read a sequence of elements into an ArrayList
	 *
	 * @return the collection
	 * @throws IOException
	 */
	public <T> Collection<T> readSequence() throws IOException
	{
		return readSequence(new ArrayList<T>());
	}


	/**
	 * Load entries into a collection. The collection is cleaned first.
	 *
	 * @param filled collection to populate
	 * @return the collection
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public <T> Collection<T> readSequence(Collection<T> filled) throws IOException
	{
		try {
			filled.clear();
			while (hasNextEntry()) {
				filled.add((T) readObject());
			}
			return filled;
		} catch (final ClassCastException e) {
			throw new CorruptDataException("Unexpected element type in sequence.", e);
		}
	}


	/**
	 * Read element pairs into a HashMap
	 *
	 * @return the map
	 * @throws IOException
	 */
	public <K, V> Map<K, V> readMap() throws IOException
	{
		return readMap(new HashMap<K, V>());
	}


	/**
	 * Load data into a map. The map is cleaned first.
	 *
	 * @param filled filled map
	 * @return the map
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> readMap(Map<K, V> filled) throws IOException
	{
		try {
			filled.clear();
			while (hasNextEntry()) {
				final K key = (K) readObject();
				final V value = (V) readObject();

				filled.put(key, value);
			}
			return filled;
		} catch (final ClassCastException e) {
			throw new CorruptDataException("Unexpected element type in map.", e);
		}
	}


	@Override
	public void close() throws IOException
	{
		stream.close();
	}
}

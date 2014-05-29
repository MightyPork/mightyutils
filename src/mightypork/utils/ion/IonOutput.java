package mightypork.utils.ion;


import java.io.Closeable;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Ion output stream
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class IonOutput implements Closeable {
	
	private final DataOutput out;
	private final OutputStream stream;
	
	
	@SuppressWarnings("resource")
	public IonOutput(File outFile) throws FileNotFoundException
	{
		this(new FileOutputStream(outFile));
	}
	
	
	public IonOutput(OutputStream out)
	{
		this.stream = out;
		this.out = new DataOutputStream(out);
	}
	
	
	public void writeBoolean(boolean a) throws IOException
	{
		out.writeBoolean(a);
	}
	
	
	public void writeByte(int a) throws IOException
	{
		out.writeByte(a);
	}
	
	
	public void writeShort(int a) throws IOException
	{
		out.writeShort(a);
	}
	
	
	public void writeChar(int a) throws IOException
	{
		out.writeChar(a);
	}
	
	
	public void writeInt(int a) throws IOException
	{
		out.writeInt(a);
	}
	
	
	public void writeIntShort(int a) throws IOException
	{
		out.writeShort(a);
	}
	
	
	public void writeIntByte(int a) throws IOException
	{
		out.writeByte(a);
	}
	
	
	public void writeLong(long a) throws IOException
	{
		out.writeLong(a);
	}
	
	
	public void writeFloat(float a) throws IOException
	{
		out.writeFloat(a);
	}
	
	
	public void writeDouble(double a) throws IOException
	{
		out.writeDouble(a);
	}
	
	
	public void writeBytes(String a) throws IOException
	{
		out.writeBytes(a);
	}
	
	
	public void writeString(String a) throws IOException
	{
		out.writeUTF(a);
	}
	
	
	public void writeBooleans(boolean[] arr) throws IOException
	{
		writeLength(arr.length);
		for (final boolean a : arr) {
			out.writeBoolean(a);
		}
	}
	
	
	public void writeBytes(byte[] arr) throws IOException
	{
		writeLength(arr.length);
		for (final byte a : arr) {
			out.writeByte(a);
		}
	}
	
	
	public void writeChars(char[] arr) throws IOException
	{
		writeLength(arr.length);
		for (final char a : arr) {
			out.writeChar(a);
		}
	}
	
	
	public void writeShorts(short[] arr) throws IOException
	{
		writeLength(arr.length);
		for (final short a : arr) {
			out.writeShort(a);
		}
	}
	
	
	public void writeInts(int[] arr) throws IOException
	{
		writeLength(arr.length);
		for (final int a : arr) {
			out.writeInt(a);
		}
	}
	
	
	public void writeLongs(long[] arr) throws IOException
	{
		writeLength(arr.length);
		for (final long a : arr) {
			out.writeLong(a);
		}
	}
	
	
	public void writeFloats(float[] arr) throws IOException
	{
		writeLength(arr.length);
		for (final float a : arr) {
			out.writeFloat(a);
		}
	}
	
	
	public void writeDoubles(double[] arr) throws IOException
	{
		writeLength(arr.length);
		for (final double a : arr) {
			out.writeDouble(a);
		}
	}
	
	
	public void writeStrings(String[] arr) throws IOException
	{
		writeLength(arr.length);
		for (final String a : arr) {
			out.writeUTF(a);
		}
	}
	
	
	/**
	 * Write a bundle without a mark
	 */
	public void writeBundle(IonBundle bundle) throws IOException
	{
		bundle.save(this);
	}
	
	
	/**
	 * Write array of objects. Works with all that is supported by writeObject()
	 * 
	 * @param arr array to write
	 * @throws IOException on IO error or on invalid object type.
	 */
	public void writeObjects(Object[] arr) throws IOException
	{
		writeLength(arr.length);
		for (final Object a : arr) {
			writeObject(a);
		}
	}
	
	
	public <T> void writeSequence(Collection<T> sequence) throws IOException
	{
		for (final T element : sequence) {
			startEntry();
			writeObject(element);
		}
		endSequence();
	}
	
	
	public <K, V> void writeMap(Map<K, V> map) throws IOException
	{
		for (final Entry<K, V> e : map.entrySet()) {
			if (e.getValue() == null) {
				continue;
			}
			
			startEntry();
			writeObject(e.getKey());
			writeObject(e.getValue());
		}
		endSequence();
	}
	
	
	public void endSequence() throws IOException
	{
		writeMark(Ion.END);
	}
	
	
	public void startEntry() throws IOException
	{
		writeMark(Ion.ENTRY);
	}
	
	
	private void writeMark(int mark) throws IOException
	{
		writeIntByte(mark);
	}
	
	
	private void writeLength(int length) throws IOException
	{
		writeInt(length);
	}
	
	
	/**
	 * Write an object. Supported are built-in types and types registered to
	 * Ion.
	 * 
	 * @param obj obj to write
	 * @throws IOException on IO error or invalid object type.
	 */
	public void writeObject(Object obj) throws IOException
	{
		if (obj == null) {
			writeMark(Ion.NULL);
			return;
		}
		
		if (obj instanceof IonObjBinary) {
			final IonObjBinary iObj = (IonObjBinary) obj;
			
			writeMark(Ion.getMark(iObj));
			iObj.save(this);
			return;
		}
		
		if (obj instanceof IonObjBundled) {
			final IonObjBundled iObj = (IonObjBundled) obj;
			
			writeMark(Ion.getMark(iObj));
			
			final IonBundle bundle = new IonBundle();
			iObj.save(bundle);
			writeBundle(bundle);
			
			return;
		}
		
		if (obj instanceof Map) {
			writeMark(Ion.MAP);
			writeMap((Map<?, ?>) obj);
			return;
		}
		
		if (obj instanceof Collection) {
			writeMark(Ion.SEQUENCE);
			writeSequence((Collection<?>) obj);
			return;
		}
		
		if (obj instanceof Boolean) {
			writeMark(Ion.BOOLEAN);
			writeBoolean((Boolean) obj);
			return;
		}
		
		if (obj instanceof Byte) {
			writeMark(Ion.BYTE);
			writeByte((Byte) obj);
			return;
		}
		
		if (obj instanceof Character) {
			writeMark(Ion.CHAR);
			writeChar((Character) obj);
			return;
		}
		
		if (obj instanceof Short) {
			writeMark(Ion.SHORT);
			writeShort((Short) obj);
			return;
		}
		
		if (obj instanceof Integer) {
			writeMark(Ion.INT);
			writeInt((Integer) obj);
			return;
		}
		
		if (obj instanceof Long) {
			writeMark(Ion.LONG);
			writeLong((Long) obj);
			return;
		}
		
		if (obj instanceof Float) {
			writeMark(Ion.FLOAT);
			writeFloat((Float) obj);
			return;
		}
		
		if (obj instanceof Double) {
			writeMark(Ion.DOUBLE);
			writeDouble((Double) obj);
			return;
		}
		
		if (obj instanceof String) {
			writeMark(Ion.STRING);
			writeString((String) obj);
			return;
		}
		
		if (obj instanceof boolean[]) {
			writeMark(Ion.BOOLEAN_ARRAY);
			writeBooleans((boolean[]) obj);
			return;
		}
		
		if (obj instanceof byte[]) {
			writeMark(Ion.BYTE_ARRAY);
			writeBytes((byte[]) obj);
			return;
		}
		
		if (obj instanceof char[]) {
			writeMark(Ion.CHAR_ARRAY);
			writeChars((char[]) obj);
			return;
		}
		
		if (obj instanceof short[]) {
			writeMark(Ion.SHORT_ARRAY);
			writeShorts((short[]) obj);
			return;
		}
		
		if (obj instanceof int[]) {
			writeMark(Ion.INT_ARRAY);
			writeInts((int[]) obj);
			return;
		}
		
		if (obj instanceof long[]) {
			writeMark(Ion.LONG_ARRAY);
			writeLongs((long[]) obj);
			return;
		}
		
		if (obj instanceof float[]) {
			writeMark(Ion.FLOAT_ARRAY);
			writeFloats((float[]) obj);
			return;
		}
		
		if (obj instanceof double[]) {
			writeMark(Ion.DOUBLE_ARRAY);
			writeDoubles((double[]) obj);
			return;
		}
		
		if (obj instanceof String[]) {
			writeMark(Ion.STRING_ARRAY);
			writeStrings((String[]) obj);
			return;
		}
		
		if (obj instanceof Object[]) {
			writeMark(Ion.OBJECT_ARRAY);
			writeObjects((Object[]) obj);
			return;
		}
		
		throw new IOException("Object " + obj + " could not be be written to stream.");
	}
	
	
	@Override
	public void close() throws IOException
	{
		stream.close();
	}
}

package mightypork.utils.ion;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Universal data storage system (main API class)
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class Ion {
	
	private static final int RESERVED_LOW = 0;
	private static final int RESERVED_HIGH = 49;
	
	private static final int RANGE_LOW = 0;
	private static final int RANGE_HIGH = 255;
	
	// marks for object saving
	/** Null mark */
	static final int NULL = 0;
	/** Boolean mark */
	static final int BOOLEAN = 1;
	/** Byte mark */
	static final int BYTE = 2;
	/** Character mark */
	static final int CHAR = 3;
	/** Short mark */
	static final int SHORT = 4;
	/** Integer mark */
	static final int INT = 5;
	/** Long mark */
	static final int LONG = 6;
	/** Float mark */
	static final int FLOAT = 7;
	/** Double mark */
	static final int DOUBLE = 8;
	/** String mark */
	static final int STRING = 9;
	/** Boolean array mark */
	static final int BOOLEAN_ARRAY = 10;
	/** Byte array mark */
	static final int BYTE_ARRAY = 11;
	/** Character array mark */
	static final int CHAR_ARRAY = 12;
	/** Short array mark */
	static final int SHORT_ARRAY = 13;
	/** Integer array mark */
	static final int INT_ARRAY = 14;
	/** Long array mark */
	static final int LONG_ARRAY = 15;
	/** Float array mark */
	static final int FLOAT_ARRAY = 16;
	/** Double array mark */
	static final int DOUBLE_ARRAY = 17;
	/** String array mark */
	static final int STRING_ARRAY = 18;
	/** Entry mark - start of map or sequence entry */
	static final int ENTRY = 19;
	/** End mark - end of sequence or map */
	static final int END = 20;
	/** Bundle */
	static final int ION_BUNDLE = 21;
	/** Sequence wrapper for bundle */
	static final int SEQUENCE_WRAPPER = 22;
	/** Map wrapper for bundle */
	static final int MAP_WRAPPER = 23;
	/** Sequence saved as object */
	public static final int SEQUENCE = 24;
	/** Map saved as object */
	public static final int MAP = 25;
	/** Array of arbitrary objects */
	public static final int OBJECT_ARRAY = 26;
	
	
	/** Ionizables<Mark, Class> */
	private static Map<Integer, Class<?>> markToClass = new HashMap<>();
	private static Map<Class<?>, Integer> classToMark = new HashMap<>();
	
	private static boolean reservedMarkChecking;
	
	static {
		reservedMarkChecking = false;
		
		// register built-ins
		register(ION_BUNDLE, IonBundle.class);
		register(SEQUENCE_WRAPPER, IonSequenceWrapper.class);
		register(MAP_WRAPPER, IonMapWrapper.class);
		
		reservedMarkChecking = true;
	}
	
	
	/**
	 * Register new {@link IonObjBinary} class for writing/loading.
	 * 
	 * @param mark mark to be used 50..255, unless internal
	 * @param objClass class of the registered object
	 */
	public static void register(int mark, Class<?> objClass)
	{
		// negative marks are allowed.
		if (mark > RANGE_HIGH) throw new IllegalArgumentException("Mark must be < 256.");
		if (mark < RANGE_LOW) throw new IllegalArgumentException("Mark must be positive.");
		
		if (reservedMarkChecking && isMarkReserved(mark)) {
			throw new IllegalArgumentException("Marks " + RESERVED_LOW + ".." + RESERVED_HIGH + " are reserved.");
		}
		
		if (markToClass.containsKey(objClass)) {
			throw new IllegalArgumentException("Mark " + mark + " is already in use.");
		}
		
		if (classToMark.containsKey(objClass)) {
			throw new IllegalArgumentException(objClass + " is already registered.");
		}
		
		if (!IonObjBundled.class.isAssignableFrom(objClass)) {
			if (!IonObjBinary.class.isAssignableFrom(objClass)) {
				throw new IllegalArgumentException(objClass + " cannot be registered to Ion.");
			}
		}
		
		// make sure the type has implicit constructor
		try {
			objClass.getConstructor();
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Class " + objClass + " doesn't have an implicit constructor.");
		}
		
		markToClass.put(mark, objClass);
		classToMark.put(objClass, mark);
	}
	
	
	/**
	 * Try to register the type using a static final ION_MARK int field.
	 * 
	 * @param objClass type class
	 */
	public static void register(Class<?> objClass)
	{
		try {
			final Field fld = objClass.getDeclaredField("ION_MARK");
			
			final int modif = fld.getModifiers();
			
			if (!Modifier.isFinal(modif) || !Modifier.isStatic(modif)) {
				throw new RuntimeException("The ION_MARK field must be static and final.");
			}
			
			fld.setAccessible(true);
			final int mark = fld.getInt(null);
			
			register(mark, objClass);
			
		} catch (final Exception e) {
			throw new RuntimeException("Could not register " + objClass + " using an ION_MARK field.", e);
		}
	}
	
	
	/**
	 * Load binary from file and cast.
	 */
	public static <T> T fromFile(String path) throws IOException
	{
		return fromFile(new File(path));
	}
	
	
	/**
	 * Load binary from file and cast.
	 */
	public static <T> T fromFile(File file) throws IOException
	{
		try(InputStream in = new FileInputStream(file)) {
			return fromStream(in);
		}
	}
	
	
	/**
	 * Write binary to file with mark.
	 */
	public static void toFile(String path, Object obj) throws IOException
	{
		toFile(new File(path), obj);
	}
	
	
	/**
	 * Write binary to file with mark.
	 */
	public static void toFile(File file, Object obj) throws IOException
	{
		try(OutputStream out = new FileOutputStream(file)) {
			
			toStream(out, obj);
			
			out.flush();
		} catch (final Exception e) {
			throw new IOException("Error writing to ION file.", e);
		}
	}
	
	
	/**
	 * Load object from stream based on mark, try to cast.
	 */
	public static <T> T fromStream(InputStream in) throws IOException
	{
		try(final IonInput inp = new IonInput(in)) {
			return (T) inp.readObject();
		}
	}
	
	
	/**
	 * Write object to output with a mark.
	 */
	public static void toStream(OutputStream out, Object obj) throws IOException
	{
		try(IonOutput iout = new IonOutput(out)) {
			iout.writeObject(obj);
		}
	}
	
	
	/**
	 * Get ion input
	 * 
	 * @param path file path to read
	 * @return input
	 * @throws IOException
	 */
	public static IonInput getInput(String path) throws IOException
	{
		return getInput(new File(path));
	}
	
	
	/**
	 * Get ion input
	 * 
	 * @param file file to read
	 * @return input
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static IonInput getInput(File file) throws IOException
	{
		return new IonInput(new FileInputStream(file));
	}
	
	
	/**
	 * Get ion output
	 * 
	 * @param path file path to write
	 * @return output
	 * @throws IOException
	 */
	public static IonOutput getOutput(String path) throws IOException
	{
		return getOutput(new File(path));
	}
	
	
	/**
	 * Get ion output
	 * 
	 * @param file file to write
	 * @return output
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static IonOutput getOutput(File file) throws IOException
	{
		return new IonOutput(new FileOutputStream(file));
	}
	
	
	/**
	 * Create new bundle and write the object to it.
	 */
	public static IonBundle wrapBundled(IonObjBundled content) throws IOException
	{
		final IonBundle ib = new IonBundle();
		content.save(ib);
		return ib;
	}
	
	
	/**
	 * Try to unwrap an object from bundle. The object class must have implicit
	 * accessible constructor.
	 * 
	 * @param bundle unwrapped bundle
	 * @param objClass class of desired object
	 * @return the object unwrapped
	 * @throws IOException
	 */
	public static <T extends IonObjBundled> T unwrapBundled(IonBundle bundle, Class<? extends T> objClass) throws IOException
	{
		try {
			final T inst = objClass.newInstance();
			inst.load(bundle);
			return inst;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IOException("Could not instantiate " + objClass + ".");
		}
	}
	
	
	static Class<?> getClassForMark(int mark)
	{
		return markToClass.get(mark);
	}
	
	
	public static int getMark(Object object)
	{
		assertRegistered(object);
		
		return classToMark.get(object.getClass());
	}
	
	
	/**
	 * @return true if the mark is for a registered {@link IonObjBinary} object
	 */
	static boolean isMarkForBinary(int mark)
	{
		if (!markToClass.containsKey(mark)) return false;
		
		return IonObjBinary.class.isAssignableFrom(markToClass.get(mark));
	}
	
	
	/**
	 * @return true if the mark is for a registered {@link IonObjBinary} object
	 */
	static boolean isMarkForBundled(int mark)
	{
		if (!markToClass.containsKey(mark)) return false;
		
		return IonObjBundled.class.isAssignableFrom(markToClass.get(mark));
	}
	
	
	/**
	 * @return true if the mark is reserved for internal use
	 */
	static boolean isMarkReserved(int mark)
	{
		return mark >= RESERVED_LOW && mark <= RESERVED_HIGH;
	}
	
	
	/**
	 * @return true if the mark is for a registered {@link IonObjBinary} object
	 */
	static boolean isRegistered(Object object)
	{
		return classToMark.containsKey(object.getClass());
	}
	
	
	/**
	 * Make sure object is registered in the table.
	 * 
	 * @throws IOException if not registered or class mismatch
	 */
	static void assertRegistered(Object obj)
	{
		if (!isRegistered(obj)) {
			throw new RuntimeException("Type not registered: " + (obj.getClass()));
		}
	}
	
	
	/**
	 * For get all external registered types - just like if the class was
	 * freshly loaded. Can be used for unit tests.
	 */
	public static void reset()
	{
		final List<Integer> toRemove = new ArrayList<>();
		for (final Entry<Integer, Class<?>> e : markToClass.entrySet()) {
			final int mark = e.getKey();
			
			if (!isMarkReserved(mark)) {
				toRemove.add(mark);
			}
		}
		
		for (final int i : toRemove) {
			classToMark.remove(markToClass.remove(i));
		}
	}
}

package mightypork.utils.files.config;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import mightypork.utils.Convert;
import mightypork.utils.logging.Log;


/**
 * Property manager with advanced formatting and value checking.
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class PropertyManager {
	
	private class BooleanProperty extends Property<Boolean> {
		
		public BooleanProperty(String key, Boolean defaultValue, String comment)
		{
			super(key, defaultValue, comment);
		}
		
		
		@Override
		public Boolean decode(String string, Boolean defval)
		{
			return Convert.toBoolean(string, defval);
		}
	}
	
	private class IntegerProperty extends Property<Integer> {
		
		public IntegerProperty(String key, Integer defaultValue, String comment)
		{
			super(key, defaultValue, comment);
		}
		
		
		@Override
		public Integer decode(String string, Integer defval)
		{
			return Convert.toInteger(string, defval);
		}
	}
	
	private class DoubleProperty extends Property<Double> {
		
		public DoubleProperty(String key, Double defaultValue, String comment)
		{
			super(key, defaultValue, comment);
		}
		
		
		@Override
		public Double decode(String string, Double defval)
		{
			return Convert.toDouble(string, defval);
		}
	}
	
	private class StringProperty extends Property<String> {
		
		public StringProperty(String key, String defaultValue, String comment)
		{
			super(key, defaultValue, comment);
		}
		
		
		@Override
		public String decode(String string, String defval)
		{
			return Convert.toString(string, defval);
		}
	}
	
	/** put newline before entry comments */
	private boolean cfgNewlineBeforeComments = true;
	
	/** Put newline between sections. */
	private boolean cfgSeparateSections = true;
	
	private final File file;
	private String fileComment = "";
	
	private final TreeMap<String, Property<?>> entries;
	private final TreeMap<String, String> renameTable;
	private SortedProperties props = new SortedProperties();
	
	
	/**
	 * Create property manager from file path and an initial comment.
	 * 
	 * @param file file with the props
	 * @param comment the initial comment. Use \n in it if you want.
	 */
	public PropertyManager(File file, String comment)
	{
		this.file = file;
		this.entries = new TreeMap<>();
		this.renameTable = new TreeMap<>();
		this.fileComment = comment;
	}
	
	
	/**
	 * Load, fix and write to file.
	 */
	public void load()
	{
		if (!file.getParentFile().mkdirs()) {
			if (!file.getParentFile().exists()) {
				throw new RuntimeException("Cound not create config file.");
			}
		}
		
		try(FileInputStream fis = new FileInputStream(file)) {
			props.load(fis);
		} catch (final IOException e) {
			props = new SortedProperties();
		}
		
		props.cfgBlankRowBetweenSections = cfgSeparateSections;
		props.cfgBlankRowBeforeComment = cfgNewlineBeforeComments;
		
		// rename keys
		for (final Entry<String, String> entry : renameTable.entrySet()) {
			
			final String pr = props.getProperty(entry.getKey());
			
			if (pr == null) continue;
			
			props.remove(entry.getKey());
			props.setProperty(entry.getValue(), pr);
		}
		
		for (final Property<?> entry : entries.values()) {
			entry.parse(props.getProperty(entry.getKey()));
		}
		
		renameTable.clear();
	}
	
	
	public void save()
	{
		try {
			final ArrayList<String> keyList = new ArrayList<>();
			
			// validate entries one by one, replace with default when needed
			for (final Property<?> entry : entries.values()) {
				keyList.add(entry.getKey());
				
				if (entry.getComment() != null) {
					props.setKeyComment(entry.getKey(), entry.getComment());
				}
				
				props.setProperty(entry.getKey(), entry.toString());
			}
			
			// removed unused props
			for (final String propname : props.keySet().toArray(new String[props.size()])) {
				if (!keyList.contains(propname)) {
					props.remove(propname);
				}
			}
			
			try(FileOutputStream fos = new FileOutputStream(file)) {
				
				props.store(fos, fileComment);
			}
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	
	/**
	 * @param newlineBeforeComments put newline before comments
	 */
	public void cfgNewlineBeforeComments(boolean newlineBeforeComments)
	{
		this.cfgNewlineBeforeComments = newlineBeforeComments;
	}
	
	
	/**
	 * @param separateSections do separate sections by newline
	 */
	public void cfgSeparateSections(boolean separateSections)
	{
		this.cfgSeparateSections = separateSections;
	}
	
	
	/**
	 * Get a property entry (rarely used)
	 * 
	 * @param k key
	 * @return the entry
	 */
	public Property<?> getProperty(String k)
	{
		try {
			return entries.get(k);
		} catch (final Exception e) {
			Log.w(e);
			return null;
		}
	}
	
	
	/**
	 * Get boolean property
	 * 
	 * @param k key
	 * @return the boolean found, or false
	 */
	public Boolean getBoolean(String k)
	{
		return Convert.toBoolean(getProperty(k).getValue());
	}
	
	
	/**
	 * Get numeric property
	 * 
	 * @param k key
	 * @return the int found, or null
	 */
	public Integer getInteger(String k)
	{
		return Convert.toInteger(getProperty(k).getValue());
	}
	
	
	/**
	 * Get numeric property as double
	 * 
	 * @param k key
	 * @return the double found, or null
	 */
	public Double getDouble(String k)
	{
		return Convert.toDouble(getProperty(k).getValue());
	}
	
	
	/**
	 * Get string property
	 * 
	 * @param k key
	 * @return the string found, or null
	 */
	public String getString(String k)
	{
		return Convert.toString(getProperty(k).getValue());
	}
	
	
	/**
	 * Get arbitrary property. Make sure it's of the right type!
	 * 
	 * @param k key
	 * @return the prioperty found
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(String k)
	{
		try {
			return ((Property<T>) getProperty(k)).getValue();
		} catch (final ClassCastException e) {
			return null;
		}
	}
	
	
	/**
	 * Add a boolean property
	 * 
	 * @param k key
	 * @param d default value
	 * @param comment the in-file comment
	 */
	public void putBoolean(String k, boolean d, String comment)
	{
		putProperty(new BooleanProperty(k, d, comment));
	}
	
	
	/**
	 * Add a numeric property (double)
	 * 
	 * @param k key
	 * @param d default value
	 * @param comment the in-file comment
	 */
	public void putDouble(String k, double d, String comment)
	{
		putProperty(new DoubleProperty(k, d, comment));
	}
	
	
	/**
	 * Add a numeric property
	 * 
	 * @param k key
	 * @param d default value
	 * @param comment the in-file comment
	 */
	public void putInteger(String k, int d, String comment)
	{
		putProperty(new IntegerProperty(k, d, comment));
	}
	
	
	/**
	 * Add a string property
	 * 
	 * @param k key
	 * @param d default value
	 * @param comment the in-file comment
	 */
	public void putString(String k, String d, String comment)
	{
		putProperty(new StringProperty(k, d, comment));
	}
	
	
	/**
	 * Add a range property
	 * 
	 * @param prop property to put
	 */
	public <T> void putProperty(Property<T> prop)
	{
		entries.put(prop.getKey(), prop);
	}
	
	
	/**
	 * Rename key before loading; value is preserved
	 * 
	 * @param oldKey old key
	 * @param newKey new key
	 */
	public void renameKey(String oldKey, String newKey)
	{
		renameTable.put(oldKey, newKey);
		return;
	}
	
	
	/**
	 * Set value saved to certain key.
	 * 
	 * @param key key
	 * @param value the saved value
	 */
	public void setValue(String key, Object value)
	{
		getProperty(key).setValue(value);
	}
	
	
	public void setFileComment(String fileComment)
	{
		this.fileComment = fileComment;
	}
	
}

package mightypork.utils.config.propmgr.store;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import mightypork.utils.config.propmgr.PropertyStore;


/**
 * File based implementation utilizing {@link java.util.Properties}, hacked to
 * support UTF-8.
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class PropertyFile implements PropertyStore {
	
	private String comment;
	private File file;
	private SortedProperties props;
	
	
	public PropertyFile(File file) {
		this.file = file;
		this.comment = null;
		this.props = new SortedProperties();
	}
	
	
	public PropertyFile(File file, String comment) {
		this.file = file;
		this.comment = comment;
		this.props = new SortedProperties();
	}
	
	
	@Override
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	
	
	@Override
	public void load()
	{
		if (!file.exists()) return;
		
		try (FileInputStream in = new FileInputStream(file)) {
			props.load(in);
		} catch (IOException e) {
			// ignore
		}
	}
	
	
	@Override
	public void save() throws IOException
	{
		if (!file.getParentFile().mkdirs()) {
			if (!file.getParentFile().exists()) {
				throw new IOException("Cound not create config file.");
			}
		}
		
		try (FileOutputStream out = new FileOutputStream(file)) {
			props.store(out, comment);
		}
	}
	
	
	@Override
	public String getProperty(String key)
	{
		return props.getProperty(key);
	}
	
	
	@Override
	public void setProperty(String key, String value, String comment)
	{
		props.setProperty(key, value);
		props.setKeyComment(key, comment);
	}
	
	
	@Override
	public void removeProperty(String key)
	{
		props.remove(key);
	}
	
	
	@Override
	public void clear()
	{
		props.clear();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> keys()
	{
		
//		Set<String> keys = new HashSet<>();
//		for (Object o : props.keySet()) {
//			keys.add((String) o);
//		}
//		return keys;
		
		// we know it is strings.
		return (Collection<String>) (Collection<?>) props.keySet();
	}
	
}

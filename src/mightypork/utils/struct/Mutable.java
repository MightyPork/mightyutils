package mightypork.utils.struct;


/**
 * Mutable object
 * 
 * @author Ondřej Hruška (MightyPork)
 * @param <T> type
 */
public class Mutable<T> {
	
	/** The wrapped value */
	private T o = null;
	
	
	/**
	 * New mutable object
	 * 
	 * @param o value
	 */
	public Mutable(T o) {
		this.o = o;
	}
	
	
	/**
	 * Get the wrapped value
	 * 
	 * @return value
	 */
	public T get()
	{
		return o;
	}
	
	
	/**
	 * Set value
	 * 
	 * @param o new value to set
	 */
	public void set(T o)
	{
		this.o = o;
	}
	
	
	@Override
	public String toString()
	{
		if (o == null) return "<null>";
		return o.toString();
	}
	
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((o == null) ? 0 : o.hashCode());
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Mutable)) return false;
		final Mutable<?> other = (Mutable<?>) obj;
		if (o == null) {
			if (other.o != null) return false;
		} else if (!o.equals(other.o)) return false;
		return true;
	}
}

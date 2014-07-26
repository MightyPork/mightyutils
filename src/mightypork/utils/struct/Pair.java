package mightypork.utils.struct;


/**
 * Structure of 2 objects.
 * 
 * @author Ondřej Hruška (MightyPork)
 * @copy (c) 2012
 * @param <T1> 1st object class
 * @param <T2> 2nd object class
 */
public class Pair<T1, T2> {
	
	/**
	 * 1st object
	 */
	public T1 first;
	
	/**
	 * 2nd object
	 */
	public T2 second;
	
	
	/**
	 * Make structure of 2 objects
	 * 
	 * @param first 1st object
	 * @param second 2nd object
	 */
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}
	
	
	/**
	 * @return 1st object
	 */
	public T1 getFirst()
	{
		return first;
	}
	
	
	/**
	 * @return 2nd object
	 */
	public T2 getSecond()
	{
		return second;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Pair)) return false;
		final Pair<?, ?> other = (Pair<?, ?>) obj;
		if (first == null) {
			if (other.first != null) return false;
		} else if (!first.equals(other.first)) return false;
		if (second == null) {
			if (other.second != null) return false;
		} else if (!second.equals(other.second)) return false;
		return true;
	}
	
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}
	
	
	@Override
	public String toString()
	{
		return "PAIR{" + first + "," + second + "}";
	}
	
}

package mightypork.utils.struct;


/**
 * Structure of 3 objects.
 * 
 * @author Ondřej Hruška (MightyPork)
 * @copy (c) 2012
 * @param <T1> 1st object class
 * @param <T2> 2nd object class
 * @param <T3> 3rd object class
 */
public class Triad<T1, T2, T3> extends Pair<T1, T2> {
	
	/**
	 * 3rd object
	 */
	public T3 third;
	
	
	/**
	 * Make structure of 3 objects
	 * 
	 * @param objA 1st object
	 * @param objB 2nd object
	 * @param objC 3rd object
	 */
	public Triad(T1 objA, T2 objB, T3 objC) {
		super(objA, objB);
		third = objC;
	}
	
	
	/**
	 * @return 3rd object
	 */
	public T3 getThird()
	{
		return third;
	}
	
	
	/**
	 * Set 1st object
	 * 
	 * @param obj 1st object
	 */
	public void setThird(T3 obj)
	{
		third = obj;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Triad)) return false;
		
		if (!super.equals(obj)) return false;
		
		final Triad<?, ?, ?> other = (Triad<?, ?, ?>) obj;
		
		if (third == null) {
			if (other.third != null) return false;
		} else if (!third.equals(other.third)) return false;
		
		return true;
	}
	
	
	@Override
	public int hashCode()
	{
		return super.hashCode() + (third == null ? 0 : third.hashCode());
	}
	
	
	@Override
	public String toString()
	{
		return "TRIAD{" + first + "," + second + "," + third + "}";
	}
	
}

package mightypork.utils.math.constraints.num;


/**
 * Pluggable numeric constraint
 *
 * @author Ondřej Hruška (MightyPork)
 */
public interface PluggableNumBound extends NumBound {

	/**
	 * @param num bound to set
	 */
	abstract void setNum(NumBound num);

}

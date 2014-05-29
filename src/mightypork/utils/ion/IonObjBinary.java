package mightypork.utils.ion;


import java.io.IOException;


/**
 * Binary ion object. If a class implements both binary and bundled, then binary
 * will be preferred by both IonInput and IonOutput.
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public interface IonObjBinary {
	
	/**
	 * Load data from the input stream.
	 * 
	 * @param in input stream
	 * @throws IOException
	 */
	void load(IonInput in) throws IOException;
	
	
	/**
	 * Store data to output stream (in such way that the load method will later
	 * be able to read it).
	 * 
	 * @param out Output stream
	 * @throws IOException
	 */
	void save(IonOutput out) throws IOException;
}

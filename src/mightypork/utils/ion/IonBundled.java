package mightypork.utils.ion;


/**
 * Bundled ion object. If a class implements both binary and bundled, then
 * binary will be preferred by both IonInput and IonOutput.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public interface IonBundled {
	
	/**
	 * Load this object from the data bundle
	 *
	 * @param in bundle to load from
	 */
	void load(IonDataBundle in);
	
	
	/**
	 * Save this object to the data bundle
	 *
	 * @param out bundle to save into
	 */
	void save(IonDataBundle out);
}

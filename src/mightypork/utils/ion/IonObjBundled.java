package mightypork.utils.ion;


/**
 * Bundled ion object. If a class implements both binary and bundled, then
 * binary will be preferred by both IonInput and IonOutput.
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public interface IonObjBundled {
	
	void load(IonBundle in);
	
	
	void save(IonBundle out);
}

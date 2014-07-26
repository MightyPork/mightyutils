package mightypork.utils.ion;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


@SuppressWarnings({ "rawtypes", "unchecked" })
class IonSequenceWrapper implements IonBinary {
	
	private Collection collection = new ArrayList();
	
	
	public IonSequenceWrapper() {
		collection = new ArrayList();
	}
	
	
	public IonSequenceWrapper(Collection saved) {
		collection = saved;
	}
	
	
	@Override
	public void load(IonInput in) throws IOException
	{
		collection.clear();
		in.readSequence(collection);
	}
	
	
	@Override
	public void save(IonOutput out) throws IOException
	{
		out.writeSequence(collection);
	}
	
	
	public void fill(Collection o)
	{
		o.clear();
		o.addAll(collection);
	}
	
	
	public Collection getSequence()
	{
		return collection;
	}
	
}

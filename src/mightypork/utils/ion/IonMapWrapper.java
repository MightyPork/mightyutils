package mightypork.utils.ion;


import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


@SuppressWarnings({ "rawtypes", "unchecked" })
class IonMapWrapper implements IonBinary {

	private final Map map;


	public IonMapWrapper()
	{
		map = new LinkedHashMap<>();
	}


	public IonMapWrapper(Map saved)
	{
		map = saved;
	}


	@Override
	public void load(IonInput in) throws IOException
	{
		map.clear();
		in.readMap(map);
	}


	@Override
	public void save(IonOutput out) throws IOException
	{
		out.writeMap(map);
	}


	public void fill(Map o)
	{
		o.clear();
		o.putAll(map);
	}


	public Map getMap()
	{
		return map;
	}

}

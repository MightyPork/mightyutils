package mightypork.utils.math.constraints.num.proxy;


import mightypork.utils.math.constraints.num.Num;


public abstract class NumAdapter extends Num {

	protected abstract Num getSource();


	@Override
	public double value()
	{
		return getSource().value();
	}

}

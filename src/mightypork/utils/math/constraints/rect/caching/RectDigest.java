package mightypork.utils.math.constraints.rect.caching;


import mightypork.utils.math.constraints.rect.Rect;


public class RectDigest {

	public final double x;
	public final double y;
	public final double width;
	public final double height;

	public final double left;
	public final double right;
	public final double top;
	public final double bottom;


	public RectDigest(Rect rect)
	{
		this.x = rect.origin().x();
		this.y = rect.origin().y();

		this.width = rect.size().x();
		this.height = rect.size().y();

		this.left = x;
		this.right = x + width;
		this.top = y;
		this.bottom = y + height;
	}


	@Override
	public String toString()
	{
		return String
				.format("Rect{ at: (%.1f, %.1f), size: (%.1f, %.1f), bounds: L %.1f R %.1f T %.1f B %.1f }", x, y, width, height, left, right, top, bottom);
	}
}

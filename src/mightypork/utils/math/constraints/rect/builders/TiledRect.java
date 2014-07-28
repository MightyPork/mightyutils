package mightypork.utils.math.constraints.rect.builders;


import mightypork.utils.math.constraints.num.Num;
import mightypork.utils.math.constraints.rect.Rect;
import mightypork.utils.math.constraints.rect.proxy.RectProxy;
import mightypork.utils.math.constraints.vect.Vect;


/**
 * Utility for cutting rect into evenly sized cells.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class TiledRect extends RectProxy {

	final private int tilesY;
	final private int tilesX;
	final private Num perRow;
	final private Num perCol;

	/** Left top tile */
	private Rect aTile;


	public TiledRect(Rect source, int horizontal, int vertical)
	{
		super(source);
		this.tilesX = horizontal;
		this.tilesY = vertical;

		this.perRow = height().div(vertical);
		this.perCol = width().div(horizontal);

		this.aTile = Rect.make(origin(), perCol, perRow);
	}


	/**
	 * Set tile overlap. Applies only to tile, not span.
	 *
	 * @param overlap how far to overlap to neighbouring tiles on all sides
	 */
	public void setOverlap(double overlap)
	{
		aTile = aTile.grow(overlap);
	}


	/**
	 * Get a tile.
	 *
	 * @param x x position
	 * @param y y position
	 * @return tile
	 * @throws IndexOutOfBoundsException when invalid index is specified.
	 */
	public Rect tile(int x, int y)
	{
		if (x >= tilesX || x < 0) {
			throw new IndexOutOfBoundsException("X coordinate out fo range: " + x);
		}

		if (y >= tilesY || y < 0) {
			throw new IndexOutOfBoundsException("Y coordinate out of range: " + y);
		}

		return aTile.move(perCol.mul(x), perRow.mul(y));
	}


	/**
	 * Get a span (tile spanning across multiple cells)
	 *
	 * @param x x start position
	 * @param y y start position
	 * @param size_x horizontal size (columns)
	 * @param size_y vertical size (rows)
	 * @return tile the tile
	 * @throws IndexOutOfBoundsException when invalid index / size is specified.
	 */
	public Rect span(int x, int y, int size_x, int size_y)
	{
		final int x_to = x + size_x - 1;
		final int y_to = y + size_y - 1;

		if (size_x <= 0 || size_y <= 0) {
			throw new IndexOutOfBoundsException("Size must be > 0.");
		}

		if (x >= tilesX || x < 0 || x_to >= tilesX || x_to < 0) {
			throw new IndexOutOfBoundsException("X coordinate(s) out of range.");
		}

		if (y >= tilesY || y < 0 || y_to >= tilesY || y_to < 0) {
			throw new IndexOutOfBoundsException("Y coordinate(s) out of range.");
		}

		final Vect orig = origin().add(perCol.mul(x), perRow.mul(y));

		return Rect.make(orig, perCol.mul(size_x), perRow.mul(size_y));
	}


	/**
	 * Get n-th column
	 *
	 * @param n column index
	 * @return the column tile
	 * @throws IndexOutOfBoundsException when invalid index is specified.
	 */
	public Rect column(int n)
	{
		return tile(n, 0);
	}


	/**
	 * Get n-th row
	 *
	 * @param n row index
	 * @return the row rect
	 * @throws IndexOutOfBoundsException when invalid index is specified.
	 */
	public Rect row(int n)
	{
		return tile(0, n);
	}
}

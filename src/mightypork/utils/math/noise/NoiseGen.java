package mightypork.utils.math.noise;


/**
 * 2D Perlin noise generator
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class NoiseGen {
	
	private static final double lowBound = -0.7072;
	private static final double highBound = 0.7072;
	
	private final PerlinNoiseGenerator noiser;
	
	private final double lowMul;
	private final double highMul;
	private final double middle;
	private final double density;
	
	
	/**
	 * make a new noise generator with a random seed
	 * 
	 * @param density noise density (0..1). Lower density means larger "spots".
	 * @param low low bound ("valley")
	 * @param middle middle bound ("surface")
	 * @param high high bound ("hill")
	 */
	public NoiseGen(double density, double low, double middle, double high)
	{
		this(density, low, middle, high, Double.doubleToLongBits(Math.random()));
	}
	
	
	/**
	 * make a new noise generator
	 * 
	 * @param density noise density (0..1). Lower density means larger "spots".
	 * @param low low bound ("valley")
	 * @param middle middle bound ("surface")
	 * @param high high bound ("hill")
	 * @param seed random seed to use
	 */
	public NoiseGen(double density, double low, double middle, double high, long seed)
	{
		if (low > middle || middle > high) throw new IllegalArgumentException("Invalid value range.");
		
		this.density = density;
		
		// norm low and high to be around zero
		low -= middle;
		high -= middle;
		
		// scale		
		this.middle = middle;
		
		lowMul = Math.abs(low / lowBound);
		highMul = Math.abs(high / highBound);
		
		noiser = new PerlinNoiseGenerator(seed);
	}
	
	
	/**
	 * Get value at coord
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return value
	 */
	public double valueAt(double x, double y)
	{
		double raw = noiser.noise2(x * density, y * density);
		
		if (raw < lowBound) {
			raw = lowBound;
		} else if (raw > highBound) {
			raw = highBound;
		}
		
		if (raw < 0) {
			return middle + lowMul * raw;
		} else {
			return middle + highMul * raw;
		}
	}
	
	
	/**
	 * Build a map [height][width] of noise values
	 * 
	 * @param width map width (number of columns)
	 * @param height map height (number of rows )
	 * @return the map
	 */
	public double[][] buildMap(int width, int height)
	{
		final double[][] map = new double[height][width];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				map[y][x] = valueAt(x, y);
			}
		}
		
		return map;
	}
}

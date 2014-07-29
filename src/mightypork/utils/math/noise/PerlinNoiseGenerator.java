/*****************************************************************************
 *                        J3D.org Copyright (c) 2000
 *                               Java Source
 *
 * This source is licensed under the GNU LGPL v2.1
 * Please read http://www.gnu.org/copyleft/lgpl.html for more information
 *
 * This software comes with the standard NO WARRANTY disclaimer for any
 * purpose. Use it at your own risk. If there's a problem you get to fix it.
 *
 ****************************************************************************/
package mightypork.utils.math.noise;


import java.util.Random;


/**
 * Computes Perlin Noise for three dimensions.
 * <p>
 * The result is a continuous function that interpolates a smooth path along a
 * series random points. The function is consitent, so given the same
 * parameters, it will always return the same value. The smoothing function is
 * based on the Improving Noise paper presented at Siggraph 2002.
 * <p>
 * Computing noise for one and two dimensions can make use of the 3D problem
 * space by just setting the un-needed dimensions to a fixed value.
 *
 * @author Justin Couch
 * @edited by Ondřej Hruška
 * @version $Revision: 1.4 $
 * @source http://code.j3d.org/download.html
 */
public class PerlinNoiseGenerator {
	
	// Constants for setting up the Perlin-1 noise functions
	private static final int B = 0x1000;
	private static final int BM = 0xff;
	
	private static final int N = 0x1000;
	
	/** Default seed to use for the random number generation */
	private static final int DEFAULT_SEED = 100;
	
	/** Default sample size to work with */
	private static final int DEFAULT_SAMPLE_SIZE = 256;
	
	private final Random rand = new Random(DEFAULT_SEED);
	
	/** Permutation array for the improved noise function */
	private final int[] p_imp;
	
	/** P array for perline 1 noise */
	private int[] p;
	private double[][] g3;
	private double[][] g2;
	private double[] g1;
	
	
	/**
	 * Create a new noise creator with the default seed value
	 */
	public PerlinNoiseGenerator()
	{
		this(DEFAULT_SEED);
	}
	
	
	/**
	 * Create a new noise creator with the given seed value for the randomness
	 *
	 * @param seed The seed value to use
	 */
	public PerlinNoiseGenerator(long seed)
	{
		p_imp = new int[DEFAULT_SAMPLE_SIZE << 1];
		
		int i, j, k;
		rand.setSeed(seed);
		
		// Calculate the table of psuedo-random coefficients.
		for (i = 0; i < DEFAULT_SAMPLE_SIZE; i++)
			p_imp[i] = i;
		
		// generate the psuedo-random permutation table.
		while (--i > 0) {
			k = p_imp[i];
			j = (int) (rand.nextLong() & DEFAULT_SAMPLE_SIZE);
			p_imp[i] = p_imp[j];
			p_imp[j] = k;
		}
		
		initPerlin1();
	}
	
	
	/**
	 * Computes noise function for three dimensions at the point (x,y,z).
	 *
	 * @param x x dimension parameter
	 * @param y y dimension parameter
	 * @param z z dimension parameter
	 * @return the noise value at the point (x, y, z)
	 */
	public double improvedNoise(double x, double y, double z)
	{
		// Constraint the point to a unit cube
		final int uc_x = (int) Math.floor(x) & 255;
		final int uc_y = (int) Math.floor(y) & 255;
		final int uc_z = (int) Math.floor(z) & 255;
		
		// Relative location of the point in the unit cube
		final double xo = x - Math.floor(x);
		final double yo = y - Math.floor(y);
		final double zo = z - Math.floor(z);
		
		// Fade curves for x, y and z
		final double u = fade(xo);
		final double v = fade(yo);
		final double w = fade(zo);
		
		// Generate a hash for each coordinate to find out where in the cube
		// it lies.
		final int a = p_imp[uc_x] + uc_y;
		final int aa = p_imp[a] + uc_z;
		final int ab = p_imp[a + 1] + uc_z;
		
		final int b = p_imp[uc_x + 1] + uc_y;
		final int ba = p_imp[b] + uc_z;
		final int bb = p_imp[b + 1] + uc_z;
		
		// blend results from the 8 corners based on the noise function
		final double c1 = grad(p_imp[aa], xo, yo, zo);
		final double c2 = grad(p_imp[ba], xo - 1, yo, zo);
		final double c3 = grad(p_imp[ab], xo, yo - 1, zo);
		final double c4 = grad(p_imp[bb], xo - 1, yo - 1, zo);
		final double c5 = grad(p_imp[aa + 1], xo, yo, zo - 1);
		final double c6 = grad(p_imp[ba + 1], xo - 1, yo, zo - 1);
		final double c7 = grad(p_imp[ab + 1], xo, yo - 1, zo - 1);
		final double c8 = grad(p_imp[bb + 1], xo - 1, yo - 1, zo - 1);
		
		return lerp(w, lerp(v, lerp(u, c1, c2), lerp(u, c3, c4)), lerp(v, lerp(u, c5, c6), lerp(u, c7, c8)));
	}
	
	
	/**
	 * 1-D noise generation function using the original perlin algorithm.
	 *
	 * @param x Seed for the noise function
	 * @return The noisy output
	 */
	public double noise1(double x)
	{
		final double t = x + N;
		final int bx0 = ((int) t) & BM;
		final int bx1 = (bx0 + 1) & BM;
		final double rx0 = t - (int) t;
		final double rx1 = rx0 - 1;
		
		final double sx = sCurve(rx0);
		
		final double u = rx0 * g1[p[bx0]];
		final double v = rx1 * g1[p[bx1]];
		
		return lerp(sx, u, v);
	}
	
	
	/**
	 * Create noise in a 2D space using the orignal perlin noise algorithm.
	 *
	 * @param x The X coordinate of the location to sample
	 * @param y The Y coordinate of the location to sample
	 * @return A noisy value at the given position
	 */
	public double noise2(double x, double y)
	{
		double t = x + N;
		final int bx0 = ((int) t) & BM;
		final int bx1 = (bx0 + 1) & BM;
		final double rx0 = t - (int) t;
		final double rx1 = rx0 - 1;
		
		t = y + N;
		final int by0 = ((int) t) & BM;
		final int by1 = (by0 + 1) & BM;
		final double ry0 = t - (int) t;
		final double ry1 = ry0 - 1;
		
		final int i = p[bx0];
		final int j = p[bx1];
		
		final int b00 = p[i + by0];
		final int b10 = p[j + by0];
		final int b01 = p[i + by1];
		final int b11 = p[j + by1];
		
		final double sx = sCurve(rx0);
		final double sy = sCurve(ry0);
		
		double[] q = g2[b00];
		double u = rx0 * q[0] + ry0 * q[1];
		q = g2[b10];
		double v = rx1 * q[0] + ry0 * q[1];
		final double a = lerp(sx, u, v);
		
		q = g2[b01];
		u = rx0 * q[0] + ry1 * q[1];
		q = g2[b11];
		v = rx1 * q[0] + ry1 * q[1];
		final double b = lerp(sx, u, v);
		
		return lerp(sy, a, b);
	}
	
	
	/**
	 * Create noise in a 3D space using the orignal perlin noise algorithm.
	 *
	 * @param x The X coordinate of the location to sample
	 * @param y The Y coordinate of the location to sample
	 * @param z The Z coordinate of the location to sample
	 * @return A noisy value at the given position
	 */
	public double noise3(double x, double y, double z)
	{
		double t = x + N;
		final int bx0 = ((int) t) & BM;
		final int bx1 = (bx0 + 1) & BM;
		final double rx0 = t - (int) t;
		final double rx1 = rx0 - 1;
		
		t = y + N;
		final int by0 = ((int) t) & BM;
		final int by1 = (by0 + 1) & BM;
		final double ry0 = t - (int) t;
		final double ry1 = ry0 - 1;
		
		t = z + N;
		final int bz0 = ((int) t) & BM;
		final int bz1 = (bz0 + 1) & BM;
		final double rz0 = t - (int) t;
		final double rz1 = rz0 - 1;
		
		final int i = p[bx0];
		final int j = p[bx1];
		
		final int b00 = p[i + by0];
		final int b10 = p[j + by0];
		final int b01 = p[i + by1];
		final int b11 = p[j + by1];
		
		t = sCurve(rx0);
		final double sy = sCurve(ry0);
		final double sz = sCurve(rz0);
		
		double[] q = g3[b00 + bz0];
		double u = (rx0 * q[0] + ry0 * q[1] + rz0 * q[2]);
		q = g3[b10 + bz0];
		double v = (rx1 * q[0] + ry0 * q[1] + rz0 * q[2]);
		double a = lerp(t, u, v);
		
		q = g3[b01 + bz0];
		u = (rx0 * q[0] + ry1 * q[1] + rz0 * q[2]);
		q = g3[b11 + bz0];
		v = (rx1 * q[0] + ry1 * q[1] + rz0 * q[2]);
		double b = lerp(t, u, v);
		
		final double c = lerp(sy, a, b);
		
		q = g3[b00 + bz1];
		u = (rx0 * q[0] + ry0 * q[1] + rz1 * q[2]);
		q = g3[b10 + bz1];
		v = (rx1 * q[0] + ry0 * q[1] + rz1 * q[2]);
		a = lerp(t, u, v);
		
		q = g3[b01 + bz1];
		u = (rx0 * q[0] + ry1 * q[1] + rz1 * q[2]);
		q = g3[b11 + bz1];
		v = (rx1 * q[0] + ry1 * q[1] + rz1 * q[2]);
		b = lerp(t, u, v);
		
		final double d = lerp(sy, a, b);
		
		return lerp(sz, c, d);
	}
	
	
	/**
	 * Create a turbulent noise output based on the core noise function. This
	 * uses the noise as a base function and is suitable for creating clouds,
	 * marble and explosion effects. For example, a typical marble effect would
	 * set the colour to be:
	 *
	 * <pre>
	 *    sin(point + turbulence(point) * point.x);
	 * </pre>
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param loF
	 * @param hiF
	 * @return value
	 */
	public double imporvedTurbulence(double x, double y, double z, double loF, double hiF)
	{
		double p_x = x + 123.456f;
		double p_y = y;
		double p_z = z;
		double t = 0;
		double f;
		
		for (f = loF; f < hiF; f *= 2) {
			t += Math.abs(improvedNoise(p_x, p_y, p_z)) / f;
			
			p_x *= 2;
			p_y *= 2;
			p_z *= 2;
		}
		
		return t - 0.3;
	}
	
	
	/**
	 * Create a turbulance function in 2D using the original perlin noise
	 * function.
	 *
	 * @param x The X coordinate of the location to sample
	 * @param y The Y coordinate of the location to sample
	 * @param freq The frequency of the turbluance to create
	 * @return The value at the given coordinates
	 */
	public double turbulence2(double x, double y, double freq)
	{
		double t = 0;
		
		do {
			t += noise2(freq * x, freq * y) / freq;
			freq *= 0.5f;
		} while (freq >= 1);
		
		return t;
	}
	
	
	/**
	 * Create a turbulance function in 3D using the original perlin noise
	 * function.
	 *
	 * @param x The X coordinate of the location to sample
	 * @param y The Y coordinate of the location to sample
	 * @param z The Z coordinate of the location to sample
	 * @param freq The frequency of the turbluance to create
	 * @return The value at the given coordinates
	 */
	public double turbulence3(double x, double y, double z, double freq)
	{
		double t = 0;
		
		do {
			t += noise3(freq * x, freq * y, freq * z) / freq;
			freq *= 0.5f;
		} while (freq >= 1);
		
		return t;
	}
	
	
	/**
	 * Create a 1D tileable noise function for the given width.
	 *
	 * @param x The X coordinate to generate the noise for
	 * @param w The width of the tiled block
	 * @return The value of the noise at the given coordinate
	 */
	public double tileableNoise1(double x, double w)
	{
		return (noise1(x) * (w - x) + noise1(x - w) * x) / w;
	}
	
	
	/**
	 * Create a 2D tileable noise function for the given width and height.
	 *
	 * @param x The X coordinate to generate the noise for
	 * @param y The Y coordinate to generate the noise for
	 * @param w The width of the tiled block
	 * @param h The height of the tiled block
	 * @return The value of the noise at the given coordinate
	 */
	public double tileableNoise2(double x, double y, double w, double h)
	{
		return (noise2(x, y) * (w - x) * (h - y) + noise2(x - w, y) * x * (h - y) + noise2(x, y - h) * (w - x) * y + noise2(x - w, y - h) * x * y) / (w * h);
	}
	
	
	/**
	 * Create a 3D tileable noise function for the given width, height and
	 * depth.
	 *
	 * @param x The X coordinate to generate the noise for
	 * @param y The Y coordinate to generate the noise for
	 * @param z The Z coordinate to generate the noise for
	 * @param w The width of the tiled block
	 * @param h The height of the tiled block
	 * @param d The depth of the tiled block
	 * @return The value of the noise at the given coordinate
	 */
	public double tileableNoise3(double x, double y, double z, double w, double h, double d)
	{
		return (noise3(x, y, z) * (w - x) * (h - y) * (d - z) + noise3(x - w, y, z) * x * (h - y) * (d - z) + noise3(x, y - h, z) * (w - x) * y * (d - z)
				+ noise3(x - w, y - h, z) * x * y * (d - z) + noise3(x, y, z - d) * (w - x) * (h - y) * z + noise3(x - w, y, z - d) * x * (h - y) * z
				+ noise3(x, y - h, z - d) * (w - x) * y * z + noise3(x - w, y - h, z - d) * x * y * z)
				/ (w * h * d);
	}
	
	
	/**
	 * Create a turbulance function that can be tiled across a surface in 2D.
	 *
	 * @param x The X coordinate of the location to sample
	 * @param y The Y coordinate of the location to sample
	 * @param w The width to tile over
	 * @param h The height to tile over
	 * @param freq The frequency of the turbluance to create
	 * @return The value at the given coordinates
	 */
	public double tileableTurbulence2(double x, double y, double w, double h, double freq)
	{
		double t = 0;
		
		do {
			t += tileableNoise2(freq * x, freq * y, w * freq, h * freq) / freq;
			freq *= 0.5f;
		} while (freq >= 1);
		
		return t;
	}
	
	
	/**
	 * Create a turbulance function that can be tiled across a surface in 3D.
	 *
	 * @param x The X coordinate of the location to sample
	 * @param y The Y coordinate of the location to sample
	 * @param z The Z coordinate of the location to sample
	 * @param w The width to tile over
	 * @param h The height to tile over
	 * @param d The depth to tile over
	 * @param freq The frequency of the turbluance to create
	 * @return The value at the given coordinates
	 */
	public double tileableTurbulence3(double x, double y, double z, double w, double h, double d, double freq)
	{
		double t = 0;
		
		do {
			t += tileableNoise3(freq * x, freq * y, freq * z, w * freq, h * freq, d * freq) / freq;
			freq *= 0.5f;
		} while (freq >= 1);
		
		return t;
	}
	
	
	/**
	 * Simple lerp function using doubles.
	 */
	private double lerp(double t, double a, double b)
	{
		return a + t * (b - a);
	}
	
	
	/**
	 * Fade curve calculation which is 6t^5 - 15t^4 + 10t^3. This is the new
	 * algorithm, where the old one used to be 3t^2 - 2t^3.
	 *
	 * @param t The t parameter to calculate the fade for
	 * @return the drop-off amount.
	 */
	private double fade(double t)
	{
		return t * t * t * (t * (t * 6 - 15) + 10);
	}
	
	
	/**
	 * Calculate the gradient function based on the hash code.
	 */
	private double grad(int hash, double x, double y, double z)
	{
		// Convert low 4 bits of hash code into 12 gradient directions.
		final int h = hash & 15;
		final double u = (h < 8 || h == 12 || h == 13) ? x : y;
		final double v = (h < 4 || h == 12 || h == 13) ? y : z;
		
		return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
	}
	
	
	/**
	 * S-curve function for value distribution for Perlin-1 noise function.
	 */
	private double sCurve(double t)
	{
		return (t * t * (3 - 2 * t));
	}
	
	
	/**
	 * 2D-vector normalisation function.
	 */
	private void normalize2(double[] v)
	{
		final double s = 1 / Math.sqrt(v[0] * v[0] + v[1] * v[1]);
		v[0] *= s;
		v[1] *= s;
	}
	
	
	/**
	 * 3D-vector normalisation function.
	 */
	private void normalize3(double[] v)
	{
		final double s = 1 / Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
		v[0] *= s;
		v[1] *= s;
		v[2] *= s;
	}
	
	
	/**
	 * Initialise the lookup arrays used by Perlin 1 function.
	 */
	private void initPerlin1()
	{
		p = new int[B + B + 2];
		g3 = new double[B + B + 2][3];
		g2 = new double[B + B + 2][2];
		g1 = new double[B + B + 2];
		int i, j, k;
		
		for (i = 0; i < B; i++) {
			p[i] = i;
			
			g1[i] = (((rand.nextDouble() * Integer.MAX_VALUE) % (B + B)) - B) / B;
			
			for (j = 0; j < 2; j++)
				g2[i][j] = (((rand.nextDouble() * Integer.MAX_VALUE) % (B + B)) - B) / B;
			normalize2(g2[i]);
			
			for (j = 0; j < 3; j++)
				g3[i][j] = (((rand.nextDouble() * Integer.MAX_VALUE) % (B + B)) - B) / B;
			normalize3(g3[i]);
		}
		
		while (--i > 0) {
			k = p[i];
			j = (int) ((rand.nextDouble() * Integer.MAX_VALUE) % B);
			p[i] = p[j];
			p[j] = k;
		}
		
		for (i = 0; i < B + 2; i++) {
			p[B + i] = p[i];
			g1[B + i] = g1[i];
			for (j = 0; j < 2; j++)
				g2[B + i][j] = g2[i][j];
			for (j = 0; j < 3; j++)
				g3[B + i][j] = g3[i][j];
		}
	}
}

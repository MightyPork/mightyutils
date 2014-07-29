package mightypork.utils.math.angles;


/**
 * Common angles functionality
 *
 * @author Ondřej Hruška (MightyPork)
 */
class Angles {
	
	/**
	 * Delta of two angles (positive or negative - positive is CCW)
	 *
	 * @param alpha first angle
	 * @param beta second angle
	 * @param fullAngle value of full angle
	 * @return delta of the two angles
	 */
	public static double delta(double alpha, double beta, double fullAngle)
	{
		while (Math.abs(alpha - beta) > fullAngle / 2D) {
			alpha = norm(alpha + fullAngle / 2D, fullAngle);
			beta = norm(beta + fullAngle / 2D, fullAngle);
		}
		
		return beta - alpha;
	}
	
	
	/**
	 * Difference of two angles (same as delta, but always positive)
	 *
	 * @param alpha first angle
	 * @param beta second angle
	 * @param fullAngle value of full angle
	 * @return delta of the two angles
	 */
	public static double diff(double alpha, double beta, double fullAngle)
	{
		return Math.abs(delta(alpha, beta, fullAngle));
	}
	
	
	/**
	 * Normalize angle to 0-full range
	 *
	 * @param angle angle
	 * @param fullAngle full angle
	 * @return angle normalized
	 */
	public static double norm(double angle, double fullAngle)
	{
		while (angle < 0)
			angle += fullAngle;
		while (angle > fullAngle)
			angle -= fullAngle;
		return angle;
	}
}

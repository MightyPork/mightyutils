package mightypork.utils.math.timing;


import mightypork.utils.Support;


/**
 * Time metering utils for profiling.<br>
 * The profiler work with long (starting ms time), so it has very little
 * overhead and you can easily have multiple "profilers" running at the same
 * time.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class Profiler {

	/**
	 * Get current time, to be later used in the end*() methods
	 *
	 * @return current time (ms)
	 */
	public static long begin()
	{
		return System.currentTimeMillis();
	}


	/**
	 * Get seconds since begin.
	 *
	 * @param begun profiling start time (ms), obtained using begin()
	 * @return seconds elapsed
	 */
	public static double end(long begun)
	{
		return endMs(begun) / 1000D;
	}


	/**
	 * Get milliseconds since begin.
	 *
	 * @param begun profiling start time (ms), obtained using begin()
	 * @return milliseconds elapsed
	 */
	public static long endMs(long begun)
	{
		return System.currentTimeMillis() - begun;
	}


	/**
	 * Elapsed time in human readable format, in seconds.
	 *
	 * @param begun profiling start time (ms), obtained using begin()
	 * @return something like "0.121 s"
	 */
	public static String endStr(long begun)
	{
		return Support.str(end(begun)) + " s";
	}

}

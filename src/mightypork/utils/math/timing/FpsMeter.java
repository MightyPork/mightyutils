package mightypork.utils.math.timing;


/**
 * Class for counting FPS in games.<br>
 * This class can be used also as a simple frequency meter - output is in Hz.
 *
 * @author Ondřej Hruška (MightyPork)
 */
public class FpsMeter {
	
	private long frames = 0;
	private long lastTimeMillis = System.currentTimeMillis();
	private long lastSecFPS = 0;
	
	
	/**
	 * @return current second's FPS
	 */
	public long getFPS()
	{
		return lastSecFPS;
	}
	
	
	/**
	 * Notification that frame was rendered
	 */
	public void frame()
	{
		if (System.currentTimeMillis() - lastTimeMillis > 1000) {
			lastSecFPS = frames;
			frames = 0;
			final long over = System.currentTimeMillis() - lastTimeMillis - 1000;
			lastTimeMillis = System.currentTimeMillis() - over;
		}
		frames++;
	}
}

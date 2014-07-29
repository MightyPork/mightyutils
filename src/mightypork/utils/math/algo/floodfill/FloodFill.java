package mightypork.utils.math.algo.floodfill;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import mightypork.utils.math.algo.Coord;
import mightypork.utils.math.algo.Move;


public abstract class FloodFill {
	
	public abstract boolean canEnter(Coord pos);
	
	
	public abstract boolean canSpreadFrom(Coord pos);
	
	
	public abstract List<Move> getSpreadSides();
	
	
	/**
	 * Get the max distance filled form start point. Use -1 for unlimited range.
	 *
	 * @return max distance
	 */
	public abstract double getMaxDistance();
	
	
	/**
	 * @return true if start should be spread no matter what
	 */
	public abstract boolean forceSpreadStart();
	
	
	/**
	 * Fill an area
	 *
	 * @param start start point
	 * @param foundNodes collection to put filled coords in
	 * @return true if fill was successful; false if max range was reached.
	 */
	public final boolean fill(Coord start, Collection<Coord> foundNodes)
	{
		final Queue<Coord> activeNodes = new LinkedList<>();
		
		final double maxDist = getMaxDistance();
		
		activeNodes.add(start);
		
		boolean forceSpreadNext = forceSpreadStart();
		
		boolean limitReached = false;
		
		while (!activeNodes.isEmpty()) {
			final Coord current = activeNodes.poll();
			foundNodes.add(current);
			
			if (!canSpreadFrom(current) && !forceSpreadNext) continue;
			
			forceSpreadNext = false;
			
			for (final Move spr : getSpreadSides()) {
				final Coord next = current.add(spr);
				if (activeNodes.contains(next) || foundNodes.contains(next)) continue;
				
				if (next.dist(start) > maxDist) {
					limitReached = true;
					continue;
				}
				
				if (canEnter(next)) {
					activeNodes.add(next);
				} else {
					foundNodes.add(next);
				}
			}
		}
		
		return !limitReached;
	}
}

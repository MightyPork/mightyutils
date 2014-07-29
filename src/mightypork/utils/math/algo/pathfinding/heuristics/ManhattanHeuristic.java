package mightypork.utils.math.algo.pathfinding.heuristics;


import mightypork.utils.math.algo.Coord;
import mightypork.utils.math.algo.pathfinding.Heuristic;


public class ManhattanHeuristic extends Heuristic {
	
	@Override
	public double getCost(Coord pos, Coord target)
	{
		return Math.abs(target.x - pos.x) + Math.abs(target.y - pos.y);
	}
}

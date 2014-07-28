package mightypork.utils.math.algo.pathfinding;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import mightypork.utils.math.algo.Coord;
import mightypork.utils.math.algo.Move;
import mightypork.utils.math.algo.pathfinding.heuristics.DiagonalHeuristic;
import mightypork.utils.math.algo.pathfinding.heuristics.ManhattanHeuristic;


/**
 * A* pathfinder
 *
 * @author Ondřej Hruška (MightyPork)
 */
public abstract class PathFinder {

	private static final FComparator F_COMPARATOR = new FComparator();

	public static final Heuristic CORNER_HEURISTIC = new ManhattanHeuristic();
	public static final Heuristic DIAGONAL_HEURISTIC = new DiagonalHeuristic();

	private boolean ignoreStart;
	private boolean ignoreEnd;


	public List<Move> findPathRelative(Coord start, Coord end)
	{
		return findPathRelative(start, end, ignoreStart, ignoreEnd);
	}


	public List<Move> findPathRelative(Coord start, Coord end, boolean ignoreStart, boolean ignoreEnd)
	{
		final List<Coord> path = findPath(start, end, ignoreStart, ignoreEnd);

		if (path == null) return null;

		final List<Move> out = new ArrayList<>();

		final Coord current = start.copy();
		for (final Coord c : path) {
			if (c.equals(current)) continue;
			out.add(Move.make(c.x - current.x, c.y - current.y));
			current.x = c.x;
			current.y = c.y;
		}

		return out;
	}


	public List<Coord> findPath(Coord start, Coord end)
	{
		return findPath(start, end, ignoreStart, ignoreEnd);
	}


	public List<Coord> findPath(Coord start, Coord end, boolean ignoreStart, boolean ignoreEnd)
	{
		final LinkedList<Node> open = new LinkedList<>();
		final LinkedList<Node> closed = new LinkedList<>();

		final Heuristic heuristic = getHeuristic();

		// add first node
		{
			final Node n = new Node(start);
			n.h_cost = (int) (heuristic.getCost(start, end) * getMinCost());
			n.g_cost = 0;
			open.add(n);
		}

		Node current = null;

		while (true) {
			current = open.poll();

			if (current == null) {
				break;
			}

			closed.add(current);

			if (current.pos.equals(end)) {
				break;
			}

			for (final Move go : getWalkSides()) {

				final Coord c = current.pos.add(go);
				if (!isAccessible(c) && !(c.equals(end) && ignoreEnd) && !(c.equals(start) && ignoreStart)) continue;
				final Node a = new Node(c);
				a.g_cost = current.g_cost + getCost(c, a.pos);
				a.h_cost = (int) (heuristic.getCost(a.pos, end) * getMinCost());
				a.parent = current;

				if (!closed.contains(a)) {

					if (open.contains(a)) {

						boolean needSort = false;

						// find where it is
						for (final Node n : open) {
							if (n.pos.equals(a.pos)) { // found it
								if (n.g_cost > a.g_cost) {
									n.parent = current;
									n.g_cost = a.g_cost;
									needSort = true;
								}
								break;
							}
						}

						if (needSort) Collections.sort(open, F_COMPARATOR);

					} else {
						open.add(a);
					}
				}
			}

		}

		if (current == null) {
			return null; // no path found
		}

		final LinkedList<Coord> path = new LinkedList<>();

		// extract path elements
		while (current != null) {
			path.addFirst(current.pos);
			current = current.parent;
		}

		return path;
	}

	private static class Node {

		Coord pos;
		int g_cost; // to get there
		int h_cost; // to target
		Node parent;


		public Node(Coord pos)
		{
			this.pos = pos;
		}


		int fCost()
		{
			return g_cost + h_cost;
		}


		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((pos == null) ? 0 : pos.hashCode());
			return result;
		}


		@Override
		public boolean equals(Object obj)
		{
			if (this == obj) return true;
			if (obj == null) return false;
			if (!(obj instanceof Node)) return false;
			final Node other = (Node) obj;
			if (pos == null) {
				if (other.pos != null) return false;
			} else if (!pos.equals(other.pos)) return false;
			return true;
		}


		@Override
		public String toString()
		{
			return "N " + pos + ", G =" + g_cost + ", H = " + h_cost;
		}
	}

	private static class FComparator implements Comparator<Node> {

		@Override
		public int compare(Node n1, Node n2)
		{
			return n1.fCost() - n2.fCost();
		}
	}


	public void setIgnoreEnd(boolean ignoreEnd)
	{
		this.ignoreEnd = ignoreEnd;
	}


	public void setIgnoreStart(boolean ignoreStart)
	{
		this.ignoreStart = ignoreStart;
	}


	/**
	 * @return used heuristic
	 */
	protected abstract Heuristic getHeuristic();


	protected abstract List<Move> getWalkSides();


	/**
	 * @param pos tile pos
	 * @return true if the tile is walkable
	 */
	public abstract boolean isAccessible(Coord pos);


	/**
	 * Cost of walking onto a tile. It's useful to use ie. 10 for basic step.
	 *
	 * @param from last tile
	 * @param to current tile
	 * @return cost
	 */
	protected abstract int getCost(Coord from, Coord to);


	/**
	 * @return lowest cost. Used to multiply heuristics.
	 */
	protected abstract int getMinCost();
}

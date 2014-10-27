package com.bryanww.cycle.model;

import java.util.Iterator;

/**
 * Iterator to traverse the {@link Vertex}es of a {@link ConvexHull} in order
 * so that paths and cycles can be generated.
 * 
 * @author  Bryan Wagner
 * @since   2014-10-25
 * @version 2014-10-25
 */
public class ConvexHullIterator implements Iterator<Vertex> {
	
	public enum Direction {LEFT, RIGHT}
	
	protected ConvexHull convexHull;      // the {@link ConvexHull} to iterate
	protected int        currentIndex;    // the current {@link Vertex} index in the iteration
	protected Direction  direction;       // the iteration direction
	
	protected int        iterationCount;  // the current iteration index of this {@link ConvexHullIterator}
	protected int        iterationSize;   // the total number of iterations expected by this {@link ConvexHullIterator}
	
	/**
	 * Creates a new {@link ConvexHullIterator}.
	 * @param convexHull the {@link ConvexHull} to iterate
	 * @param startIndex the starting index of the iteration
	 * @param direction  the iteration direction
	 */
	public ConvexHullIterator(ConvexHull convexHull, int startIndex, Direction direction) {
		this.convexHull     = convexHull;
		this.currentIndex   = startIndex;
		this.direction      = direction;
		this.iterationCount = 0;
		this.iterationSize  = convexHull.size();
		if (currentIndex < 0 || currentIndex >= iterationSize) {
			throw new IndexOutOfBoundsException(String.format("startIndex=%s, convexHull.size()=%d", startIndex, convexHull.size()));
		}
	}
	
	/**
	 * Returns the current {@link Vertex} index in the iteration.
	 * @return the current {@link Vertex} index in the iteration
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	/**
	 * Returns the total number of remaining indices in the iteration.
	 * @return the total number of remaining indices in the iteration
	 */
	public int getRemainingIndices() {
		return iterationSize - iterationCount;
	}
	
	/**
	 * Returns the left or right {@link Direction} of this iteration.
	 * @return the left or right {@link Direction} of this iteration
	 */
	public Direction getDirection() {
		return direction;
	}
	
	@Override
	public boolean hasNext() {
		return iterationCount < iterationSize;
	}
	
	@Override
	public Vertex next() {
		if (!hasNext()) {
			return null;
		}
		Vertex vertex = convexHull.getVertex(currentIndex);
		if (direction == Direction.RIGHT) {
			if (++currentIndex >= iterationSize) {
				currentIndex = 0;
			}
		}
		else {
			if (--currentIndex < 0) {
				currentIndex = iterationSize - 1;
			}
		}
		iterationCount++;
		return vertex;
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[').append(getClass().getName()).append(':');
		
		builder.append("convexHull=").append(convexHull);
		builder.append("|currentIndex=").append(currentIndex);
		builder.append("|direction=").append(direction);
		builder.append("|iterationCount=").append(iterationCount);
		builder.append("|iterationSize=").append(iterationSize);
		
		return builder.append(']').toString();
	}
}

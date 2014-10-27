package com.bryanww.cycle.model;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.bryanww.cycle.model.ConvexHullIterator.Direction;

/**
 * Iterator to traverse the {@link Vertex}es of a {@link Cycle} in the ordering
 * of a Hamiltonian Cycle according to its built list of {@link CycleLayer}s.
 * This iterator is used to build the ordered cycle vertices contained within
 * the {@link Cycle} instance.  The Hamiltonian cycle is formed by connecting
 * the last vertex that is returned by the iteration to the first one.
 * 
 * @author  Bryan Wagner
 * @since   2014-10-25
 * @version 2014-10-25
 */
public class CycleIterator implements Iterator<Vertex> {
	
	protected List<CycleLayer>          cycleLayerList;   // the list of {@link CycleLayer}s being traversed
	protected int                       cycleLayerIndex;  // the current cycle layer index in the iteration that walks the cycle
	
	protected Stack<ConvexHullIterator> iteratorStack;    // the stack of {@link ConvexHullIterator}s used when walking the cycle
	protected ConvexHullIterator        iterator;         // the current {@link ConvexHullIterator} used within this {@link CycleIterator}
	protected int                       iterationCount;   // the current iteration index of this {@link CycleIterator}
	protected int                       iterationSize;    // the total number of iterations expected by this {@link CycleIterator}
	
	/**
	 * Creates a new {@link CycleIterator}.
	 * @param cycle the {@link Cycle} containing the build list of {@link CycleLayer}s
	 */
	public CycleIterator(Cycle cycle) {
		this.cycleLayerList   = cycle.getCycleLayerList();
		this.cycleLayerIndex  = cycleLayerList.isEmpty() ? -1 : 0;
		this.iteratorStack    = new Stack<ConvexHullIterator>();
		CycleLayer cycleLayer = cycleLayerList.isEmpty() ? null : cycleLayerList.get(0);
		int        startIndex = Math.max(0, cycleLayer.getChildRightBridgeIndex());
		this.iterator         = cycleLayer == null ? null : new ConvexHullIterator(cycleLayer.getConvexHull(), startIndex, Direction.RIGHT);
		this.iterationCount   = 0;
		this.iterationSize    = cycle.size();
	}
	
	@Override
	public boolean hasNext() {
		return iterationCount < iterationSize;
	}
	
	@Override
	public Vertex next() {
		iterationCount++;
		
		// pop finished layers
		while (!iteratorStack.isEmpty() && !iterator.hasNext()) {
			iterator = iteratorStack.pop();
			cycleLayerIndex--;
		}
		
		int    currentIndex = iterator.getCurrentIndex();
		Vertex vertex       = iterator.next();
		
		// check the child layer for bridges
		if (cycleLayerIndex < cycleLayerList.size() - 1) {
			CycleLayer parentCycleLayer = cycleLayerList.get(cycleLayerIndex);
			CycleLayer childCycleLayer  = cycleLayerList.get(cycleLayerIndex + 1);
			
			if (iterator.getDirection() == Direction.RIGHT) {
				if (currentIndex == parentCycleLayer.getChildLeftBridgeIndex()) {
					iteratorStack.push(iterator);
					cycleLayerIndex++;
					iterator = new ConvexHullIterator(childCycleLayer.getConvexHull(), childCycleLayer.getParentLeftBridgeIndex(), Direction.LEFT);
				}
			}
			else {
				if (currentIndex == parentCycleLayer.getChildRightBridgeIndex()) {
					iteratorStack.push(iterator);
					cycleLayerIndex++;
					iterator = new ConvexHullIterator(childCycleLayer.getConvexHull(), childCycleLayer.getParentRightBridgeIndex(), Direction.RIGHT);
				}
			}
		}
		
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
		
		builder.append("cycleLayerList=").append(cycleLayerList);
		builder.append("|iteratorStack=").append(iteratorStack);
		builder.append("|cycleLayerIndex=").append(cycleLayerIndex);
		builder.append("|iterator=").append(iterator);
		builder.append("|iterationCount=").append(iterationCount);
		builder.append("|iterationSize=").append(iterationSize);
		
		return builder.append(']').toString();
	}
}

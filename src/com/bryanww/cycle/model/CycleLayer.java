package com.bryanww.cycle.model;

/**
 * Encapsulates a layer of a {@link Cycle} with its {@link ConvexHull} layers
 * and corresponding parent and child bridge vertices.
 * 
 * 
 * @see     Cycle
 * @author  Bryan Wagner
 * @since   2014-10-25
 * @version 2014-10-25
 */
public class CycleLayer {
	
	protected ConvexHull convexHull;              // the {@link ConvexHull} containing the {@link Vertex}es of this layer
	protected int        childRightBridgeIndex;   // the right-hand "bridge" vertex of the hull on this layer that connects to the child layer ({@code -1} if none exists)
	protected int        childLeftBridgeIndex;    // the left-hand "bridge" vertex of the hull on this layer that connects to the child layer ({@code -1} if none exists)
	protected int        parentRightBridgeIndex;  // the right-hand "bridge" vertex of the hull on this layer that connects to the parent layer ({@code -1} if none exists)
	protected int        parentLeftBridgeIndex;   // the left-hand "bridge" vertex of the hull on this layer that connects to the parent layer ({@code -1} if none exists)
	
	/**
	 * Creates a new {@link CycleLayer}.
	 */
	public CycleLayer() {
		this.convexHull             = new ConvexHull();
		this.childRightBridgeIndex  = -1;
		this.childLeftBridgeIndex   = -1;
		this.parentRightBridgeIndex = -1;
		this.parentLeftBridgeIndex  = -1;
	}
	
	/**
	 * Returns the {@link ConvexHull} containing the {@link Vertex}es of this layer.
	 * @return the {@link ConvexHull} containing the {@link Vertex}es of this layer
	 */
	public ConvexHull getConvexHull() {
		return convexHull;
	}
	
	/**
	 * Sets the {@link ConvexHull} containing the {@link Vertex}es of this layer.
	 * @param convexHull the {@link ConvexHull} containing the {@link Vertex}es of this layer
	 */
	public void setConvexHull(ConvexHull convexHull) {
		this.convexHull = convexHull;
	}
	
	/**
	 * Returns the right-hand "bridge" vertex of the hull on this layer that connects to the child layer ({@code -1} if none exists).
	 * @return the right-hand "bridge" vertex of the hull on this layer that connects to the child layer ({@code -1} if none exists)
	 */
	public int getChildRightBridgeIndex() {
		return childRightBridgeIndex;
	}
	
	/**
	 * Sets the right-hand "bridge" vertex of the hull on this layer that connects to the child layer ({@code -1} if none exists).
	 * @param childRightBridgeIndex the right-hand "bridge" vertex of the hull on this layer that connects to the child layer ({@code -1} if none exists)
	 */
	public void setChildRightBridgeIndex(int childRightBridgeIndex) {
		this.childRightBridgeIndex = childRightBridgeIndex;
	}
	
	/**
	 * Returns the left-hand "bridge" vertex of the hull on this layer that connects to the child layer ({@code -1} if none exists).
	 * @return the left-hand "bridge" vertex of the hull on this layer that connects to the child layer ({@code -1} if none exists)
	 */
	public int getChildLeftBridgeIndex() {
		return childLeftBridgeIndex;
	}
	
	/**
	 * Sets the left-hand "bridge" vertex of the hull on this layer that connects to the child layer ({@code -1} if none exists).
	 * @param childLeftBridgeIndex the left-hand "bridge" vertex of the hull on this layer that connects to the child layer ({@code -1} if none exists)
	 */
	public void setChildLeftBridgeIndex(int childLeftBridgeIndex) {
		this.childLeftBridgeIndex = childLeftBridgeIndex;
	}
	
	/**
	 * Returns the right-hand "bridge" vertex of the hull on this layer that connects to the parent layer ({@code -1} if none exists).
	 * @return the right-hand "bridge" vertex of the hull on this layer that connects to the parent layer ({@code -1} if none exists)
	 */
	public int getParentRightBridgeIndex() {
		return parentRightBridgeIndex;
	}
	
	/**
	 * Sets the right-hand "bridge" vertex of the hull on this layer that connects to the parent layer ({@code -1} if none exists).
	 * @param parentRightBridgeIndex the right-hand "bridge" vertex of the hull on this layer that connects to the parent layer ({@code -1} if none exists)
	 */
	public void setParentRightBridgeIndex(int parentRightBridgeIndex) {
		this.parentRightBridgeIndex = parentRightBridgeIndex;
	}
	
	/**
	 * Returns the right-hand "bridge" vertex of the hull on this layer that connects to the parent layer ({@code -1} if none exists).
	 * @return the right-hand "bridge" vertex of the hull on this layer that connects to the parent layer ({@code -1} if none exists)
	 */
	public int getParentLeftBridgeIndex() {
		return parentLeftBridgeIndex;
	}
	
	/**
	 * Returns the left-hand "bridge" vertex of the hull on this layer that connects to the parent layer ({@code -1} if none exists).
	 * @param parentLeftBridgeIndex the left-hand "bridge" vertex of the hull on this layer that connects to the parent layer ({@code -1} if none exists)
	 */
	public void setParentLeftBridgeIndex(int parentLeftBridgeIndex) {
		this.parentLeftBridgeIndex = parentLeftBridgeIndex;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[').append(getClass().getName()).append(':');
		
		builder.append("convexHull=").append(convexHull);
		builder.append("|childRightBridgeIndex=").append(childRightBridgeIndex);
		builder.append("|childLeftBridgeIndex=").append(childLeftBridgeIndex);
		builder.append("|parentRightBridgeIndex=").append(parentRightBridgeIndex);
		builder.append("|parentLeftBridgeIndex=").append(parentLeftBridgeIndex);
		
		return builder.append(']').toString();
	}
}
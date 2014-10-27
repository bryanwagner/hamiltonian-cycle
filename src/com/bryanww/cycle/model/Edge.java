package com.bryanww.cycle.model;

/**
 * Basic encapsulation of two {@link Vertex}es that form an edge in a graph.
 * The edge can also be interpretted as a line segment in the Cartesian plan,
 * and methods are provided accordingly.
 * 
 * @author  Bryan Wagner
 * @since   2014-10-25
 * @version 2014-10-25
 */
public class Edge {
	
	protected Vertex vertexA;  // the first vertex of the edge
	protected Vertex vertexB;  // the second vertex of the edge
	
	/**
	 * Creates a new {@link Edge}.
	 */
	public Edge() {}
	
	/**
	 * Creates a new {@link Edge}.
	 * @param vertexA the first vertex of the edge
	 * @param vertexB the second vertex of the edge
	 */
	public Edge(Vertex vertexA, Vertex vertexB) {
		this.vertexA = vertexA;
		this.vertexB = vertexB;
	}
	
	/**
	 * Returns the first vertex of the edge.
	 * @return the first vertex of the edge
	 */
	public Vertex getVertexA() {
		return vertexA;
	}
	
	/**
	 * Returns the first vertex of the edge.
	 * @param vertexA the first vertex of the edge
	 */
	public void setVertexA(Vertex vertexA) {
		this.vertexA = vertexA;
	}
	
	/**
	 * Returns the second vertex of the edge.
	 * @return the second vertex of the edge
	 */
	public Vertex getVertexB() {
		return vertexB;
	}
	
	/**
	 * Sets the second vertex of the edge.
	 * @param vertexB the second vertex of the edge
	 */
	public void setVertexB(Vertex vertexB) {
		this.vertexB = vertexB;
	}
	
	/**
	 * Returns the linear slope of this edge, interpreted as a line segment in the Cartesian plane.
	 * @return the linear slope of this edge
	 */
	public float getSlope() {
		float divisor = vertexB.getX() - vertexA.getX();
		return divisor == 0.0f ? 0.0f : ((vertexB.getY() - vertexA.getY()) / divisor);
	}
	
	/**
	 * Returns the y-intercept of this edge, interpreted as a line segment in the Cartesian plane.
	 * @return the y-intercept of this edge
	 */
	public float getYIntercept() {
		return -getSlope() * vertexA.getX() + vertexA.getY();
	}
	
	/**
	 * Returns the intersecting x-coordinate of this edge intersected with the given edge, interpreted as line segments in the Cartesian plane.
	 * @param otherEdge the {@link Edge} to intersect this edge with
	 * @return the intersecting x-coordinate of this edge intersected with the given edge
	 */
	public float getIntersectingX(Edge otherEdge) {
		// m1x + b1 = m2x + b2
		// (m1 - m2) x = b2 - b1
		// x = (b2 - b1) / (m1 - m2)
		float divisor = getSlope() - otherEdge.getSlope();
		return divisor == 0.0f ? 0.0f : ((otherEdge.getYIntercept() - getYIntercept()) / divisor);
	}
	
	/**
	 * Returns the y-coordinate alone the line defined by this edge corresponding with the given x-coordinate.
	 * The returned coordinate may lie away from the actual line segment of this edge.
	 * @param x the x=coordinate to get the corresponding y-coordinate for
	 * @return the y-coordinate alone the line defined by this edge corresponding with the given x-coordinate
	 */
	public float getYForX(float x) {
		return getSlope() * (x - vertexA.getX()) + vertexA.getY();
	}
	
	/**
	 * Returns {@code true} if this edge intersects with the given edge when interpreted as line segments in the Cartesian plane.
	 * @param otherEdge the other {@link Edge} to check this edge for intersection
	 * @return {@code true} if this edge intersects with the given edge
	 */
	public boolean isIntersecting(Edge otherEdge) {
		float intersectingX = getIntersectingX(otherEdge);
		float intersectingY = getYForX(intersectingX);
		
		float thisAX = vertexA.getX();
		float thisAY = vertexA.getY();
		float thisBX = vertexB.getX();
		float thisBY = vertexB.getY();
		float thisLeftX, thisRightX;
		if (thisAX < thisBX) {
			thisLeftX   = thisAX;
			thisRightX  = thisBX;
		}
		else {
			thisLeftX   = thisBX;
			thisRightX  = thisAX;
		}
		float thisTopY, thisBottomY;
		if (thisAY < thisBY) {
			thisTopY    = thisAY;
			thisBottomY = thisBY;
		}
		else {
			thisTopY    = thisBY;
			thisBottomY = thisAY;
		}
		
		// compare the intersecting coordinates against the bounds of this line segment (edge)
		boolean isOnThisEdge =
				(thisLeftX - Vertex.PRECISION < intersectingX && intersectingX < thisRightX  + Vertex.PRECISION) &&
				(thisTopY  - Vertex.PRECISION < intersectingY && intersectingY < thisBottomY + Vertex.PRECISION);
		
		float otherAX = otherEdge.getVertexA().getX();
		float otherAY = otherEdge.getVertexA().getY();
		float otherBX = otherEdge.getVertexB().getX();
		float otherBY = otherEdge.getVertexB().getY();
		float otherLeftX, otherRightX;
		if (otherAX < otherBX) {
			otherLeftX   = otherAX;
			otherRightX  = otherBX;
		}
		else {
			otherLeftX   = otherBX;
			otherRightX  = otherAX;
		}
		float otherTopY, otherBottomY;
		if (otherAY < otherBY) {
			otherTopY    = otherAY;
			otherBottomY = otherBY;
		}
		else {
			otherTopY    = otherBY;
			otherBottomY = otherAY;
		}
		
		// compare the intersecting coordinates against the bounds of the other line segment (edge)
		boolean isOnOtherEdge =
				(otherLeftX - Vertex.PRECISION < intersectingX && intersectingX < otherRightX  + Vertex.PRECISION) &&
				(otherTopY  - Vertex.PRECISION < intersectingY && intersectingY < otherBottomY + Vertex.PRECISION);
		
		return isOnThisEdge && isOnOtherEdge;
	}
	
	/**
	 * Returns {@code true} if this edge shares a connected {@link Vertex} with the given edge.
	 * @param otherEdge the other edge to check {@link Vertex}es against
	 * @return {@code true} if this edge shares a connected {@link Vertex} with the given edge
	 */
	public boolean isConnected(Edge otherEdge) {
		return
				vertexA.equals(otherEdge.vertexA) ||
				vertexA.equals(otherEdge.vertexB) || 
				vertexB.equals(otherEdge.vertexA) ||
				vertexB.equals(otherEdge.vertexB);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[').append(getClass().getName()).append(':');
		
		builder.append("vertexA=").append(vertexA);
		builder.append("|vertexB=").append(vertexB);
		builder.append("|slope=").append(getSlope());
		builder.append("|yIntercept=").append(getYIntercept());
		
		return builder.append(']').toString();
	}
}

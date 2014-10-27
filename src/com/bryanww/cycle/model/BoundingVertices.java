package com.bryanww.cycle.model;

import java.util.Arrays;
import java.util.List;

/**
 * Encapsulates the "bounding vertices" of a plot area (used to create/debug
 * the plot area).
 * 
 * @author  Bryan Wagner
 * @since   2014-10-25
 * @version 2014-10-25
 */
public class BoundingVertices {
	
	protected Vertex top;          // the top-most {@link Vertex}
	protected Vertex bottom;       // the bottom-most {@link Vertex}
	protected Vertex left;         // the left-most {@link Vertex}
	protected Vertex right;        // the right-most {@link Vertex}
	protected Vertex topLeft;      // the top-left corner {@link Vertex}
	protected Vertex topRight;     // the top-right corner {@link Vertex}
	protected Vertex bottomLeft;   // the bottom-left corner {@link Vertex}
	protected Vertex bottomRight;  // the bottom-right corner {@link Vertex}
	
	/**
	 * Creates a new {@link BoundingVertices}.
	 */
	public BoundingVertices() {
		this.topLeft     = new Vertex();
		this.topRight    = new Vertex();
		this.bottomLeft  = new Vertex();
		this.bottomRight = new Vertex();
	}
	
	/**
	 * Creates a new {@link BoundingVertices}.
	 * @param vertexList the list of {@link Vertex}es used to set the bounding vertices
	 */
	public BoundingVertices(List<Vertex> vertexList) {
		this();
		setVertices(vertexList);
	}
	
	/**
	 * Returns the top-most {@link Vertex}.
	 * @return the top-most {@link Vertex}
	 */
	public Vertex getTop() {
		return top;
	}
	
	/**
	 * Returns the bottom-most {@link Vertex}.
	 * @return the bottom-most {@link Vertex}
	 */
	public Vertex getBottom() {
		return bottom;
	}
	
	/**
	 * Returns the left-most {@link Vertex}.
	 * @return the left-most {@link Vertex}
	 */
	public Vertex getLeft() {
		return left;
	}
	
	/**
	 * Returns the right-most {@link Vertex}.
	 * @return the right-most {@link Vertex}
	 */
	public Vertex getRight() {
		return right;
	}
	
	/**
	 * Returns the top-left corner {@link Vertex}.
	 * @return the top-left corner {@link Vertex}
	 */
	public Vertex getTopLeft() {
		return topLeft;
	}
	
	/**
	 * Returns the top-right corner {@link Vertex}.
	 * @return the top-right corner {@link Vertex}
	 */
	public Vertex getTopRight() {
		return topRight;
	}
	
	/**
	 * Returns the bottom-left corner {@link Vertex}.
	 * @return the bottom-left corner {@link Vertex}
	 */
	public Vertex getBottomLeft() {
		return bottomLeft;
	}
	
	/**
	 * Returns the bottom-right corner {@link Vertex}.
	 * @return the bottom-right corner {@link Vertex}
	 */
	public Vertex getBottomRight() {
		return bottomRight;
	}
	
	/**
	 * Sets the max/min and corner bounding vertices.
	 * @param vertexList the list of {@link Vertex}es used to set the bounding vertices
	 */
	public void setVertices(List<Vertex> vertexList) {
		float top = 2.0f, bottom = -2.0f, left = 2.0f, right = -2.0f;
		for (Vertex vertex : vertexList) {
			float x = vertex.getX();
			float y = vertex.getY();
			if (y < top) {
				this.top    = vertex;
				top         = y;
			}
			if (y > bottom) {
				this.bottom = vertex;
				bottom      = y;
			}
			if (x < left) {
				this.left   = vertex;
				left        = x;
			}
			if (x > right) {
				this.right  = vertex;
				right       = x;
			}
		}
		topLeft.setY(top);
		topLeft.setX(left);
		topRight.setY(top);
		topRight.setX(right);
		bottomLeft.setY(bottom);
		bottomLeft.setX(left);
		bottomRight.setY(bottom);
		bottomRight.setX(right);
	}
	
	/**
	 * Returns a list of "corner" vertices that can be used to draw a bounding rectangle.
	 * @return a list of "corner" vertices that can be used to draw a bounding rectangle
	 */
	public List<Vertex> getCornerVertexList() {
		return Arrays.asList(topLeft, topRight, bottomRight, bottomLeft);
	}
	
	/**
	 * Returns a list of top, bottom, right, and left vertices.
	 * @return a list of top, bottom, right, and left vertices
	 */
	public List<Vertex> getBoundingVertexList() {
		return Arrays.asList(top, bottom, right, left);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[').append(getClass().getName()).append(':');
		
		builder.append("top=").append(top);
		builder.append("|bottom=").append(bottom);
		builder.append("|left=").append(left);
		builder.append("|right=").append(right);
		builder.append("|topLeft=").append(topLeft);
		builder.append("|topRight=").append(topRight);
		builder.append("|bottomLeft=").append(bottomLeft);
		builder.append("|bottomRight=").append(bottomRight);
		
		return builder.append(']').toString();
	}
}

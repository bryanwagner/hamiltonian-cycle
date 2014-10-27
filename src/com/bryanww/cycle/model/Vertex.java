package com.bryanww.cycle.model;

/**
 * Basic representation of a vertex that has an X and Y coordinate in the
 * Cartesian plane.
 * 
 * @author  Bryan Wagner
 * @since   2014-10-25
 * @version 2014-10-25
 */
public class Vertex {
	
	public static final float PRECISION = 1e-5f;  // the precision used for floating point comparison
	
	protected float x;  // the x-coordinate
	protected float y;  // the y-coordinate
	
	/**
	 * Creates a new {@link Vertex}.
	 */
	public Vertex() {}
	
	/**
	 * Creates a new {@link Vertex}.
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 */
	public Vertex(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the x-coordinate.
	 * @return the x-coordinate
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * Sets the x-coordinate.
	 * @param x the x-coordinate
	 */
	public void setX(float x) {
		this.x = x;
	}
	
	/**
	 * Returns the y-coordinate.
	 * @return the y-coordinate
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * Sets the y-coordinate.
	 * @param y the y-coordinate
	 */
	public void setY(float y) {
		this.y = y;
	}
	
	/**
	 * Sets both the x-coordinate and the y-coordinate.
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 */
	public void setValues(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the distance between this vertex and the given vertex.
	 * @param otherVertex the other vertex to calculate the distance from
	 * @return the distance between this vertex and the given vertex
	 */
	public float getDistance(Vertex otherVertex) {
		float deltaX = otherVertex.x - x;
		float deltaY = otherVertex.y - y;
		return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}
	
	/**
	 * Returns {@code true} if the given floating point numbers are "equal" within the predefined floating-point roundoff error precision defined by {@link #PRECISION}.
	 * @param a the first value to compare
	 * @param b the second value to compare
	 * @return {@code true} if the given floating point numbers are "equal"
	 */
	public static boolean isEqualValue(float a, float b) {
		return a > (b - PRECISION) && a < (b + PRECISION);
	}
	
	/**
	 * Returns {@code true} if the vectors formed by line segments b - a and c - a are oriented by a clockwise angle, as determined by the right-hand rule from the z component of the vector formed by the cross product.
	 * @param a the first vertex forming the angle (start point)
	 * @param b the second vertex forming the angle (middle point)
	 * @param c the third vertex forming the angle (end point)
	 * @return {@code true} if the vectors form a clockwise angle
	 */
	public static boolean isClockwise(Vertex a, Vertex b, Vertex c) {
		// vector u is b - a
		float ux = b.x - a.x;
		float uy = b.y - a.y;
		
		// vector v is c - b
		float vx = c.x - a.x;
		float vy = c.y - a.y;
		
		// we only care if the z component in the 3-space vector is positive or negative, which determines if the vertices rotate clockwise or counter-clockwise
		float z  = ux * vy - uy * vx;
		
		// positive if counter-clockwise; allow zero
		return z <= 0.0f;
	}
	
	@Override
	public int hashCode() {
		return (((int) (31.0f * x)) << 16) ^ ((int) (31.0f * y));
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (other instanceof Vertex) {
			Vertex otherVertex = (Vertex) other;
			return isEqualValue(x, otherVertex.x) && isEqualValue(y, otherVertex.y);
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[').append(getClass().getName()).append(':');
		
		builder.append("x=").append(x);
		builder.append("|y=").append(y);
		
		return builder.append(']').toString();
	}
	
	/**
	 * Main method to test internal functions.
	 * @param args main arguments (ignored)
	 */
	public static void main(String[] args) {
		Vertex a = new Vertex();
		Vertex b = new Vertex();
		Vertex c = new Vertex();
		
		a.setValues(0.2f, 0.2f);
		b.setValues(0.4f, 0.4f);
		c.setValues(0.2f, 0.6f);
		System.out.println(isClockwise(a, b, c));  // false
		
		a.setValues(0.2f, 0.2f);
		b.setValues(0.0f, 0.4f);
		c.setValues(0.2f, 0.6f);
		System.out.println(isClockwise(a, b, c));  // true
	}
}

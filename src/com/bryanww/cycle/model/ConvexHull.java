package com.bryanww.cycle.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.bryanww.cycle.model.ConvexHullIterator.Direction;

/**
 * Encapsulates the {@link Vertex}es of a Convex Hull and provides an algorithm
 * to calculate them given a list of vertices.
 * <p>
 * The algorithm is the "Monotone Chain" algorithm adapted from Wikibooks:
 * http://en.wikibooks.org/wiki/Algorithm_Implementation/Geometry/Convex_hull/Monotone_chain
 * 
 * @author  Bryan Wagner
 * @since   2014-10-25
 * @version 2014-10-25
 */
public class ConvexHull implements Iterable<Vertex> {
	
	public static Comparator<Vertex> VERTEX_POSITION_COMPARATOR = new Comparator<Vertex>() {
		
		@Override
		public int compare(Vertex vertex1, Vertex vertex2) {
			float x1 = vertex1.getX();
			float x2 = vertex2.getX();
			if (x1 == x2) {
				float y1 = vertex1.getY();
				float y2 = vertex2.getY();
				return y1 < y2 ? -1 : (y1 > y2 ? 1 : 0);
			}
			return x1 < x2 ? -1 : (x1 > x2 ? 1 : 0);
		}
	};
	
	protected List<Vertex> vertexList;  // the ordered list of {@link Vertex}es composing the Convex Hull
	protected List<Edge>   edgeList;    // the list of edges composing the hull that is checked for intersections with bridge edges
	
	/**
	 * Creates a new {@link ConvexHull}.
	 */
	public ConvexHull() {
		this.vertexList = new ArrayList<Vertex>();
		this.edgeList   = new ArrayList<Edge>();
	}
	
	/**
	 * Creates a new {@link ConvexHull}.
	 * @param vertexList the list of {@link Vertex}es to build the Convex Hull with
	 */
	public ConvexHull(List<Vertex> vertexList) {
		this();
		setVertexList(vertexList);
	}
	
	/**
	 * Returns the ordered list of {@link Vertex}es composing the Convex Hull.
	 * @return the ordered list of {@link Vertex}es composing the Convex Hull
	 */
	public List<Vertex> getVertexList() {
		return vertexList;
	}
	
	/**
	 * Returns the list of edges composing the hull that is checked for intersections with bridge edges.
	 * @return the list of edges composing the hull that is checked for intersections with bridge edges
	 */
	public List<Edge> getEdgeList() {
		return edgeList;
	}
	
	/**
	 * Builds the Convex Hull using the "Monotone Chain" algorithm with the given list of vertices.
	 * The input list is not modified.
	 * @param vertexList the list of {@link Vertex}es to build the Convex Hull with
	 */
	public void setVertexList(List<Vertex> vertexList) {
		this.vertexList.clear();
		edgeList.clear();
		if (vertexList.size() <= 1) {
			this.vertexList.addAll(vertexList);
			return;
		}
		
		// sort is where all the time is spent in the O(nlgn) time complexity
		List<Vertex> sortedVertexList = new ArrayList<Vertex>(vertexList);  // don't mutate the original list
		Collections.sort(sortedVertexList, VERTEX_POSITION_COMPARATOR);
		
		// Build lower hull (from wikipedia algorithm)
		int      n            = sortedVertexList.size();
		int      k            = 0;
		Vertex[] hullVertices = new Vertex[2 * n];
		for (int i = 0; i < n; ++i) {
			while (k >= 2 && Vertex.isClockwise(hullVertices[k - 2], hullVertices[k - 1], sortedVertexList.get(i))) {
				k--;
			}
			hullVertices[k++] = sortedVertexList.get(i);
		}
		
		// Build upper hull (from wikipedia algorithm)
		for (int i = n - 2, t = k + 1; i >= 0; i--) {
			while (k >= t && Vertex.isClockwise(hullVertices[k - 2], hullVertices[k - 1], sortedVertexList.get(i))) {
				k--;
			}
			hullVertices[k++] = sortedVertexList.get(i);
		}
		if (k > 1) {
			hullVertices = Arrays.copyOfRange(hullVertices, 0, k - 1);
		}
		for (Vertex vertex : hullVertices) {
			this.vertexList.add(vertex);
		}
		for (int i = 1; i < this.vertexList.size(); i++) {
			Edge edge = new Edge(this.vertexList.get(i), this.vertexList.get(i - 1));
			edgeList.add(edge);
		}
		if (this.vertexList.size() > 2) {
			Edge edge = new Edge(this.vertexList.get(this.vertexList.size() - 1), this.vertexList.get(0));
			edgeList.add(edge);
		}
	}
	
	/**
	 * Returns the hull {@link Vertex} for the given index in the vertex list.
	 * @param index the hull index within the vertex list
	 * @return the hull {@link Vertex} for the given index in the vertex list
	 */
	public Vertex getVertex(int index) {
		return vertexList.get(index);
	}
	
	/**
	 * Returns {@code true} if the given edge intersects with this {@link ConvexHull}.
	 * @param edge the {@link Edge} to check for intersection with this hull
	 * @return {@code true} if the given edge intersects with this {@link ConvexHull}
	 */
	public boolean isIntersecting(Edge edge) {
		for (Edge convexHullEdge : edgeList) {
			if (edge.isIntersecting(convexHullEdge)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the index of the hull vertex that has the minimum distance to the given vertex.
	 * @param vertex the {@link Vertex} to compare against the vertices within this hull
	 * @return the index of the hull vertex that has the minimum distance to the given vertex
	 */
	public int getMinDistanceIndex(Vertex vertex) {
		int   minDistanceIndex = -1;
		float minDistance      = Float.MAX_VALUE;
		for (int i = 0; i < vertexList.size(); i++) {
			float distance = vertex.getDistance(vertexList.get(i));
			if (distance < minDistance) {
				minDistance      = distance;
				minDistanceIndex = i;
			}
		}
		return minDistanceIndex;
	}
	
	/**
	 * Returns the hull vertex one index to the left of the given index, wrapping to the other side if needed.
	 * @param index the index to get the vertex to the left of
	 * @return the hull vertex one index to the left of the given index, wrapping to the other side if needed
	 */
	public Vertex getLeftVertex(int index) {
		if (--index < 0) {
			index = vertexList.size() - 1;
		}
		return vertexList.get(index);
	}
	
	/**
	 * Returns the hull vertex one index to the right of the given index, wrapping to the other side if needed.
	 * @param index the index to get the vertex to the right of
	 * @return the hull vertex one index to the right of the given index, wrapping to the other side if needed
	 */
	public Vertex getRightVertex(int index) {
		if (++index > vertexList.size()) {
			index = 0;
		}
		return vertexList.get(index);
	}
	
	/**
	 * Returns {@code true} if the list of hull vertices is empty.
	 * @return {@code true} if the list of hull vertices is empty
	 */
	public boolean isEmpty() {
		return vertexList.isEmpty();
	}
	
	/**
	 * Returns the total number of vertices contained within the hull vertex list.
	 * @return the total number of vertices contained within the hull vertex list
	 */
	public int size() {
		return vertexList.size();
	}
	
	@Override
	public Iterator<Vertex> iterator() {
		return new ConvexHullIterator(this, 0, Direction.RIGHT);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[').append(getClass().getName()).append(':');
		
		builder.append("vertexList=").append(vertexList);
		builder.append("|edgeList=").append(edgeList);
		
		return builder.append(']').toString();
	}
}

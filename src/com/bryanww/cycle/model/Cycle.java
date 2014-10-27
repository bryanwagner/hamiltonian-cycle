package com.bryanww.cycle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Data structure encapsulating a Hamiltonian Cycle of a graph of
 * {@link Vertex}es, which are assumed to be completely connected on the
 * Cartesian plane.  The restriction on the cycle is that no edge can intersect
 * with any other edge.
 * <p>
 * The general solution for finding Hamiltonian Cycles is NP-complete.
 * In the worst case, this solution still runs in nondeterministic
 * polynomial time since the distance between vertices can be interpreted as
 * a weighted graph.  However, this solution uses a distance heuristic to
 * dramatically reduce the average run time.  The insight into the heuristic is
 * that Hamiltonian cycles appear to form by creating "bridges" between layers
 * of Convex Hulls.  Each "bridge" consists of two pairs of adjacent Convex
 * Hull vertices, one on the "parent" hull and one on the "child" hull.
 * Each bridge is searched for recursively, and intersections are tested by
 * using line equations to find intersection points.  The boundary conditions
 * for hulls of three vertices or less is specifically handled.
 * <p>
 * The naive implementation of the algorithm would perform a brute-force search
 * to find the bridges.  However, to improve on the runtime performance, a
 * distance heuristic is first performed which sorts candidate bridge vertices
 * by distance from the parent hull vertices.  This heuristic takes advantage
 * of the observation that the best bridges tend to be the shortest and that
 * hull vertices on the opposing side of the hull are likely to intersect with
 * the hull vertices closer to the parent bridge vertices.
 * <p>
 * If the bridge heuristic fails the algorithm might fail since it does not
 * fall back to an exhaustive search.  However, the algorithm might be still be
 * mathematically correct; a proof would need to take advantage of Graph Theory
 * and likely the Triangle Inequality.  The algorithm so far has been tested
 * using the {@link GraphPanel} over a few thousand iterations (see the
 * auto-reset function).
 * <p>
 * The {@link Iterable} implementation of this class performs the Hamiltonian
 * walk after {@link #setVertexList(List)} creates the cycle.  The Hamiltonian
 * cycle is formed by connecting the last vertex that is returned by the
 * iteration to the first one.
 * 
 * @author  Bryan Wagner
 * @since   2014-10-25
 * @version 2014-10-25
 */
public class Cycle implements Iterable<Vertex> {
	
	protected List<CycleLayer> cycleLayerList;          // the list of {@link CycleLayer}s representing the Hamiltonian Cycle, built by bridging Convex Hulls from the outer-most layer to the inner-most layer
	protected List<Vertex>     cycleVertexList;         // the ordered list of {@link Vertex}es that can be iterated to traverse the graph in a Hamiltonian Cycle
	
	protected StatCounter      recursionCallStatCount;  // the {@link StatCounter} to measure the total number of recursive calls
	protected StatCounter      recursionTimeStatCount;  // the {@link StatCounter} to measure time spent in recursion
	protected int              recursionCount;          // the number of recursive calls in the current cycle search
	
	/**
	 * Creates a new {@link Cycle}.
	 */
	public Cycle() {
		this.cycleLayerList         = new ArrayList<CycleLayer>();
		this.cycleVertexList        = new ArrayList<Vertex>();
		this.recursionCallStatCount = new StatCounter();
		this.recursionTimeStatCount = new StatCounter();
	}
	
	/**
	 * Returns the list of {@link CycleLayer}s representing the Hamiltonian Cycle, built by bridging Convex Hulls from the outer-most layer to the inner-most layer.
	 * @return the list of {@link CycleLayer}s representing the Hamiltonian Cycle
	 */
	public List<CycleLayer> getCycleLayerList() {
		return cycleLayerList;
	}
	
	/**
	 * Returns the ordered list of {@link Vertex}es that can be iterated to traverse the graph in a Hamiltonian Cycle.
	 * @return the ordered list of {@link Vertex}es that can be iterated to traverse the graph in a Hamiltonian Cycle
	 */
	public List<Vertex> getCycleVertexList() {
		return cycleVertexList;
	}
	
	/**
	 * Returns the {@link StatCounter} to measure the total number of recursive calls.
	 * @return the {@link StatCounter} to measure the total number of recursive calls
	 */
	public StatCounter getRecursionCallStatCount() {
		return recursionCallStatCount;
	}
	
	/**
	 * Returns the {@link StatCounter} to measure time spent in recursion.
	 * @return the {@link StatCounter} to measure time spent in recursion
	 */
	public StatCounter getRecursionTimeStatCount() {
		return recursionTimeStatCount;
	}
	
	/**
	 * Recursive method that builds the "bridges" for the Hamiltonian Cycle, starting from the parent layers and searching inwardly to the child layers.
	 * @param parentCycleLayerIndex the current index of the "parent" layer to build cycle bridges for
	 */
	protected void buildCycleLayerBridges(int parentCycleLayerIndex) {
		recursionCount++;
		if (parentCycleLayerIndex >= cycleLayerList.size() - 1) {
			return;  // recursive base case
		}
		
		CycleLayer parentCycleLayer = cycleLayerList.get(parentCycleLayerIndex);
		CycleLayer childCycleLayer  = cycleLayerList.get(parentCycleLayerIndex + 1);
		ConvexHull parentConvexHull = parentCycleLayer.getConvexHull();
		ConvexHull childConvexHull  = childCycleLayer.getConvexHull();
		
		// search the "parent" layer for "bridge" vertices
		boolean    isBridgeOk       = false;
		int        leftParentIndex  = parentCycleLayer.parentRightBridgeIndex;  // start one index to the right of the previous right-hand parent bridge
		int        rightParentIndex;
		for (int remainingParentIndices = parentConvexHull.size(); !isBridgeOk && remainingParentIndices > 0; remainingParentIndices--) {
			if (++leftParentIndex >= parentConvexHull.size()) {
				leftParentIndex = 0;
			}
			rightParentIndex = leftParentIndex;
			if (++rightParentIndex >= parentConvexHull.size()) {
				rightParentIndex = 0;
			}
			
			// check boundary conditions related to triangles, single points, and channels caused by reusing both parent vertices from a previous bridge
			if (	(leftParentIndex != rightParentIndex) &&
					(leftParentIndex  == parentCycleLayer.parentLeftBridgeIndex  || leftParentIndex  == parentCycleLayer.parentRightBridgeIndex) &&
					(rightParentIndex == parentCycleLayer.parentLeftBridgeIndex  || rightParentIndex == parentCycleLayer.parentRightBridgeIndex)) {
				
				continue;
			}
			
			// sort children by distance to the bridge; this adds a "greedy algorithm" heuristic which pragmatically limits the depth of recursion
			List<VertexIndexDistance> vertexIndexDistanceList = new ArrayList<VertexIndexDistance>();;
			Vertex                    leftParentVertex        = parentConvexHull.getVertex(leftParentIndex);
			Vertex                    rightParentVertex       = parentConvexHull.getVertex(rightParentIndex);
			List<Vertex>              childVertexList         = childConvexHull.getVertexList();
			for (int i = 0; i < childVertexList.size(); i++) {
				VertexIndexDistance vertexIndexDistance = new VertexIndexDistance(i, rightParentVertex, childVertexList.get(i));
				vertexIndexDistanceList.add(vertexIndexDistance);
			}
			Collections.sort(vertexIndexDistanceList);
			
			// search the "child" layer for "bridge" vertices
			for (int i = 0; !isBridgeOk && i < vertexIndexDistanceList.size(); i++) {
				int rightChildIndex = vertexIndexDistanceList.get(i).getIndex();
				int leftChildIndex  = rightChildIndex - 1;
				if (leftChildIndex < 0) {
					leftChildIndex = childConvexHull.size() - 1;
				}
				
				parentCycleLayer.childLeftBridgeIndex  = leftParentIndex;
				parentCycleLayer.childRightBridgeIndex = rightParentIndex;
				childCycleLayer.parentLeftBridgeIndex  = leftChildIndex;
				childCycleLayer.parentRightBridgeIndex = rightChildIndex;
				
				// perform recursion to set up the remaining bridge indices for the next inner layer
				buildCycleLayerBridges(parentCycleLayerIndex + 1);
				
				Edge bridgeLeftEdge  = new Edge(leftParentVertex,  childConvexHull.getVertex(leftChildIndex));
				Edge bridgeRightEdge = new Edge(rightParentVertex, childConvexHull.getVertex(rightChildIndex));
				
				// check bridge conditions (initialize "OK" to true)
				isBridgeOk = true;
				
				// skip intersection checks for connected edges
				if (!bridgeLeftEdge.isConnected(bridgeRightEdge)) {
					
					// check for intersection between the bridge edges
					isBridgeOk &= !bridgeLeftEdge.isIntersecting(bridgeRightEdge);
				}
				
				// check for intersection with any of the edges on the child convex hull
				List<Edge> childEdgeList = childConvexHull.getEdgeList();
				for (int edgeIndex = 0; isBridgeOk && edgeIndex < childEdgeList.size(); edgeIndex++) {
					Edge edge = childEdgeList.get(edgeIndex);
					
					// skip intersection checks for connected edges
					if (!edge.isConnected(bridgeLeftEdge)) {
						isBridgeOk &= !edge.isIntersecting(bridgeLeftEdge);
					}
					if (!edge.isConnected(bridgeRightEdge)) {
						isBridgeOk &= !edge.isIntersecting(bridgeRightEdge);
					}
				}
				
				// if a Cartesian intersection (or other problem) was found, reject this bridge and try again
				if (!isBridgeOk) {
					parentCycleLayer.childLeftBridgeIndex  = -1;
					parentCycleLayer.childRightBridgeIndex = -1;
					childCycleLayer.parentLeftBridgeIndex  = -1;
					childCycleLayer.parentRightBridgeIndex = -1;
				}
			}
		}
	}
	
	/**
	 * Sets the list of graph {@link Vertex}es and builds the Hamiltonian Cycle (with no Cartesian intersections).
	 * <p>
	 * The {@link Iterable} implementation of this class will then perform the Hamiltonian walk.
	 * The Hamiltonian cycle is formed by connecting the last vertex with the first one.
	 * @param vertexList the list of graph {@link Vertex}es to build the Hamiltonian Cycle for
	 */
	public void setVertexList(List<Vertex> vertexList) {
		cycleLayerList.clear();
		
		// create Convex Hulls that represent each "layer" of the algorithm
		List<Vertex> remainingVertexList = new ArrayList<Vertex>(vertexList);
		while (!remainingVertexList.isEmpty()) {
			
			CycleLayer childCycleLayer = new CycleLayer();
			ConvexHull childConvexHull = childCycleLayer.getConvexHull();
			childConvexHull.setVertexList(remainingVertexList);
			cycleLayerList.add(childCycleLayer);
			
			remainingVertexList.removeAll(childConvexHull.getVertexList());
		}
		
		// recursively find all bridges
		recursionCount = 0;
		long latency   = System.currentTimeMillis();
		buildCycleLayerBridges(0);
		recursionCallStatCount.addValue(recursionCount);
		recursionTimeStatCount.addValue(System.currentTimeMillis() - latency);
		
		// create a linear walk of the Hamiltonian circuit so further iteration is fast (e.g. when rendering)
		cycleVertexList.clear();
		CycleIterator iterator = new CycleIterator(this);
		while (iterator.hasNext()) {
			Vertex vertex = iterator.next();
			cycleVertexList.add(vertex);
		}
	}
	
	/**
	 * Returns {@code true} if the cycle contains no {@link Vertex}es.
	 * @return {@code true} if the cycle contains no {@link Vertex}es
	 */
	public boolean isEmpty() {
		return size() <= 0;
	}
	
	/**
	 * Returns the total number of {@link Vertex}es in each {@link CycleLayer}.
	 * @return the total number of {@link Vertex}es in each {@link CycleLayer}
	 */
	public int size() {
		int size = 0;
		for (CycleLayer cycleLayer : cycleLayerList) {
			size += cycleLayer.getConvexHull().size();
		}
		return size;
	}
	
	@Override
	public Iterator<Vertex> iterator() {
		return cycleVertexList.iterator();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[').append(getClass().getName()).append(':');
		
		builder.append("cycleLayerList=").append(cycleLayerList);
		builder.append("|cycleVertexList=").append(cycleVertexList);
		builder.append("|recursionCallStatCount=").append(recursionCallStatCount);
		builder.append("|recursionTimeStatCount=").append(recursionTimeStatCount);
		builder.append("|recursionCount=").append(recursionCount);
		
		return builder.append(']').toString();
	}
	
	/**
	 * Encapsulation of a vertex index with a distance so it can be sorted from closest to farthest.
	 */
	protected class VertexIndexDistance implements Comparable<VertexIndexDistance> {
		
		protected int   index;     // the index of the vertex
		protected float distance;  // the distance to another vertex represented by this encapsulation
		
		/**
		 * Creates a new {@link VertexIndexDistance}.
		 * @param index    the index of the vertex
		 * @param distance the distance to another vertex represented by this encapsulation
		 */
		public VertexIndexDistance(int index, float distance) {
			this.index    = index;
			this.distance = distance;
		}
		
		/**
		 * Creates a new {@link VertexIndexDistance}.
		 * @param index   the index of the vertex
		 * @param vertexA the first vertex in the distance calculation
		 * @param vertexB the second vertex in the distance calculation
		 */
		public VertexIndexDistance(int index, Vertex vertexA, Vertex vertexB) {
			this(index, vertexA.getDistance(vertexB));
		}
		
		/**
		 * Returns the index of the vertex.
		 * @return the index of the vertex
		 */
		public int getIndex() {
			return index;
		}
		
		@Override
		public int compareTo(VertexIndexDistance other) {
			return distance < other.distance ? -1 : (distance > other.distance ? 1 : 0);
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append('[').append(getClass().getName()).append(':');
			
			builder.append("index=").append(index);
			builder.append("|distance=").append(distance);
			
			return builder.append(']').toString();
		}
	}
}

package com.bryanww.cycle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data structure encapsulating the {@link Vertex}es of a graph on the
 * Cartesian plane.  Also contains the {@link Cycle} instance that builds the
 * Hamiltonian Cycle.
 * <p>
 * Note that normally the x and y coordinates of all {@link Vertex}es are
 * normalized between {@code 0.0f} inclusive and {@code 1.0f} exclusive so that
 * they can be used directly as scalars when rendering the graph on a canvas of
 * arbitrary width and height.
 * 
 * @author  Bryan Wagner
 * @since   2014-10-25
 * @version 2015-04-27
 */
public class Graph {
	
	protected List<Vertex> vertexList;        // the list of {@link Vertex}es defining the graph
	protected Cycle        cycle;             // the {@link Cycle} that builds the Hamiltonian Cycle when the vertex list is set
	protected List<Vertex> sortedVertexList;  // the sorted list of {@link Vertex}es defining the cycle (using the new algorithm)
	
	/**
	 * Creates a new {@link Graph}.
	 */
	public Graph() {
		this.vertexList       = new ArrayList<Vertex>();
		this.cycle            = new Cycle();
		this.sortedVertexList = new ArrayList<Vertex>();
	}
	
	/**
	 * Returns the list of {@link Vertex}es defining the graph.
	 * @return the list of {@link Vertex}es defining the graph
	 */
	public List<Vertex> getVertexList() {
		return vertexList;
	}
	
	/**
	 * Resets the sorted vertex list containing the graph cycle using the new algorithm, using the internal vertex list.
	 * This is a O(nlgn) algorithm.
	 */
	protected void resetSortexVertexList() {
		sortedVertexList.clear();
		for (Vertex vertex : vertexList) {
			sortedVertexList.add(new Vertex(vertex));
		}
		if (sortedVertexList.isEmpty()) {
			return;
		}
		
		// translate all vertexes with an offset so the origin is in the center
		Vertex firstVertex = sortedVertexList.get(0);
		float minX = firstVertex.getX();
		float maxX = minX;
		float minY = firstVertex.getY();
		float maxY = minY;
		for (Vertex vertex : sortedVertexList) {
			float x = vertex.getX();
			float y = vertex.getY();
			if (x < minX) {
				minX = x;
			}
			if (x > maxX) {
				maxX = x;
			}
			if (y < minY) {
				minY = y;
			}
			if (y > maxY) {
				maxY = y;
			}
		}
		float shiftX = -0.5f * (maxX - minX);
		float shiftY = -0.5f * (maxY - minY);
		for (Vertex vertex : sortedVertexList) {
			vertex.addValues(shiftX, shiftY);
		}
		Collections.sort(sortedVertexList);
		for (Vertex vertex : sortedVertexList) {
			vertex.addValues(-shiftX, -shiftY);
		}
	}
	
	/**
	 * Sets the list of {@link Vertex}es defining the graph and builds the {@link Cycle}.
	 * The input list is not modified.
	 * @param vertexList the list of {@link Vertex}es defining the graph
	 */
	public void setVertexList(List<Vertex> vertexList) {
		try {
			if (this.vertexList != vertexList) {
				this.vertexList.clear();
				this.vertexList.addAll(vertexList);
			}
			this.cycle.setVertexList(this.vertexList);
			resetSortexVertexList();
		}
		catch (Exception e) {
			e.printStackTrace();
			printVertexList();
		}
	}
	
	/**
	 * Returns the {@link Cycle} that builds the Hamiltonian Cycle when the vertex list is set.
	 * @return the {@link Cycle} that builds the Hamiltonian Cycle when the vertex list is set
	 */
	public Cycle getCycle() {
		return cycle;
	}
	
	/**
	 * Returns the sorted list of {@link Vertex}es defining the cycle (using the new algorithm).
	 * @return the sorted list of {@link Vertex}es defining the cycle (using the new algorithm)
	 */
	public List<Vertex> getSortedVertexList() {
		return sortedVertexList;
	}
	
	/**
	 * Resets the graph with the given number of randomly generated {@link Vertex}es.
	 * @param count the number of random {@link Vertex}es to create
	 */
	public void setRandomVertexList(int count) {
		vertexList.clear();
		for (int i = 0; i < count; i++) {
			Vertex vertex = new Vertex((float) Math.random(), (float) Math.random());
			vertexList.add(vertex);
		}
		
		setVertexList(vertexList);
	}
	
	/**
	 * Prints the list of {@link Vertex}es of the graph to console.
	 */
	public void printVertexList() {
		System.out.println("vertexList:");
		for (int i = 0; i < vertexList.size(); i++) {
			Vertex vertex = vertexList.get(i);
			System.out.print(String.format("[%d]\t", i));
			System.out.println(vertex == null ? null : String.format("%.3f\t%.3f", vertex.getX(), vertex.getY()));
		}
		System.out.println();
		System.out.println("recursion calls: " + cycle.getRecursionCallStatCount());
		System.out.println("recursion times: " + cycle.getRecursionTimeStatCount());
		System.out.println();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[').append(getClass().getName()).append(':');
		
		builder.append("vertexList=").append(vertexList);
		builder.append("|cycle=").append(cycle);
		builder.append("|sortedVertexList=").append(sortedVertexList);
		
		return builder.append(']').toString();
	}
}

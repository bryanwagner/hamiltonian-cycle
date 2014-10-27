package com.bryanww.cycle.model;

import java.util.ArrayList;
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
 * @version 2014-10-25
 */
public class Graph {
	
	protected List<Vertex> vertexList;  // the list of {@link Vertex}es defining the graph
	protected Cycle        cycle;       // the {@link Cycle} that builds the Hamiltonian Cycle when the vertex list is set
	
	/**
	 * Creates a new {@link Graph}.
	 */
	public Graph() {
		this.vertexList = new ArrayList<Vertex>();
		this.cycle      = new Cycle();
	}
	
	/**
	 * Returns the list of {@link Vertex}es defining the graph.
	 * @return the list of {@link Vertex}es defining the graph
	 */
	public List<Vertex> getVertexList() {
		return vertexList;
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
		
		return builder.append(']').toString();
	}
}

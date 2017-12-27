/*
 * ParisMetro class where the graph is stored
 * Implementations of new methods will be done here too.
 * 
 * Student name: Wilfried Tanezick Dondji Fogang
 * Student number: 7968381
 * Student email: wdond086@uottawa.ca
 */

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import net.datastructures.*;
import net.datastructures.Map;
import net.datastructures.ArrayList;

public class ParisMetro {
	
	private static Graph<Integer, Integer> parisGraph;
	@SuppressWarnings("unused")
	private static String numVertices;
	@SuppressWarnings("unused")
	private static String numEdges;
	
	/**
	 * Returns the created paris subway graph
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * @return Returns the created paris subway graph
	 */
	public Graph<Integer, Integer> getGraph() {
		return parisGraph;
	}
	
	/**
	 * Returns the number of vertices in the graph
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * @return Returns the number of vertices in the graph
	 */
	public int getNumVertices() {
		return parisGraph.numVertices();
	}
	
	/**
	 * Returns the number of edges in the graph
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * @return Returns the number of edges in the graph
	 */
	public int getNumEdges() {
		return parisGraph.numEdges();
	}
	
	/**
	 * Constructs a paris graph and identifies the line containing the station n1
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * Implemented using a breadth first search algorithm
	 * @param n1 The target station
	 * @throws Exception
	 * @throws IOException
	 */
	public ParisMetro(int n1) throws Exception, IOException {
		readMetro("metro.txt");
		identifyStations(n1);
	}
	
	/**
	 * Constructs a paris graph and prints the shortest path from station n1 to station n2
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * @param n1 The starting station
	 * @param n2 The destination station
	 * @throws Exception
	 * @throws IOException
	 */
	public ParisMetro(int n1, int n2) throws Exception, IOException {
		readMetro("metro.txt");
		shortPath1(n1, n2);
	}
	
	/**
	 * Constructs a paris graph and prints the shortest path from station n1, to station n2, 
	 * when station n3 is out of service
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * @param n1 The starting station
	 * @param n2 The destination station
	 * @param n3 The out of service station
	 * @throws Exception
	 * @throws IOException
	 */
	public ParisMetro(int n1, int n2, int n3) throws Exception, IOException {
		readMetro("metro.txt");
		shortPath2(n1, n2, n3);
	}
	
	/**
	 * Reads the text file and creates the graph from the data in the graph.
	 * Adapted from the read() method in the WeightGraph class.
	 * Modified by Thais Bardini on November 19th, 2017 (tbard069@uottawa.ca)
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 */
	@SuppressWarnings({ "unused", "resource" })
	public static void readMetro(String filename) throws Exception, IOException {
		parisGraph = new AdjacencyMapGraph<Integer, Integer>(true);
		BufferedReader graphFile = new BufferedReader(new FileReader(filename));

		// Create a hash map to store all the vertices read
		Hashtable<Integer, Vertex<Integer>> vertices = new Hashtable<Integer, Vertex<Integer>>();

		// Read the edges and insert
		String line = graphFile.readLine();
		// Check for correctness of input for first line
		if (line == null) {
			throw new IOException("Incorrect input for number of vertices and edges!");
		}
		StringTokenizer st = new StringTokenizer(line);
		
		numVertices = st.nextToken().substring(3, 6);
		numEdges = st.nextToken();
		
		// Skip the part of the file with the vertices only
		// Ends at the line with the "$"
		while (!(line.equals("$"))) {
			line = graphFile.readLine();
		}
		// Go through each line and add the vertices and edges to the graph
		while ((line = graphFile.readLine()) != null) {
			st = new StringTokenizer(line);
			if (st.countTokens() != 3) {
				throw new IOException("Incorrect input file at line " + line);
			}
			Integer source = new Integer(st.nextToken());
			Integer dest = new Integer(st.nextToken());
			Integer weight = new Integer(st.nextToken());
			if (weight == -1) {
				weight = 90;
			}
			Vertex<Integer> sv = vertices.get(source);
			
			if (sv == null) {
				// Source vertex not in graph -- insert
				sv = parisGraph.insertVertex(source);
				vertices.put(source, sv);
			}
			Vertex<Integer> dv = vertices.get(dest);
			if (dv == null) {
				// Destination vertex not in graph -- insert
				dv = parisGraph.insertVertex(dest);
				vertices.put(dest, dv);
			}
			// check if edge is already in graph
			if (parisGraph.getEdge(sv, dv) == null) {
				// edge not in graph -- add
				//e's element is now the distance between the vertices
				//Modified by Thais Bardini on November 19th, 2017 (tbard069@uottawa.ca)
				Edge<Integer> e = parisGraph.insertEdge(sv, dv, weight);
			}
		}
	}
	
	/**
	 * Printing all the vertices in the list, followed by printing all the edges
	 * Modified by Thais Bardini on November 19th, 2017 (tbard069@uottawa.ca) 
	 * Modified by Wilfried Tanezick Dondji Fogang on December 4th, 2017 (wdond086@uottawa.ca)
	 */
	@SuppressWarnings("unused")
	private void print() {
		System.out.println("Vertices: " + parisGraph.numVertices() + " Edges: " + parisGraph.numEdges());
		// Print aoll vertices
		for (Vertex<Integer> vs : parisGraph.vertices()) {
			System.out.println(vs.getElement());
		}
		// Print all edges
		for (Edge<Integer> es : parisGraph.edges()) {
			System.out.println(es.getElement());
		}
		return;
	}
	
	/**
	 * Helper method to get a specific vertex from the graph
	 * Gotten from the WeightGraph class in lab files
	 * @param vert the station to be found
	 * @return The vertex of the specified station
	 */
	protected Vertex<Integer> getVertex( int vert ) {
		// Go through vertex list to find vertex -- why is this not a map
		for( Vertex<Integer> vs : parisGraph.vertices() ) {
			if ( vs.getElement().equals( vert )) {
			return vs;
			}
	    }
		return null;
	}
	
	Hashtable<Integer,Boolean> visited;
	
	/**
	 * Traverses the graph to find and print all the stations on the same line as vertex v
	 * This method is implemented using a Depth-First search algorithm.
	 * Adapted from the implementation of the DFS method in the SimpleGraph class of lab 9 files
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * @param vert The starting vertex
	 */
	public void identifyStations(int vert) {
		DoublyLinkedList<Integer> line = identifyStations1(vert);
	    while (!(line.isEmpty())) {
	    	int remove = line.removeFirst();
	    	System.out.print(remove + " ");
	    }
	}
	
	/**
	 * Traverses the graph to find and print all the stations on the same line as vertex v
	 * This method is implemented using a Depth-First search algorithm.
	 * Adapted from the implementation of the DFS method in the SimpleGraph class of lab 9 files
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * @param vert The starting vertex
	 */
	@SuppressWarnings("static-access")
	private DoublyLinkedList<Integer> identifyStations1(int vert) {
		// Remove all edges of weight 90 from the graph
		for (Edge<Integer> edge: parisGraph.edges()) {
			if (edge.getElement() == 90) {
				parisGraph.removeEdge(edge);
			}
		}
	    Vertex<Integer> vt = getVertex(vert);
	    DoublyLinkedList<Integer> toReturn = new DoublyLinkedList<Integer>();
	    visited = new Hashtable<>();
	    DFS(this.parisGraph, vt, toReturn);
	    return toReturn;
	}
	
	/**
	 * Depth first traversal of the graph to print the stations in the line, starting from vertex v
	 * Adapted from the implementation of the DFS method in the SimpleGraph class of lab 9 files
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * @param graph The graph to traverse
	 * @param v The vertex to start the traversal
	 */
	private void DFS(Graph<Integer,Integer> graph, Vertex<Integer> v, DoublyLinkedList<Integer> toReturn) {
	    if (visited.get(v.getElement()) != null) {
	    	return;
	    }
	    visited.put(v.getElement(), Boolean.TRUE);
	    startVisit(v, toReturn);
	    for(Edge<Integer> e : graph.outgoingEdges(v)){
	        Vertex<Integer> s = graph.opposite(v, e);
	        DFS(graph, s, toReturn);
	    }
	    finishVisit(v);
	    return;
	  }
	
	/**
	 * Helper method to print the current station during the traversal
	 * Adapted from the implementation of the DFS method in the SimpleGraph class of lab 9 files
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * @param v The current vertex in the traversal
	 */
	private void startVisit( Vertex<Integer> v, DoublyLinkedList<Integer> toReturn) {
	    // System.out.print( v.getElement() + " ");
	    toReturn.addLast(v.getElement());
	    return;
	}
	
	/**
	 * Helper method for the traversal
	 * Adapted from the implementation of the DFS method in the SimpleGraph class of lab 9 files
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * @param v The current vertex in the traversal
	 */
	private void finishVisit( Vertex<Integer> v ) {
		System.out.print("");
	}
	
	/**
	 * Prints the path from station n1 to n2 in order, and also prints the total time the path costs
	 * Done by using the shortestPathLengths method in the GraphAlgorithms class, and the method
	 * path1 implemented below.
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * @param n1 The start station
	 * @param n2 The destination station
	 */
	@SuppressWarnings({ "static-access", "unused" })
	public void shortPath1(int n1, int n2) {
		Vertex<Integer> vStart = getVertex(n1);
		Vertex<Integer> vEnd = getVertex(n2);
		GraphAlgorithms dj = new GraphAlgorithms();
		Map<Vertex<Integer>, Integer> shortCloud = dj.shortestPathLengths(parisGraph, vStart);
		int totalTime = shortCloud.get(vEnd);
		DoublyLinkedList<Integer> toPrint = new DoublyLinkedList<Integer>();
		toPrint.addFirst(n2);
		int station = path1(n1, n2);
		while (station != n1) {
			toPrint.addFirst(station);
			station = path1(n1, station);
		}
		toPrint.addFirst(n1);
		while (!(toPrint.isEmpty())) {
			System.out.print(toPrint.removeFirst() + " ");
		}
		System.out.println();
		System.out.println("The total time is " + shortCloud.get(vEnd));
	}
	
	/**
	 * Private method to find the edge used by Djikstra's algorithm in finding the shortest distance
	 * By finding the that edge, we find the station used in calculating the path
	 * Done by comparing the difference between the shortest distance from the start station to the end station and
	 * the weight of its incident edges, to the shortest distance from the start station and the
	 * adjacent stations to the end station
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * @param n1 The starting vertex
	 * @param n2 The destination vertex
	 * @return The station used in the shortest path
	 */
	private int path1(int n1, int n2) {
		Vertex<Integer> vStart = getVertex(n1);
		Vertex<Integer> vEnd = getVertex(n2);
		GraphAlgorithms dj = new GraphAlgorithms();
		@SuppressWarnings("static-access")
		Map<Vertex<Integer>, Integer> shortCloud = dj.shortestPathLengths(parisGraph, vStart);
		int totalTime = shortCloud.get(vEnd);
		int difference = 0;
		int station = 0;
		for (Edge<Integer> e: parisGraph.incomingEdges(vEnd)) {
			Vertex<Integer> opposite;
			opposite = parisGraph.opposite(vEnd, e);
			difference = totalTime - e.getElement();
			if (shortCloud.get(opposite) == difference) {
				station = opposite.getElement();
			}
		}
		return station;
	}
	
	/**
	 * Prints the shortest path and total time from station n1 to station n2 when the stations on the same line as
	 * station n3 are not available.
	 * Done by first getting the list of station on the unavailable line using the identifyStations1 method above.
	 * Then a new graph is created which excludes all the stations and edges related to those in the unavailable list.
	 * Finally a call is made to the method shortPath1, which prints the shortest path and total time.
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * @param n1 The start station
	 * @param n2 The destination station
	 * @param n3 The unavailable station
	 * @throws Exception
	 * @throws IOException
	 */
	public void shortPath2(int n1, int n2, int n3) throws Exception, IOException {
		DoublyLinkedList<Integer> inactive = identifyStations1(n3);
		int i = 0;
		ArrayList<Integer> list = new ArrayList<Integer>(50);
		// Copy contents of inactive to an ArrayList for easy iteration
		while (!(inactive.isEmpty())) {
			list.add(i, inactive.removeFirst());
			i++;
		}
		readMetro("metro.txt", list);
		shortPath1(n1, n2);
	}
	
	/**
	 * Creates a new graph which excludes the stations and edges related to those in the "inactive" list
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * @param filename The name of the file from which the graph is created
	 * @param inactive The ArrayList of inactive stations
	 * @throws Exception When incorrect input is found in the file
	 * @throws IOException When incorrect input is found in the file
	 */
	@SuppressWarnings({ "resource", "unused" })
	public static void readMetro(String filename, ArrayList<Integer> inactive) throws Exception, IOException {
		parisGraph = new AdjacencyMapGraph<Integer, Integer>(true);
		BufferedReader graphFile = new BufferedReader(new FileReader(filename));

		// Create a hash map to store all the vertices read
		Hashtable<Integer, Vertex<Integer>> vertices = new Hashtable<Integer, Vertex<Integer>>();

		// Read the edges and insert
		String line = graphFile.readLine();
		// Check for correctness of input for first line
		if (line == null) {
			throw new IOException("Incorrect input for number of vertices and edges!");
		}
		StringTokenizer st = new StringTokenizer(line);
		
		numVertices = st.nextToken().substring(3, 6);
		numEdges = st.nextToken();
		
		// Skip the part of the file with the vertices only
		// Ends at the line with the "$"
		while (!(line.equals("$"))) {
			line = graphFile.readLine();
		}
		// Go through each line and add the vertices and edges to the graph
		outerloop:
		while ((line = graphFile.readLine()) != null) {
			st = new StringTokenizer(line);
			if (st.countTokens() != 3) {
				throw new IOException("Incorrect input file at line " + line);
			}
			Integer source = new Integer(st.nextToken());
			Integer dest = new Integer(st.nextToken());
			Integer weight = new Integer(st.nextToken());
			// Replace the weights of value -1 to 90
			if (weight == -1) {
				weight = 90;
			}
			// Check if the edge is related to unavailable stations
			// If yes, skip the line and move on
			for (Integer i: inactive) {
				if ((source.equals(i)) || (dest.equals(i))) {
					// System.out.println(i+" won't be added to the graph");
					continue outerloop;
				}
			}
			Vertex<Integer> sv = vertices.get(source);
			
			if (sv == null) {
				// Source vertex not in graph -- insert
				sv = parisGraph.insertVertex(source);
				vertices.put(source, sv);
			}
			Vertex<Integer> dv = vertices.get(dest);
			if (dv == null) {
				// Destination vertex not in graph -- insert
				dv = parisGraph.insertVertex(dest);
				vertices.put(dest, dv);
			}
			// check if edge is already in graph
			if (parisGraph.getEdge(sv, dv) == null) {
				// edge not in graph -- add
				//e's element is now the distance between the vertices
				//Modified by Thais Bardini on November 19th, 2017 (tbard069@uottawa.ca)
				Edge<Integer> e = parisGraph.insertEdge(sv, dv, weight);
			}
		}
	}
	
	/**
	 * The main method where the graph is created and stored
	 * Modified by Wilfried Dondji on December 4th, 2017 (wdond086@uottawa.ca)
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: java WeightGraph fileName");
			System.exit(-1);
		} else if (args.length == 1) {
			try {
				ParisMetro paris = new ParisMetro(new Integer(args[0]));
			} catch (Exception err) {
				System.err.println(err);
				err.printStackTrace();
			}
		} else if (args.length == 2) {
			try {
				ParisMetro paris = new ParisMetro(new Integer(args[0]), new Integer(args[1]));
			} catch (Exception err) {
				System.err.println(err);
				err.printStackTrace();
			}
		} else {
			try {
				ParisMetro paris = new ParisMetro(new Integer(args[0]), new Integer(args[1]), new Integer(args[2]));
			} catch (Exception err) {
				System.err.println(err);
				err.printStackTrace();
			}
		}
	}
}

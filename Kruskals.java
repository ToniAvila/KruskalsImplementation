package project5;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.PriorityQueue;

/* 
 * Toni Avila
 * 
 * This program finds the minimum spanning tree of a graph read in and created
 * through an adjacency list using Kruskal's algorithm. The DisjSets class 
 * is used as well as an Edge class to represent weighted edges.
 * 
 */

public class Kruskals {
	
	ArrayList<Edge> Edges = new ArrayList<>();
	ArrayList<String> Vertices = new ArrayList<>();
	
	public Kruskals() {
		Edges = new ArrayList<Edge>();
		Vertices = new ArrayList<String>();
	}
	
	// method to perform file reading, filling our vertex and edge lists along the way
	public void readFile(String dataFile) throws IOException {
		
		File file = new File(dataFile);
		FileReader fileRead = new FileReader(file);
		BufferedReader buffRead = new BufferedReader(fileRead);
		String line = "";
		
		// while text is present, read and split
		while((line = buffRead.readLine()) != null) {
			String temp[] = line.split(",");
			
			Vertices.add(temp[0]);
			
			Edges.add(new Edge(temp[0], temp[1], Integer.parseInt(temp[2])));
			
			// need loop for rest of line (i.e. 3 and onward)
			for(int i = 3; i < temp.length; i+=2) {
				Edges.add(new Edge(temp[0], temp[i], Integer.parseInt(temp[i+1])));
			}
			
		}
		
	}
    
	public void kruskal(String file) throws IOException {
		
		// read contents of csv file into storage (vector and edge lists)
		readFile(file);
	
		int edgesAccepted = 0;
		int totalDist = 0; // will keep track of sum
		DisjSet ds = new DisjSet(Vertices.size()); // initialize disjoint set for vertices
		PriorityQueue<Edge> pq = new PriorityQueue<>(); // initialize p queue for edges
		
		// filling Priority Queue
		for(int i = 0; i < Edges.size(); i++) {
			pq.add(Edges.get(i));
		}
		
		Edge e;
		int u,v; // vertices 
		
		
		while(edgesAccepted < Vertices.size() - 1) {
			e = pq.remove(); // performs same operation as deleteMin
			
			// find index of vert
			u = Vertices.indexOf(e.v1);
			v = Vertices.indexOf(e.v2);
			
			// perform find() to find set which the vertices belong to
			int  uset = ds.find(u);
			int  vset = ds.find(v);
			
			// if not same set, increment distance, perform union()
			if(uset != vset) {
				edgesAccepted++;
				totalDist += e.distance;
				ds.union(uset,  vset);
				// output path and output specific distance
				System.out.println(e.v1 + " to " + e.v2);
				System.out.println("Distance = " + e.distance);
			}
		}
		System.out.println("Total dist (msp): " + totalDist);
		
	}
	

	// main method
	public static void main(String[] args) throws IOException {
		Kruskals msp = new Kruskals();
		msp.kruskal(args[0]);
	}
}

// Edge class utilized within Kruskal's class
class Edge{
	int distance;
	String v1 , v2;
	
	public Edge(String s1 , String s2, int d){
		if(s1.isEmpty() || s2.isEmpty())
			throw new IllegalArgumentException("Unacceptable string or edge weight.");
		
		this.v1 = s1;
		this.v2 = s2;
		this.distance = d;
	}
}

// DisjSet class used within Kruskal's class
class DisjSet{
    
    public DisjSet( int numElements )
    {
        s = new int [ numElements ];
        for( int i = 0; i < s.length; i++ )
            s[ i ] = -1;
    }
    
    public void union( int root1, int root2 ){
        if( s[ root2 ] < s[ root1 ] )  // root2 is deeper
            s[ root1 ] = root2;        // Make root2 new root
        else{
            if( s[ root1 ] == s[ root2 ] )
                s[ root1 ]--;          // Update height if same
            s[ root2 ] = root1;        // Make root1 new root
        }
    }

    public int find( int x ){
        if( s[ x ] < 0 )
            return x;
        else
            return s[ x ] = find( s[ x ] );
    }

    private int [ ] s;

}

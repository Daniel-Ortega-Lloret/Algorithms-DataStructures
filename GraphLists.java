/*
 * Program to demonstrate weighted graph traversal using Prim's Algorithm, Dijsktra's Algorithm, Depth-First Search, and Breadth-First Search.
 * 
 * Start Date: 25/03/2024
 * 
 * Authors: 
 *  Daniel Ortega Lloret C22726225
 *  Dylan O'rourke C22341463
 *  Ciaran Coyne C22416392
 *  George Crossan C22374763
 */

// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;

enum C {White, Gray, Black};

class Heap
{
    private int[] a;	   // heap array
    private int[] hPos;	   // hPos[h[k]] == k
    private int[] dist;    // dist[v] = priority of v

    private int N;         // heap size
   
    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) 
    {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }


    public boolean isEmpty() 
    {
        return N == 0;
    }


    public void siftUp( int k) 
    {
        int v = a[k];
        hPos[a[k]] = k;

        a[0] = 0;    // consider 0 as a kind of dummy heap value
        dist[0] = 0; // pay close attention to this, smaller dist means higher priority

        while( dist[v] < dist[a[k/2]] ) {
            a[k] = a[k/2];
            hPos[a[k]] = k;
            k = k/2;
        }
        a[k] = v;
        hPos[v] = k;
    }


    public void siftDown( int k) 
    {
        int v, j;
        v = a[k];  

        while( k <= N/2) {
            j = 2 * k;
            if(j < N && dist[a[j]] > dist[a[j+1]]) ++j;
            if( dist[v] <= dist[a[j]]) break;

            a[k] = a[j];	  
            hPos[a[k]] = k;
            k = j;
        }
        a[k] = v;
        hPos[a[k]] = k; 
    }


    public void insert( int x) 
    {
        a[++N] = x;
        siftUp( N);
    }


    public int remove() 
    {   
        int v = a[1];
        hPos[v] = 0; // v is no longer in heap
        a[N+1] = 0;  // put null node into empty spot
        
        a[1] = a[N--];
        siftDown(1);
        
        return v;
    }
}

class Queue 
{
    private class Node 
    {
        int data;
        Node next;
    }

    Node z, head, tail;

    public Queue() 
    {
        z = new Node(); z.next = z;
        head = z;  tail = null;
    }
   

    public void enQueue(int x) 
    {
        Node t;

        t = new Node();
        t.data = x;
        t.next = z;

        if(head == z)       // case of empty list
            head = t;
        else                // case of list not empty
            tail.next = t;
            
        tail = t;           // new node is now at the tail
    }

  // assume the queue is non-empty when this method is called
    public int deQueue() 
    {
        int value;
        value = head.data;
        
        head = head.next;
        
        return value;
    }
    

    public boolean isEmpty() 
    {
        return head == head.next;
    }
}
class Graph 
{
    class Node 
    {
        public int vert;
        public int wgt;
        public Node next;
    }
    
    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;
    
    // used for traversing graph
    private int[] visited;
    private C[] colour;
    private int time;
    private int id;
    
    //For storing the traversal tree and distance from starting vertex
    private int[] parent, d, f;
    private int[] inOrder;
    
    // default constructor
    public Graph(String graphFile)  throws IOException
    {
        int u, v;
        int e, wgt;
        Node t, y;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        // create sentinel node
        z = new Node(); 
        z.next = z;
        
        // create adjacency lists, initialized to sentinel node z     
        //Initialize visited to size of given graph  
        adj = new Node[V+1];    
        visited = new int[V+1];  
        for(v = 1; v <= V; ++v)
        {
            adj[v] = z;  
            visited[v] = 0;
        }

       // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            wgt = Integer.parseInt(parts[2]);
            
            System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));   

            // write code to put edge into adjacency matrix (did you mean adjacency list?)
            t = new Node();
            t.vert = v;
            t.wgt = wgt;
            t.next = adj[u];
            adj[u] = t; 

            y = new Node();
            y.vert = u;
            y.wgt = wgt;
            y.next = adj[v];
            adj[v] = y;
        }	
        reader.close();
    }
   
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
    
    // method to display the graph representation
    public void display() 
    {
        int v;
        Node n;
        
        for(v=1; v<=V; ++v)
        {
            System.out.print("\nadj[" + toChar(v) + "] ->" );
            for(n = adj[v]; n != z; n = n.next) 
            {
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");   
            }
        }
        System.out.println("");
    }
    
    //Prim's Algorithm
	public void MST_Prim(int s)
	{
        System.out.println("\n\nPrim's MST:");

        int v, d;
        int wgt, wgt_sum = 0;
        int[]  dist, parent, hPos;
        dist = new int[V+1];
        parent = new int[V+1];
        hPos = new int[V+1];
        
        Node u;


        //code here
        
        for (int i= 0; i< V+1; ++i) {
            dist[i] = Integer.MAX_VALUE;
            hPos[i] = 0;
            parent[i] = 0;

        }
        
        Heap h =  new Heap(V, dist, hPos);
        h.insert(s);
        
        while (!h.isEmpty())  
        {
            // most of alg here
            v = h.remove();
            dist[v] = -dist[v];
            for(u = adj[v]; u != z; u = u.next){
                wgt = u.wgt;
                if(wgt < dist[u.vert]){
                    d = dist[u.vert];

                    dist[u.vert] = wgt;
                    parent[u.vert] = v;
                    if(hPos[u.vert] == 0){
                        h.insert(u.vert);
                        wgt_sum += wgt;

                    }
                    else{
                        wgt_sum -= d;
                        h.siftUp(hPos[u.vert]);
                        wgt_sum += wgt;
                    }
                }
            }
       }

        System.out.print("Weight of MST = " + wgt_sum + "\n");
        System.out.println("Vertex\tParent");  
        for (int i = 1; i <= V; i++)  
        System.out.println(toChar(i) + "\t" + toChar(parent[i])); 
	}
    
    public void showMST()
    {
        System.out.print("\n\nMinimum Spanning tree parent array is:\n");
        for(int v = 1; v <= V; ++v)
        {
            System.out.println(toChar(v) + " -> " + toChar(mst[v]));
        }
            
        System.out.println("");
    }

    //Dijkstra's Algorithm
    public void SPT_Dijkstra(int s)
    {
        System.out.println("\n\nDijkstra's SPT:");

        int v;
        int wgt, wgt_sum = 0;
        int[]  dist, parent, hPos;
        dist = new int[V+1];
        parent = new int[V+1];
        hPos = new int[V+1];
        
        Heap pq;
        Node u;

        for (int i= 0; i< V+1; ++i) {
            dist[i] = Integer.MAX_VALUE;
            hPos[i] = 0;
            parent[i] = 0;

        }
        pq = new Heap(V,dist,hPos);
        v = s;
        dist[s] = 0;

        pq.insert(s);

        while(!pq.isEmpty()){
            for(u = adj[v]; u != z; u = u.next){
                wgt = u.wgt;
                if(dist[v] + wgt < dist[u.vert]){
                    dist[u.vert] = dist[v] + wgt;
                    if(hPos[u.vert] == 0){
                        pq.insert(u.vert);
                    }
                    else{
                        pq.siftUp(hPos[u.vert]);
                    }
                    parent[u.vert] = v;
                }
            } 
            v = pq.remove();

        }
        System.out.println("Shortest Path Tree as it is built is: \n");  
        System.out.println("Vertex\tParent\tDistance from root");  
        for (int i = 1; i <= V; i++)  
        System.out.println(toChar(i) + "\t" + toChar(parent[i]) + "\t" + dist[i]); 
    }

    // Breadth-First Traversal using a queue
    public void breadthFirst(int s)
    {
        // Make The Distance Array, And Parent Array
        int[]  dist, parent;
        int[] Neighbors;
        dist = new int[V+1];
        parent = new int[V + 1];
        Neighbors = new int[V]; // array to store all adjacent nodes of current node
        colour = new C[V+1]; // stores colour status of each vertex

        // Used For Storing Dequeued Value
        int u;

        // Set All Vertices To White
        for (int i = 0; i <= V; i++)
        {
            colour[i] = C.White;
            dist[i] = Integer.MAX_VALUE;
            parent[i] = 0;
        }

        System.out.println("\nBreadth-First Graph Traversal\n");
        System.out.println("Starting At Vertex " + toChar(s) + " Visiting Children");

        // Make First Vertex Grey
        colour[s] = C.Gray;

        // Set parent & distance of first vertex to 0
        dist[s] = 0; 
        parent[s] = 0;

        Queue Q = new Queue();
        
        Q.enQueue(s); // place first vertex first into the queue

        while (!Q.isEmpty())
        {
            u = Q.deQueue(); // removes queue vertex and stores it temporarily

            Neighbors = NeighborCount(u); // list of adjacent nodes of current node

            // For Every Child Of U
            for (int v : Neighbors)
            {
                // If Child Is White
                if (colour[v] == C.White)
                {
                    colour[v] = C.Gray; // mark child as visited
                    dist[v] = dist[u] + 1; // record distance
                    parent[v] = u;
                    Q.enQueue(v); //visit child

                    System.out.println("\nBFS just visited vertex " + toChar(v) + " along edge " + toChar(u) + "--" + toChar(v));
                }
            }

            // When Done With Parent Make It Black
            colour[u] = C.Black;
        }
    }

    //Recursive Depth-First Traversal
    public void DF(int s)
    {
        int v; // var for loops

        colour = new C[V+1]; // stores colour status of each vertex
        parent = new int[V+1]; // stores parent node of each vertex
        d = new int[V+1]; // distance array

        // Initializes colour and parent arrays with empty/unvisited values before algorithm starts
        for (v = 1; v <= V; ++v)
        {
            colour[v] = C.White;
            parent[v] = 0;
        }

        System.out.println("\nDepth-First Graph Traversal\n");
        System.out.println("Starting with Vertex " + toChar(s));

        // Visits every node marked as unvisited(White)
        for (v = 1; v <= V; ++v)
        {
            if (colour[v] == C.White)
            {
                dfVisit(s);
            }
        }
        
        System.out.print("\n\n");
    }

    // Recursive Depth-First Search Algorithm
    private void dfVisit( int u)
    {
        // integer list representation of all the vertices neighboring the current vertex
        int[] neighbors = NeighborCount(u);

        // Marks current vertex as visited
        colour[u] = C.Gray;

        System.out.println("\n DF just visited vertex " + toChar(u) + " along edge " + toChar(parent[u]) + "--" + toChar(u));

        //Foreach loop that iterates for however many neighbors the current vertex has
        for ( int v : neighbors)
        {
            // If neighboring vertex is unvisited, mark its parent as the current vertex, record its distance, and visit it.
            if (colour[v] == C.White)
            {
                parent[v] = u;
                d[v] = d[u] + 1;
                dfVisit(v);
            }
        }

        //Mark current vertex as processed
        colour[u] = C.Black;
    }

    //Function that returns a list of the numeric representation of all the vertices connected to the given vertex
    public int[] NeighborCount(int v)
    {
        int[] neighbors;
        int size = 0;
        Node p = adj[v];
        //Find out how many neighbors there are
        while (p != z)
        {
            ++size;
            p = p.next;
        }

        neighbors = new int[size];
        p = adj[v]; //Reset p

        //Fill out integer list with all the neighbors
        if (p == z)
        {
            return neighbors;
        }
        else
        {
            for (int i = 0; i < neighbors.length; ++i)
            {
                if (p == z)
                {
                    break;
                }
                neighbors[i] = p.vert;
                p = p.next;
            }
            return neighbors;
        }
    }
}

public class GraphLists
{
    public static void main(String[] args) throws IOException
    {
        // int s = 2;
        // String fname = "wGraph1.txt";               
        // Graph g = new Graph(fname);
        boolean validTxt = false;
        boolean validVert = false;

        String graphTxt = "";
        int choice = 0;

        //User error handling
        while (!validTxt)
        {
            try
            {
                System.out.println("Please enter the name of a text file with a weighted graph:\n");
                graphTxt = System.console().readLine();

                //Check if the inputted file exists
                File file = new File(graphTxt);

                if (!file.exists())
                {
                    throw new FileNotFoundException();
                }
                
                validTxt = true;
            } catch (FileNotFoundException e)
            {
                System.out.println("Error, please make sure you spelled text file name correctly.\n");
            }
        }
        while (!validVert)
        {
            try
            {
                System.out.println("Please enter the desired starting vertex:\n");
                choice = Integer.parseInt(System.console().readLine());

                validVert = true;
            } catch (Exception e)
            {
                System.out.println("Error, only integers allowed.\n");
            }
        }
        
        Graph g = new Graph(graphTxt);

        g.display();

        g.SPT_Dijkstra(choice); 
        g.MST_Prim(choice);  
        g.DF(choice);
        g.breadthFirst(choice);             
    }
}
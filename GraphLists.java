/*
 * Program to demonstrate weighted graph traversal using Prim's Algorithm, Dijsktra's Algorithm, Depth-First Search, and Breadth-First Search.
 * 
 * Start Date: 25/03/2024
 * 
 * Authors: 
 *  Daniel Ortega Lloret C22726225
 *  Dylan O'rourke
 *  Ciaran Coyne
 *  George Crossan
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

        // code yourself
        // must use hPos[] and dist[] arrays
        a[0] = 0;
        dist[0] = 0;

        while (dist[v] < dist[a[k / 2]])
        {
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
        
        // code yourself 
        // must use hPos[] and dist[] arrays
        while (k <= N/2 && dist[v] < dist[a[v]])
        {
            j = 2 * k;
            if (j < N && a[j] < a[j + 1])
            {
                ++j;
            }
            if (v >= a[j])
            {
                break;
            }

            a[k] = a[j];
            hPos[a[k]] = j;

            k = j;
        }

        a[k] = v;
        hPos[v] = k;
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

class Queue {

    private class Node {
        int data;
        Node next;
    }

    Node z, head, tail;

    public Queue() {
        z = new Node(); z.next = z;
        head = z;  tail = null;
    }
   

    public void enQueue(int x) {
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
    public int deQueue() {
        int value;
        value = head.data;
        
        head = head.next;
        
        return value;
    }
    

    public boolean isEmpty() {
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
        int v, u;
        int wgt, wgt_sum = 0;
        int[]  dist, parent, hPos;
        Node t;

        //code here
        dist = new int[V];
        parent = new int[V];
        hPos = new int[V];

        dist[s] = 0;
        
        Heap h =  new Heap(V, dist, hPos);
        h.insert(s);
        
        while (true)  
        {
            //most of alg here
            
        }
        //System.out.print("\n\nWeight of MST = " + wgt_sum + "\n");
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
        
    }

    public void breadthFirst(int s)
    {
        System.out.println("Starting At Vertex " + toChar(s) + " Visiting Children");
        // Make The Distance Array, And Parent Array
        int[]  dist, parent;
        int[] Neighbors;
        dist = new int[V+1];
        parent = new int[V + 1];
        Neighbors = new int[V];

        // Used For Storing Dequeued Value
        int u;


        // Set All Vertices To White
        for (int i = 0; i < V; i++)
        {
            visited[i] = 0;
            dist[i] = Integer.MAX_VALUE;
            parent[i] = 0;
        }

        // Make First Vertex Grey
        visited[s] = 1;
        dist[s] = 0;
        parent[s] = 0;

        Queue Q = new Queue();
        
        Q.enQueue(s);
        while (!Q.isEmpty())
        {
            u = Q.deQueue();
            //System.out.println("Dequeued " + toChar(u) + " From Queue.\n");

            Neighbors = NeighborCount(u);
            // For Every Child Of U
            for (int v : Neighbors)
            {
                // If Child Is White
                if (visited[v] == 0)
                {
                    visited[v] = 1;
                    dist[v] = dist[u] + 1;
                    parent[v] = u;
                    Q.enQueue(v);
                    System.out.println("BFS just visited vertex " + toChar(v) + " along edge " + toChar(u) + "--" + toChar(v));
                }
            }
            //System.out.println("Processing Next Element In Queue...\n");
            // When Done With Parent Make It Black
            visited[u] = 2;
        }
        
        // For Checking The Dist and Parent Array
        for (int i = 1; i < V + 1; i++)
        {
            System.out.print("\nDistance of Node " + toChar(i) + " From source is " + dist[i]);
            System.out.print("\tParent of Node is " + toChar(parent[i]));
        }

        showBF(parent);
    
    }

    public void showBF(int [] parent)
    {
        Node[] inOrder = ParentInOrder(parent);
        System.out.println("\n\nThe minimum spanning tree found by Breadth-First Search is:\n");

        int v;
        Node n;
        
        for(v=1; v<=V; ++v)
        {
            System.out.print("\nadj[" + toChar(v) + "] ->" );
            for(n = inOrder[v]; n != z; n = n.next) 
            {
                System.out.print(" " + toChar(n.vert) + " -> ");   
            }
        }
        System.out.println("");
    }

    //Recursive Depth-First Traversal
    public void DF(int s)
    {
        int v;
        colour = new C[V+1];
        parent = new int[V+1];
        d = new int[V+1];
        f = new int[V+1];
        inOrder = new int[V+1];
        for (v = 1; v <= V; ++v)
        {
            colour[v] = C.White;
            parent[v] = 0;
        }

        System.out.println("\nDepth First Graph Traversal\n");
        System.out.println("Starting with Vertex " + toChar(s));

        time = 0;
        for (v = 1; v <= V; ++v)
        {
            if (colour[v] == C.White)
            {
                dfVisit(s);
            }
        }

        ShowDF(parent);
        
        System.out.print("\n\n");
    }

    private void dfVisit( int u)
    {
        int[] neighbors = NeighborCount(u);
        ++time;
        d[u] = time;
        colour[u] = C.Gray;

        System.out.println("\n DF just visited vertex " + toChar(u) + " along edge " + toChar(parent[u]) + "--" + toChar(u));

        for ( int v : neighbors)
        {
            if (colour[v] == C.White)
            {
                parent[v] = u;
                dfVisit(v);
            }
        }

        colour[u] = C.Black;
        ++time;
        f[u] = time;
    }
    
    public void ShowDF(int[] parent)
    {
        Node[] inOrder = ParentInOrder(parent);
        System.out.println("\n\nThe minimum spanning tree found by Depth-First Search is:\n");

        int v;
        Node n;
        
        for(v=1; v<=V; ++v)
        {
            System.out.print("\nadj[" + toChar(v) + "] ->" );
            for(n = inOrder[v]; n != z; n = n.next) 
            {
                System.out.print(" " + toChar(n.vert) + " -> ");   
            }
        }
        System.out.println("");
    }

    //Function to return an adjacency list with the spanning tree
    public Node[] ParentInOrder(int[] parent)
    {
        int v;
        int j;
        Node[] inOrder = new Node[V+1];

        for (v = 1; v <= V; ++v)
        {
            inOrder[v] = z;
        }


        for (v = 1; v <= V; ++v)
        {
            for (j = 1; j <= V; ++j)
            {
                if (parent[j] == v)
                {
                    Node p = new Node();
                    p.vert = j;
                    p.next = inOrder[v];
                    inOrder[v] = p;
                }
            }
        }

        return inOrder;
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

        //g.SPT_Dijkstra(choice); 
        //g.MST_Prim(choice);  
        g.DF(choice);
        g.breadthFirst(choice);             
    }
}
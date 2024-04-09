/*
 * Program to demonstrate weighted graph traversal using Prim's Algorithm, Dijsktra's Algorithm, Depth-First Search, and Breadth-First Search.
 * 
 * Start Date: 25/03/2024
 * 
 * Author: Daniel Ortega Lloret C22726225, Dylan Orourke C22341463
 */
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;

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
    private Node[] df;
    
    // used for traversing graph
    private int[] visited;
    private int id;
    
    
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
        // Make The Distance Array, And Parent Array
        int[]  dist, parent;
        dist = new int[V];
        parent = new int[V];


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

        //ENQUEUE(Q, s);
        //while Q isnt empty
        // u = DEQUEUE(Q);

        int[] Neighbors = NeighborCount(u);
        // For Every Child Of U
        for (int v : Neighbors)
        {
            // If Child Is White
            if (visited[v] == 0)
            {
                visited[v] = 1;
                dist[v] = dist[u] + 1;
                parent[v] = u;
                //ENQUEUE(Q, v);
            }
        }
        // When Done With Parent Make It Black
        visited[u] = 2;
    }

    public void DF_Show(int v)
    {
        
        df = new Node[1];  
        df[0] = z;

        System.out.println("\nPerforming Recursive Depth-First Search Traversal:\n");

        DF(v);

        Node printer = df[0];

        for (int i = 0; i < V; i++)
        {
            System.out.println(toChar(printer.vert) + " -> " + toChar(printer.next.vert));
            printer = printer.next;
        }
        
    }

    //Recursive Depth-First Traversal
    public void DF(int v)
    {
        int[] neighbors = NeighborCount(v);
        
        if (visited[v] == 1)
        {
            return;
        }

        visited[v] = 1;
        if (df[0] == z)
        {
            df[0] = adj[v];
            df[0].next = z;
        }
        else
        {
            df[0].next = adj[v];
        }

       for (int u : neighbors)
       {
            if (visited[u] == 0)
            {
                DF(u);
            }
       }
            
    }

    public int[] NeighborCount(int v)
    {
        int[] neighbors;
        int size = 0;
        Node p = adj[v].next;
        //Find out how many neighbors there are
        while (p != z)
        {
            size++;
            p = p.next;
        }

        neighbors = new int[size];
        p = adj[v].next; //Reset p

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
        //g.DF_Show(choice);
        g.breadthFirst(choice);             
    }
}
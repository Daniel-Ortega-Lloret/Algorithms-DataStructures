// Kruskal's Minimum Spanning Tree Algorithm
// Union-find implemented using disjoint set trees without compression

import java.io.*;
import java.util.HashSet;    
 
// Stores Information Of Each Edge
class Edge 
{
    public int u, v, wgt;

    // Default Edge Values If None Passed
    public Edge() 
    {
        u = 0;
        v = 0;
        wgt = 0;
    }


    public Edge( int x, int y, int w) 
    {
        u = x;
        v = y;
        wgt = w;
    }
    
    // Show The Vertex u, v And Weight Of An Edge
    public void show() 
    {
        System.out.print("Edge " + toChar(u) + "--" + wgt + "--" + toChar(v) + "\n") ;
    }
    
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
}


// We Will Store The Edges In A Minimum Heap
class Heap
{
	private int[] h;
    int N, Nmax;
    Edge[] edge;    // Contains All The Edges From Graph


    // Bottom up heap constructor
    public Heap(int _N, Edge[] _edge) 
    {
        int i;
        Nmax = N = _N;
        h = new int[N+1];
        edge = _edge;
       
        // initially just fill heap array with indices of edge[] array.
        for (i=0; i <= N; ++i) 
        {
            h[i] = i;
        }
            
           
        // Then convert h[] into a heap from the bottom up.
        for(i = N/2; i > 0; --i)
        {
            siftDown(i);
        }
           
    }

    // Siftdown Sorts Based On The Weight Of An Edge
    private void siftDown( int k) 
    {
        int e, j;

        e = h[k];   
        j = 2 * k;

        // While left siblings exist
        while( j <= N)
        {
            // If N > J that means there is a right sibling. And if right sibling > left
            if ((j < N) && (edge[h[j + 1]].wgt < edge[h[j]].wgt))
            {
                j++;
            }

            if (edge[e].wgt <= edge[h[j]].wgt)
            {
                break;
            }

            // child goes up to fill gaps
            h[k] = h[j];
            k = j;
            j = 2 * k;
            h[k] = e;
        }
    }


    // Remove From The Top Of The Heap And Sift The Next Value Down
    public int remove() 
    {
        h[0] = h[1];
        h[1] = h[N--];
        siftDown(1);
        return h[0];
    }
}


//UnionFind partition to support union-find operations
class UnionFindSets
{
    private int[] treeParent;   // For Storing The Vertex's Parents
    private int[] Rank; // For Union By Rank Improvement
    private int N;
    
    // Initalise The Arrays And Set A Local Vertex Amount
    public UnionFindSets()
    {
        int V = Graph.V;
        int N = V;
        Rank = new int[V + 1];  // Used For Union by Rank
        treeParent = new int[V + 1];
    }

    // When We Make A Set, The Vertex's Parent Will Be Itself
    public void MakeSet(int vertex)
    {
        treeParent[vertex] = vertex;
        Rank[vertex] = 0;
    }

    // Recursively Find The Root Of The Set. We Choose 1 Letter To Be The Root, Then All Of The Other Nodes In Set Will Point To Root.
    public int findSet( int vertex)
    {   
        // If The Node Doesnt Point To Itself, Then Its Not The Root 
        if (treeParent[vertex] != vertex)
        {
            treeParent[vertex] = findSet(treeParent[vertex]);
        }
        return treeParent[vertex];
    }
    
    // Merge Set1 And Set2 Together
    public void union( int set1, int set2)
    {
        // Which Set Has More Depth
        if (Rank[set1] < Rank[set2])
        {
            treeParent[set1] = set2;
        }

        else if (Rank[set1] > Rank[set2])
        {
            treeParent[set2] = set1;
        }

        // If They Are Equal
        else
        {
            treeParent[set2] = set1;
            Rank[set1] = Rank[set1] + 1;
        }
    }
    
    // Prints The treeParent Array
    public void showTrees()
    {
        int i;
        for(i=1; i<=Graph.V; ++i)
            System.out.print(toChar(i) + "->" + toChar(treeParent[i]) + "  " );
        System.out.print("\n");
    }
    

    public void showSets()
    {
        int u, root;
        int[] shown = new int[N+1];
        for (u=1; u<=Graph.V; ++u)
        {   
            root = findSet(u);
            if(shown[root] != 1) {
                showSet(root);
                shown[root] = 1;
            }            
        }   
        System.out.print("\n");
    }

    private void showSet(int root)
    {
        int v;
        System.out.print("Set{");
        for(v=1; v<=N; ++v)
            if(findSet(v) == root)
                System.out.print(toChar(v) + " ");
        System.out.print("}  ");
    
    }
    
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }

    public void printParent()
    {
        System.out.print("\nVertex:\t");

        for(int i = 1; i < Graph.V + 1; i++)
        {
            System.out.print("\t" + toChar(i));
        }

        System.out.print("\nParent: ");

        for (int i = 1; i < Graph.V + 1; i++)
        {
            System.out.print("\t" + toChar(treeParent[i]));
        }

        System.out.print("\n\n");
    }
}







class Graph 
{ 
    public static int V;
    public int E;
    private Edge[] edge;
    private Edge[] mst;        

    public Graph(String graphFile) throws IOException
    {
        int u, v;
        int w, e;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        // create edge array
        edge = new Edge[E+1];   
        
        // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            w = Integer.parseInt(parts[2]);
            
            System.out.println("Edge " + toChar(u) + "--(" + w + ")--" + toChar(v));                         
            
            // create Edge object  
            edge[e] = new Edge(u, v, w);
        }
    }

    /**********************************************************
    *
    *       Kruskal's minimum spanning tree algorithm
    *
    **********************************************************/
    public Edge[] MST_Kruskal() 
    {
        int ei;
        Edge e;
        int uSet, vSet;
        UnionFindSets partition;
        
        // create edge array to store MST
        // Initially it has no edges.
        mst = new Edge[V-1];

        // priority queue for indices of array of edges
        Heap h = new Heap(E, edge);

        // create partition of singleton sets for the vertices
        partition = new UnionFindSets();
        

        // Make Sets Out Of Each Vertex
        for (int i = 1; i <= V; i++)
        {
            partition.MakeSet(i);
        }

        
    
        // T Starts As An Empty Tree That Will Hold The MST
        int v, u;
        Edge smallest; 
        int count = 0;  // To keep track of position in mst[]
        for (int i = 0; i < E - 1; i++)
        {
            smallest = new Edge(0, 0, 0);
            smallest = edge[h.remove()];

            v = smallest.v;
            u = smallest.u;
            

            // Find Root Of Set v And Set u
            v = partition.findSet(v);
            u = partition.findSet(u);

            // If They Arent in The Same Set, Merge Them
            if (v != u)
            {
                partition.union(u, v);
                mst[count] = smallest;
                count++;
                System.out.println("\nEdge " + toChar(smallest.u) + " -> " + toChar(smallest.v) + " Wgt : " + smallest.wgt + " was added to MST");
            }

            else
            {
                System.out.println("\nEdge " + toChar(smallest.u) + " -> " + toChar(smallest.v) + " Wgt : " + smallest.wgt + " NOT ADDED AS MST ALREADY CONTAINS BOTH VERTICES");
            }
            partition.showTrees();
            //partition.printParent();
        }
        return mst;
    }


    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }

    public void showMST()
    {
        System.out.print("\nMinimum spanning tree build from following edges:\n");
        for(int e = 0; e < V-1; ++e) {
            mst[e].show(); 
        }
        System.out.println();
    }

} // end of Graph class
    

class KruskalTrees {
    public static void main(String[] args) throws IOException
    {
        boolean validTxt = false;

        String graphTxt = "";

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

        Graph g = new Graph(graphTxt);

        g.MST_Kruskal();

        g.showMST();
          
    }
}    



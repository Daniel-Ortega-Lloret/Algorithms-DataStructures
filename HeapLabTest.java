

import java.io.*;

class Heap
{
    private int[] a;	   // heap array
    private int[] hPos;	   // hPos[a[k]] == k
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

        a[0] = 0;    // consider 0 as a kind of dummy heap value
        dist[0] = 0; // pay close attention to this, smaller dist means higher priority

        while(dist[v] < dist[a[k / 2]]) {
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
        while(k <= N/2 && dist[v] < dist[a[v]]) {
            j = 2 * k;
            if(j < N && a[j] < a[j+1]) ++j;
            if( v >= a[j]) break;
            a[k] = a[j];
            hPos[a[k]] = j;	  
            
            k = j;
        }
        a[k] = v;
        hPos[v] = k;
        
    }

    void minHeapify(int i) {
        int l, r, smallest, temp;
        l = 2*i; r = l+1;
    
        if (l <= N && dist[a[l]] < dist[a[i]]) smallest = l;
        else smallest = i;
    
        if(r <= N && dist[a[r]] < dist[a[smallest]]) smallest = r;
    
        if(smallest != i) {
            temp = a[i]; a[i] = a[smallest]; a[smallest] = temp;
            minHeapify(smallest);
        }
    }


    public void insert( int x) 
    {
        a[++N] = x;
        siftUp( N);
    }


    public int remove() 
    {   
        int v = a[1];
        hPos[v] = 0; // means v is no longer in heap
                
        a[1] = a[N--];
		a[N+1] = 0;  // put null node into empty spot just outside the heap
        siftDown(1);
        
        return v;
    }
    
    // display heap values and their priorities or distances
    void display() {
        System.out.println("\n\nThe tree structure of the heaps is:");
        System.out.println( a[1] + "(" + dist[a[1]] + ")" );
        for(int i = 1; i<= N/2; i = i * 2) {
            for(int j = 2*i; j < 4*i && j <= N; ++j)
                System.out.print( a[j] + "(" + dist[a[j]] + ")  ");
            System.out.println();
        }
    }

}



public class HeapLabTest {
    public static void main(String[] args) throws IOException
    {
        System.out.println("My name is Dylan Orourke and student ID C22341463\n");
        
		// dist[u] gives the distance of heap value u from somewhere
		// For example, in the following array which records the distance of 15 heap values
		// dist[1] = 100, dist[0] = 0 where 0 is a dummy node, dist[15] = 76
        int dist[] = {0, 100, 70, 120, 20, 60 , 50, 130, 90, 60, 11, 154, 43, 114, 52, 76};
        int i, u;
        double x;
        int heapPos[] = new int[16]; // for heap values 1 to 15
        
        Heap h = new Heap(15, dist, heapPos);  
        h.insert(1);
        for(i = 0; i < 10; ++i) {
            // pick a random integer between 1 and 15 and 
            // insert it into heap only if is not already there
            x = Math.random()*15.0;
            u = (int) x + 1;
            if(heapPos[u] == 0) {  // heapPos[u] = 0 mean u is not in  the heap
                System.out.println("Inserting " + u);
                h.insert(u);
            }
        }
        h.display();
        
        // increase the priority of heap value 1 and sift it up.
        dist[1] = 3; h.siftUp(heapPos[1]); h.display();
        
        h.remove(); h.display();
            
    }
    
    
}

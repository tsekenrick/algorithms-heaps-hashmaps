import java.io.*;
import java.util.*;

public class Solution {
    public static void main (String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 4096);
        int candidateCount = Integer.parseInt(br.readLine());
        
        //put all data into a hash map and Soldier array, then heapify the array
        Soldier[] soldiers = new Soldier[candidateCount];
        HashMap<String, Soldier> soldierHash = new HashMap(candidateCount);
        for(int i=0; i<candidateCount; i++){     
            String data = br.readLine();
            String[] dataSplit = data.split("\\s");
            Soldier curr = new Soldier(dataSplit[0], Long.parseLong(dataSplit[1]), i);
            soldiers[i] = curr;
            soldierHash.put(dataSplit[0], curr);
        }
        //heapify
        Heap soldierHeap = new Heap(soldiers);
        soldierHeap.makeHeap();
        
        //grab the operations from input
        int opCount = Integer.parseInt(br.readLine());
        String[] ops = new String[opCount];
        for(int i=0; i<opCount; i++){
            ops[i] = br.readLine();
        }
        
        //iterate over operations and perform them
        for(int i=0; i<ops.length; i++){
            String[] op = ops[i].split("\\s");
            switch(Integer.parseInt(op[0])){
            
            case 1:
                //add score to the soldier as obtained from hashmap
                Soldier improvedSol = soldierHash.get(op[1]);
                improvedSol.score += Long.parseLong(op[2]);
                //update soldier pos in heap
                soldierHeap.heapify(improvedSol.pos);
                break;
            case 2:
                //while score of soldier at root is < k, remove it from heap and hash
                while(soldierHeap.heap[0].score < Integer.parseInt(op[1])){
                    Soldier toRemove = soldierHeap.remove();
                    soldierHash.remove(toRemove.name);
                }
                output.write(soldierHeap.size + "\n");
                break;
                
            }
        }
        output.flush();
    }
    
}

//build by putting all items in and then heapify from bottom up
class Heap{
    Soldier[] heap;
    int size;
    int capacity;
    
    //standard constructor
    public Heap(int capacity){
        this.capacity = capacity;
        this.size = 0;
        this.heap = new Soldier[capacity];
    }
    
    //constructor that populates with starting Soldier array
    //this is the one we use
    public Heap(Soldier[] heap){
        this.capacity = heap.length;
        this.size = heap.length;
        this.heap = heap;
    }
    
    private int parent(int pos){
        return (pos-1)/2;
    }
 
    private int leftChild(int pos){
        return (2*pos) + 1;
    }
 
    private int rightChild(int pos){
        return (2*pos) + 2;
    }
 
    private boolean isLeaf(int pos){
        
        if (pos>=(size/2) && pos<=size){ 
            return true;
        }
        
        return false;
    }
    
    private void swap(int pos1, int pos2){
        Soldier tmp = this.heap[pos1];
        this.heap[pos1].pos = pos2;
        this.heap[pos2].pos = pos1;
        this.heap[pos1] = this.heap[pos2];
        this.heap[pos2] = tmp;
    }
    
    public void heapify(int pos){
        //leaves are left alone
        if(!isLeaf(pos)){
            //if either children is bigger, do something
            if((leftChild(pos) < this.size && this.heap[pos].score > this.heap[leftChild(pos)].score)  
                    || (rightChild(pos) < this.size && this.heap[pos].score > this.heap[rightChild(pos)].score)){
                
                //swap with the lesser of the two children then call heapify again on its new position
                if(rightChild(pos) >= this.size || this.heap[leftChild(pos)].score < this.heap[rightChild(pos)].score){
                    swap(pos, leftChild(pos));
                    heapify(leftChild(pos));
                }
                
                else{
                    swap(pos, rightChild(pos));
                    heapify(rightChild(pos));
                }
            }
        }
    }
    
    public Soldier remove(){
        Soldier toRem = this.heap[0];
        this.heap[0] = this.heap[size-1];
        size--;
        this.heapify(0);
        return toRem;
    }
    
    //repeatedly call heapify starting at rightmost node above the
    //leaf level, working up to the root
    public void makeHeap(){
        for (int pos = (size / 2); pos >= 0 ; pos--)
        {
            heapify(pos);
        }
    }
    
}

class Soldier{
    String name;
    long score;
    int pos;
    
    Soldier(String name, long score, int pos){
        this.name = name;
        this.score = score;
        this.pos = pos;
    }
}

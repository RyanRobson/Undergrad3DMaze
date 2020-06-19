
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class DungeonGenerator {
	 
	    public static void main(String[]args) throws IOException
	    {
	    	BufferedWriter writer;
	    	File file = new File("curMaze.txt");
            file.createNewFile();

	        
	        writer = new BufferedWriter(new FileWriter(file));
	    	// dimensions of generated maze
	    	
	    	int r =  30;//randInt(20,40);
	    	int c =  30;//randInt(20,40);
	    	
	    	
	    	// build maze and initialize with only walls
	        StringBuilder s = new StringBuilder(c);
	        for(int x=0;x<c;x++)
	        	s.append('*');
	        char[][] maz = new char[r][c];
	        for(int x=0;x<r;x++) maz[x] = s.toString().toCharArray();
	        
	       
	 
	        // select random point and open as start node
	        Point st = new Point((int) (Math.random()*r),(int)(Math.random()*c),null);
	        maz[ st.r][st.c] = 'S';
	        
	        // add between 1 and 10 treasure to maze
	        int trRand = randInt(1,10);
	        for (int i=0; i<trRand; i++){
	        Point tr = new Point ((int )( Math.random()*r),(int)(Math.random()*c),null);
	        maz[tr.r][tr.c] = 'T';
	        }
	        
	        // add between and 7 enemies 
	        int enRand = randInt(0,7);
	        for (int i=0; i<enRand; i++){
	        Point en = new Point ((int )( Math.random()*r),(int)(Math.random()*c),null);
	        maz[en.r][en.c] = 'M';
	        }
	        // iterate through direct neighbors of node
	        ArrayList<Point> frontier = new ArrayList<Point>();
	        for(int x=-1;x<=1;x++)
	        	for(int y=-1;y<=1;y++){
	        		if(x==0&&y==0||x!=0&&y!=0)
	        			continue;
	        		try{
	        			if(maz[st.r+x][st.c+y]=='.') continue;
	        		}catch(Exception e){ // ignore ArrayIndexOutOfBounds
	        			continue;
	        		}
	        		// add eligible points to frontier
	        		frontier.add(new Point(st.r+x,st.c+y,st));
	        	}
	 
	        Point last=null;
	        while(!frontier.isEmpty()){
	 
	        	// pick current node at random
	        	Point cu = frontier.remove((int)(Math.random()*frontier.size()));
	        	Point op = cu.opposite();
	        	try{
	        		// if both node and its opposite are walls
	        		if(maz[cu.r][cu.c]=='*'){
	        			if(maz[op.r][op.c]=='*'){
	 
	        				// open path between the nodes
	        				maz[cu.r][cu.c]='.';
	        				maz[op.r][op.c]='.';
	 
	        				// store last node in order to mark it later
	        				last = op;
	 
	        				// iterate through direct neighbors of node, same as earlier
	        				for(int x=-1;x<=1;x++)
					        	for(int y=-1;y<=1;y++){
					        		if(x==0&&y==0||x!=0&&y!=0)
					        			continue;
					        		try{
					        			if(maz[op.r+x][op.c+y]=='.') continue;
					        		}catch(Exception e){
					        			continue;
					        		}
					        		frontier.add(new Point(op.r+x,op.c+y,op));
					        	}
	        			}
	        		}
	        	}catch(Exception e){ // ignore NullPointer and ArrayIndexOutOfBounds
	        	}
	          	
		     
	 
	        	// if algorithm has resolved, mark end node
	        	if(frontier.isEmpty())
	        		maz[last.r][last.c]='E';
	        }
	        for (int i=0; i<r;i++){
	        	//System.out.println("r: "+ r);
	        	//System.out.println("i: "+ i);
	        	maz[i][0] = '*';
	        	maz[0][i] = '*';
	        	
	        }
	        for (int i=0; i<c; i++){
	        	maz[c-1][i] = '*';
	        	maz[i][c-1] = '*';
	        	
	        }
	        	try
	            {
	            for(int i=0;i<r;i++){
					for(int j=0;j<c;j++){
						writer.write(maz[i][j]);
						}
					writer.newLine();	
				 }
	            		writer.close();
	            }
	            catch(FileNotFoundException e)
	            {
	                System.out.println("File Not Found");
	                System.exit( 1 );
	            }
	            catch(IOException e)
	            {
	                System.out.println("Error");
	                System.exit( 1 );
	            }
	      
	        }

	    static class Point{
	    	// refactor - veery clunky 
	    	Integer r;
	    	Integer c;
	    	Point parent;
	    	public Point(int x, int y, Point p){
	    		r=x;c=y;parent=p;
	    	}
	    	// compute opposite node given that it is in the other direction from the parent
	    	public Point opposite(){
	    		if(this.r.compareTo(parent.r)!=0)
	    			return new Point(this.r+this.r.compareTo(parent.r),this.c,this);
	    		if(this.c.compareTo(parent.c)!=0)
	    			return new Point(this.r,this.c+this.c.compareTo(parent.c),this);
	    		return null;
	    	}
	    }
	    
	    
	    public static int randInt(int min, int max) {
	    	// generates a random int between 2 min and max.
	        Random rand = new Random();
	        int randomNum = rand.nextInt((max - min) + 1) + min;
	        return randomNum;
	    }
	}
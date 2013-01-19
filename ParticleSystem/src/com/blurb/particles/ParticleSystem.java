package com.blurb.particles;

import java.util.ArrayList;


/**
 *
 * @author Joshua Waring
 */


public class ParticleSystem {
    private class worker{
	   /**
	       Interthread communication key
		   1: Update followed by 128 bits 2x int 2x float
		  
	   
	   */
	  public PipedInputStream input = new PipedInputStream();
	  public PipedOutputStream output = new PipedOutputStream();
	  public Runnable thread;
	 
	  worker(){
		// parse the streams to the thread and create tnhe thread in constructor
		
		thread = new WorkerThreads( 
		           new PipedInputStream(output),
		   		   new PipedOutputStream(input));
		new Thread(thread).start();		 	
      }
	  public void update(){
		 output.write(2);
		 
	  }
	  public void addParticle(int x, int y, float xv, float yv){
		 output.write(1);
		 output.write(x);
		 output.write(y);
		 output.write(xv);
		 output.write(yv);
	  }
	  public void check(){
		 switch(input.read()){
			case null:
			    output.write(3);
			    break;
		    case 3:
			    input.flush();
				break;
		 }
	  }
	}

    private int workerThreads = 1;
    private ArrayList<ArrayList<Particle>> particles = new ArrayList<>();
    
    ParticleSystem(int workerThreads) {
        this.workerThreads = workerThreads;
        for (int x = 0; x < workerThreads; x++) {
            particles.add(new ArrayList<Particle>());
        }
    }
    ParticleSystem() {
	   // initilizes the threads 
        for (int x = 0; x < workerThreads; x++) {
            particles.add(new ArrayList<Particle>());
        }
    }
    public void update(){
        
    }
}

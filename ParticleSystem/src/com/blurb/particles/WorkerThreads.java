/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blurb.particles;

/**
 *
 * @author Maddison
 */
public class WorkerThreads implements Runnable{
	private PipedOutputStream output;
	private PipedInputStream input;
	private ArrayList<Particle> particles = new ArrayList<>();
	
	public WorkerThreads(PipedInputStream input, PipedOutputStream output){
	   this.output = output;
	   this.input = input;
	}
	
	@Override
    public void run(){
        // check input stream
		switch(input.read()){
		   // request stream flush
		   case null:
		       break;
		   // requested stream flush
		   case 3: 
		       output.flush();
		       break;
		   // requested Particles update
		   case 2:
		       break;
		   // Add Particles
		   case 1:
		       
		       break;
		}
    }
}

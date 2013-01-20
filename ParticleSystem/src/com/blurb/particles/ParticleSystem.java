package com.blurb.particles;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream; 
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Joshua Waring
 */
public class ParticleSystem {

    private class Worker {

        /**
         * Inter thread communication key 1: Update followed by 128 bits 2x int
         * 2x float
         *
         *
         */
        private PipedInputStream input = new PipedInputStream();
        private PipedOutputStream output = new PipedOutputStream();
        private Runnable thread;
        public boolean ready = false;
        private ByteConversion converter = new ByteConversion();
        Worker() {
            // parse the streams to the thread and create tnhe thread in constructor
            try {
                thread = new WorkerThreads(
                        new PipedInputStream(output),
                        new PipedOutputStream(input));
                new Thread(thread).start(); 
            } catch (IOException e) {
                
                System.err.println("Error Adding Particle: " + e.getMessage());
            }
        }

        public void update() {
            try {
                ready = false;
                output.write(2);
            } catch (IOException e) {
                
                System.err.println("Error Adding Particle: " + e.getMessage());
            }
        }

        public void addParticle(int x, int y, float xv, float yv) {
            try{
                output.write(1);
                output.write(converter.IntToByte(x));
                output.write(converter.IntToByte(y));
                output.write(converter.FloatToByte(xv));
                output.write(converter.FloatToByte(yv));
            } catch (IOException e) { 
                System.err.println("Error Adding Particle: " + e.getMessage());
            }
        }
       public void requestRender(){
           try {
               ready = false;
               output.write(3);
            } catch (IOException e) {
                
                System.err.println("Error Adding Particle: " + e.getMessage());
            }
       }

        public void check() {
            try {
                switch (input.read()) {
                    case 5:
                        ready = true;
                        break;
                } 
            } catch (IOException e) {
                
                System.err.println("Error Adding Particle: " + e.getMessage());
            } 
        }
    }
    private int workerThreads = 1;
    ArrayList<Worker> workers = new ArrayList<>();
    public ParticleSystem(int workerThreads, GL11 context) {
        this.workerThreads = workerThreads;
        for (int x = 0; x < workerThreads; x++) {
            workers.add(new Worker());
        }
    }

    public ParticleSystem() {
        // initilizes the threads 
        for (int x = 0; x < workerThreads; x++) {
            workers.add(new Worker());
        }
    }

    public void update() {
        for (int x = 0; x < workerThreads; x++) {
            workers.get(x).update();
        }
    }
    public void render(){
        for (int x = 0; x < workerThreads; x++) {
            workers.get(x).requestRender();
        }
    }
    int index = 0;
    public void addParticle(int x, int y){
       if(index > workerThreads){
           index = 0;
       }
       workers.get(index).addParticle(x, y, 0, 0);
    }
    public boolean isReady(){
        for(int x = 0; x < workerThreads; x++){
            workers.get(x).check();
            if(!workers.get(x).ready){
                return false;
            }
        }
        return true;
    }
}

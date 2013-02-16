package com.blurb.particles;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream; 
import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Joshua Waring
 */
public class ParticleSystem {

    private class Worker {
        private PipedInputStream input = new PipedInputStream();
        private PipedOutputStream output = new PipedOutputStream();
        private Runnable thread;
        
        public FloatBuffer vBuffer = BufferUtils.createFloatBuffer(200000);
        ArrayList<Particle> particles = new ArrayList<>();
        Particle cacheParticle;
        
        byte[] tempByte0 = new byte[4];
        byte[] tempByte1 = new byte[4];
        byte[] tempByte2 = new byte[4];
        byte[] tempByte3 = new byte[4];
        
        private ByteConversion converter = new ByteConversion();
        
        Worker(float gravityX,float gravityY) {
            // parse the streams to the thread and create tnhe thread in constructor
            try {
                thread = new WorkerThreads(
                        new PipedInputStream(output),
                        new PipedOutputStream(input),
						gravityX,
						gravityY ); 		
                new Thread(thread).start(); 
            } catch (IOException e) {
                System.err.println("Error Creating Thread: " + e.getMessage());
            }
        }

        public void update() {
            try {
                for(int index = 0; index<particles.size();index++){
				   cacheParticle = particles.get(index);
				   output.write(converter.IntToByte(cacheParticle.x));
				   output.write(converter.IntToByte(cacheParticle.y)); 
				   output.write(converter.FloatToByte(cacheParticle.xv));
				   output.write(converter.FloatToByte(cacheParticle.yv));
				}
				for(int index = 0; index<particles.size();index++){
				   cacheParticle = particles.get(index);
				   
				   input.read(tempByte0);
				   input.read(tempByte1);
				   input.read(tempByte2);
				   input.read(tempByte3);
				   
				   cacheParticle.x = converter.ByteToInt(tempByte0);
				   cacheParticle.y = converter.ByteToInt(tempByte1);
				   cacheParticle.xv = converter.ByteToFloat(tempByte2);
				   cacheParticle.yv = converter.ByteToFloat(tempByte3);
				   
				   vBuffer.put(cacheParticle.x);
				   vBuffer.put(cacheParticle.y);
				}
				vBuffer.flip();
				
				glEnableClientState(GL_VERTEX_ARRAY);
				glVertexPointer(2, 3 << 2, vBuffer);
				glDrawArrays(GL_POINTS, 0, 2);
				glDisableClientState(GL_VERTEX_ARRAY);
				
            } catch (IOException e) {
                System.err.println("Error Updating Particles: " + e.getMessage());
            }
        }

        public void addParticle(int x, int y, float xv, float yv) {
             
                particles.add(new Particle(x,y,xv,yv)); 
        }
    }
	
    private int workerThreads = 1;
    ArrayList<Worker> workers = new ArrayList<>();
	
    public ParticleSystem(int workerThreads ) {
        this.workerThreads = workerThreads;
        for (int x = 0; x < workerThreads; x++) {
            // worker takes point of gravity to no affect
            workers.add(new Worker( 1, 1));
        }
    }

    public ParticleSystem() {
        // initilizes the threads 
        for (int x = 0; x < workerThreads; x++) {
            // worker takes point of gravity
            workers.add(new Worker(1, 1));
        }
    }

    public void update() {
        for (int x = 0; x < workerThreads; x++) {
            workers.get(x).update();
        }
    }
	
    int index = 0;

    public void addParticle(int x, int y) {
        
        workers.get(index).addParticle(x, y, 0, 0);
        if (index > workerThreads) {
            index = 0;
        } else {
            index++;
        }
    }
}

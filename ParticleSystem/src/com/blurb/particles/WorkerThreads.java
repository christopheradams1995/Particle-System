/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blurb.particles;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11.*;
/**
 *
 * @author Maddison
 */
public class WorkerThreads implements Runnable {

    private PipedOutputStream output;
    private PipedInputStream input;
    private ArrayList<Particle> particles = new ArrayList<>();
    private ByteConversion converter = new ByteConversion();
    private IntBuffer returningCords;
    public WorkerThreads(PipedInputStream input, PipedOutputStream output) {
        this.output = output;
        this.input = input;
    }
    
    byte[] tempByte0 = new byte[4];
    byte[] tempByte1 = new byte[4];
    byte[] tempByte2 = new byte[4];
    byte[] tempByte3 = new byte[4];
    boolean running = true;
    Particle cacheParticle;
    @Override
    
    /*
     * Thread Communication Key
     * 1- Add Particle
     * 2- Update Particle
     * 3- Render Particle
     * 4- Placement of Gravity   
     * 5- Return ready Status
     * 6-
     * 7- Wait for n seconds
     * 8- Terminate Thread
     * 9-
     * 10-
     */
    public void run() {
        while(running){
            // check input stream
            try {
                switch (input.read()) {
                    // requested Particles update
                    case 1:
                        // Add Particles 
                        input.read(tempByte0);
                        input.read(tempByte1);
                        input.read(tempByte2);
                        input.read(tempByte3);
                        particles.add(new Particle(
                                converter.ByteToInt(tempByte0),
                                converter.ByteToInt(tempByte1),
                                converter.ByteToInt(tempByte2),
                                converter.ByteToInt(tempByte3)));
                        break;
                    case 2:
                        // Update Particles
                        // to-do: Particle Culling 
                        returningCords = IntBuffer.allocate(particles.size() * 2);
                        for(int x = 0; x < particles.size(); x++){
                            cacheParticle = particles.get(x);

                            // Update Gravity
                            cacheParticle.yv -= 0.1f;
                            // update Position
                            cacheParticle.x += cacheParticle.xv; 
                            cacheParticle.y += cacheParticle.yv;
                            returningCords.put(cacheParticle.x);
                            returningCords.put(cacheParticle.y);
                        } 
                        returningCords.flip();
                        output.write(5);
                        break;
                    case 3:
                        // as it's a state machine this may cause a problem
                        GL11.glBegin(GL11.GL_POINTS);
                            while(returningCords.hasRemaining()){
                                GL11.glVertex2i(returningCords.get(), returningCords.get());
                            }
                        GL11.glEnd();
                        output.write(5);
                         
                    case 7:
                        input.read(tempByte0);
                        Thread.sleep(converter.ByteToInt(tempByte0));
                        
                    case 8:
                        running = false;
                        break;
                    
                }
            } catch (IOException e) {
                
                System.err.println("Error Adding Particle: " + e.getMessage());
            } catch(InterruptedException e){
                
                System.err.println("Error Adding Particle: " + e.getMessage());
            }
        }
    }
}

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
    private ByteConversion converter = new ByteConversion();
    public WorkerThreads(PipedInputStream input, PipedOutputStream output, float gx, float gy) {
        this.output = output;
        this.input = input;
    }
    byte Byte;
    byte[] tempByte0 = new byte[4];
    byte[] tempByte1 = new byte[4];
    byte[] tempByte2 = new byte[4];
    byte[] tempByte3 = new byte[4];
	byte[] tempByte4 = new byte[3];
    boolean running = true;
	
	int x;
	int y;
	float xv;
	float yv;
	float gravX;
	float gravY;
	
    Particle cacheParticle;
    @Override
	private byte[] push(byte[] x, byte y){}
	   byte[] Byte = new byte[4];
	   Byte[0] = y;
	   Byte[1] = x[0];
       Byte[2] = x[1];
       Byte[3] = x[2];
	   return Byte;
	}
    public void run() {
        while(running){
            // check input stream
            try {
			   if( (int) (Byte = input.read()) != -1){
				  input.read(tempByte4);
				  input.read(tempByte1);
				  input.read(tempByte2);
				  input.read(tempByte3);
				  tempByte0 = push(tempByte4, Byte);
				  
				  x = converter.ByteToInt(tempByte0);
				  y = converter.ByteToInt(tempByte1);
				  xv = converter.ByteToFloat(tempByte2);
				  yv = converter.ByteToFloat(tempByte3);
				  
				  // update gravity here
				  yv += 0.1f;
				  
				  x += xv;
				  y += yv;
				  
				  // output
				  
				  output.write(x);
				  output.write(y);
				  output.write(xv);
				  output.write(yv);
			   }else{
			   }
                
            } catch (IOException e) {              
                System.err.println("Error Reading Pipe: " + e.getMessage());
            } catch(InterruptedException e){
                System.err.println("Error Interrupted Thread: " + e.getMessage());
            }
        }
    }
}

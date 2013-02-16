/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blurb.particles;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;   
/**
 *
 * @author Joshua Waring
 */
public class WorkerThreads implements Runnable {

    private PipedOutputStream output;
    private PipedInputStream input;
    private ByteConversion converter = new ByteConversion();
    private float gx;
    private float gy;
    
    public WorkerThreads(PipedInputStream input, PipedOutputStream output, float gx, float gy) {
        this.output = output;
        this.input = input;
        this.gx = gx;
        this.gy = gy;
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

    private byte[] push(byte[] x, byte y) {
        byte[] Byte = new byte[4];
        Byte[0] = y;
        Byte[1] = x[0];
        Byte[2] = x[1];
        Byte[3] = x[2];
        return Byte;
    }

    @Override
    public void run() {
        while(running){
            // check input stream
            try {
			   if(input.available() >= 4){
                                  System.out.println(input.available());
				  input.read(tempByte0); 
                                  System.out.println(input.available());
				  input.read(tempByte1);
                                  System.out.println(input.available());
				  input.read(tempByte2);
                                  System.out.println(input.available());
				  input.read(tempByte3);
                                  System.out.println(input.available()); 
				  
				  x = converter.ByteToInt(tempByte0);
				  y = converter.ByteToInt(tempByte1);
				  xv = converter.ByteToFloat(tempByte2);
				  yv = converter.ByteToFloat(tempByte3);
				  
				  // update gravity here
				  yv += 0.1f;
                                 // float angle2d = (float) (Math.atan2(x - gx, y - gy) / Math.PI * 180);
                                 // xv += -(Math.sin(angle2d * Math.PI / 180) / 30) ;
                                 // yv += -(Math.cos(angle2d * Math.PI / 180) / 30) ;

				  x += xv;
				  y += yv;
				  
				  // output
				  
				  output.write(x);
				  output.write(y);
				  output.write(converter.FloatToByte(xv));
				  output.write(converter.FloatToByte(yv));
			   }else{
			   } 
            } catch (IOException e) {              
                System.err.println("Error Reading Pipe: " + e.getMessage());
            }  //catch(InterruptedException e){
              //  System.err.println("Error Interrupted Thread: " + e.getMessage());
             //}
        }
    }
}

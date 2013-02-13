/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blurb.particles;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
/**
 *
 * @author josh
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            DisplayMode dm = new DisplayMode(800, 800);
            Display.setDisplayMode(dm);
            Display.create();

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, 800, 0, 800, -100, 100);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            
            ParticleSystem system = new ParticleSystem();
            
            while(!Display.isCloseRequested()){
                
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                
                if(Mouse.isButtonDown(0)){
                 system.addParticle(Mouse.getX(), Mouse.getY());
                }
                
                system.update();
                Display.update();  
            }

            
        }catch(LWJGLException e){
            
        }
    }
}

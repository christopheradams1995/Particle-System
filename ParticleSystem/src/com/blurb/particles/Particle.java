/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blurb.particles;

/**
 *
 * @author Maddison
 */
public class Particle {
    public int x;
    public int y;
    public float xv;
    public float yv;
    Particle(int x, int y, float xv, float yv){
        this.x = x;
        this.y = y;
        this.xv = xv;
        this.yv = yv;
    }
}

package com.blurb.particles;

import java.util.ArrayList;


/**
 *
 * @author Joshua Waring
 */


public class ParticleSystem {

    private int workerThreads = 1;
    private ArrayList<ArrayList<Particle>> particles = new ArrayList<>();

    ParticleSystem(int workerThreads) {
        this.workerThreads = workerThreads;
        for (int x = 0; x < workerThreads; x++) {
            particles.add(new ArrayList<Particle>());
        }
    }
    ParticleSystem() {
        for (int x = 0; x < workerThreads; x++) {
            particles.add(new ArrayList<Particle>());
        }
    }
    public void update(){
        
    }
}

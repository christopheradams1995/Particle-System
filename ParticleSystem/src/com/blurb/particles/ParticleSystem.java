package com.blurb.particles;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Joshua Waring
 */


public class ParticleSystem
{
   private int workerThreads = 1;
   private ArrayList<Particle>[] particles;

   ParticleSystem(int workerThreads)
   {
	  this.workerThreads = workerThreads;
	  particles = new ArrayList<>()[workerThreads];

   }
   ParticleSystem()
   {
	  particles = new ArrayList<>()[workerThreads];
   }
}

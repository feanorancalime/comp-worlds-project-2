package particles.behaviors;

import forcefield.ParticleInterface;
import particles.Particle;
import particles.ParticleSystem;
import particles.ParticleSystemInterface;

/**
 * Defines behaviors for particles
 */
public interface ParticleBehavior {
    void behave(ParticleSystemInterface particleSystem, ParticleInterface particle);
    
    void updateBehaviorValue(float percentage);
}

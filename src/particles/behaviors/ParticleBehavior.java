package particles.behaviors;

import particles.Particle;
import particles.ParticleSystem;

/**
 * Defines behaviors for particles
 */
public interface ParticleBehavior {
    void behave(ParticleSystem particleSystem, Particle particle);
    
    void updateBehaviorValue(float percentage);
}

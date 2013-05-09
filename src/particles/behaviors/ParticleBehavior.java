package particles.behaviors;

import particles.ParticleInterface;
import particles.ParticleSystemInterface;

/**
 * Defines behaviors for particles
 */
public interface ParticleBehavior {
    void behave(ParticleSystemInterface particleSystem, ParticleInterface particle);
    void update(float dt);

    String getName();
}

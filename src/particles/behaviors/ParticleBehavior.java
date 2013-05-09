package particles.behaviors;

import particles.ParticleInterface;
import particles.ParticleSystemInterface;

/**
 * Defines behaviors for particles
 */
public interface ParticleBehavior {
    /**Alter the given particle in the given particle system.**/
    void behave(ParticleSystemInterface particleSystem, ParticleInterface particle);

    /**Update this behavior
     *
     * @param dt Time since the last update (seconds)
     */
    void update(float dt);

    /**What is the name of this behavior?**/
    String getName();
}

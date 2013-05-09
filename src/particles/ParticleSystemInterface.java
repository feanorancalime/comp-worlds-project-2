package particles;

import particles.behaviors.ParticleBehavior;

/**
 * Abstracts the ParticleSystem
 */
public interface ParticleSystemInterface {
    /**Adds an arbitrary particle behavior**/
    void addBehavior(ParticleBehavior particleBehavior);

    /**Removes an arbitrary particle behavior**/
    void removeBehavior(ParticleBehavior particleBehavior);

    /**Adds a particle behavior to the force section (before update / collision)**/
    void addParticleForceBehavior(ParticleBehavior particleBehavior);

    /**Add a particle behavior to the collision section (after update / forces)**/
    void addParticleCollisionBehavior(ParticleBehavior particleBehavior);

    /**Update the system**/
    void update(double dt);
}

package particles;

import particles.behaviors.ParticleBehavior;

/**
 * Created with IntelliJ IDEA.
 * User: mojito
 * Date: 5/8/13
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ParticleSystemInterface {
    void addBehavior(ParticleBehavior particleBehavior);

    void removeBehavior(ParticleBehavior particleBehavior);
}

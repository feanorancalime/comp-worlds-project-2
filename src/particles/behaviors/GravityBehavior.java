package particles.behaviors;

import particles.Particle;
import particles.ParticleSystem;

/**
 *
 */
public class GravityBehavior implements ParticleBehavior {
    private final double G;
    public GravityBehavior(final double gravitationalConstant) {
        G = gravitationalConstant;
    }

    @Override
    public void behave(ParticleSystem particleSystem, Particle particle) {
        particle.forceAccumulator.setY((float)(-G * particle.mass));
    }
}

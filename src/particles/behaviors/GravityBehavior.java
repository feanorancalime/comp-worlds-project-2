package particles.behaviors;

import particles.Particle;
import particles.ParticleSystem;

/**
 * Gravity behavior for a particle system.
 */
public class GravityBehavior implements ParticleBehavior {
	
    private double G;
    
    public GravityBehavior(final double gravitationalConstant) {
        G = gravitationalConstant;
    }

    @Override
    public void behave(ParticleSystem particleSystem, Particle particle) {
        particle.forceAccumulator.setY((float)(-G * particle.mass));
    }

	@Override
	public void updateBehaviorValue(float percentageChange) {
		G = G * percentageChange;
	}
}

package particles.behaviors;

import forcefield.ParticleInterface;
import particles.Particle;
import particles.ParticleSystem;
import particles.ParticleSystemInterface;

/**
 * Gravity behavior for a particle system.
 */
public class GravityBehavior implements ParticleBehavior {
	
    private double G;
    
    public GravityBehavior(final double gravitationalConstant) {
        G = gravitationalConstant;
    }

    @Override
    public void behave(ParticleSystemInterface particleSystem, ParticleInterface particle) {
        particle.getForceAccumulator().setY((float)(-G * particle.getMass()));
    }

    @Override
	public void updateBehaviorValue(float percentageChange) {
		G = G * percentageChange;
	}
}

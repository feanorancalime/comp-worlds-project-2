package particles.behaviors;

import forcefield.ParticleInterface;
import particles.Particle;
import particles.ParticleSystem;
import particles.ParticleSystemInterface;

/**
 * Gravity behavior for a particle system.
 */
public class GravityBehavior implements ForceBehavior {
	
    private float G;
    
    public GravityBehavior(final float gravitationalConstant) {
        G = gravitationalConstant;
    }

    @Override
    public void behave(ParticleSystemInterface particleSystem, ParticleInterface particle) {
        particle.getForceAccumulator().setY((float) (-G * particle.getMass()));
    }

    @Override
	public void updateBehaviorValue(float percentageChange) {
		G = G * percentageChange;
	}

    @Override
    public String getName() {
        return "Gravity";
    }

    @Override
    public void setForceMagnitude(float newtons) {
        G = newtons;
    }

    @Override
    public float getForceMagnitude() {
        return G;
    }
}

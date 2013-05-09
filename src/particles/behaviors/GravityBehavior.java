package particles.behaviors;

import forcefield.ParticleInterface;
import particles.Particle;
import particles.ParticleSystem;
import particles.ParticleSystemInterface;

/**
 * Gravity behavior for a particle system.
 */
public class GravityBehavior implements ForceBehavior {
	private static final float MIN_VALUE = 0;
    private static final float MAX_VALUE = 100;
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

    @Override
    public float getForceMaximum() {
        return MAX_VALUE;
    }

    @Override
    public float getForceMinimum() {
        return MIN_VALUE;
    }
}

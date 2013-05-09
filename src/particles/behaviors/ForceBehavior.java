package particles.behaviors;

/**
 * Represents a behavior that performs only Forces.
 *
 * These behaviors go before collision behaviors.
 */
public interface ForceBehavior extends ParticleBehavior {
    /**Set the magnitude of the force (newtons)**/
    void setForceMagnitude(float newtons);
    /**Current magnitude of force (newtons)**/
    float getForceMagnitude();
    /**Maximum magnitude of force (newtons)**/
    float getForceMaximum();
    /**Minimum magnitude of force (newtons)**/
    float getForceMinimum();
}

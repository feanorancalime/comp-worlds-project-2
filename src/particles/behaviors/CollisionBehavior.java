package particles.behaviors;

/**
 *Represents a behavior that performs only Collision.
 *
 * These behaviors go after force behaviors.
 */
public interface CollisionBehavior extends ParticleBehavior {
    /**Set the coefficient of restitution.**/
    void setCoefficientOfRestitution(float coefficientOfRestitution);

    /**Get the current coefficient of restitution.**/
    float getCoefficientOfRestitution();
}

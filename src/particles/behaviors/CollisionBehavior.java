package particles.behaviors;

/**
 * Created with IntelliJ IDEA.
 * User: Lemtzas
 * Date: 5/8/13
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CollisionBehavior extends ParticleBehavior {
    void setCoefficientOfRestitution(float coefficientOfRestitution);
    float getCoefficientOfRestitution();
}

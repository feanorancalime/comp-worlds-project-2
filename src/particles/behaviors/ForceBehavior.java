package particles.behaviors;

/**
 * Created with IntelliJ IDEA.
 * User: Lemtzas
 * Date: 5/8/13
 * Time: 4:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ForceBehavior extends ParticleBehavior {
    void setForceMagnitude(float newtons);
    float getForceMagnitude();
}

package particles;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Interface that abstracts the primary aspects of particles.
 */
public interface ParticleInterface {

    /**This updates the position and orientation of a particle's display**/
    void updateTransformGroup();

    /**Get this particle's position**/
    Point3f getPosition();

    /**This update's the particle's position and physical attributes**/
    void update(float dt);

    /**This particle's velocity**/
    Vector3f getVelocity();
    /**This particle's current forces**/
    Vector3f getForceAccumulator();
    /**This particle's mass (kg?)**/
    double getMass();
    /**Set this particle's mass(kg?)**/
    void setMass(double mass);

    float getArea();
}

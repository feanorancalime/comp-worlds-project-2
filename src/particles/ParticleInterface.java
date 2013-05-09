package particles;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Interface that abstracts the primary aspects of particles.
 */
public interface ParticleInterface {

    /**This updates the position and orientation of a particle's display**/
    void updateTransformGroup();

    Point3f getPosition();

    /**This update's the particle's position and physical attributes**/
    void update(float dt);


    Vector3f getVelocity();
    Vector3f getForceAccumulator();

    double getMass();
    void setMass(double mass);

    float getArea();
}

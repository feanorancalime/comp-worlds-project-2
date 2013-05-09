package forcefield;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Created with IntelliJ IDEA.
 * User: mojito
 * Date: 5/8/13
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ParticleInterface {


    void updateTransformGroup();

    Point3f getPosition();

    void update(float dt);

    Vector3f getVelocity();
    Vector3f getForceAccumulator();

    double getMass();
    void setMass(double mass);

    float getArea();
}

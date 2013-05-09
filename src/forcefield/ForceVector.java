package forcefield;

import particles.ParticleInterface;

import javax.media.j3d.*;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * A particle. Meant to live within the ParticleSystem class.
 */
public class ForceVector extends BranchGroup implements ParticleInterface {
    private static final float DEFAULT_MASS = 10;

    //these are all required for rendering
    public final Point3f position;
    public final VectorField vectorField;

    //these are for the simulation
    public Vector3f velocity;
    public double mass;

    //workspace variables
    private Vector3f v3f;
    public Vector3f forceAccumulator;
    private final int index;

    public ForceVector(final VectorField vectorField, final int index, final Point3f position) {
        this.index = index;
        this.vectorField = vectorField;
        this.position = position;
        this.velocity = new Vector3f();
        this.mass = DEFAULT_MASS;
        v3f = new Vector3f();


        forceAccumulator = new Vector3f();
        updateTransformGroup();

    }

    @Override
    public void updateTransformGroup() {
        //position.get(v3f);
        vectorField.setForce(index,forceAccumulator);
//        t3d.setTranslation(v3f);
//        tg.setTransform(t3d);
    }

    /**
     * @return the particle's current position
     */
    @Override
    public Point3f getPosition() {
        return position;
    }

    /**
     * update the particle
     * @param dt The time difference
     */
    @Override
    public void update(float dt) {
        // The force accumulator vector (net force) now becomes
        // the acceleration vector.
//        forceAccumulator.scale(1 / (float)mass);
//        position.scaleAdd(dt, velocity, position);
//        position.scaleAdd(dt*dt / 2, forceAccumulator, position);
//        velocity.scaleAdd(dt, forceAccumulator, velocity);
    }

    private final float[] points = new float[2*3];

    public void setForceVector(final Vector3f forceVector) {
        vectorField.setForce(index,forceVector);
    }

    @Override
    public Vector3f getVelocity() {
        return velocity;
    }

    @Override
    public Vector3f getForceAccumulator() {
        return forceAccumulator;
    }

    @Override
    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    @Override
    public float getArea() {
        return 0;
    }
}

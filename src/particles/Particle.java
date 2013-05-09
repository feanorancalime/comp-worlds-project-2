package particles;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.geometry.Sphere;
import forcefield.ParticleInterface;

import javax.media.j3d.*;
import javax.vecmath.Color4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.util.Random;

/**
 * A particle. Meant to live within the ParticleSystem class.
 */
public class Particle extends BranchGroup implements ParticleInterface {
    private static final float DEFAULT_MASS = 10;

    //these are all required for rendering
    public final Point3f position;
    public final Color4f color;
    public final ParticleSystem particleSystem;
    public final float radius;
    public final Node shape;

    //these are for the simulation
    public Vector3f velocity;
    public double mass;
    public TransformGroup tg;
    public Transform3D t3d;

    //workspace variables
    private Vector3f v3f;
    public Vector3f forceAccumulator;

    public Particle(final ParticleSystem particleSystem) {
        this(particleSystem,new Point3f(), new Color4f(), 10);
    }

    public Particle(final ParticleSystem particleSystem, final Point3f position, final Color4f color, final float radius) {
        this.particleSystem = particleSystem;
        this.position = position;
        this.color = color;
        this.velocity = new Vector3f();
        this.radius = radius;
        this.mass = DEFAULT_MASS;
        v3f = new Vector3f();


        forceAccumulator = new Vector3f();
        t3d = new Transform3D();
        tg = new TransformGroup();
        this.addChild(tg);
        shape = createShape(radius,color);
        tg.addChild(shape);
        updateTransformGroup();

        this.setCapability(BranchGroup.ALLOW_DETACH);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

    }

    public void updateTransformGroup() {
        position.get(v3f);
        t3d.setTranslation(v3f);
        tg.setTransform(t3d);
    }

    /**
     * @return the particle's current position
     */
    public Point3f getPosition() {
        return position;
    }

    /**
     * @return the particle's current color
     */
    public Color4f getColor() {
        return color;
    }

    /**
     * update the particle
     * @param dt The time difference
     */
    public void update(float dt) {
        // The force accumulator vector (net force) now becomes
        // the acceleration vector.
        forceAccumulator.scale(1 / (float)mass);
        position.scaleAdd(dt, velocity, position);
        position.scaleAdd(dt*dt / 2, forceAccumulator, position);
        velocity.scaleAdd(dt, forceAccumulator, velocity);
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

    private Node createShape(float radius, Color4f color) {
        if (color == null)
            color = new Color4f(Color.getHSBColor((float) Math.random(), 1, 1));

        Appearance appearance = new Appearance();

//        TransparencyAttributes transparencyAttributes = new TransparencyAttributes();
//        transparencyAttributes.setTransparency(color.getW());

        appearance.setColoringAttributes(new ColoringAttributes(color.getX(), color.getY(), color.getZ(), ColoringAttributes.FASTEST));
        return new Sphere(radius,0,3,appearance);
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    @Override
    public float getArea() {
        return (float)(radius*radius*Math.PI);
    }
}

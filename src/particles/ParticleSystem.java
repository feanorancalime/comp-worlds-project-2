package particles;

import particles.behaviors.ParticleBehavior;

import javax.media.j3d.*;
import javax.vecmath.Color4f;
import javax.vecmath.Point3f;
import java.util.Random;
import java.util.Set;

/**
 * Represents a system of particles with a universal size and set of behaviors.
 */
public class ParticleSystem extends Shape3D {
    private Particle[] particles;
    private float[] points_ref;
    private float[] colors_ref;
    private Set<ParticleBehavior> particleBehaviors;

    public ParticleSystem(final int numPoints) {
        setGeometry(createGeometry(numPoints));


        Appearance appearance = new Appearance();
        PointAttributes pointAttributes = new PointAttributes(5,true);
        appearance.setPointAttributes(pointAttributes);
        setAppearance(appearance);
    }

    private final Geometry createGeometry(final int numPoints) {
        PointArray pointArray = new PointArray(numPoints,
                PointArray.COORDINATES|PointArray.COLOR_3|PointArray.BY_REFERENCE);

        points_ref = new float[numPoints*3];
        colors_ref = new float[numPoints*4];
        particles = new Particle[numPoints];

        Random rand = new Random();


        Point3f point = new Point3f();
        Color4f color = new Color4f();
        for(int i = 0; i < numPoints; i++ ){
            color.set(rand.nextFloat(),rand.nextFloat(),rand.nextFloat(),1f);
            point.set(rand.nextFloat(),rand.nextFloat(),rand.nextFloat());
            particles[i] = new Particle(this,i,point,color);
            setPosition(i, point);
            setColor(i, color);
        }

        pointArray.setColorRefFloat(colors_ref);
        pointArray.setCoordRefFloat(points_ref);

        return pointArray;
    }

    public void addParticleBehavior(final ParticleBehavior particleBehavior) {
        particleBehaviors.add(particleBehavior);
    }

    public void update(final double dt) {
        //create one array to avoid unnecessary iterator instantiation (avoiding a foreach loop)
        //this could be further sped up by updating an array every time a behavior is added
        //    ...which presumes that behaviors aren't frequently added or removed realtime
        ParticleBehavior[] behaviors = particleBehaviors.toArray(new ParticleBehavior[particleBehaviors.size()]);


        for(int i = 0; i < particles.length; i++) {
            for(int j = 0; j < behaviors.length; j++) {
                behaviors[j].behave(this,particles[i]);
                updateParticle(i);
            }
        }
    }


    /**
     * Set the position of a point specified by the index
     * @param index the point's identifying index
     * @param point the point's new position
     */
    private final void setPosition(int index, Point3f point) {
        //update display
        points_ref[index*3   ] = point.getX();
        points_ref[index*3 +1] = point.getY();
        points_ref[index*3 +2] = point.getZ();
    }

    /**
     * Set the color of a point specified by the index
     * @param index the point's identifying index
     * @param color the point's new color
     */
    private final void setColor(int index, final Color4f color) {
        //update display
        colors_ref[index*4   ] = color.getX();
        colors_ref[index*4 +1] = color.getY();
        colors_ref[index*4 +2] = color.getZ();
        colors_ref[index*4 +3] = color.getW();
    }

    /**
     * Updates a points color and position
     * @param index the index of the point to check + modify
     */
    private final void updateParticle(int index) {
        Color4f color = particles[index].getColor();
        Point3f point = particles[index].getPosition();
        setColor(index,color);
        setPosition(index,point);
    }

}

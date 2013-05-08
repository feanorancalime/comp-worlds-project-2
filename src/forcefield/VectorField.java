package forcefield;

import particles.Particle;
import particles.behaviors.ParticleBehavior;

import javax.media.j3d.*;
import javax.vecmath.Color4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Represents a system of particleSet with a universal size and set of behaviors.
 */
public class VectorField extends Shape3D {
    private Set<Particle> particleSet;
    private Set<ParticleBehavior> particleForces;
    private Set<ParticleBehavior> particleCollisions;
    private static final float DEFAULT_RADIUS = 0.2f;

    public VectorField(final float halfWidth, final int divisions) {
        particleForces = new HashSet<ParticleBehavior>();
        particleCollisions = new HashSet<ParticleBehavior>();
        //particleSet = new HashSet<Particle>(numPoints);
        setGeometry(createGeometry(halfWidth,divisions));

        Appearance appearance = new Appearance();
        TransparencyAttributes ta = new TransparencyAttributes();
        ta.setTransparencyMode(TransparencyAttributes.BLENDED);
        appearance.setTransparencyAttributes(ta);
        setAppearance(appearance);
    }

    float positions[];
    private void setPosition(int index,float x, float y, float z) {
        positions[index*3   ] = x;
        positions[index*3 +1] = y;
        positions[index*3 +2] = z;
    }

    private Geometry createGeometry(float halfWidth, int divisions) {
        LineArray geometry = new LineArray(2*divisions*divisions*divisions,LineArray.COLOR_4|LineArray.BY_REFERENCE|LineArray.COORDINATES);

        positions = new float[2*3*divisions*divisions*divisions];
        float colors[] = new float[2*4*divisions*divisions*divisions];
        for(int i = 0; i < divisions*divisions*divisions; i++) {
            colors[i*8   ] = 1;
            colors[i*8 +1] = 1;
            colors[i*8 +2] = 1;
            colors[i*8 +3] = 1;

            colors[i*8 +4] = 0;
            colors[i*8 +5] = 0;
            colors[i*8 +6] = 0;
            colors[i*8 +7] = 0;
        }

//        for(int i = 0; i < divisions; i++ ){
//            for(int j=0; j < divisions; j++) {
//                for(int k=0; k < divisions; k++) {
//
//                }
//            }
//        }

        float x,y,z;
        x = 0; y = 0; z = 0;
        setPosition(0,x,y,z);
        ForceVector p = new ForceVector(this,0,new Point3f(x,y,z));
        x = 1; y = 1; z = 1;
        setPosition(1,x,y,z);


        geometry.setCoordRefFloat(positions);
        geometry.setColorRefFloat(colors);

        return geometry;  //To change body of created methods use File | Settings | File Templates.
    }

    public void addParticleForceBehavior(final ParticleBehavior particleBehavior) {
        particleForces.add(particleBehavior);
    }

    public void addParticleCollisionBehavior(final ParticleBehavior particleBehavior) {
        particleCollisions.add(particleBehavior);
    }

//    public void update(final double dt) {
//        //create one array to avoid unnecessary iterator instantiation (avoiding a foreach loop)
//        //this could be further sped up by updating an array every time a behavior is added
//        //    ...which presumes that behaviors aren't frequently added or removed realtime
//        ParticleBehavior[] forces = particleForces.toArray(new ParticleBehavior[particleForces.size()]);
//        ParticleBehavior[] collisions = particleCollisions.toArray(new ParticleBehavior[particleCollisions.size()]);
//        Particle[] particles = particleSet.toArray(new Particle[particleSet.size()]);
//
//        for(int i = 0; i < particles.length; i++) {
//            //accumulate forces
//            for(int j = 0; j < forces.length; j++) {
//                forces[j].behave(this, particles[i]);
//            }
//            //update position
//            particles[i].update((float)dt);
//            //resolve collisions
//            for(int j = 0; j < collisions.length; j++) {
//                collisions[j].behave(this, particles[i]);
//            }
//
//            //reset particle forces
//            particles[i].forceAccumulator.scale(0);
//
//            //update display position
//            particles[i].updateTransformGroup();
//        }
//    }

    public void setForce(int index, Vector3f forceVector) {
        //To change body of created methods use File | Settings | File Templates.
    }
}

package forcefield;

import particles.Particle;
import particles.ParticleSystemInterface;
import particles.behaviors.CollisionBehavior;
import particles.behaviors.ForceBehavior;
import particles.behaviors.ParticleBehavior;

import javax.media.j3d.*;
import javax.vecmath.Color4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * This displays the forces acting on stationary parts. It is otherwise identical to the ParticleSystem class.
 */
public class VectorField extends Shape3D implements ParticleSystemInterface {
    private Set<Particle> particleSet;
    private Set<ParticleBehavior> particleForces;
    private Set<ParticleBehavior> particleCollisions;
    private static final float DEFAULT_RADIUS = 0.2f;
    private static final Color4f START_COLOR = new Color4f(.5f,0,0,1);
    private static final Color4f END_COLOR = new Color4f(.5f,0,0,0);

    /**
     * @param halfWidth Half the width of the boundary space
     * @param divisions How many divisions should there be to this space (for point locations)
     */
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

    /**The point data, for updating the rendering.**/
    float positions[];
    /**The points in the region acting as stationary particles to collect forces**/
    ForceVector forces[];
    /**Helper method for accessing point data.**/
    private void setPosition(int index,float x, float y, float z) {
        positions[index*3   ] = x;
        positions[index*3 +1] = y;
        positions[index*3 +2] = z;
    }
    /**Helper method for accessing point data.**/
    private float getPosStartX(int index) {
        return positions[index*3   ];
    }
    /**Helper method for accessing point data.**/
    private float getPosStartY(int index) {
        return positions[index*3 +1];
    }
    /**Helper method for accessing point data.**/
    private float getPosStartZ(int index) {
        return positions[index*3 +2];
    }
    /**Creates all the geometry (lines/colors/vectors)**/
    private Geometry createGeometry(float halfWidth, int divisions) {
        LineArray geometry = new LineArray(2*divisions*divisions*divisions,LineArray.COLOR_4|LineArray.BY_REFERENCE|LineArray.COORDINATES);

        positions = new float[2*3*divisions*divisions*divisions];
        forces = new ForceVector[divisions*divisions*divisions];
        float colors[] = new float[2*4*divisions*divisions*divisions];
        for(int i = 0; i < divisions*divisions*divisions; i++) {
            colors[i*8   ] = START_COLOR.getX();
            colors[i*8 +1] = START_COLOR.getY();
            colors[i*8 +2] = START_COLOR.getZ();
            colors[i*8 +3] = START_COLOR.getW();

            colors[i*8 +4] = END_COLOR.getX();
            colors[i*8 +5] = END_COLOR.getY();
            colors[i*8 +6] = END_COLOR.getZ();
            colors[i*8 +7] = END_COLOR.getW();
        }


        float x,y,z;
        for(int i = 0; i < divisions; i++ ){
            for(int j=0; j < divisions; j++) {
                for(int k=0; k < divisions; k++) {
                    int index = (i*divisions*divisions + j*divisions + k)*2;
                    x = (i*2f*halfWidth)/divisions - halfWidth;
                    y = (j*2f*halfWidth)/divisions - halfWidth;
                    z = (k*2f*halfWidth)/divisions - halfWidth;
                    setPosition(index,x,y,z);
                    forces[index/2] = new ForceVector(this,index,new Point3f(x,y,z));
                    x = x+1; y = y+1; z = z+1;
                    setPosition(index+1,x,y,z);
                }
            }
        }

        x = 0; y = 0; z = 0;
        ForceVector p = new ForceVector(this,0,new Point3f(x,y,z));
        setPosition(1,x,y,z);


        geometry.setCoordRefFloat(positions);
        geometry.setColorRefFloat(colors);

        return geometry;  //To change body of created methods use File | Settings | File Templates.
    }

    /**Add a force behavior**/
    public void addParticleForceBehavior(final ParticleBehavior particleBehavior) {
        particleForces.add(particleBehavior);
    }

    /**Add a collision behavior (not used here)**/
    public void addParticleCollisionBehavior(final ParticleBehavior particleBehavior) {
        particleCollisions.add(particleBehavior);
    }

    /**Update the particles(force vectors)
     * @param dt Time difference (seconds)
     **/
    public void update(final double dt) {
        //create one array to avoid unnecessary iterator instantiation (avoiding a foreach loop)
        //this could be further sped up by updating an array every time a behavior is added
        //    ...which presumes that behaviors aren't frequently added or removed realtime
        ParticleBehavior[] forces = particleForces.toArray(new ParticleBehavior[particleForces.size()]);
        ParticleBehavior[] collisions = particleCollisions.toArray(new ParticleBehavior[particleCollisions.size()]);


        for(int i = 0; i < this.forces.length; i++) {
            //accumulate forces
            for(int j = 0; j < forces.length; j++) {
                forces[j].behave(this, this.forces[i]);
            }
            //update position
            this.forces[i].update((float)dt);
            //resolve collisions
            for(int j = 0; j < collisions.length; j++) {
                collisions[j].behave(this, this.forces[i]);
            }

            //update display position
            this.forces[i].updateTransformGroup();

            //reset particle forces
            this.forces[i].forceAccumulator.scale(0);
        }
    }

    //workspace fields for setForce
    Vector3f forceVectorWorkspace = new Vector3f();
    double maxLength = 0;
    double scale = 1;
    double maxLengthSquared = 0;

    /**
     * Sets a force to be displayed and scales it accordingly.
     * @param index The index of the particle(force vector)
     * @param forceVector The force (in newtons)
     */
    public void setForce(int index, Vector3f forceVector) {

        double len2 = forceVector.lengthSquared();
        if(len2 > maxLengthSquared) {
            maxLengthSquared = len2;
            maxLength = forceVector.length();
            scale = 1 / maxLength;
            System.out.println("Max Length: " + maxLength);
        }
        forceVectorWorkspace.scale((float)scale,forceVector);


        setPosition(index+1,
                forceVectorWorkspace.x+getPosStartX(index),
                forceVectorWorkspace.y+getPosStartY(index),
                forceVectorWorkspace.z+getPosStartZ(index));
    }

    /**
     * Resets the maximum force, which is used for force scaling. Use this after the magnitudes of the forces have been changed.
     */
    public void resetMaxLength() {
        maxLengthSquared = 0;
    }

    @Override
    public void addBehavior(ParticleBehavior particleBehavior) {
        if(particleBehavior instanceof ForceBehavior)
            particleForces.add(particleBehavior);
        if(particleBehavior instanceof CollisionBehavior)
            particleCollisions.add(particleBehavior);
    }

    @Override
    public void removeBehavior(ParticleBehavior particleBehavior) {
        particleForces.remove(particleBehavior);
        particleCollisions.remove(particleBehavior);
    }
}

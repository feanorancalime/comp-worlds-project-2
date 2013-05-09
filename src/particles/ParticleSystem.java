package particles;

import particles.behaviors.CollisionBehavior;
import particles.behaviors.ForceBehavior;
import particles.behaviors.ParticleBehavior;

import javax.media.j3d.*;
import javax.vecmath.Color4f;
import javax.vecmath.Point3f;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Represents a system of particleSet with a universal size and set of behaviors.
 */
public class ParticleSystem extends BranchGroup implements ParticleSystemInterface {
    private Set<Particle> particleSet;
    private Set<ParticleBehavior> particleForces;
    private Set<ParticleBehavior> particleCollisions;
    private static final float DEFAULT_RADIUS = 0.2f;

    public ParticleSystem() {
        this(0);
    }
    public ParticleSystem(final float range) {
        this(0,range);
    }

    public ParticleSystem(final int numPoints) {
        this(numPoints,DEFAULT_RADIUS);
    }

    public ParticleSystem(final int numPoints, final float range) {
        this(numPoints,DEFAULT_RADIUS,range);
    }

    public ParticleSystem(final int numPoints, final float radius,final float range) {
        Random rand = new Random();
        particleForces = new HashSet<ParticleBehavior>();
        particleCollisions = new HashSet<ParticleBehavior>();
        particleSet = new HashSet<Particle>(numPoints);
        for(int i = 0; i < numPoints; i++ ){
            double  r = rand.nextFloat(),
                    g = rand.nextFloat(),
                    b = rand.nextFloat();
            double max = Math.max(Math.max(r,g),b);
            r /= max; g /= max; b /= max; //normalize colors for MAXIMUM POWER

            Particle p = new Particle(this,
                    new Point3f(rand.nextFloat()*range*2-range,rand.nextFloat()*range*2-range,rand.nextFloat()*range*2-range),
                    new Color4f((float)r,(float)g,(float)b,1f),radius);
            particleSet.add(p);
            this.addChild(p);
        }
    }

    public void addParticleForceBehavior(final ParticleBehavior particleBehavior) {
        particleForces.add(particleBehavior);
    }

    public void addParticleCollisionBehavior(final ParticleBehavior particleBehavior) {
        particleCollisions.add(particleBehavior);
    }

    public void update(final double dt) {
        //create one array to avoid unnecessary iterator instantiation (avoiding a foreach loop)
        //this could be further sped up by updating an array every time a behavior is added
        //    ...which presumes that behaviors aren't frequently added or removed realtime
        ParticleBehavior[] forces = particleForces.toArray(new ParticleBehavior[particleForces.size()]);
        ParticleBehavior[] collisions = particleCollisions.toArray(new ParticleBehavior[particleCollisions.size()]);
        Particle[] particles = particleSet.toArray(new Particle[particleSet.size()]);

        for(int i = 0; i < particles.length; i++) {
            //accumulate forces
            for(int j = 0; j < forces.length; j++) {
                forces[j].behave(this, particles[i]);
            }
            //update position
            particles[i].update((float)dt);
            //resolve collisions
            for(int j = 0; j < collisions.length; j++) {
                collisions[j].behave(this, particles[i]);
            }

            //reset particle forces
            particles[i].getForceAccumulator().scale(0);

            //update display position
            particles[i].updateTransformGroup();
        }
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

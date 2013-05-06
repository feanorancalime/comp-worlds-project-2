package application;

import particles.HalfSpace;
import particles.Particle;
import particles.ParticleSystem;
import particles.behaviors.ParticleBehavior;

/**
 * Created with IntelliJ IDEA.
 * User: Lemtzas
 * Date: 5/6/13
 * Time: 12:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class BoundingBehavior implements ParticleBehavior {
    private HalfSpace[] boundaries;
    private final float COEFFICIENT_OF_RESTITUTION;

    public BoundingBehavior(final float half_width, final float COEFFICIENT_OF_RESTITUTION) {
        boundaries = new HalfSpace[]{
                //x
                new HalfSpace(-half_width,0,0   ,1,0,0),
                new HalfSpace(half_width,0,0    ,-1,0,0),
                //y
                new HalfSpace(0,-half_width,0   ,0,1,0),
                new HalfSpace(0,half_width,0    ,0,-1,0),
                //z
                new HalfSpace(0,0,-half_width   ,0,0,1),
                new HalfSpace(0,0,half_width   ,0,0,-1),
        };
        this.COEFFICIENT_OF_RESTITUTION = COEFFICIENT_OF_RESTITUTION;
    }

    @Override
    public void behave(ParticleSystem particleSystem, Particle particle) {
        for(int i = 0; i < boundaries.length; i++) {
            boundaries[i].checkAndResolveCollision(particle,COEFFICIENT_OF_RESTITUTION);
        }
    }
}

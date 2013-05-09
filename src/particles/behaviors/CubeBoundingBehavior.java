package particles.behaviors;

import forcefield.ParticleInterface;
import particles.HalfSpace;
import particles.Particle;
import particles.ParticleSystem;
import particles.ParticleSystemInterface;
import particles.behaviors.ParticleBehavior;

/**
 * Created with IntelliJ IDEA.
 * User: Lemtzas
 * Date: 5/6/13
 * Time: 12:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class CubeBoundingBehavior implements CollisionBehavior {
    private HalfSpace[] boundaries;
    private float coefficientOfRestitution;

    public CubeBoundingBehavior(final float half_width, final float COEFFICIENT_OF_RESTITUTION) {
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
        this.coefficientOfRestitution = COEFFICIENT_OF_RESTITUTION;
    }

    @Override
    public void behave(ParticleSystemInterface particleSystem, ParticleInterface particle) {
        for(int i = 0; i < boundaries.length; i++) {
            if(particle instanceof Particle)
                boundaries[i].checkAndResolveCollision((Particle)particle, coefficientOfRestitution);
        }
    }

    @Override
    public void update(float dt) {
        //does nothing
    }

    @Override
    public String getName() {
        return "Bounding Box";
    }

    @Override
    public void setCoefficientOfRestitution(float coefficientOfRestitution) {
        this.coefficientOfRestitution = coefficientOfRestitution;
    }

    @Override
    public float getCoefficientOfRestitution() {
        return coefficientOfRestitution;
    }
}

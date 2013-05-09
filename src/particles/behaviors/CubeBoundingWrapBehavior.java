package particles.behaviors;

import particles.HalfSpace;
import particles.Particle;
import particles.ParticleInterface;
import particles.ParticleSystemInterface;

import javax.vecmath.Vector3f;

/**
 * This CollisionBehavior wraps points in a cube, but instead of bouncing off the walls, they wrap around to the other side.
 */
public class CubeBoundingWrapBehavior implements CollisionBehavior {
    private HalfSpace[] boundaries;
    private float coefficientOfRestitution;
    private final float half_width;

    public CubeBoundingWrapBehavior(final float half_width, final float COEFFICIENT_OF_RESTITUTION) {
        boundaries = new HalfSpace[]{
                //x
                new HalfSpace(-half_width,0,0   ,1,0,0),
                new HalfSpace(half_width,0,0    ,-1,0,0),
                //z
                new HalfSpace(0,0,-half_width   ,0,0,1),
                new HalfSpace(0,0,half_width   ,0,0,-1),
                //y
                new HalfSpace(0,-half_width,0   ,0,1,0),
                new HalfSpace(0,half_width,0    ,0,-1,0),
        };
        this.half_width = half_width;
        this.coefficientOfRestitution = COEFFICIENT_OF_RESTITUTION;
    }

    Vector3f v3f = new Vector3f();

    @Override
    public void behave(ParticleSystemInterface particleSystem, ParticleInterface particle) {
        if(particle instanceof Particle) {
            //side wrapping
            for(int i = 0; i < 4; i++) {
                if(boundaries[i].checkCollision((Particle)particle)) {
                    ((Particle) particle).position.scaleAdd(2*half_width,boundaries[i].normal,((Particle) particle).position);
                }
            }
            //top-bottom collision
            for(int i = 4; i < boundaries.length; i++) {
                boundaries[i].checkAndResolveCollision((Particle)particle, coefficientOfRestitution);
            }
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

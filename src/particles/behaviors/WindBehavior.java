package particles.behaviors;

import forcefield.ParticleInterface;
import org.omg.CORBA.PRIVATE_MEMBER;
import particles.ParticleSystemInterface;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Lemtzas
 * Date: 5/8/13
 * Time: 7:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class WindBehavior implements ForceBehavior {
    private static final float FORCE_MAXIMUM = 100;
    private static final float FORCE_MINIMUM = 0;

    private static final float FORCE_VARIANCE_PERCENT = 0.2f;

    private static final float FORCE_PERIOD_BIG = 3f;
    private static final float FORCE_PERIOD_MED = 1f;
    private static final float FORCE_PERIOD_LOW = 0.5f;
    private static final Random RAND = new Random();

    private Vector3f direction;

    private float force;

    public WindBehavior(float newtons) {
        force = newtons;
        direction = new Vector3f(RAND.nextFloat(),RAND.nextFloat(),RAND.nextFloat());
        direction.normalize();
    }

    private Vector3f v3f = new Vector3f();

    @Override
    public void behave(ParticleSystemInterface particleSystem, ParticleInterface particle) {
        float force_base = force * (float)Math.sin(particle.getPosition().x/2);
        v3f.scale(force_base,direction);
        particle.getForceAccumulator().add(v3f);
    }

    @Override
    public void update(float dt) {
        //soon
    }

    @Override
    public void setForceMagnitude(float newtons) {
        this.force = newtons;
    }

    @Override
    public float getForceMagnitude() {
        return force;
    }

    @Override
    public float getForceMaximum() {
        return FORCE_MAXIMUM;
    }

    @Override
    public float getForceMinimum() {
        return FORCE_MINIMUM;
    }

    @Override
    public String getName() {
        return "Wind";
    }
}

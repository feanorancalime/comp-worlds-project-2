package particles.behaviors;

import particles.ParticleInterface;
import particles.ParticleSystemInterface;

import javax.media.j3d.Transform3D;
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
    private static final float OFFSET_MAXIMUM = 10f;
    private static final float ROT_MAXIMUM = 3f;

    private static final Random RAND = new Random();

    private Vector3f direction;

    private float force;


    private float FORCE_PERIOD_BIG = 1.2f;

    private float offset1;
    private float offset2;
    private float offset3;

    public WindBehavior(float newtons) {
        force = newtons;
        direction = new Vector3f(RAND.nextFloat(),RAND.nextFloat(),RAND.nextFloat());
        direction.normalize();
    }

    private Vector3f v3f = new Vector3f();

    @Override
    public void behave(ParticleSystemInterface particleSystem, ParticleInterface particle) {
        float force_base_x = (float)Math.sin(particle.getPosition().x/FORCE_PERIOD_BIG + offset1) + 1;
        float force_base_y = (float)Math.sin(particle.getPosition().y/FORCE_PERIOD_BIG + offset1) + 1;
        float force_base_z = (float)Math.sin(particle.getPosition().z/FORCE_PERIOD_BIG + offset1) + 1;

        v3f.set(direction.x*force_base_x,
                direction.y*force_base_y,
                direction.z*force_base_z);
        v3f.normalize();
        float scale = (float)(force_base_x+force_base_y+force_base_z)/6*FORCE_VARIANCE_PERCENT;
        scale *= force;

        v3f.scale(force - scale);

        //v3f.scale(force_base,direction);
        particle.getForceAccumulator().add(v3f);
    }

    Transform3D t3d = new Transform3D();

    @Override
    public void update(float dt) {
        offset1 += (RAND.nextFloat()*OFFSET_MAXIMUM*2-OFFSET_MAXIMUM)*dt;
        offset2 += (RAND.nextFloat()*OFFSET_MAXIMUM*2-OFFSET_MAXIMUM)*dt;
        offset2 += (RAND.nextFloat()*OFFSET_MAXIMUM*2-OFFSET_MAXIMUM)*dt;
        t3d.rotX((RAND.nextFloat()*ROT_MAXIMUM*2-ROT_MAXIMUM)*dt);
        t3d.rotY((RAND.nextFloat()*ROT_MAXIMUM*2-ROT_MAXIMUM)*dt);
        t3d.rotZ((RAND.nextFloat()*ROT_MAXIMUM*2-ROT_MAXIMUM)*dt);
        t3d.transform(direction);
        direction.normalize();
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

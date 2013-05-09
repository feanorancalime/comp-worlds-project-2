package particles.behaviors;

import particles.ParticleInterface;
import particles.ParticleSystemInterface;

import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3f;

/**
 * This ForceBehavior adds a drag force in opposition to the points velocity.
 */
public class DragBehavior implements ForceBehavior {
    private static final float FORCE_MAXIMUM = 20;
    private static final float FORCE_MINIMUM = 0;

    private float drag_coefficient;

    private float FORCE_PERIOD_BIG = 1.2f;

    private float offset1;
    private float offset2;
    private float offset3;

    public DragBehavior(float newtons) {
        drag_coefficient = newtons;
    }

    private Vector3f v3f = new Vector3f();

    @Override
    public void behave(ParticleSystemInterface particleSystem, ParticleInterface particle) {
        //v3f.scale(force_base,direction);
        float v_2 = particle.getVelocity().lengthSquared();
        float drag_force = v_2 * drag_coefficient * particle.getArea() / 2; // 1/2 * fluid_density * v*v * drag_coefficient * area

        v3f.scale(-drag_force,particle.getVelocity());

        particle.getForceAccumulator().add(v3f);
    }

    Transform3D t3d = new Transform3D();

    @Override
    public void update(float dt) {
        //nothing
    }

    @Override
    public void setForceMagnitude(float newtons) {
        this.drag_coefficient = newtons;
    }

    @Override
    public float getForceMagnitude() {
        return drag_coefficient;
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
        return "Drag";
    }
}

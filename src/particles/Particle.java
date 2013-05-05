package particles;

import javax.vecmath.Color4f;
import javax.vecmath.Point3f;

/**
 * A particle. Meant to live within the ParticleSystem class.
 */
public class Particle {
    private final int index;
    private final Point3f position;
    private final Color4f color;
    private final ParticleSystem particleSystem;
    private Point3f point;

    public Particle(final ParticleSystem particleSystem, final int index, final Point3f position, final Color4f color) {
        this.particleSystem = particleSystem;
        this.index = index;
        this.position = position;
        this.color = color;
    }

    /**
     * @return the particle's current position
     */
    public Point3f getPosition() {
        return point;
    }

    /**
     * @return the particle's current color
     */
    public Color4f getColor() {
        return color;
    }
}

package application;

import javax.vecmath.Color4f;
import javax.vecmath.Point3f;

/**
 *
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

    public Point3f getPoint() {
        return point;
    }

    public Color4f getColor() {
        return color;
    }
}

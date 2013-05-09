package particles;

import javax.vecmath.*;

public class HalfSpace {
	public Vector3f position;
	public Vector3f normal;
	// Right-hand side of the plane equation A * x + B * y = C
	public float intercept;
	
	public HalfSpace(float positionX, float positionY, float positionZ, float normalX, float normalY, float normalZ) {
		position = new Vector3f(positionX, positionY, positionZ);
		normal = new Vector3f(normalX, normalY, normalZ);
		normal.normalize();
		intercept = normal.dot(position);
	}

	public HalfSpace(Tuple3f position, Tuple3f normal) {
		this(position.x, position.y, position.z, normal.x, normal.y, normal.z);
	}


    Vector3f v3f = new Vector3f();
    /**Is the particle on the wrong side of the fence?**/
    public boolean checkCollision(Particle p) {
        p.position.get(v3f);
        float distance = this.normal.dot(v3f) - this.intercept;
        return distance < 0;
    }
    /**Check if a particle is on the wrong side...and then do something about it**/
    public void checkAndResolveCollision(Particle p, float COEFFICIENT_OF_RESTITUTION) {
        p.position.get(v3f);
        float distance = this.normal.dot(v3f) - this.intercept;
        if (distance < 0) {
            // Use Torricelli's equation to approximate the particle's
            // velocity (v_i) at the time it contacts the halfspace.
            // v_f^2 = v_i^2 + 2 * acceleration * distance

            // Final velocity of the particle in the direction of the halfspace normal
            float v_f = this.normal.dot(p.velocity);
            // Velocity of the particle in the direction of the halfspace normal at the
            // time of contact, squared
            float v_i_squared = v_f * v_f - 2 * p.forceAccumulator.dot(this.normal) * distance;
            // If v_i_squared is less than zero, then the quantities involved are so small
            // that numerical inaccuracy has produced an impossible result.  The velocity
            // at the time of contact should therefore be zero.
            if (v_i_squared < 0)
                v_i_squared = 0;
            // Remove the incorrect velocity acquired after the contact and add the flipped
            // correct velocity.
            p.velocity.scaleAdd(-v_f + COEFFICIENT_OF_RESTITUTION * (float)Math.sqrt(v_i_squared), this.normal, p.velocity);

            // Old code for adjusting the velocity.
            // p.velocity.scaleAdd(-(1 + COEFFICIENT_OF_RESTITUTION) * hs.normal.dot(p.velocity), hs.normal, p.velocity);

            p.position.scaleAdd(-distance, this.normal, p.position);
        }
    }
}

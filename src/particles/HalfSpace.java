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
}

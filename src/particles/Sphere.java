package particles;

import java.awt.Color;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Sphere {
	public float mass;
	public Vector3f position;
	public Vector3f velocity;
	public Vector3f forceAccumulator;
	public float radius;
	public BranchGroup BG;
	private TransformGroup TG;
	private Transform3D T3D;
	
	public Sphere(float mass, float positionX, float positionY, float velocityX, float velocityY, float radius, Color3f color, float velocityZ, float positionZ) {
		if (mass <= 0 || radius <= 0)
			throw new IllegalArgumentException();
		
		this.mass = mass;
		position = new Vector3f(positionX, positionY, positionZ);
		velocity = new Vector3f(velocityX, velocityY, velocityZ);
		forceAccumulator = new Vector3f();
		this.radius = radius;
		BG = new BranchGroup();
		BG.setCapability(BranchGroup.ALLOW_DETACH);
		TG = new TransformGroup();
		TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		TG.addChild(createShape(radius, 20, color));
		BG.addChild(TG);
		T3D = new Transform3D();
		updateTransformGroup();
	}
	
	public Sphere(float mass, Vector3f position, Vector3f velocity, float radius, Color3f color) {
		this(mass, position.x, position.y, velocity.x, velocity.y, radius, color, radius, radius);
	}

	public void updateState(float duration) {
		// The force accumulator vector (net force) now becomes
		// the acceleration vector.
		forceAccumulator.scale(1 / mass);
		position.scaleAdd(duration, velocity, position);
		position.scaleAdd(duration * duration / 2, forceAccumulator, position);
		velocity.scaleAdd(duration, forceAccumulator, velocity);
	}

	public void updateTransformGroup() {
		T3D.setTranslation(new Vector3f(position.x, position.y, 0));
		TG.setTransform(T3D);
	}
	
	private Node createShape(float radius, int samples, Color3f color) {
		TriangleFanArray geometry = new TriangleFanArray(samples + 2, GeometryArray.COORDINATES, new int[] {samples + 2});
		Point3f[] vertices = new Point3f[samples + 2];
		vertices[0] = new Point3f();
		for (int i = 0; i <= samples; i++)
			vertices[i+1] = new Point3f(radius * (float)Math.cos(2 * Math.PI * i / samples), radius * (float)Math.sin(2 * Math.PI * i / samples), 0);

		geometry.setCoordinates(0, vertices);
		
		if (color == null)
			color = new Color3f(Color.getHSBColor((float)Math.random(), 1, 1));
		Appearance appearance = new Appearance();
		appearance.setColoringAttributes(new ColoringAttributes(color, ColoringAttributes.FASTEST));
		PolygonAttributes polyAttr = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 0);
		appearance.setPolygonAttributes(polyAttr);		
		return new Shape3D(geometry, appearance);
	}
}

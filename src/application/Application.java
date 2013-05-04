package application;

import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Light;
import javax.media.j3d.PickInfo;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.pickfast.behaviors.PickRotateBehavior;
import com.sun.j3d.utils.pickfast.behaviors.PickTranslateBehavior;
import com.sun.j3d.utils.pickfast.behaviors.PickZoomBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Application {

	private SimpleUniverse simpleU;
	private BranchGroup scene;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Application().createAndShowGUI();
			}
		});
	}

	/**
	 * Create and display the GUI, including menus and the scene graph. 
	 */
	private void createAndShowGUI() {
		// Fix for background flickering on some platforms
		System.setProperty("sun.awt.noerasebackground", "true");
		
		// Build the universe
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);

		simpleU = new SimpleUniverse(canvas);
		simpleU.getViewingPlatform().setNominalViewingTransform();
		simpleU.getViewer().getView().setSceneAntialiasingEnable(true);

		// Scene graph
		scene = new BranchGroup();
		scene.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		scene.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		scene.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);

		Light light = new AmbientLight(new Color3f(1f, 1f, 1f));
		scene.addChild(light);
		light = new DirectionalLight(new Color3f(1f, 1f, 1f), new Vector3f(-1f, -1f, -1f));
		light.setInfluencingBounds(new BoundingBox());
		
		// View movement
		TransformGroup cameraTransform = simpleU.getViewingPlatform().getViewPlatformTransform();
		Transform3D translate = new Transform3D();
		translate.setTranslation(new Vector3f(0f, 0f, 20f)); // set as translation
		cameraTransform.setTransform(translate); // used for initial position
		KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(cameraTransform);
		keyNavBeh.setSchedulingBounds(new BoundingSphere(
		new Point3d(),1000.0));
		scene.addChild(keyNavBeh);
		
		// Picking
		Behavior b = new PickRotateBehavior(scene, canvas,
				new BoundingSphere(), PickInfo.PICK_BOUNDS);
		b.setSchedulingBounds(new BoundingSphere());
		scene.addChild(b);
		b = new PickTranslateBehavior(scene, canvas, new BoundingSphere(),
				PickInfo.PICK_BOUNDS);
		b.setSchedulingBounds(new BoundingSphere());
		scene.addChild(b);
		b = new PickZoomBehavior(scene, canvas, new BoundingSphere(),
				PickInfo.PICK_BOUNDS);
		b.setSchedulingBounds(new BoundingSphere());
		b = new MouseWheelZoom(simpleU.getViewingPlatform().getViewPlatformTransform());
		b.setSchedulingBounds(new BoundingSphere());
		scene.addChild(b);
		
		// Extent
		Appearance app = new Appearance();
		PolygonAttributes polyAttribs = new PolygonAttributes(PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_NONE, 0);
		app.setPolygonAttributes(polyAttribs);
		
		app.setColoringAttributes(new ColoringAttributes(new Color3f(1.0f, 1.0f, 1.0f), ColoringAttributes.FASTEST));
		Box box = new Box(5f, 5f, 5f, app);
		box.setPickable(false);
		scene.addChild(box);
		
		simpleU.addBranchGraph(scene);

		// Swing-related code
		JFrame appFrame = new JFrame("Physics Engine - Project 2");
		appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		appFrame.add(canvas);
		appFrame.pack();
		if (Toolkit.getDefaultToolkit().isFrameStateSupported(
				Frame.MAXIMIZED_BOTH)) {
			appFrame.setExtendedState(appFrame.getExtendedState()
					| Frame.MAXIMIZED_BOTH);
		}

		appFrame.setVisible(true);
	}


}

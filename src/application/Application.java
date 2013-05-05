package application;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Light;
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
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Application {

	// Physics updates per second (approximate).
	private static final int UPDATE_RATE = 30;
	// Width of the extent in meters.
	private static final float EXTENT_WIDTH = 20;
	private static final float COEFFICIENT_OF_RESTITUTION = .95f;
	
	private SimpleUniverse simpleU;

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
		
		
		// Add a scaling transform that resizes the virtual world to fit
		// within the standard view frustum.
		BranchGroup trueScene = new BranchGroup();
		TransformGroup worldScaleTG = new TransformGroup();
		Transform3D t3D = new Transform3D();
		t3D.setScale( 6 / EXTENT_WIDTH);
		worldScaleTG.setTransform(t3D);
		trueScene.addChild(worldScaleTG);
		BranchGroup scene = new BranchGroup();
		worldScaleTG.addChild(scene);

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
		
		// Extent
		Appearance app = new Appearance();
		PolygonAttributes polyAttribs = new PolygonAttributes(PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_NONE, 0);
		app.setPolygonAttributes(polyAttribs);
		
		app.setColoringAttributes(new ColoringAttributes(new Color3f(1.0f, 1.0f, 1.0f), ColoringAttributes.FASTEST));
		Box extent = new Box(EXTENT_WIDTH / 2, EXTENT_WIDTH / 2, EXTENT_WIDTH / 2, app);
		extent.setPickable(false);
		scene.addChild(extent);

        scene.addChild(new ParticleSystem(10));
		
		simpleU.addBranchGraph(trueScene);

		// Swing-related code
		JFrame appFrame = new JFrame("Physics Engine - Project 2");
		appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas.setPreferredSize(new Dimension(800,600));
		appFrame.add(canvas);
		appFrame.pack();
        appFrame.setLocationRelativeTo(null); //center the window
        //disabled because it's super annoying for testing --david
//		if (Toolkit.getDefaultToolkit().isFrameStateSupported(
//				Frame.MAXIMIZED_BOTH)) {
//			appFrame.setExtendedState(appFrame.getExtendedState()
//					| Frame.MAXIMIZED_BOTH);
//		}

		appFrame.setVisible(true);
	}
}

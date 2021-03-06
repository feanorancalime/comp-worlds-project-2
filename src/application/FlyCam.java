package application;

import javax.media.j3d.*;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * A Behavior that works in conjunction with CamGrabBehavior and InsertBehavior (both optional).
 *
 * This behavior allows right click dragging with the mouse to look around, the use of WASD
 * for XZ planar movement, and QE for y axis movement. In addition, it maintains a distance
 * from the camera to a cursor (curTransform's origin or "focus"). This position is used by
 * InsertBehavior to manage the placement and orientation of objects.
 *
 */
public class FlyCam extends Behavior {
    /**for use with Transform.lookAt**/
    private Vector3d up;
    /**for use with Transform.lookAt**/
    private Point3d focus;
    /**for use with Transform.lookAt**/
    private Point3d camera;

    /**temporary Transform3Ds (so we don't have to instantiate a bunch)**/
    Transform3D t3d = new Transform3D();
    /**temporary Transform3Ds (so we don't have to instantiate a bunch)**/
    Transform3D t3d2 = new Transform3D();

    /**this is where the lookAt results go**/
    TransformGroup vpTrans;

    /**how far should we be from our "cursor" ?**/
    double distance;

    /**What do we listen to when keys are down?**/
    WakeupOr wakeupEventsKeyDown;
    /**What do we listen to when keys aren't down?**/
    WakeupOr wakeupEventsNormal;

    /**What keys are currently down?**/
    Set<Integer> pressedKeys = new HashSet<Integer>();

    /**movement speed**/
    private static final double SPEED = 0.1d;
    /**wakeUpTime duration**/
    private static final long MILLISECONDS = 20;
    /**transform for the lighting (camera origin)**/
    private TransformGroup lightTransform;
    /**transform for the cursor (focus origin)**/
    private TransformGroup curTransform;


    public FlyCam(TransformGroup vpTrans, Point3d focus, Point3d camera, Vector3d up, double distance, TransformGroup lightTransform, TransformGroup curTransform) {
        //copy values
        this.vpTrans = vpTrans;
        this.focus = focus;
        this.camera = camera;
        this.up = up;
        this.distance = distance;
        this.lightTransform = lightTransform;
        this.curTransform = curTransform;

        correctCam();
        updateCam();

        //set actions that awaken the behavior
        WakeupCriterion[] eventsNormal = {
                new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED),
                new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED),
                new WakeupOnAWTEvent(KeyEvent.KEY_TYPED),
                new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED),
                new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED),
                new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED)};
        wakeupEventsNormal = new WakeupOr(eventsNormal);
        WakeupCriterion[] eventsKeyDown = {
                new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED),
                new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED),
                new WakeupOnAWTEvent(KeyEvent.KEY_TYPED),
                new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED),
                new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED),
                new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED),
                new WakeupOnElapsedTime(MILLISECONDS)};
        wakeupEventsKeyDown = new WakeupOr(eventsKeyDown);
    }

    @Override
    public void initialize() {
        //System.out.println("RISE");
        wakeupOn(wakeupEventsNormal);
    }



    //workspace variables
    private WakeupCriterion wakeup;
    private AWTEvent[] events;

    @Override
    public void processStimulus(Enumeration criteria) {

        // Check all wakeup criteria...
        while(criteria.hasMoreElements()) {
            wakeup = (WakeupCriterion)criteria.nextElement();

            if (wakeup instanceof WakeupOnAWTEvent) {
                events = ((WakeupOnAWTEvent)wakeup).getAWTEvent();
                for(AWTEvent e : events) {
                    if(e instanceof MouseEvent) {
                        handleMouse((MouseEvent)e);
                    } else if(e instanceof KeyEvent) {
                        handleWASD((KeyEvent)e);
                    } else {
                        //eh?
                    }
                }
            } else if(wakeup instanceof WakeupOnElapsedTime) {
                processTimeSlice();
            }
        }


        //repeat
        if(pressedKeys.isEmpty())
            this.wakeupOn(wakeupEventsNormal);
        else
            this.wakeupOn(wakeupEventsKeyDown);
    }

    //more workspace variables
    Vector3d look = new Vector3d();
    Vector3d lr = new Vector3d();
    Vector3d fb = new Vector3d();
    Vector3d res = new Vector3d();

    /**
     * Processes a timeslice (which are triggered only when keys are down).
     *
     * This moves the user through the virtual world with WASD+QE
     */
    private void processTimeSlice() {
        int forward_back = 0;
        int left_right = 0;
        int up_down = 0;
        if(pressedKeys.contains(KeyEvent.VK_W))
            forward_back++;
        if(pressedKeys.contains(KeyEvent.VK_S))
            forward_back--;
        if(pressedKeys.contains(KeyEvent.VK_D))
            left_right++;
        if(pressedKeys.contains(KeyEvent.VK_A))
            left_right--;
        if(pressedKeys.contains(KeyEvent.VK_Q))
            up_down++;
        if(pressedKeys.contains(KeyEvent.VK_E))
            up_down--;
        look.scale(0); //clear
        look.sub(focus,camera);
        ////System.out.println(look);
        lr.scale(0); //clear
        fb.scale(0); //clear
        lr.cross(look,up); //+x is to the right
        fb.cross(up,lr);   //-z is forward
        Vector3d up_local = (Vector3d)up.clone();
        up_local.normalize();
        up_local.scale(up_down);

        //scale by how much in each direction
        fb.scale(forward_back);
        lr.scale(left_right);
        res.scale(0); //clear
        res.add(fb,lr);
        res.add(up_local);
        //System.out.println("res" + res);

        translate(res);
    }


    Vector3d offset = new Vector3d();

    /**
     * Correct the distance of the camera from the focus point.
     */
    private void correctCam() {
        offset.scale(0); //clear
        offset.sub(camera, focus); //this should be opposite of view direction
        offset.normalize();
        offset.scale(distance);
        camera.add(focus, offset);
    }

    /**
     * Updates the camera, lighting, and cursor transforms.
     */
    private void updateCam() {
        //System.out.println(camera + " @ " + focus);

        //set the new correct position
        t3d.lookAt(camera,focus,up);
        t3d.invert();
        vpTrans.setTransform(t3d);
        t3d.setIdentity();
        //update light position
        t3d.setTranslation(new Vector3f(camera));
        lightTransform.setTransform(t3d);
        //update insertion point
        curTransform.getTransform(t3d);
        t3d.setTranslation(new Vector3f(focus));
        curTransform.setTransform(t3d);
    }

    /**
     * Translate the camera in a direction
     * @param direction
     */
    private void translate(Vector3d direction) {
        if(direction.lengthSquared() == 0) return;
        direction.normalize();
        //System.out.println(direction);
        direction.scale(SPEED);
        //System.out.println(direction);
        camera.add(direction);
        focus.add(direction);
        updateCam();
    }


    Vector3d viewDir= new Vector3d();

    /**
     * Rotate the camera with its own position as the origin.
     * @param xRadians
     * @param yRadians
     */
    private void rotate(double xRadians, double yRadians) {
        viewDir.scale(0);
        viewDir.sub(focus, camera); //the view direction
        viewDir.normalize();
        //System.out.println("rotate " + xRadians + " , " + yRadians + "  " + viewDir);
        t3d.setIdentity();
        t3d.rotY(-xRadians);
        t3d2.setIdentity();
        //quick fix for gimbal lock scenario. TODO: Use quaternions?
        if(Math.abs(viewDir.dot(new Vector3d(1,0,0))) < Math.abs(viewDir.dot(new Vector3d(0, 0, 1))))
            t3d2.rotX(Math.signum(viewDir.dot(new Vector3d(0,0,1)))*yRadians);
        else
            t3d2.rotZ(-Math.signum(viewDir.dot(new Vector3d(1,0,0)))*yRadians);
        t3d.mul(t3d2);
        t3d.transform(viewDir);

        //scale it out
        viewDir.scale(distance);
        focus.add(camera,viewDir);

        updateCam();
    }


    /**
     * Handle keypresses (WASD+QE)
     * @param e
     */
    private void handleWASD(KeyEvent e) {
        //if(e.getID() != KeyEvent.KEY_PRESSED && e.getID() != KeyEvent.KEY_TYPED) return;


        switch(e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_S:
            case KeyEvent.VK_A:
            case KeyEvent.VK_D:
            case KeyEvent.VK_Q:
            case KeyEvent.VK_E:
                if(e.getID() == KeyEvent.KEY_PRESSED) {
                    if(!pressedKeys.contains(e.getKeyCode())) {
                        pressedKeys.add(e.getKeyCode());
                        //System.out.println("add '" + e.getKeyChar() + "'");
                        processTimeSlice();
                    }
                } else if(e.getID() == KeyEvent.KEY_RELEASED) {
                    pressedKeys.remove(e.getKeyCode());
                    //System.out.println("rem '" + e.getKeyChar() + "'");
                }
            default:
                //eh?
        }
    }

    /**Utility variables for handleMouse**/
    int prevMouseX=0,prevMouseY=0;
    /**Utility variables for handleMouse**/
    double xFactor = 0.005;
    /**Utility variables for handleMouse**/
    double yFactor = 0.005;
    /**Utility variables for handleMouse**/
    boolean drag = false;

    /**
     * Handle mouse input. Right-click Dragging the mouse swivels the camera.
     * @param e
     */
    private void handleMouse(MouseEvent e) {
        if(e.getID() == MouseEvent.MOUSE_DRAGGED && drag) {
            //setup
            int curMouseX = e.getX();
            int curMouseY = e.getY();
            int offsetX = curMouseX - prevMouseX;
            int offsetY = curMouseY - prevMouseY;

            //the job
            rotate(offsetX*xFactor,offsetY*yFactor);
            //TODO: Capture and center mouse

            //housekeeping
            prevMouseX = curMouseX;
            prevMouseY = curMouseY;
        } else if(e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON3) {
            //prep
            prevMouseX = e.getX();
            prevMouseY = e.getY();
            drag = true;
        } else if(e.getID() == MouseEvent.MOUSE_RELEASED && e.getButton() == MouseEvent.BUTTON3){
            drag = false;
        }
    }
}

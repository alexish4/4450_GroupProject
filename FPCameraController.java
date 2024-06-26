/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package groupproject;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

/**
 *
 * @author alexhernandez
 */
public class FPCameraController {
    //3d vector to store the camera's position in
    private Vector3f position = null;
    private Vector3f lPosition = null;
    //the rotation around the Y axis of the camera
    private float yaw = 0.0f;
    //the rotation around the X axis of the camera
    private float pitch = 0.0f;
    private Vector3Float me;
    private Chunk chunk = new Chunk(0,0,0);
    
    //private boolean boundary;
    private boolean boundaryKeyPressed = false;
    
    public FPCameraController(float x, float y, float z)
    {
        //instantiate position Vector3f to the x y z params.
        position = new Vector3f(x, y, z);
        lPosition = new Vector3f(x,y,z);
        lPosition.x = 0f;
        lPosition.y = 15f;
        lPosition.z = 0f;
    }

    //increment the camera's current yaw rotation
    public void yaw(float amount)
    {
        //increment the yaw by the amount param
        yaw += amount;
    }
    //increment the camera's current yaw rotation
    public void pitch(float amount)
    {
        //increment the pitch by the amount param
        pitch -= amount;
    }

    //moves the camera forward relative to its current rotation (yaw)
    public void walkForward(float distance, boolean boundary) //for x and z middle is -30, upper is 0 and lower is -60
    {
        //System.out.println("Boundary toggled: " + boundary);
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        
        if(boundary) {
            if(position.x - xOffset > -60 && position.x - xOffset < 0)
                position.x -= xOffset;
            if(position.z + zOffset < 0 && position.z - zOffset > -60)
                position.z += zOffset;

            //teleport if boundaries are on and out of bounds
            if(position.x < -60)
                position.x = -59;
            if(position.x > 0)
                position.x = -1;

            if(position.z < -60)
                position.z = -59;
            if(position.z > 0)
                position.z = -1;
        }
        else {
            position.x -= xOffset;
            position.z += zOffset;
        }
    }

    //moves the camera backward relative to its current rotation (yaw)
    public void walkBackwards(float distance, boolean boundary)
    {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        
        if(boundary) {
            if(position.x + xOffset < 0 && position.x + xOffset > -60)
                position.x += xOffset;
            if(position.z - zOffset > -60 && position.z - zOffset < 0)
                position.z -= zOffset;

            //teleport if boundaries are on and out of bounds
            if(position.x < -60)
                position.x = -59;
            if(position.x > 0)
                position.x = -1;

            if(position.z < -60)
                position.z = -59;
            if(position.z > 0)
                position.z = -1;
        }
        else {
            position.x += xOffset;
            position.z -= zOffset;
        }
    }
    
    //strafes the camera left relative to its current rotation (yaw)
    public void strafeLeft(float distance, boolean boundary)
    {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
        
        if(boundary) {
            if(position.x - xOffset > -60 && position.x - xOffset < 0)
                position.x -= xOffset;
            if(position.z + zOffset < 0 && position.z + zOffset > -60)
                position.z += zOffset;

            //teleport if boundaries are on and out of bounds
            if(position.x < -60)
                position.x = -59;
            if(position.x > 0)
                position.x = -1;

            if(position.z < -60)
                position.z = -59;
            if(position.z > 0)
                position.z = -1;
        }
        else {
            position.x -= xOffset;
            position.z += zOffset;
        }
    }

    //strafes the camera right relative to its current rotation (yaw)
    public void strafeRight(float distance, boolean boundary)
    {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
        
        if(boundary) {
            if(position.x - xOffset > -60 && position.x - xOffset < 0)
                position.x -= xOffset;
            if(position.z + zOffset < 0 && position.z + zOffset > -60)
                position.z += zOffset;

            //teleport if boundaries are on and out of bounds
            if(position.x < -60)
                position.x = -59;
            if(position.x > 0)
                position.x = -1;

            if(position.z < -60)
                position.z = -59;
            if(position.z > 0)
                position.z = -1;
        }
        else {
            position.x -= xOffset;
            position.z += zOffset;
        }
    }

    //moves the camera up relative to its current rotation (yaw)
    public void moveUp(float distance)
    {
        position.y -= distance;
    }
    //moves the camera down
    public void moveDown(float distance)
    {
        position.y += distance;
    }
    
    //translates and rotate the matrix so that it looks through the camera
    //this does basically what gluLookAt() does
    public void lookThrough()
    {
        //roatate the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //roatate the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);
        
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(0.0f).put(
            100.0f).put(30.0f).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    public void gameLoop()
    {
        FPCameraController camera = new FPCameraController(-30, -60, -30); //for x and z middle is -30, upper is 0 and lower is -60
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f; //length of frame
        float lastTime = 0.0f; // when the last frame was
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        //hide the mouse
        Mouse.setGrabbed(true);
        boolean boundary = false; //player can't leave boundary, but off by default
        
        // keep looping till the display window is closed the ESC key is down
        while (!Display.isCloseRequested() &&
            !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
        {
            time = Sys.getTime();
            lastTime = time;
            //distance in mouse movement
            //from the last getDX() call.
            dx = Mouse.getDX();
            //distance in mouse movement
            //from the last getDY() call.
            dy = Mouse.getDY();
            
            //controll camera yaw from x movement fromt the mouse
            camera.yaw(dx * mouseSensitivity);
            //controll camera pitch from y movement fromt the mouse
            camera.pitch(dy * mouseSensitivity);
            
            //when passing in the distance to move
            //we times the movementSpeed with dt this is a time scale
            //so if its a slow frame u move more then a fast frame
            //so on a slow computer you move just as fast as on a fast computer
            if (Keyboard.isKeyDown(Keyboard.KEY_W))//move forward
            {
                camera.walkForward(movementSpeed, boundary);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S))//move backwards
            {
                camera.walkBackwards(movementSpeed, boundary);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_A))//strafe left 
            {
                camera.strafeLeft(movementSpeed, boundary);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D))//strafe right 
            {
                camera.strafeRight(movementSpeed, boundary);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))//move up 
            {
                camera.moveUp(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) 
            {
                camera.moveDown(movementSpeed);
            }
            boolean isKeyDown = Keyboard.isKeyDown(Keyboard.KEY_0); //button to change boundary setting, off by default
            if (isKeyDown && !boundaryKeyPressed) 
            {
                boundary = !boundary;
                boundaryKeyPressed = true;
                System.out.println("Boundary toggled: " + boundary);
            }
            else if(!isKeyDown)
                boundaryKeyPressed = false;

            //set the modelview matrix back to the identity
            glLoadIdentity();
            //look through the camera before you draw anything
            camera.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            //you would draw your scene here.
            chunk.render();
            //draw the buffer to the screen
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }
    
    private void render() {
        try{
            
            glEnable(GL_DEPTH_TEST);
            glBegin(GL_QUADS);
            // top
            glColor3f(1.0f,0.0f,0.0f);  //red
            
            glVertex3f(1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(1.0f, 1.0f, 1.0f);
            
            // bottom
            glColor3f(0.0f,1.0f,0.0f);  //green
            
            glVertex3f(1.0f, -1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(1.0f, -1.0f, -1.0f);
            
            // front
            glColor3f(0.0f,0.0f,1.0f);  //blue
            
            glVertex3f(1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, 1.0f);
            
            // back
            glColor3f(0.5f, 0.5f, 0.0f);    //Bronze
            
            glVertex3f(1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(1.0f, 1.0f, -1.0f);
            
            // left
            glColor3f(1.0f, 0.5f, 0.0f);    //Orange
            
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);
            
            // right
            glColor3f(1.0f,0.0f,1.0f);  //purple
            
            glVertex3f(1.0f, 1.0f, -1.0f);
            glVertex3f(1.0f, 1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, -1.0f);
            
            glEnd();
            
            glBegin(GL_LINE_LOOP);
                //top
                glColor3f(0.0f, 0.0f, 0.0f);
                
                glVertex3f(1.0f, 1.0f, -1.0f);
                glVertex3f(-1.0f, 1.0f, -1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(1.0f, 1.0f, 1.0f);
            glEnd();
            
            glBegin(GL_LINE_LOOP);
                //bottom                
                glVertex3f(1.0f, -1.0f, 1.0f);
                glVertex3f(-1.0f, -1.0f, 1.0f);
                glVertex3f(-1.0f, -1.0f, -1.0f);
                glVertex3f(1.0f, -1.0f, -1.0f);
            glEnd();
            
            glBegin(GL_LINE_LOOP);
                //front                
                glVertex3f(1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, -1.0f, 1.0f);
                glVertex3f(1.0f, -1.0f, 1.0f);
            glEnd();
            
            glBegin(GL_LINE_LOOP);
                //back                
                glVertex3f(1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, 1.0f, -1.0f);
                glVertex3f(1.0f, 1.0f, -1.0f);
            glEnd();
            
            glBegin(GL_LINE_LOOP);
                //Left                
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f, -1.0f);
                glVertex3f(-1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, -1.0f, 1.0f);
            glEnd();
            
            glBegin(GL_LINE_LOOP);
                //right                
                glVertex3f(1.0f, 1.0f, -1.0f);
                glVertex3f(1.0f, 1.0f, 1.0f);
                glVertex3f(1.0f, -1.0f, 1.0f);
                glVertex3f(1.0f, -1.0f, -1.0f);
            glEnd();
                
        }catch(Exception e){
        }
    }
}

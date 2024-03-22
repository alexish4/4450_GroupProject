/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package groupproject;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author alexhernandez, ethan, megan
 */
public class Basic {
    public void start() {
        try {
            createWindow();
            initGL();
            render();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createWindow() throws Exception {
        Display.setFullscreen(false); //we don't want our window to take up the full screen
        
        Display.setDisplayMode(new DisplayMode(640, 480));
        Display.setTitle("Hey Mom! I am using OpenGL!!!");
        Display.create();
    }
    
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f); //specifying what background color
                                                                //we want through RGB values and Alpha
        glMatrixMode(GL_PROJECTION); //loading our camera using projection to view scene
        glLoadIdentity(); //load identity matrix
        
        glOrtho(0, 640, 0, 480, 1, -1); //setup orthographic matrix with 
                                                                    //size of 640 by 480 with clipping
                                                                    //distance between 1 and -1
        
        glMatrixMode(GL_MODELVIEW); //set up scene to Model view, and provide rendering hints
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
        
    }
    
    private void render() {        
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            try {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();
                
                glColor3f(1.0f, 0.0f, 0.0f);
                glPointSize(1);
                
                glBegin(GL_POINTS);                
                
                
                glEnd();
                
                Display.update();
                Display.sync(60);
            }
            catch (Exception e) {
                
            }
        }
        Display.destroy();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package groupproject;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

/**
 *
 * @author alexhernandez, ethan, megan
 */
public class Basic3D {
    private FPCameraController fp;
    private DisplayMode displayMode;
    
    
    public void start() {
        try {
            createWindow();
            initGL();
            fp = new FPCameraController(0f, 0f, 0f);
            fp.gameLoop(); //render();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createWindow() throws Exception {
        Display.setFullscreen(false); //we don't want our window to take up the full screen
        
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for (int i = 0; i < d.length; i++) {
            if (d[i].getWidth() == 640
                    && d[i].getHeight() == 480
                    && d[i].getBitsPerPixel() == 32) {
                displayMode = d[i];
                break;
            }
        }
        Display.setDisplayMode(displayMode);
        Display.setTitle("Hey Mom! I am using OpenGL!!!");
        Display.create();
    }

    
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f); //specifying what background color
                                                                //we want through RGB values and Alpha
        glMatrixMode(GL_PROJECTION); //loading our camera using projection to view scene
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_DEPTH_TEST);
        
        //part of texture mapping slides
        glEnable(GL_TEXTURE_2D);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        
        
        glLoadIdentity(); //load identity matrix
        
        GLU.gluPerspective(100.0f, (float)displayMode.getWidth() /
            (float) displayMode.getHeight(), 0.1f, 300.0f);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
        
    }
    
//    private void render() {        
//        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
//            try {
//                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//                glLoadIdentity();
//                
//                glColor3f(1.0f, 0.0f, 0.0f);
//                glPointSize(1);
//                
//                glBegin(GL_POINTS);                
//                
//                
//                glEnd();
//                
//                Display.update();
//                Display.sync(60);
//            }
//            catch (Exception e) {
//                
//            }
//        }
//        Display.destroy();
//    }
}

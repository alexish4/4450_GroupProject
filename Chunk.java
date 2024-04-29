/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package groupproject;

import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


/**
 *
 * @author alexhernandez, ethan,megan
 */
public class Chunk {
    static final int CHUNK_SIZE = 30;
    static final int CUBE_LENGTH = 2;
    static final int WATER_LEVEL = 13;      //random num for now for water level
    private Block[][][] Blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int StartX, StartY, StartZ;
    private Random r;
    
    //from texture mapping slides
    private int VBOTextureHandle;
    private Texture texture;
    
    public void render() {
        glPushMatrix();
            glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
            glVertexPointer(3, GL_FLOAT, 0, 0L);
            glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
            glColorPointer(3, GL_FLOAT, 0, 0L);
            
            glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
            glBindTexture(GL_TEXTURE_2D, 1);
            glTexCoordPointer(2, GL_FLOAT, 0, 0L);
            
            glDrawArrays(GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);
        glPopMatrix();
    }
    
    //call when we modify chunk
    public void rebuildMesh(float startX, float startY, float startZ) {
        SimplexNoise noise = new SimplexNoise(CHUNK_SIZE, 0.3, 1);
        
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        
        VBOTextureHandle = glGenBuffers(); //plaec among the other VBOs
        //the following among our other Float Buffers before our for loops
        FloatBuffer VertexTextureData = 
                BufferUtils.createFloatBuffer((CHUNK_SIZE *
                        CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        for (float x = 0; x < CHUNK_SIZE; x+=1) {
            for (float z = 0; z < CHUNK_SIZE; z+=1) {
                double height = (startY + (double)(14*((noise.getNoise((int)x, (int)CHUNK_SIZE, (int)z)) + 1)/2)) * CUBE_LENGTH;
                for (float y = 0; y < height; y++) {
                    VertexPositionData.put(createCube((float) (startX + x * CUBE_LENGTH), (float)(y * CUBE_LENGTH + (int)(CHUNK_SIZE * .8)),(float) (startZ + z *CUBE_LENGTH)));
                    VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int) x][(int) y][(int) z])));
                    VertexTextureData.put(createCubeatLevel((float) 0, (float) 0, (float) height, y,
                         Blocks[(int)(x)] [(int) (y)] [(int) (z)]));
                }
            }
        }   
        
        for (int x = 0; x < CHUNK_SIZE; x+=1) {
            for (int z = 0; z < CHUNK_SIZE; z+=1) {
                for (int y = 0; y < WATER_LEVEL; y++) {
                    VertexPositionData.put(createCube((float) (startX + x * CUBE_LENGTH), (float)(y * CUBE_LENGTH + (int)(CHUNK_SIZE * .8)),(float) (startZ + z *CUBE_LENGTH)));
                    VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int) x][(int) y][(int) z])));
                    VertexTextureData.put(createTexCube(x, y, 4));    
                }  
            }
        }
        
        VertexColorData.flip();
        VertexPositionData.flip();
        VertexTextureData.flip();
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    private float[] createCubeVertexCol(float[] CubeColorArray) {
        float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
        for (int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
        }
        return cubeColors;
    }
    
    public static float[] createCube(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;       //center of cube
        return new float[] {
            //TOP QUAD
            x + offset, y + offset, z,
            x - offset, y + offset, z,
            x - offset, y + offset, z - CUBE_LENGTH,
            x + offset, y + offset, z - CUBE_LENGTH,
            // BOTTOM QUAD
            x + offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z,
            x + offset, y - offset, z,
            // FRONT QUAD
            x + offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            //BACK QUAD
            x + offset, y - offset, z,
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,
            // LEFT QUAD
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z,
            x - offset, y - offset, z,
            x - offset, y - offset, z - CUBE_LENGTH,
            // RIGHT QUAD
            x + offset, y + offset, z,
            x + offset, y + offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z };
    }
    
    //Update this when we add texture
    private float[] getCubeColor(Block block) {
//        switch (block.getID()) {
//            case 0: 
//                return new float[] {0, 1, 0};
//            case 1:
//                return new float[] {1, 1, 0};
//            case 2:
//                return new float[] {1, 0.5f, 0};
//            case 3: 
//                return new float[] {0, 0f, 1f};
//            case 4:
//                return new float[] {0.5f, 0.5f, 0.5f};
//            case 5:
//                return new float[] {0, 0, 0};
//        }
        return new float[] {1,1,1}; //we only need this according to texture mapping slides?
    }
    
    public static float[] createCubeatLevel(float x, float y, float height, float currentHeight, Block block) {
        if (currentHeight == (int) height) {
            if (currentHeight < WATER_LEVEL - 1) {
                return createTexCube(x, y, 1);
            }    
            else {
                return createTexCube(x, y, 0);
            }
        }
        
        //lower levels = bedrock, middle = stone, upper(under top layer)=dirt
        float level = currentHeight/height;
        if (level < 0.1) {
            return createTexCube(x, y, 5);  //bedrock
        }
        else if (level < 0.7) {
            return createTexCube(x, y, 3);  //stone
        }
        else {
            return createTexCube(x, y, 2);  //dirt
        }
    }
    
    public static float[] createTexCube(float x, float y, int blockID) {
        float offset = (1024f / 16) / 1024f;
        switch (blockID) {
            case 0: //slides are wrong according to video lecture this is grass
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y), // this should be top?
                    x + offset*3, y + offset*10,
                    x + offset*2, y + offset*10,
                    x + offset*2, y + offset*9,
                    x + offset*3, y + offset*9,
                    // TOP!, //this should be bottom?
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    // FRONT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    // BACK QUAD
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    // LEFT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    // RIGHT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1

                };
            //SAND
            case 1:
                return new float[] {
                    // TOP QUAD
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    // BOTTOM
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    //FRONT
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    //BACK
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    //LEFT
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    //RIGHT
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                };
            //DIRT
            case 2:
                return new float[] {
                    //TOP
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    //BOTTOM
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    //FRONT
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    //BACK
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    //LEFT
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    //RIGHT
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                };
            //STONE
            case 3:
                return new float[] {
                    //TOP
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                    x + offset*1, y + offset*0,
                    x + offset*2, y + offset*0,
                    //BOTTOM
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                    x + offset*1, y + offset*0,
                    x + offset*2, y + offset*0,
                    //FRONT
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                    x + offset*1, y + offset*0,
                    x + offset*2, y + offset*0,
                    //BACK
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                    x + offset*1, y + offset*0,
                    x + offset*2, y + offset*0,
                    //LEFT
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                    x + offset*1, y + offset*0,
                    x + offset*2, y + offset*0,
                    //RIGHT
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                    x + offset*1, y + offset*0,
                    x + offset*2, y + offset*0,
                };
            //WATER
            case 4: 
                return new float[] {
                    //TOP
                    x + offset*2, y + offset*12,
                    x + offset*1, y + offset*12,
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11,
                    //BOTTOM
                    x + offset*2, y + offset*12,
                    x + offset*1, y + offset*12,
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11,
                    //FRONT
                    x + offset*2, y + offset*12,
                    x + offset*1, y + offset*12,
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11,
                    //BACK
                    x + offset*2, y + offset*12,
                    x + offset*1, y + offset*12,
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11,
                    //LEFT
                    x + offset*2, y + offset*12,
                    x + offset*1, y + offset*12,
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11,
                    //RIGHT
                    x + offset*2, y + offset*12,
                    x + offset*1, y + offset*12,
                    x + offset*1, y + offset*11,
                    x + offset*2, y + offset*11,
                };
            //BEDROCK
            case 5:
                return new float[] {
                    //TOP
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    //BOTTOM
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    //FRONT
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    //BACK
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    //LEFT
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                    //RIGHT
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    x + offset*1, y + offset*1,
                    x + offset*2, y + offset*1,
                };
            default:
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    // TOP!
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    // FRONT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    // BACK QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    // LEFT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    // RIGHT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,

                };
        }
    }
    
    //Constructor 
    public Chunk(int startX, int startY, int startZ) {
        try {
            texture = TextureLoader.getTexture("PNG",
                ResourceLoader.getResourceAsStream("terrain.png"));
        }
        catch(Exception e) {
            System.out.print("ER-ROAR!");
        }
        
        r = new Random();
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        /*
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    if(r.nextFloat()>0.8f){
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Grass);
                    } else if(r.nextFloat()>0.6f){
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                    }else if(r.nextFloat()>0.4f){
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
                    }else if(r.nextFloat()>0.2f){
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
                    } else if(r.nextFloat()>0.1f){
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
                    } else if(r.nextFloat()>0.0f){
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Bedrock);
                    } else {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Default);
                    }
                }
            }
        }*/
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        StartX = startX;
        StartY = startY;
        StartZ = startZ;
        rebuildMesh(startX, startY, startZ); 
    }
 }

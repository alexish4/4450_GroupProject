/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package groupproject;

/**
 *
 * @author alexhernandez, ethan, megan
 */
public class Block {
    private boolean isActive;
    private BlockType Type;
    private float x,y,z;
    
    public enum BlockType {
        BlockType_Grass(0),
        BlockType_Sand(1),
        BlockType_Water(2),
        BlockType_Dirt(3),
        BlockType_Stone(4),
        BlockType_Bedrock(5),
        BlockType_Default(6); 
        private int BlockID;

    
        BlockType(int i) {
            BlockID = i;
        }
        
        public int getID() {
            return BlockID;
        }
        
        public void setID(int id) {
            BlockID = id;
        }
    }
    
    //Constructor
    public Block(BlockType type) {
        Type = type;
    }
    
    public void setCoords(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public int getID() {
        return Type.getID();
    }
    
}

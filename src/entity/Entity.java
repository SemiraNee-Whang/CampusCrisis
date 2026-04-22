package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Base class for all game entities (player characters, NPCs).
 * Stores shared attributes: position, movement speed, sprite images,
 * direction, and collision area.
 */

public class Entity {
	//Position
    public int x, y;
    //Movement
    public int speed;
    
    //Sprite Frames
    public BufferedImage up1, up2, upNeutral, down1, down2, downNeutral, 
                         left1, left2, left3, leftNeutral, 
                         right1, right2, right3, right4, rightNeutral;
    //Stores direction
    //Current facing direction: "up", "down", "left", or "right" 
    public String direction;
    
    //Sprite Animation State
    //Counts frames elapsed; resets when a sprite frame advances
    public int spriteCounter = 0;
    //Current frame index (0 = neutral/idle, 1-4 = walk cycle)
    public int spriteNum = 1;

    //Collision
    //Solid bounding box used for collision detection; defaults to full tile size
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); 
    //Set to true when this entity is currently overlapping a solid tile
    public boolean collisionOn = false;
}
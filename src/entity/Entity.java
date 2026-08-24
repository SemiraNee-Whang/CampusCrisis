package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

// TODO: Auto-generated Javadoc
/**
 * The Class Entity.
 */
public class Entity {
    
    /** The y. */
    public int x, y;
    
    /** The speed. */
    public int speed;
    
    /** The right neutral. */
    //Stores Pictures
    public BufferedImage up1, up2, upNeutral, down1, down2, downNeutral, 
                         left1, left2, left3, leftNeutral, 
                         right1, right2, right3, right4, rightNeutral;
    
    /** The direction. */
    //Stores direction
    public String direction;
    
    /** The sprite counter. */
    //Stores Sprite Logic
    public int spriteCounter = 0;
    
    /** The sprite num. */
    public int spriteNum = 1;
    
    /** The solid area. */
    //Initialised so that we don't get null problems
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); 
    
    /** The collision on. */
    public boolean collisionOn = false;
}
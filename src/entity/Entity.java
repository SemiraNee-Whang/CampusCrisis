package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {
    public int x, y;
    public int speed;
    
    public BufferedImage up1, up2, upNeutral, down1, down2, downNeutral, 
                         left1, left2, left3, leftNeutral, 
                         right1, right2, right3, right4, rightNeutral;
    public String direction;
    
    public int spriteCounter = 0;
    public int spriteNum = 1;
    
    // INITIALISE THIS HERE TO AVOID NULLPOINTEREXCEPTION
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); 
    public boolean collisionOn = false;
}
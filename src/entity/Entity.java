package entity;

import java.awt.image.BufferedImage;

public class Entity {
    
    public int x, y;
    public int speed; 
    
    // Movement Sprites - Up and Down
    public BufferedImage up1, up2, up3, up4, upNeutral;
    public BufferedImage down1, down2, down3, down4, downNeutral;
    
    // Movement Sprites - Left and Right
    public BufferedImage left1, left2, left3, left4, leftNeutral;
    public BufferedImage right1, right2, right3, right4, rightNeutral;
    
    public String direction;
    
    public int spriteCounter = 0;
    public int spriteNum = 1; // Now supports 1-4 for walking, 0 for neutral
}
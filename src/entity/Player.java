package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;
import java.awt.Rectangle;

/**
 * Represents the student president character controlled by the player.
 * Extends Entity to inherit sprite/collision fields.
 * Handles movement input, boundary clamping, animation cycling, and rendering.
 */
public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    public Rectangle solidArea;
    public boolean collisionOn = false;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        
        solidArea = new Rectangle(16, 48, 32, 16);
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = (int)(gp.tileSize * 7.7);
        y = (int)(gp.tileSize * 2.8);
        speed = 4;
        direction = "down";
    }

    //Player Entity picture frames
    public void getPlayerImage() {
        try {
        	
            // UP (3 frames)
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/1 Walk up.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/2 Walk up.png"));
            upNeutral = ImageIO.read(getClass().getResourceAsStream("/player/Walking Up Neutral.png"));

            // DOWN (3 frames)
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/Walk Down.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/Walk Down 2.png"));
            downNeutral = ImageIO.read(getClass().getResourceAsStream("/player/Jessie Walking.png"));

            // LEFT (4 frames)
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/1 Walking Left.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/2nd Walking Left.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/player/4th Walking Left.png")); // Using 4th as the 3rd frame
            leftNeutral = ImageIO.read(getClass().getResourceAsStream("/player/Neutral Looking left.png"));

            // RIGHT (5 frames)
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/1 Walking Right.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/2 Walking Right.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/player/3 Walking Right.png"));
            right4 = ImageIO.read(getClass().getResourceAsStream("/player/4 Walking Right.png"));
            rightNeutral = ImageIO.read(getClass().getResourceAsStream("/player/Neural looking right.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Handles Character Moving
    public void update() {
        boolean moving = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;
    
        if (moving) {
            if (keyH.upPressed) direction = "up";
            else if (keyH.downPressed) direction = "down";
            else if (keyH.leftPressed) direction = "left";
            else if (keyH.rightPressed) direction = "right";

            // CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (!collisionOn) {
                switch(direction) {
                    case "up": y -= speed; break;
                    case "down": y += speed; break;
                    case "left": x -= speed; break;
                    case "right": x += speed; break;
                }
            }

            //RE-CALIBRATED BOUNDARIES
            
            // Left Wall
            if (x < 80) x = 80; 

            // Right Wall
            if (x > gp.screenWidth - gp.tileSize - 95) x = gp.screenWidth - gp.tileSize - 95;

            // Top Wall
            if (y < 140) y = 140; 

            // Bottom Wall
            if (y > gp.screenHeight - gp.tileSize - 70) {
                y = gp.screenHeight - gp.tileSize - 70;
            }

            // 3
            spriteCounter++;
            if (spriteCounter > 10) {
                spriteNum++;
                if (spriteNum > 4) spriteNum = 1;
                spriteCounter = 0;
            }
        } else {
            spriteNum = 0;
        }
    }
    //Player Graphics
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 0) image = upNeutral;
                // Since Up only has 2 walking frames, we alternate them
                else image = (spriteNum == 1 || spriteNum == 3) ? up1 : up2;
                break;
            case "down":
                if (spriteNum == 0) image = downNeutral;
                // Since Down only has 2 walking frames, we alternate them
                else image = (spriteNum == 1 || spriteNum == 3) ? down1 : down2;
                break;
            case "left":
                if (spriteNum == 0) image = leftNeutral;
                else {
                    if (spriteNum == 1) image = left1;
                    else if (spriteNum == 2 || spriteNum == 4) image = left2;
                    else if (spriteNum == 3) image = left3;
                }
                break;
            case "right":
                if (spriteNum == 0) image = rightNeutral;
                else {
                    if (spriteNum == 1) image = right1;
                    else if (spriteNum == 2) image = right2;
                    else if (spriteNum == 3) image = right3;
                    else if (spriteNum == 4) image = right4;
                }
                break;
                
                
        }
        
        g2.setColor(new java.awt.Color(0, 0, 0, 60)); // Transparent black
        g2.fillOval(x + 10, y + gp.tileSize - 15, gp.tileSize - 20, 10);

        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}
package main;

import entity.Entity;
import entity.Player;

//Helps with Character Collision
public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        // Current feet position
        int entityLeftX = entity.x + entity.solidArea.x;
        int entityRightX = entity.x + entity.solidArea.x + entity.solidArea.width;
        int entityTopY = entity.y + entity.solidArea.y;
        int entityBottomY = entity.y + entity.solidArea.y + entity.solidArea.height;

        // Define the "Desk Line"
        // Based on your Top Wall boundary (140), this stops her right at the wood
        int deskYLimit = 192; 

        switch(entity.direction) {
            case "up":
                // Predict if the next step goes above the desk limit
                if (entityTopY - entity.speed < deskYLimit) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                // If you add objects at the bottom, use:
                // if (entityBottomY + entity.speed > someBoundary)
                break;
            case "left":
                // Handled by your GamePanel boundaries (x < 80)
                break;
            case "right":
                // Handled by your GamePanel boundaries
                break;
        }
    }
}
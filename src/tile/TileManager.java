package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        
        // We only need 5 slots now (0 to 4)
        tile = new Tile[5];
        
        getTileImage();
    }

    public void getTileImage() {
        try {
            // Index 0: Floor/Base
            tile[0] = new Tile(); 
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Background.png"));

            // Index 1: (Skipped/Removed Full.png)

            // Index 2: Walls/Static Decorations
            tile[2] = new Tile(); 
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Layer 1.png"));

            // Index 3: Foreground (Ceiling/Overhead)
            tile[3] = new Tile(); 
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Layer 2.png"));

            // Index 4: Interactive Objects (Desks/Chairs)
            tile[4] = new Tile(); 
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Objects.png"));

        } catch (IOException | NullPointerException e) {
            System.out.println("Error: Could not find Classroom images. Check your res folder.");
            e.printStackTrace();
        }
    }

    // Draws everything the player walks ON TOP of
    public void drawBackground(Graphics2D g2) {
        // Draw Background -> Layer 1 -> Objects
        drawLayer(g2, 0); 
        drawLayer(g2, 2); 
        drawLayer(g2, 4); 
    }

    // Draws everything that covers the player
    public void drawForeground(Graphics2D g2) {
        // Draw Layer 2 (The overhead layer)
        drawLayer(g2, 3); 
    }

    private void drawLayer(Graphics2D g2, int index) {
        if (tile[index] != null && tile[index].image != null) {
            g2.drawImage(tile[index].image, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }
    }
}
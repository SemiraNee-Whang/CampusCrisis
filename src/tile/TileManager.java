package tile;

import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        getTileImage();
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile(); 
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Background.png"));
            
            tile[1] = new Tile(); 
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Chalkboard.png"));

            tile[2] = new Tile(); 
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Desks.png"));

            tile[3] = new Tile(); 
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Main desks.png"));
            tile[3].collision = true; 

            // ADD THIS PART - Index 4: Layer 2
            tile[4] = new Tile(); 
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Layer 2.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawBackground(Graphics2D g2) { drawLayer(g2, 0); } // Floor/Wall
    public void drawChalkboard(Graphics2D g2) { drawLayer(g2, 1); } // Chalkboard
    public void drawDesks(Graphics2D g2) { drawLayer(g2, 2); }      // Standard Desks
    public void drawMainDesks(Graphics2D g2) { drawLayer(g2, 3); }  // Main/Podium Desks
    public void drawLayer2(Graphics2D g2) { drawLayer(g2, 4); }     // Foreground Overlay

    private void drawLayer(Graphics2D g2, int index) {
        if (tile[index] != null && tile[index].image != null) {
            g2.drawImage(tile[index].image, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }
    }
    
    public void drawDeskAt(Graphics2D g2, int x, int y, int index) {
        if (tile[index] != null && tile[index].image != null) {
            g2.drawImage(tile[index].image, x, y, gp.tileSize, gp.tileSize, null);
        }
    }
}
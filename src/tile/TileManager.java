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
            // Index 0: Floor, Index 2: Walls, Index 3: Ceiling, Index 4: Desks/Objects
            tile[0] = new Tile(); tile[0].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Background.png"));
            tile[2] = new Tile(); tile[2].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Layer 1.png"));
            tile[3] = new Tile(); tile[3].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Layer 2.png"));
            tile[4] = new Tile(); tile[4].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Objects.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // LAYER 1: Floor and Walls
    public void drawBackground(Graphics2D g2) {
        drawLayer(g2, 0); 
        drawLayer(g2, 2); 
    }

    // LAYER 3: Desks and Furniture (Drawn after player)
    public void drawObjects(Graphics2D g2) {
        drawLayer(g2, 4); 
    }

    // LAYER 4: Ceiling and Lights
    public void drawForeground(Graphics2D g2) {
        drawLayer(g2, 3); 
    }

    private void drawLayer(Graphics2D g2, int index) {
        if (tile[index] != null && tile[index].image != null) {
            // Scaling to screenWidth/Height removes black borders
            g2.drawImage(tile[index].image, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }
    }
}
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
        tile = new Tile[10];
        getTileImage();
    }

    public void getTileImage() {
        try {
            // Classroom Layers
            tile[0] = new Tile(); tile[0].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Background.png"));
            tile[2] = new Tile(); tile[2].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Layer 1.png"));
            tile[3] = new Tile(); tile[3].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Layer 2.png"));
            tile[4] = new Tile(); tile[4].image = ImageIO.read(getClass().getResourceAsStream("/Classroom/Objects.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawBackground(Graphics2D g2) {
        drawLayer(g2, 0); // Floor
        drawLayer(g2, 2); // Walls
        drawLayer(g2, 4); // Desks
    }

    public void drawForeground(Graphics2D g2) {
        drawLayer(g2, 3); // Overhead beams/lights
    }

    private void drawLayer(Graphics2D g2, int index) {
        if (tile[index] != null && tile[index].image != null) {
            g2.drawImage(tile[index].image, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }
    }
}
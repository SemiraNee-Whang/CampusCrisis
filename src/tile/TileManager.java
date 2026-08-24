package tile;

import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * The Class TileManager.
 */
public class TileManager {

    /** The gp. */
    private GamePanel gp;
    
    /** The tile. */
    public Tile[] tile;

    /**
     * Instantiates a new tile manager.
     *
     * @param gp the gp
     */
    public TileManager(GamePanel gp) {
        this.gp = gp;
        //Uses 11 Arrays
        tile = new Tile[10];
        //Calls Method getTitleImage
        getTileImage();
    }

    /**
     * Gets the tile image.
     *
     * @return the tile image
     */
    //Creates the Tile images using Arrays
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

    /**
     * Draw background.
     *
     * @param g2 the g 2
     */
    //Handles the Layers
    public void drawBackground(Graphics2D g2) { drawLayer(g2, 0); } // Floor/Wall
    
    /**
     * Draw chalkboard.
     *
     * @param g2 the g 2
     */
    public void drawChalkboard(Graphics2D g2) { drawLayer(g2, 1); } // Chalkboard
    
    /**
     * Draw desks.
     *
     * @param g2 the g 2
     */
    public void drawDesks(Graphics2D g2) { drawLayer(g2, 2); }      // Standard Desks
    
    /**
     * Draw main desks.
     *
     * @param g2 the g 2
     */
    public void drawMainDesks(Graphics2D g2) { drawLayer(g2, 3); }  // Main/Podium Desks
    
    /**
     * Draw layer 2.
     *
     * @param g2 the g 2
     */
    public void drawLayer2(Graphics2D g2) { drawLayer(g2, 4); }     // Foreground Overlay

    /**
     * Draw layer.
     *
     * @param g2 the g 2
     * @param index the index
     */
    private void drawLayer(Graphics2D g2, int index) {
        if (tile[index] != null && tile[index].image != null) {
            g2.drawImage(tile[index].image, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }
    }
    
    /**
     * Draw desk at.
     *
     * @param g2 the g 2
     * @param x the x
     * @param y the y
     * @param index the index
     */
    public void drawDeskAt(Graphics2D g2, int x, int y, int index) {
        if (tile[index] != null && tile[index].image != null) {
            g2.drawImage(tile[index].image, x, y, gp.tileSize, gp.tileSize, null);
        }
    }
}
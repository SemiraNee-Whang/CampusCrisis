package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * Represents the student president character.
 * Handles the fixed character position, image loading and rendering.
 */
public class Player {

    /** The gp. */
    private GamePanel gp;

    /** The x. */
    private int x;
    
    /** The y. */
    private int y;

    /** The player image. */
    private BufferedImage playerImage;

    /**
     * Creates the player and prepares the character image.
     *
     * @param gp the gp
     */
    public Player(GamePanel gp) {

        this.gp = gp;

        setDefaultValues();
        getPlayerImage();
    }

    /**
     * Sets the fixed position of the player on the screen.
     */
    public void setDefaultValues() {

        x = (int)(gp.tileSize * 7.7);
        y = (int)(gp.tileSize * 2.8);
    }

    /**
     * Loads the image used to display the stationary player.
     *
     * @return the player image
     */
    public void getPlayerImage() {

        try {

            playerImage =
                    ImageIO.read(
                            getClass()
                            .getResourceAsStream(
                                    "/player/Jessie Walking.png"));

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * Draws the stationary player character.
     *
     * @param g2 the g 2
     */
    public void draw(Graphics2D g2) {

        //Draws a shadow underneath the character
        g2.setColor(
                new java.awt.Color(
                        0,
                        0,
                        0,
                        60));

        g2.fillOval(
                x + 10,
                y + gp.tileSize - 15,
                gp.tileSize - 20,
                10);

        //Draws the character image
        if (playerImage != null) {

            g2.drawImage(
                    playerImage,
                    x,
                    y,
                    gp.tileSize,
                    gp.tileSize,
                    null);
        }
    }
}
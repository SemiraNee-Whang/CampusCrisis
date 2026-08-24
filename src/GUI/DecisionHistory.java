package GUI;

import java.awt.*;
import main.GamePanel;
import main.Request;

// TODO: Auto-generated Javadoc
/**
 * The Class DecisionHistory.
 */
//Displays all decisions made during the current game term
public class DecisionHistory {

    /** The gp. */
    //Reference to the main GamePanel
    private GamePanel gp;

    /** The scroll offset. */
    //Controls how far the history table has been scrolled
    public int scrollOffset = 0;

    /** The back btn. */
    //Stores the clickable area of the Back button
    public Rectangle backBtn;

    /**
     * Receives the GamePanel used by the Decision History screen.
     * Creates the Back button used to return to gameplay.
     *
     * @param gp the gp
     */
    public DecisionHistory(GamePanel gp) {
        this.gp = gp;

        //Positioned at the bottom-left of the white table area
        backBtn = new Rectangle(
                gp.tileSize + 20,
                gp.screenHeight - 110,
                100,
                35);
    }

    /**
     * Receives the Graphics2D object used to draw the screen.
     * Displays all requests stored in the current decision history,
     * including the request ID, status and outcome summary.
     *
     * @param g2 the g 2
     */
    public void draw(Graphics2D g2) {

        //Dims the Background (To make the table pop)
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(
                0,
                0,
                gp.screenWidth,
                gp.screenHeight);

        //White Table Background
        int tableX = gp.tileSize;
        int tableY = gp.tileSize;
        int tableW = gp.screenWidth - (gp.tileSize * 2);
        int tableH = gp.screenHeight - (gp.tileSize * 3);

        g2.setColor(Color.WHITE);
        g2.fillRect(
                tableX,
                tableY,
                tableW,
                tableH);

        g2.setColor(Color.BLACK);
        g2.drawRect(
                tableX,
                tableY,
                tableW,
                tableH);

        //Column Labels
        g2.setFont(new Font(
                "Arial",
                Font.BOLD,
                18));

        int yHead = tableY + 40;

        g2.drawString(
                "ID",
                tableX + 30,
                yHead);

        g2.drawString(
                "STATUS",
                tableX + 130,
                yHead);

        g2.drawString(
                "OUTCOME SUMMARY",
                tableX + 300,
                yHead);

        //Header Underline
        g2.setStroke(new BasicStroke(2));

        g2.drawLine(
                tableX + 20,
                yHead + 10,
                tableX + tableW - 20,
                yHead + 10);

        //Draw History from the GamePanel ArrayList
        g2.setFont(new Font(
                "Consolas",
                Font.PLAIN,
                15));

        //Loops through every Request stored in the history
        for (int i = 0; i < gp.history.size(); i++) {

            Request r = gp.history.get(i);

            //Calculates the row position based on scrolling
            int rowY =
                    yHead
                    + 50
                    + (i * 35)
                    - scrollOffset;

            //Only draws rows that fit inside the table
            if (rowY > yHead + 20
                    && rowY < tableY + tableH - 20) {

                //Colour coding based on request status
                if ("Approved".equals(r.getStatus())) {

                    g2.setColor(
                            new Color(0, 120, 0));

                } else if ("Declined".equals(r.getStatus())) {

                    g2.setColor(
                            new Color(180, 0, 0));

                } else {

                    g2.setColor(Color.GRAY);
                }

                //Displays the Request ID
                g2.drawString(
                        r.getId(),
                        tableX + 30,
                        rowY);

                //Displays the decision status
                g2.drawString(
                        r.getStatus(),
                        tableX + 130,
                        rowY);

                //Displays the outcome summary
                g2.setColor(Color.BLACK);

                g2.drawString(
                        r.getOutcome(),
                        tableX + 300,
                        rowY);
            }
        }

        //Back Button
        drawStyledButton(
                g2,
                backBtn,
                "BACK",
                new Color(255, 215, 0));
    }

    /**
     * Receives the button position, text and colour.
     * Draws a reusable styled button on the Decision History screen.
     *
     * @param g2 the g 2
     * @param r the r
     * @param text the text
     * @param bgColor the bg color
     */
    private void drawStyledButton(
            Graphics2D g2,
            Rectangle r,
            String text,
            Color bgColor) {

        //Button background
        g2.setColor(bgColor);
        g2.fill(r);

        //Button border
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.draw(r);

        //Button text
        g2.setFont(new Font(
                "Arial",
                Font.BOLD,
                14));

        FontMetrics fm =
                g2.getFontMetrics();

        int textX =
                r.x
                + (r.width
                - fm.stringWidth(text)) / 2;

        int textY =
                r.y
                + (r.height
                + fm.getAscent()) / 2
                - 2;

        g2.drawString(
                text,
                textX,
                textY);
    }
    
    
}
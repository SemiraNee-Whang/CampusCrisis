package main;

import java.awt.*;
import javax.swing.*;
import entity.Player;
import tile.TileManager;
import GUI.UI;

public class GamePanel extends JPanel implements Runnable {
    
    // Screen Settings
    final int originalTileSize = 16;
    final int scale = 4;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 15;
    public final int maxScreenRow = 10;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    
    // FPS
    int FPS = 60;
    
    // System
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this); // Pass 'this' so it can check gameState
    public UI ui = new UI(this); 
    Thread gameThread;
    
    // Entity
    public Player player = new Player(this, keyH);
    
    // GameState
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(new Color(30, 30, 40)); 
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH); // keyH now has access to this GamePanel
        this.setFocusable(true);
        
        gameState = titleState; 
    }
    
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS; 
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }
    
    public void update() {
        if (gameState == playState) {
            player.update();
        }
    }
    
 
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // TITLE STATE
        if (gameState == titleState) {
            ui.draw(g2); 
        }
        // PLAY STATE
        else if (gameState == playState) {
            // 1. Draw the floor and walls first (Player walks ON TOP of these)
            tileM.drawBackground(g2);
            
            // 2. Draw the Player
            player.draw(g2);        
            
            // 3. Draw Objects (Drawn ON TOP of player - makes player look like they are behind)
            tileM.drawObjects(g2);
            
            // 4. Draw Foreground (Ceiling/Lights drawn last)
            tileM.drawForeground(g2);
            
            // 5. Draw UI (HUD, Dialogue, etc.)
            ui.draw(g2);
        }
        
        g2.dispose();
    }
    }
}
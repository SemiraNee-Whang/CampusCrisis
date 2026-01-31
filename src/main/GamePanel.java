package main;

import java.awt.*;
import javax.swing.*;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    
    // Screen Settings
    final int originalTileSize = 16;
    final int scale = 4;
    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 15;
    final int maxScreenRow = 10;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    
    // FPS
    int FPS = 60;
    
    // System
    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    
    // Entity
    public Player player = new Player(this, keyH);
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(new Color(30, 30, 40)); 
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
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
        player.update();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // 1. Lower layers (Floor, Walls, Desks)
        tileM.drawBackground(g2);
        
        // 2. The Character
        player.draw(g2);
        
        // 3. The Top layer (Ceiling/Lights)
        tileM.drawForeground(g2);
        
        g2.dispose();
    }
}
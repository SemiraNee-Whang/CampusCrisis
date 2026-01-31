package main;

import java.awt.*;
import javax.swing.*;
import entity.Player;
import tile.TileManager;
import GUI.UI;
import GUI.LoginManager; // Import the new class

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
    public KeyHandler keyH = new KeyHandler(this); 
    public UI ui = new UI(this); 
    public LoginManager loginM = new LoginManager(this);
    public MouseHandler mouseH = new MouseHandler(this);
    
    Thread gameThread;
    
    // Entity
    public Player player = new Player(this, keyH);
    
    // GameState
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int loginState = 2;
    
    //GamePanel 
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(new Color(30, 30, 40)); 
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH); 
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
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
        // You can add login specific update logic here if needed
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // TITLE STATE
        if (gameState == titleState) {
            ui.draw(g2); 
        }
        // LOGIN STATE
        else if (gameState == loginState) {
            loginM.draw(g2); // Use the new class to draw the login screen
        }
        // PLAY STATE
        else if (gameState == playState) {
            tileM.drawBackground(g2);
            player.draw(g2);        
            tileM.drawObjects(g2);
            tileM.drawForeground(g2);
            ui.draw(g2);
        }
        
        g2.dispose();
    }
}
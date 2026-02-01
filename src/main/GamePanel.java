package main;

import java.awt.*;
import javax.swing.*;
import entity.Player;
import tile.TileManager;
import GUI.UI;
import GUI.LoginManager;
import GUI.PresidentSetup;

public class GamePanel extends JPanel implements Runnable {
    
    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 4;
    public final int tileSize = originalTileSize * scale; // 64x64
    public final int maxScreenCol = 15;
    public final int maxScreenRow = 10;
    public final int screenWidth = tileSize * maxScreenCol; // 960px
    public final int screenHeight = tileSize * maxScreenRow; // 640px
    
    // FPS
    int FPS = 60;
    
    // SYSTEM
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this); 
    public UI ui = new UI(this); 
    public LoginManager loginM = new LoginManager(this);
    public MouseHandler mouseH = new MouseHandler(this);
    public PresidentSetup pSetup = new PresidentSetup(this);
    
    Thread gameThread;
    
    // ENTITY
    public Player player = new Player(this, keyH);
    
    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;    
    public final int loginState = 2;
    public final int setupState = 3;
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(new Color(30, 30, 40)); 
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH); 
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
        this.setFocusable(true);
        
        // Starts the game at the Title Screen
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
        // Only update player movement/logic during active gameplay
        if (gameState == playState) {
            player.update();
        }
        // Menu states (Title, Login, Setup) are handled via Mouse/Key Handlers
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // 1. TITLE SCREEN
        if (gameState == titleState) {
            ui.draw(g2); 
        }
        
        // 2. LOGIN SCREEN
        else if (gameState == loginState) {
            loginM.draw(g2);
        }

        // 3. PRESIDENT SETUP SCREEN
        else if (gameState == setupState) {
            pSetup.draw(g2);
        }
        
        // 4. PLAY STATE (Gameplay)
        else if (gameState == playState) {
            // Layer 1: Floor and Walls
            tileM.drawBackground(g2);
            
            // Layer 2: Characters (Jessie)
            player.draw(g2);        
            
            // Layer 3: Interactive Objects (Desks, Chalkboard)
            tileM.drawObjects(g2);
            
            // Layer 4: Overlays (Wall tops)
            tileM.drawForeground(g2);
            
            // Layer 5: UI (HUD, Budget, Approval Rating)
            ui.draw(g2);
        }
        
        g2.dispose();
    }
}
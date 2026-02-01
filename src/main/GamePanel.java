package main;

import java.awt.*;
import javax.swing.*;
import entity.Player;
import tile.TileManager;
import GUI.UI;
import GUI.LoginManager;
import GUI.PresidentSetup;
import GUI.Dashboard;
import GUI.RequestList; 
import main.CollisionChecker;

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
    public CollisionChecker cChecker = new CollisionChecker(this);
    public UI ui = new UI(this); 
    public LoginManager loginM = new LoginManager(this);
    public MouseHandler mouseH = new MouseHandler(this);
    public PresidentSetup pSetup = new PresidentSetup(this);
    public Dashboard dashboard = new Dashboard(this);
    public RequestList reqList = new RequestList(this); 
    
    Thread gameThread;
    
    // ENTITY
    public Player player = new Player(this, keyH);
    
    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;    
    public final int loginState = 2;
    public final int setupState = 3;
    public final int requestState = 4; 
    
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
            dashboard.updateTimer();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        //MENU STATES
        if (gameState == titleState) { 
            ui.draw(g2); 
        }
        else if (gameState == loginState) { 
            loginM.draw(g2);
        }
        else if (gameState == setupState) { 
            pSetup.draw(g2);
        }
        
        //PlayState
else if (gameState == playState || gameState == requestState) {            
            
            // 1. THE FLOOR & BACK WALL
            tileM.drawBackground(g2);   
            tileM.drawChalkboard(g2);   
            
            // 2. STANDARD DESKS (Drawn BEFORE player so she walks on top)
            tileM.drawDesks(g2);        

            // 3. THE PLAYER
            // Drawing her here means anything called AFTER this will overlap her
            player.draw(g2);            
            
            // 4. THE FOREGROUND (Drawn AFTER player so she is "behind" them)
            tileM.drawMainDesks(g2);    
            tileM.drawLayer2(g2);       
            
            // 5. UI
            dashboard.draw(g2);         
            if (gameState == requestState) {
                reqList.draw(g2);       
            }
            ui.draw(g2);
        }
        g2.dispose();
    }
}